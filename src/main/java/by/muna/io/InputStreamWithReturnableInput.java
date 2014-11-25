package by.muna.io;

import by.muna.async.AsyncRunningUtil;
import by.muna.async.IAsyncFuture;
import by.muna.async.OneTimeEventAsyncFuture;

import java.util.Deque;
import java.util.LinkedList;
import java.util.function.Function;

public class InputStreamWithReturnableInput implements IAsyncByteInputStream {
    private IAsyncByteInputStream stream;

    private Deque<IByteReader> returned = new LinkedList<>();

    private Function<IByteReader, Boolean> listener;

    private OneTimeEventAsyncFuture<Object> endEvent = new OneTimeEventAsyncFuture<>();

    private boolean ended = false;
    private boolean streamEnded = false;
    private Object endedStreamError;

    public InputStreamWithReturnableInput(IAsyncByteInputStream stream) {
        this.stream = stream;

        this.stream.onEnd().run(this::_streamEnded);
    }

    @Override
    public void onCanRead(Function<IByteReader, Boolean> listener) {
        this.listener = listener;

        this.stream.onCanRead(r -> {
            if (!this.doReturnedReading()) return false;

            return listener.apply(r);
        });
    }

    private boolean doReturnedReading() {
        if (this.ended) return false;

        while (!this.returned.isEmpty()) {
            IByteReader rest;

            while (true) {
                rest = this.returned.getFirst();

                if (rest.isEnded()) this.returned.pollFirst();
                else break;

                if (this.returned.isEmpty()) return true;
            }

            boolean readingRequested = this.listener.apply(rest);

            if (rest.isEnded()) this.returned.pollFirst();

            if (!readingRequested) return false;
        }

        if (this.streamEnded && this.returned.isEmpty()) {
            this.ended = true;
            this.endEvent.event(this.endedStreamError);
        }

        return true;
    }

    private void _streamEnded(Object error) {
        this.endedStreamError = error;
        this.streamEnded = true;

        if (this.returned.isEmpty()) {
            this.ended = true;
            this.endEvent.event(error);
        }
    }

    @Override
    public void requestReading(boolean request) {
        if (request) {
            boolean readingRequested = this.doReturnedReading();

            if (readingRequested) {
                this.stream.requestReading();
            }
        } else {
            this.stream.requestReading(false);
        }
    }

    @Override
    public boolean isEnded() {
        return this.ended;
    }

    @Override
    public IAsyncFuture<Object> onEnd() {
        return this.endEvent;
    }

    public void returnInput(IByteReader rest) {
        this.returned.addFirst(rest);
    }

    @Override
    public IAsyncFuture<Object> end() {
        return callback -> {
            this.ended = true;

            this.stream.end().run(callback);

            return AsyncRunningUtil.alreadyFinished();
        };
    }
}
