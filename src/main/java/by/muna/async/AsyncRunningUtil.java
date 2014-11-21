package by.muna.async;

public class AsyncRunningUtil {
    public static IAsyncRunning cannotBeStopped() {
        return new IAsyncRunning() {};
    }

    public static IAsyncRunning alreadyFinished() {
        return new IAsyncRunning() {
            @Override
            public IAsyncFuture<AsyncCancellationResult> cancel() {
                return callback -> {
                    callback.accept(AsyncCancellationResult.ALREADY_FINISHED);
                    return AsyncRunningUtil.alreadyFinished();
                };
            }
        };
    }
}
