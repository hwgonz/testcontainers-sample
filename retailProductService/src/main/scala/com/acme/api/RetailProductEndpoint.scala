package com.acme.api

import cats.data.EitherT
import cats.effect.{Async, Concurrent, Sync}
import com.acme.api.response.failure.ErrorOutputs.errorOutputs
import com.acme.api.response.failure.ErrorResponses.{ErrorResponse, ErrorType}
import com.acme.event.RetailProductEvent
import com.acme.logging.Logging
import fs2.kafka.KafkaProducer
import io.circe.syntax._
import org.http4s.HttpRoutes
import sttp.tapir.json.circe._
import sttp.tapir.server.http4s.{Http4sServerInterpreter, Http4sServerOptions}
import sttp.tapir._
import com.acme.api.HeaderHelper._
import com.acme.api.request.ApiRequest
import com.acme.api.response.success.RetailProductAccepted
import com.acme.api.response.success.SuccessResponses.SuccessType
import com.acme.service.RetailProductService._
import sttp.model.{StatusCode => SttpStatusCode}

import java.time.Instant
import java.util.UUID
object RetailProductEndpoint extends Logging {

  val successAcceptedMapping: EndpointOutput.OneOfVariant[RetailProductAccepted] =
    oneOfVariantValueMatcher(SttpStatusCode.Accepted, jsonBody[RetailProductAccepted]) {
      case _: RetailProductAccepted => true
    }

  val endpoint: Endpoint[Unit, ApiRequest, ErrorType, SuccessType, Any] =
    sttp.tapir.endpoint
      .post
      .in("retailproduct")
      .in(sttp.tapir.headers.and(stringBody).mapTo[ApiRequest])
      .out(oneOf(successAcceptedMapping): EndpointOutput.OneOf[SuccessType, SuccessType])
      .errorOut(errorOutputs)
      .tag("RetailProductService")
      .name("Retail Product Service")

  def routes[F[_] : Async : Concurrent](implicit
                                        kafkaRetailProduct: KafkaProducer.Metrics[F, String, RetailProductEvent],
                                        serverOptions: Http4sServerOptions[F],
                                       ): HttpRoutes[F] =
    Http4sServerInterpreter[F](serverOptions).toRoutes(endpoint.serverLogic(process(_)))
  private def process[F[_] : Sync](payload: ApiRequest, id: UUID = UUID.randomUUID())(implicit kafkaRetailProduct: KafkaProducer.Metrics[F, String, RetailProductEvent]): F[Either[ErrorType, SuccessType]] = {

    val timestamp = Instant.now().toEpochMilli
    val chain: EitherT[F, ErrorType, SuccessType] = for {
      _ <- logEntryT(message = s"Incoming retail product submission, ean=$id", log.info)
      _ <- EitherT(persistRetailProduct(id, payload.json))
    }
    yield
      RetailProductAccepted(
        receivedTimestamp = timestamp,
      )

    chain
      .leftSemiflatTap { e: ErrorResponse =>
        Sync[F].delay(log.error(s"Error while processing retail product submission: ${e.code} - ${e.message}: ${e.errors.asJson.noSpaces}"))
      }
      .value

  }

}
