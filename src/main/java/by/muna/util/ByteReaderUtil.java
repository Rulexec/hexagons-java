package by.muna.util;

import by.muna.io.IByteReader;
import by.muna.io.IByteWriter;

import java.nio.ByteBuffer;

public class ByteReaderUtil {
    public static void flush(IByteReader reader) {
        reader.read(new IByteWriter() {
            @Override
            public int write(ByteBuffer buffer) {
                return buffer.remaining();
            }

            @Override
            public int write(byte[] buffer, int offset, int length) {
                return length;
            }

            @Override public void end() { throw new UnsupportedOperationException("You cannot end this stream"); }

            @Override public boolean isEnded() { return false; }
        });

        byte[] buffer = new byte[1024];

        while (true) {
            if (reader.read(buffer) == 0) break;
        }
    }
}
