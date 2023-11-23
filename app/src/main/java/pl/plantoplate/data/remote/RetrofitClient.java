/*
 * Copyright 2023 the original author or authors
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package pl.plantoplate.data.remote;

import okhttp3.Cache;
import okhttp3.CacheControl;
import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.logging.HttpLoggingInterceptor;
import pl.plantoplate.PlanToPlate;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava3.RxJava3CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.converter.jackson.JacksonConverterFactory;
import java.io.File;
import java.util.concurrent.TimeUnit;

public class RetrofitClient {

    private static final String BASE_URL = "https://plantoplate.lm.r.appspot.com/";
    //private static final String BASE_URL = "http://192.168.0.121:8080";

    private static RetrofitClient instance = null;
    private final Retrofit client;
    private static final long cacheSize = 5 * 1024 * 1024; // 5 MB

    private RetrofitClient() {
        client = new Retrofit.Builder()
                .baseUrl(BASE_URL)
                .client(okHttpClient())
                .addCallAdapterFactory(RxJava3CallAdapterFactory.create())
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
                .connectTimeout(10, TimeUnit.SECONDS)
                .readTimeout(10, TimeUnit.SECONDS)
                .writeTimeout(10, TimeUnit.SECONDS)
                .build();
    }

    private static Cache cache(){
        return new Cache(new File(PlanToPlate.getInstance().getCacheDir(), "cache"), cacheSize);
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
            Request request = chain.request();
            CacheControl cacheControl = new CacheControl.Builder()
                    .maxAge(0, TimeUnit.SECONDS)
                    .build();

            Response response = chain.proceed(request);

            // Update cache with the response
            Response updatedResponse = response.newBuilder()
                    .removeHeader("Pragma")
                    .removeHeader("Cache-Control")
                    .header("Cache-Control", cacheControl.toString())
                    .build();

            // Get the original response's cache control
            CacheControl originalCacheControl = response.cacheControl();

            if (originalCacheControl.maxAgeSeconds() > 0) {
                // Cache the updated response
                CacheControl updatedCacheControl = new CacheControl.Builder()
                        .maxAge(originalCacheControl.maxAgeSeconds(), TimeUnit.SECONDS)
                        .build();
                updatedResponse = updatedResponse.newBuilder()
                        .header("Cache-Control", updatedCacheControl.toString())
                        .build();
            }

            return updatedResponse;
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
