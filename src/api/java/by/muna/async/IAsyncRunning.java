package by.muna.async;

/**
 * All methods in this interface must be optional and have default implementation,
 * because it returns from Async::run.
 */
public interface IAsyncRunning {
    default IAsyncFuture<AsyncCancellationResult> cancel() {
        return callback -> {
            callback.accept(AsyncCancellationResult.CANNOT_CANCEL);
            return AsyncRunningUtil.alreadyFinished();
        };
    }
}
