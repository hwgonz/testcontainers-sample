package com.acme.api.config

case class HttpClientConfig(
                             connectTimeoutSecs: Int,
                             requestTimeoutSecs: Int,
                             maxRetryWaitMilliSecs: Int,
                             maxRetries: Int,
                           )
