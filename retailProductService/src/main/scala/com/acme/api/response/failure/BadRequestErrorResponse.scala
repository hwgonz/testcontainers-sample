package com.acme.api.response.failure

import com.acme.api.response.failure.ErrorCode.e40
import com.acme.api.response.failure.ErrorResponses.ErrorResponse
import io.circe.Codec
import io.circe.generic.semiauto.deriveCodec
import sttp.tapir.Schema

case class BadRequestErrorResponse(
                                    override val message: String = BadRequestErrorResponse.ErrorMessage,
                                    override val code: Option[String] = Some(e40.toString),
                                    override val errors: Iterable[Error] = Iterable.empty,
                                  ) extends ErrorResponse

object BadRequestErrorResponse {

  implicit lazy val codec: Codec[BadRequestErrorResponse] = deriveCodec
  implicit lazy val schema: Schema[BadRequestErrorResponse] = Schema.derived
  private val ErrorMessage: String = "There was an error in the provided input"

  def apply(throwable: Throwable): BadRequestErrorResponse =
    BadRequestErrorResponse(
      errors = Iterable(
        Error(
          code = e40.toString,
          source = None,
          title = Option(ErrorMessage),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )

  def apply(title: String, throwable: Throwable): BadRequestErrorResponse =
    BadRequestErrorResponse(
      errors = Iterable(
        Error(
          code = e40.toString,
          source = None,
          title = Option(title),
          detail = Option(throwable.getMessage)
        )
      ).toList
    )


}
