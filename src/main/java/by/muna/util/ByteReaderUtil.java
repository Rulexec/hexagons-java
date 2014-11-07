package by.muna.util;

import by.muna.io.IByteReader;

public class ByteReaderUtil {
    public static void flush(IByteReader reader) {
        while (reader.canPopBuffer()) reader.popBuffer();

        // TODO: implement void-ByteBuffer
        byte[] buffer = new byte[1024];

        while (true) {
            if (reader.read(buffer) == 0) break;
        }
    }
}
