package com.acme.api

import cats.effect.{Async, Concurrent, Sync}
import cats.implicits._
import com.acme.api.response.failure.{BadRequestErrorResponse, InternalErrorResponse}
import com.acme.api.response.failure.ErrorOutputs.errorOutputs
import com.acme.api.response.failure.ErrorResponses.ErrorType
import com.acme.service.RetailProductService
import io.circe.Json
import io.circe.syntax._
import org.http4s.HttpRoutes
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.Http4sServerInterpreter
import sttp.tapir._
object RetailProductEndpoint {

  val endpoint: Endpoint[Unit, Json, ErrorType, Json, Any] =
    sttp.tapir.endpoint
      .post
      .in("v1")
      .in("retailproduct")
      .in(jsonBody[Json])
      .out(jsonBody[Json])
      .errorOut(errorOutputs)
      .tag("RetailProductService")
      .name("Retail Product Service")

  def routes[F[_] : Concurrent : Async](service: RetailProductService[F]): HttpRoutes[F] =
    Http4sServerInterpreter[F]().toRoutes(endpoint.serverLogic(process(service)))

  private def process[F[_] : Sync](service: RetailProductService[F])(json: Json): F[Either[ErrorType, Json]] =
    service.enrich(json).map {
      case Right(result) => result.asJson.asRight
      case Left(DecodingError(e)) => BadRequestErrorResponse(e).asLeft
      case Left(UnknownError(e)) => InternalErrorResponse(e).asLeft
    }

}
