package by.muna.io.returnable;

import by.muna.io.IByteReader;

public interface IReturnableByteReader extends IByteReader {
    /**
     * It means, that byte reader must be closed, but all data from rest must be somehow returned to stream.
     * If rest returns 0 on any read, it shows, that this reader hasn't anymore data, else it must block,
     * but it's not encouraged.
     * @param rest
     */
    void end(IByteReader rest);
}
