package defalt.utils.logger

import java.io.FileWriter
import java.io.PrintWriter
import java.io.StringWriter
import java.lang.AutoCloseable
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.concurrent.BlockingQueue
import java.util.concurrent.LinkedBlockingQueue
import java.util.concurrent.TimeUnit
import java.util.concurrent.atomic.AtomicBoolean

/**
 * Logger qui écrit les messages dans un fichier de manière asynchrone et
 * thread-safe en utilisant une file et un thread dédié d'écriture.
 *
 *
 * Les appels aux méthodes de logging sont non bloquants (ils enfilent les
 * messages) ; un worker consomme la file et écrit séquentiellement sur le
 * fichier. À la fermeture, la file est drainée avant la fermeture du writer.
 *
 */
class FileLogger(private val level: LogLevel, filePath: String) : Logger, AutoCloseable {
    private val writer: PrintWriter
    private val fmt: DateTimeFormatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss")

    // Async queue + worker
    private val queue: BlockingQueue<String?> = LinkedBlockingQueue<String?>()
    private val worker: Thread
    private val running = AtomicBoolean(true)

    /**
     * Crée un FileLogger pointant vers le fichier donné.
     *
     * @param level    niveau minimal de log
     * @param filePath chemin du fichier de log (créé si nécessaire)
     * @throws IOException si le fichier ne peut pas être ouvert
     */
    init {
        this.writer = PrintWriter(FileWriter(filePath, true), true)

        // worker thread writes queued messages to file
        this.worker = Thread(
            Runnable {
                try {
                    // loop until stopped and queue drained
                    while (running.get() || !queue.isEmpty()) {
                        try {
                            val line = queue.poll(500, TimeUnit.MILLISECONDS)
                            if (line != null) {
                                writer.println(line)
                            }
                        } catch (e: InterruptedException) {
                            // re-check running flag
                        }
                    }
                } finally {
                    // Ensure writer is flushed even if worker exits unexpectedly
                    writer.flush()
                }
            },
            "FileLogger-Writer",
        )
        this.worker.setDaemon(true)
        this.worker.start()
    }

    private fun enabled(l: LogLevel): Boolean {
        return l.ordinal >= level.ordinal
    }

    private fun ts(): String {
        return LocalDateTime.now().format(fmt)
    }

    private fun format(lvl: LogLevel, msg: String?): String {
        return String.format("[%s] %s - %s", ts(), lvl.name, msg)
    }

    private fun enqueue(s: String) {
        if (!running.get()) return // ignore after close initiated

        // best-effort: try to offer without blocking indefinitely
        queue.offer(s)
    }

    private fun write(lvl: LogLevel, message: String?) {
        if (!enabled(lvl)) return
        enqueue(format(lvl, message))
    }

    override fun trace(message: String) {
        write(LogLevel.TRACE, message)
    }

    override fun debug(message: String) {
        write(LogLevel.DEBUG, message)
    }

    override fun info(message: String) {
        write(LogLevel.INFO, message)
    }

    override fun warn(message: String) {
        write(LogLevel.WARN, message)
    }

    override fun error(message: String) {
        write(LogLevel.ERROR, message)
    }

    override fun error(message: String, t: Throwable) {
        if (!enabled(LogLevel.ERROR)) return
        // capture stack trace into string and enqueue as a single message
        val sw = StringWriter()
        t.printStackTrace(PrintWriter(sw))
        val combined = format(LogLevel.ERROR, message + "\n" + sw)
        enqueue(combined)
    }

    public override fun isLevelEnabled(level: LogLevel): Boolean {
        return enabled(level)
    }

    /**
     * Arrête le worker, attend la vidange de la file et ferme le writer.
     */
    @Throws(Exception::class)
    override fun close() {
        // signal stop
        running.set(false)
        // interrupt worker in case it's waiting
        worker.interrupt()
        try {
            // wait up to 2s for worker to finish
            worker.join(2000)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
        // drain any remaining messages synchronously to ensure persistence
        var line: String?
        while ((queue.poll().also { line = it }) != null) {
            writer.println(line)
        }
        writer.flush()
        writer.close()
    }
}
