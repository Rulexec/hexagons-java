package by.muna.io.returnable;

import by.muna.io.IAsyncByteInputStream;
import by.muna.io.IByteReader;
import by.muna.async.IAsyncFuture;

public interface IAsyncInputStreamWithReturningEnd {
    IAsyncByteInputStream getInputStream();

    IAsyncFuture<Object> end(IByteReader rest);
}
