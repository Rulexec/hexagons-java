package by.muna.pubsub;

public interface ISubscription {
    void pause(boolean pause);
    void cancel();

    boolean isPaused();
    boolean isCancelled();

    default boolean isActive() {
        return !this.isCancelled() && !this.isPaused();
    }
}
