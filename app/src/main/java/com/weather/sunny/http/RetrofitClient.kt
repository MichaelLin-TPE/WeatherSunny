package com.weather.sunny.http

import android.util.Log
import com.hele.hele_news.http.retrofit.CoroutineCallAdapterFactory
import com.weather.sunny.tool.Tool
import io.reactivex.android.schedulers.AndroidSchedulers
import io.reactivex.schedulers.Schedulers
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.Response
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Retrofit
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory
import retrofit2.converter.gson.GsonConverterFactory

object RetrofitClient {

    private var retrofit : Retrofit? = null


    fun getRetrofit():Retrofit{
        if (retrofit == null){
            val logging = HttpLoggingInterceptor()
            logging.setLevel(HttpLoggingInterceptor.Level.BODY)
            val httpClient = OkHttpClient.Builder()
            httpClient.addInterceptor(LoggingInterceptor())
            httpClient.addInterceptor(logging)

            retrofit = Retrofit.Builder()
                .baseUrl(Tool.getBaseUrl())
                .addConverterFactory(GsonConverterFactory.create())
                .addCallAdapterFactory(CoroutineCallAdapterFactory())
                .addCallAdapterFactory(ObserveOnMainCallAdapterFactory(AndroidSchedulers.mainThread()))
                .addCallAdapterFactory(RxJava2CallAdapterFactory.createWithScheduler(Schedulers.io()))
                .client(httpClient.build())
                .build()
            return retrofit as Retrofit
        }
        return retrofit as Retrofit
    }

    private class LoggingInterceptor : Interceptor{
        override fun intercept(chain: Interceptor.Chain): Response {
            val request = chain.request()
            val requestUrl = request.url.toString()
            Log.e("HttpRequest","requestUrl : $requestUrl")

            val response = chain.proceed(chain.request())
            val responseData = response.body?.string()
            if (responseData != null){
                Log.e("HttpRequest","response : $responseData")
            }

            return chain.proceed(request)
        }

    }


}