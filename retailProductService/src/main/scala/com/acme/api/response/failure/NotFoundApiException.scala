package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e30

case class NotFoundApiException(
                                 msg: String = "Object not found",
                                 errorCode: ErrorCode.Value = e30,
                               ) extends IllegalArgumentException(msg)