package com.weather.sunny.http;

import java.lang.annotation.Annotation;
import java.lang.reflect.Type;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import io.reactivex.Observable;
import io.reactivex.Scheduler;
import retrofit2.Call;
import retrofit2.CallAdapter;
import retrofit2.Retrofit;

final class ObserveOnMainCallAdapterFactory extends CallAdapter.Factory {
    private final Scheduler scheduler;

    ObserveOnMainCallAdapterFactory(Scheduler scheduler) {
        this.scheduler = scheduler;
    }

    @Override
    public @Nullable
    CallAdapter<?, ?> get(
            @NonNull Type returnType, @NonNull Annotation[] annotations, @NonNull Retrofit retrofit) {
        if (getRawType(returnType) != Observable.class) {
            return null; // Ignore non-Observable types.
        }

        // Look up the next call adapter which would otherwise be used if this one was not present.
        //noinspection unchecked returnType checked above to be Observable.
        final CallAdapter<Object, Observable<?>> delegate =
                (CallAdapter<Object, Observable<?>>) retrofit.nextCallAdapter(this, returnType,
                        annotations);

        return new CallAdapter<Object, Object>() {
            @Override
            public Object adapt(@NonNull Call<Object> call) {
                // Delegate to get the normal Observable...
                Observable<?> o = delegate.adapt(call);
                // ...and change it to send notifications to the observer on the specified scheduler.
                return o.observeOn(scheduler);
            }

            @Override
            public Type responseType() {
                return delegate.responseType();
            }
        };
    }
}
