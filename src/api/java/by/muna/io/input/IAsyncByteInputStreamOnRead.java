package by.muna.io.input;

public interface IAsyncByteInputStreamOnRead {
    default int bufferedSize() { return 0; }

    default int estimatedNewBytesCount() { return 0; };
}
