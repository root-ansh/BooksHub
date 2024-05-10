package io.github.curioustools.bookshub.base

import retrofit2.Call
import retrofit2.Response

/**
 * Retrofit provides a response of format Response(isSuccessful:True/False, body:T/null,...)
 * it treats all failures as null . this Response object on its own is enough to know about the
 * json response, but for convenience we can use a unified sealed class for handling high level
 * distinctions,such as success, failure, token expire failure etc.
 * */
fun <T> Call<T>.executeAndUnify(enableLogging: Boolean = false): BaseResponse<T> {
    return try {
        val response: Response<T?> = this.execute()
        //if (enableLogging) { }
        when {
            response.isSuccessful -> {
                when (val body = response.body()) {
                    null -> BaseResponse.Failure(body, BaseStatus.NULL_RESPONSE)
                    else -> BaseResponse.Success(body)
                }
            }
            else -> {
                val code = response.code()
                val body = response.body()
                val status = BaseStatus.getStatusOrDefault(code)
                val exception = Exception(status.msg)
                val resp = BaseResponse.Failure(body, status, exception)
                resp.exception = exception
                resp
            }
        }
    }
    catch (t: Throwable) {
        BaseResponse.Failure(null, BaseStatus.getStatusFromException(t), t)
    }

}