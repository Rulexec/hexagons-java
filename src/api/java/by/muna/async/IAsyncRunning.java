package by.muna.async;

public interface IAsyncRunning {
    default IAsyncFuture<AsyncCancellationResult> cancel() {
        return callback -> {
            callback.accept(AsyncCancellationResult.CANNOT_CANCEL);
            return AsyncRunningUtil.alreadyFinished();
        };
    }
}
