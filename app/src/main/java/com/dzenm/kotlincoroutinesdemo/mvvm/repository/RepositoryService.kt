package com.dzenm.kotlincoroutinesdemo.mvvm.repository

import com.dzenm.helper.log.Logger
import com.dzenm.kotlincoroutinesdemo.db.entities.BaseBean
import com.dzenm.kotlincoroutinesdemo.mvvm.exception.ErrorConvert
import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit

/**
 * 网络请求, 调用[createRetrofit]创建Retrofit的实例, 创建之后使用[create]方法创建API service
 * <pre>
 * serviceData.createRetrofit(Api.BASE_URL)
 *          .create(Api::class.java)
 * </pre>
 */
class RepositoryService private constructor() {

    companion object {
        private var instance: RepositoryService? = null
        fun getInstance(): RepositoryService = instance
            ?: synchronized(this) {
                instance ?: RepositoryService().also { instance = it }
            }
    }

    private val TAG: String = this::class.java.simpleName + "| "

    /**
     * 创建Retrofit, 添加自定义Ok http Client[provideOkHttpClient], 添加自定义Log 拦截[provideLoggingInterceptor]
     */
    fun createRetrofit(baseUrl: String): Retrofit = Retrofit.Builder()
        .baseUrl(baseUrl)
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .addConverterFactory(GsonConverterFactory.create())
        .client(provideOkHttpClient(provideLoggingInterceptor()))
        .build()

    /**
     * 创建自定义Ok http Client[createRetrofit]
     */
    private fun provideOkHttpClient(
        interceptor: HttpLoggingInterceptor
    ): OkHttpClient = OkHttpClient.Builder()
        .readTimeout(5000, TimeUnit.SECONDS)
        .connectTimeout(20000, TimeUnit.SECONDS)
        .addInterceptor(interceptor)
        .addInterceptor {
            val original = it.request()
            Logger.d(TAG + "current token is " + "token");
            val request = original.newBuilder()
                .addHeader("Content-Type", "application/json")
                .addHeader("Charset", "UTF-8")
                .addHeader("token", "token")
                .method(original.method(), original.body())
                .build();
            return@addInterceptor it.proceed(request);
        }.build()

    /**
     * 创建自定义Logger拦截器[createApi]
     */
    private fun provideLoggingInterceptor(): HttpLoggingInterceptor =
        HttpLoggingInterceptor(HttpLoggingInterceptor.Logger {
            Logger.d(TAG + it)
        }).apply { level = HttpLoggingInterceptor.Level.BODY }

    /**
     * 开始数据请求[AbsRepository]
     */
    suspend fun <T> handler(
        tag: String,
        call: Call<BaseBean<T>>,
        onSuccess: (res: Response<BaseBean<T>>) -> Unit,
        onError: (t: String, e: ErrorConvert.ResponseException) -> Unit
    ) {
        try {
            val res = handlerWithIO(call)
            if (res.isSuccessful) {
                onSuccess(res)
            } else {
                // 请求结果非200, 服务器异常, 将错误信息和响应码发送出去
                onError(tag, ErrorConvert.getInstance().exception(res.code()))
            }
        } catch (e: Exception) {
            // 如果有IO异常, 那说明是网络有问题, 将异常结果发送出去
            e.printStackTrace()
            onError(tag, ErrorConvert.getInstance().exception(e))
        }
    }

    /**
     * 切换到IO线程进行请求数据[handler]
     */
    private suspend fun <T> handlerWithIO(call: Call<T>): Response<T> =
        withContext(Dispatchers.IO) { call.execute() }
}