package com.acme.api

import cats.effect._
import cats.implicits._
import com.acme.configuration.ServiceConfiguration
import org.http4s.server.Router
import org.http4s.blaze.server.BlazeServerBuilder
import sttp.tapir.Endpoint
import sttp.tapir.docs.openapi.OpenAPIDocsInterpreter
import sttp.apispec.openapi.circe.yaml._
import sttp.tapir.server.http4s._
import sttp.tapir.swagger.SwaggerUI

import java.util.concurrent.Executors
import scala.concurrent.ExecutionContext
import scala.concurrent.duration._
import com.acme.api.response.Endpoint.decodeFailureResponse
import com.acme.configuration.HttpServerConfiguration.{DefaultInterface, DefaultPort, DefaultThreads, DefaultTimeout}
import com.acme.event.RetailProductEvent
import fs2.kafka.KafkaProducer
object HttpServer {

  val endpoints: Seq[Endpoint[_, _, _, _, _]] = Seq(
    RetailProductEndpoint.endpoint,
  )

  def apply[F[_] : Async](appConfig: ServiceConfiguration)(implicit
                                                           kafkaSuccessRetailProduct: KafkaProducer.Metrics[F, String, RetailProductEvent]): F[ExitCode] = {
    val interface = appConfig.httpServer.interface.getOrElse(DefaultInterface)
    val port = appConfig.httpServer.port.getOrElse(DefaultPort)
    val responseTimeout = appConfig.httpServer.responseTimeOutSecs.map(_.seconds).getOrElse(DefaultTimeout)
    val threads = appConfig.httpServer.threads.getOrElse(DefaultThreads)

    implicit val serverOptions: Http4sServerOptions[F] = Http4sServerOptions.default[F].prependInterceptor(decodeFailureResponse)

    val routesList = Seq(
      RetailProductEndpoint.routes
    )

    val docsAsYaml = OpenAPIDocsInterpreter().toOpenAPI(endpoints, "Retail Product API", "1.0").toYaml
    val swaggerRoutes = Http4sServerInterpreter[F]().toRoutes(SwaggerUI[F](docsAsYaml))

    val routes = routesList.reduce((a, b) => a <+> b)
    val router = Router("/" -> (routes <+> swaggerRoutes))

    val ec = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(threads))

    BlazeServerBuilder[F]
      .withExecutionContext(ec)
      .bindHttp(port, interface)
      .withResponseHeaderTimeout(responseTimeout)
      .withHttpApp(router.orNotFound)
      .serve
      .compile
      .drain
      .as(ExitCode.Success)
  }

}
