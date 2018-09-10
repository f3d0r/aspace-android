package aspace.trya.api;

import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

public class RetrofitServiceGenerator {

    public static <S> S createService(Class<S> serviceClass, String apiURL) {
        Retrofit.Builder builder =
            new Retrofit.Builder()
                .baseUrl(apiURL)
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .addConverterFactory(JacksonConverterFactory.create());
        Retrofit retrofit = builder.build();
        return retrofit.create(serviceClass);
    }
}
