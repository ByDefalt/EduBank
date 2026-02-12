package gatewayapi.wrapper;

@FunctionalInterface
public interface FeignCall<T> {
    T execute();
}