package by.muna.monads.tests;

import by.muna.monads.AsyncMonadPure;
import by.muna.monads.AsyncMonadUtil;
import by.muna.monads.IAsyncMonad;
import org.junit.Assert;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class AsyncMonadBasicTest {
    @Test
    public void basicTest() {
        // just some crzy useless things
        Function<IAsyncMonad<Integer, Object>, IAsyncMonad<Integer, Object>> f = AsyncMonadUtil.composeFL(
            m -> m.bind(x -> new AsyncMonadPure<Integer, Object>(x * 3)),
            m -> m.bind(x -> new AsyncMonadPure<Integer, Object>(x + 7))
        );

        f.apply(new AsyncMonadPure<Integer, Object>(4)).run((x, e) -> {
            Assert.assertNotNull(x);
            Assert.assertEquals(4 * 3 + 7, (int) x);
            Assert.assertNull(e);
        });
    }

    @Test
    public void skipTimerTest() {
        IAsyncMonad<Object, Object> delay = callback -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                callback.accept(null, null);
            }
        }, 1);

        delay.skip(new AsyncMonadPure<Integer, Object>(42)).run((x, e) -> {
            Assert.assertNotNull(x);
            Assert.assertEquals(42, (int) x);
            Assert.assertNull(e);
        });
    }
}
