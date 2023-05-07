package com.acme.api.response.failure

import com.acme.api.response.RichResponse

object ErrorResponses {

  type ErrorType = ErrorResponse

  val MessageError = "error"

  trait RichErrorResponse extends RichResponse {
    def errors: Iterable[Error]
  }

  trait ErrorResponse extends RichErrorResponse

}
