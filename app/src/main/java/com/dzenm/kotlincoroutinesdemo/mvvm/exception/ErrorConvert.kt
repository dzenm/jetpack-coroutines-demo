package com.dzenm.kotlincoroutinesdemo.mvvm.exception

import android.net.ParseException
import com.google.gson.JsonParseException
import org.json.JSONException
import java.net.ConnectException
import java.net.SocketTimeoutException
import java.net.UnknownHostException
import javax.net.ssl.SSLHandshakeException

/**
 * 错误
 */
class ErrorConvert private constructor() {

    companion object {
        private var requestException: ErrorConvert? = null
        fun getInstance(): ErrorConvert =
            requestException ?: synchronized(this) {
                requestException ?: ErrorConvert().also {
                    requestException = it
                }
            }
    }

    /**
     * 将前后台中的接口请求返回的结果错误码[code], 错误信息[message]转换为[ResponseException]异常
     */
    fun exception(code: Int, message: String): ResponseException =
        ResponseException().also {
            it.code = code
            it.message = message
        }

    /**
     * 将[Exception]转换为[ResponseException]异常
     */
    fun exception(e: Throwable): ResponseException {
        val ex = ResponseException()
        if (e is JsonParseException ||
            e is JSONException ||
            e is ParseException
        ) {
            ex.code = Code.PARSE
            ex.message = Message.PARSE
        } else if (e is ConnectException
            || e is SocketTimeoutException
        ) {
            ex.code = Code.CONNECT
            ex.message = Message.CONNECT
        } else if (e is UnknownHostException
        ) {
            ex.code = Code.UNKNOWN_HOST
            ex.message = Message.UNKNOWN_HOST
        } else if (e is SSLHandshakeException) {
            ex.code = Code.SSL
            ex.message = Message.SSL
        }
        return ex
    }

    /**
     * 将其他错误信息转换为[ResponseException]异常
     */
    fun exception(any: Any?): ResponseException = ResponseException()

    /**
     * 将错误码[code]转换为[ResponseException]异常
     */
    fun exception(code: Int): ResponseException {
        val ex = ResponseException()
        if (code == Code.UNAUTHORIZED) {
            ex.message = Message.UNAUTHORIZED
        } else if (code == Code.FORBIDDEN) {
            ex.message = Message.FORBIDDEN
        } else if (code == Code.NOT_FOUND) {
            ex.message = Message.NOT_FOUND
        } else if (code == Code.REQUEST_TIMEOUT) {
            ex.message = Message.REQUEST_TIMEOUT
        } else if (code == Code.GATEWAY_TIMEOUT) {
            ex.message = Message.GATEWAY_TIMEOUT
        } else if (code == Code.INTERNAL_SERVER) {
            ex.message = Message.INTERNAL_SERVER
        } else if (code == Code.BAD_GATEWAY) {
            ex.message = Message.BAD_GATEWAY
        } else if (code == Code.SERVICE_UNAVAILABLE) {
            ex.message = Message.SERVICE_UNAVAILABLE
        }
        ex.code = code
        return ex
    }

    object Message {
        const val PARSE = "解析错误, 请联系工作人员"
        const val CONNECT = "无法连接到服务器, 请稍后重试"
        const val UNKNOWN_HOST = "无法连接到服务器, 请检查你的网络"
        const val SSL = "证书验证失败, 请核对证书后再试"

        // 400
        const val UNAUTHORIZED = "未授权"
        const val FORBIDDEN = "访问被禁止"
        const val NOT_FOUND = "未不到内容"
        const val REQUEST_TIMEOUT = "请求超时"

        // 500
        const val INTERNAL_SERVER = "内部服务器错误"
        const val BAD_GATEWAY = "网关错误"
        const val SERVICE_UNAVAILABLE = "服务不可用"
        const val GATEWAY_TIMEOUT = "网关超时"
    }

    object Code {
        const val PARSE = 1001
        const val CONNECT = 1003
        const val UNKNOWN_HOST = 1004
        const val SSL = 1005

        const val UNAUTHORIZED = 401
        const val FORBIDDEN = 403
        const val NOT_FOUND = 404
        const val REQUEST_TIMEOUT = 408

        const val INTERNAL_SERVER = 500
        const val BAD_GATEWAY = 502
        const val SERVICE_UNAVAILABLE = 503
        const val GATEWAY_TIMEOUT = 504
    }

    class ResponseException : Exception() {
        override var message: String = ""
        var code: Int = 0
    }
}