package by.muna.async.tests;

import by.muna.async.AsyncFutureUtil;
import org.junit.Assert;
import org.junit.Test;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class AsyncFutureUtilBasicTest {
    @Test
    public void parallelNullAndOrderTest() {
        class Container {
            List<Integer> list = new ArrayList<>(3);
            boolean runned = false;
        }

        Container c = new Container();

        AsyncFutureUtil.parallel(Arrays.asList(
            callback -> { c.list.add(1); callback.accept(null); },
            callback -> { c.list.add(2); callback.accept(null); },
            callback -> { c.list.add(3); callback.accept(null); }
        )).run(x -> {
            Assert.assertNull(x);

            Assert.assertEquals(3, c.list.size());
            Assert.assertArrayEquals(new Integer[] { 1, 2, 3 }, c.list.toArray());

            c.runned = true;
        });

        Assert.assertTrue(c.runned);
    }

    @Test
    public void parallelTest() {
        class Container {
            int runned = 0;
        }

        Container c = new Container();

        AsyncFutureUtil.<Boolean>parallel(Arrays.asList(
            callback -> callback.accept(null),
            callback -> callback.accept(Boolean.TRUE),
            callback -> callback.accept(Boolean.FALSE)
        )).run(x -> {
            Assert.assertEquals(Boolean.TRUE, x);

            c.runned++;
        });

        Assert.assertEquals(1, c.runned);
    }
}
