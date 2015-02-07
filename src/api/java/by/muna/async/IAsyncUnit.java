package by.muna.async;

import by.muna.fun.IUnitFunction;

import java.util.function.Supplier;

public interface IAsyncUnit {
    default IAsyncUnit bind(Supplier<IAsyncUnit> f) {
        return callback -> IAsyncUnit.this.run(() -> f.get().run(callback));
    }

    default IAsyncUnit skip(IAsyncUnit m) {
        return callback -> IAsyncUnit.this.run(() -> m.run(callback));
    }

    public IAsyncRunning run(IUnitFunction callback);
}
