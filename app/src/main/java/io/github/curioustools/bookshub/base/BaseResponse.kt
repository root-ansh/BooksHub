package io.github.curioustools.bookshub.base

import androidx.annotation.Keep

@Keep
sealed class BaseResponse<T>(open val status: BaseStatus) {

    @Keep
    class Loading<T>:BaseResponse<T>(BaseStatus.UNRECOGNISED)

    @Keep
    data class Success<T>(val body: T) : BaseResponse<T>(BaseStatus.SUCCESS)

    @Keep
    data class Failure<T>(
        val body: T? = null,
        override val status: BaseStatus,
        var exception: Throwable = Exception(status.msg)
    ) : BaseResponse<T>(status)

    companion object {
        fun <T> basicError(msg: String= "something went wrong"): BaseResponse<T> {
            return Failure(null, BaseStatus.UNRECOGNISED, Exception(msg))
        }
        fun <T> errorFromThrowable(t:Throwable): BaseResponse<T> {
            return Failure(null, BaseStatus.UNRECOGNISED, t)
        }

        fun <T> userNotFoundError(): BaseResponse<T> {
            return Failure(null, BaseStatus.USER_NOT_FOUND)
        }
    }
}