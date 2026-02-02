package io.github.bokchidevchan.core.common

sealed class AppException(
    override val message: String,
    override val cause: Throwable? = null
) : Exception(message, cause) {

    data class NetworkException(
        override val message: String = "Network connection error",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    data class TimeoutException(
        override val message: String = "Request timed out",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    data class ServerException(
        val code: Int,
        override val message: String = "Server error",
        override val cause: Throwable? = null
    ) : AppException(message, cause)

    data class UnknownException(
        override val message: String = "Unknown error",
        override val cause: Throwable? = null
    ) : AppException(message, cause)
}
