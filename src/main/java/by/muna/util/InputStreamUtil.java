package by.muna.util;

import java.io.IOException;
import java.io.InputStream;
import java.util.LinkedList;
import java.util.List;

public class InputStreamUtil {
    public static byte[] readWholeStream(InputStream is) throws IOException {
        int count = 0;
        List<byte[]> buffers = new LinkedList<>();

        final int BUFFER_SIZE = 1024;

        byte[] buffer = new byte[BUFFER_SIZE];
        int pos = 0;

        while (true) {
            int readed = is.read(buffer, pos, buffer.length - pos);
            if (readed == -1) break;

            pos += readed;

            if (pos == BUFFER_SIZE) {
                buffers.add(buffer);
                count++;
                buffer = new byte[BUFFER_SIZE];
                pos = 0;
            }
        }

        byte[] result = new byte[count * BUFFER_SIZE + pos];
        int responseBodyPos = 0;

        for (byte[] filledBuffer : buffers) {
            System.arraycopy(filledBuffer, 0, result, responseBodyPos, BUFFER_SIZE);
            responseBodyPos += BUFFER_SIZE;
        }

        System.arraycopy(buffer, 0, result, responseBodyPos, pos);

        return result;
    }
}
