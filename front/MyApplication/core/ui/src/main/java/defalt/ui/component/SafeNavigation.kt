package defalt.ui.component

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun safeClick(onClick: () -> Unit): () -> Unit {
    var enabled by remember { mutableStateOf(true) }
    val scope = rememberCoroutineScope()

    return {
        if (enabled) {
            enabled = false
            onClick()
            scope.launch {
                delay(1000)
                enabled = true
            }
        }
    }
}
