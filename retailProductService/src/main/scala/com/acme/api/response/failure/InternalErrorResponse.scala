package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e11
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema
case class InternalErrorResponse(
                                  override val message: String = InternalErrorResponse.ErrorMessage,
                                  override val code: Option[String] = Some(e11.toString),
                                  override val errors: Iterable[Error] = Iterable.empty,
                                ) extends ErrorResponse

object InternalErrorResponse {

  implicit lazy val codec: Codec[InternalErrorResponse] = deriveCodec
  implicit lazy val schema: Schema[InternalErrorResponse] = Schema.derived
  val ErrorMessage: String = "An internal error has occurred"

  def apply(throwable: Throwable): InternalErrorResponse =
    InternalErrorResponse(
      errors = Iterable(
        Error(
          code = e11.toString,
          source = None,
          title = Option(ErrorMessage),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )

  def apply(errorMessage: String, throwable: Throwable): InternalErrorResponse =
    InternalErrorResponse(
      errors = Iterable(
        Error(
          code = e11.toString,
          source = None,
          title = Option(errorMessage),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )

}
