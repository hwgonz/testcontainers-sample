package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorResponses._
import sttp.model.{StatusCode => SttpStatusCode}
import sttp.tapir._
import sttp.tapir.json.circe._
object ErrorOutputs {

  val errorOutputs: EndpointOutput.OneOf[ErrorResponse, ErrorResponse] = oneOf(
    oneOfVariantValueMatcher(SttpStatusCode.BadRequest, jsonBody[DefaultErrorResponse]) {
      case _: DefaultErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.BadRequest, jsonBody[BadRequestErrorResponse]) {
      case _: BadRequestErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.Forbidden, jsonBody[DefaultErrorResponse]) {
      case _: DefaultErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.Unauthorized, jsonBody[DefaultErrorResponse]) {
      case _: DefaultErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.InternalServerError, jsonBody[InternalErrorResponse]) {
      case _: InternalErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.ServiceUnavailable, jsonBody[ServiceUnavailableErrorResponse]) {
      case _: ServiceUnavailableErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.GatewayTimeout, jsonBody[GatewayTimeoutErrorResponse]) {
      case _: GatewayTimeoutErrorResponse => true
    },
    oneOfVariantValueMatcher(SttpStatusCode.NotFound, jsonBody[NotFoundErrorResponse]) {
      case _: NotFoundErrorResponse => true
    },
  )

}
