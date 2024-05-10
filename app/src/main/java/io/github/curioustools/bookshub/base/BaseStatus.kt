package io.github.curioustools.bookshub.base

import androidx.annotation.Keep

@Keep
enum class BaseStatus(val code: Int, val msg: String) {
    SUCCESS(200, "SUCCESS"),
    NO_INTERNET_CONNECTION(1001, "No Internet found"),
    NO_INTERNET_PACKETS_RECEIVED(1002, "We are unable to connect to our server. Please check with your internet service provider"),
    USER_NOT_FOUND(400, "User Not Found"),
    NULL_RESPONSE(888, "No Response found"),
    SERVER_FAILURE(500, "server failure"),
    SERVER_DOWN_502(502, "server down 502"),
    SERVER_DOWN_503(503, "server down 503"),
    SERVER_DOWN_504(504, "server down 504"),
    UNRECOGNISED(-1, "unrecognised error in networking");

    fun <T> asResponse():BaseResponse<T>{
        return  BaseResponse.Failure(null,this)
    }

    companion object {
        fun getStatusOrDefault(code: Int? = null): BaseStatus = entries.firstOrNull { it.code == code } ?: UNRECOGNISED

        fun getStatusFromException(t: Throwable): BaseStatus = entries.firstOrNull { it.msg.contentEquals(t.message) } ?: UNRECOGNISED

    }
}