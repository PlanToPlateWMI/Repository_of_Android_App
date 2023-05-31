package pl.plantoplate.repository.remote;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.plantoplate.PlanToPlate;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;

import java.io.File;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "https://ambient-elf-336514.lm.r.appspot.com/";
    private static RetrofitClient instance = null;
    private final Retrofit client;
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private RetrofitClient() {
        client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                .addConverterFactory(JacksonConverterFactory.create())
                .addConverterFactory(GsonConverterFactory.create())
                .build();
    }

    private static OkHttpClient okHttpClient(){
        return new OkHttpClient.Builder()
                .cache(cache())
                .addInterceptor(httpLoggingInterceptor()) // used if network off OR on
                .addNetworkInterceptor(networkInterceptor()) // only used when network is on
                .addInterceptor(offlineInterceptor())
                .build();
    }

    private static Cache cache(){
        return new Cache(new File(PlanToPlate.getInstance().getCacheDir(), "someIdentifier"), cacheSize);
    }

    /**
     * This interceptor will be called both if the network is available and if the network is not available
     */
    private static Interceptor offlineInterceptor() {
        return chain -> {
            Request request = chain.request();

            // prevent caching when network is on. For that we use the "networkInterceptor"
            if (!PlanToPlate.hasNetwork()) {
                CacheControl cacheControl = new CacheControl.Builder()
                        .maxStale(7, TimeUnit.DAYS)
                        .build();

                request = request.newBuilder()
                        .removeHeader("Pragme")
                        .removeHeader("Cache-Control")
                        .cacheControl(cacheControl)
                        .build();
            }

            return chain.proceed(request);
        };
    }

    /**
     * This interceptor will be called ONLY if the network is available
     */
    private static Interceptor networkInterceptor() {
        return chain -> {

            Response response = chain.proceed(chain.request());

            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(5, TimeUnit.MINUTES)
                    .build();

            return response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build();
        };
    }

    /**
     * Logging Interceptor
     */
    private static HttpLoggingInterceptor httpLoggingInterceptor ()
    {
        HttpLoggingInterceptor httpLoggingInterceptor = new HttpLoggingInterceptor();
        httpLoggingInterceptor.setLevel( HttpLoggingInterceptor.Level.BODY);
        return httpLoggingInterceptor;
    }

    /** Get instance of retrofit client.
     * @return RetrofitClient
     */
    public static synchronized RetrofitClient getInstance() {
        if (instance == null) {
            instance = new RetrofitClient();
        }
        return instance;
    }

    public Retrofit getClient() {
        return client;
    }
}
