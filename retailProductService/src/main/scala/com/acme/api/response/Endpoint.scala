package com.acme.api.response

import io.circe.generic.auto._
import sttp.tapir.json.circe._
import sttp.tapir.server.interceptor.decodefailure.{DecodeFailureInterceptor, DefaultDecodeFailureHandler}
import sttp.tapir.server.model.ValuedEndpointOutput

object Endpoint {

  case class DecodeFailure(message: String)

  def decodeFailureResponse[F[_]]: DecodeFailureInterceptor[F] = {
    import sttp.tapir.generic.auto._
    new DecodeFailureInterceptor(
      DefaultDecodeFailureHandler
        .default
        .response(m => ValuedEndpointOutput(jsonBody[DecodeFailure], DecodeFailure(m)))
    )
  }

}
