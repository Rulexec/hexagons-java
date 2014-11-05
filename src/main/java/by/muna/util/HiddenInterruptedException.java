package by.muna.util;

import java.util.concurrent.BlockingQueue;

public class HiddenInterruptedException extends RuntimeException {
    private HiddenInterruptedException(Throwable cause) {
        super(cause);
    }

    public static <T> T takeFromBlockingQueue(BlockingQueue<T> queue) {
        try {
            return queue.take();
        } catch (InterruptedException ex) {
            throw new HiddenInterruptedException(ex);
        }
    }
    public static <T> void putToBlockingQueue(BlockingQueue<T> queue, T el) {
        try {
            queue.put(el);
        } catch (InterruptedException ex) {
            throw new HiddenInterruptedException(ex);
        }
    }
}
