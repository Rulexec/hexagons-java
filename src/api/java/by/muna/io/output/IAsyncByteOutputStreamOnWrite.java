package by.muna.io.output;

public interface IAsyncByteOutputStreamOnWrite {
    default int estimatedCapacity() { return 0; }
}
