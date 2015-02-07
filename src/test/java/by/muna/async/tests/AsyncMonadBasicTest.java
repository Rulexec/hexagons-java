package by.muna.async.tests;

import by.muna.async.AsyncPure;
import by.muna.async.AsyncUtil;
import by.muna.async.IAsync;
import org.junit.Assert;
import org.junit.Test;

import java.util.Timer;
import java.util.TimerTask;
import java.util.function.Function;

public class AsyncMonadBasicTest {
    @Test
    public void basicTest() {
        // just some crzy useless things
        Function<IAsync<Integer, Object>, IAsync<Integer, Object>> f = AsyncUtil.composeFL(
            m -> m.bind(x -> new AsyncPure<Integer, Object>(x * 3)),
            m -> m.bind(x -> new AsyncPure<Integer, Object>(x + 7))
        );

        f.apply(new AsyncPure<Integer, Object>(4)).run((x, e) -> {
            Assert.assertNotNull(x);
            Assert.assertEquals(4 * 3 + 7, (int) x);
            Assert.assertNull(e);
        });
    }

    @Test
    public void skipTimerTest() {
        IAsync<Object, Object> delay = AsyncUtil.cannotBeStopped(callback -> new Timer().schedule(new TimerTask() {
            @Override
            public void run() {
                callback.accept(null, null);
            }
        }, 1));

        delay.skip(new AsyncPure<Integer, Object>(42)).run((x, e) -> {
            Assert.assertNotNull(x);
            Assert.assertEquals(42, (int) x);
            Assert.assertNull(e);
        });
    }
}
