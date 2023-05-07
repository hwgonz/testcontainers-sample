package com.acme.api

import cats.effect._
import cats.implicits._
import com.acme.configuration.ServiceConfiguration
import com.acme.service.RetailProductService
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
object HttpServer {

  val endpoints: Seq[Endpoint[_, _, _, _, _]] = Seq(
    RetailProductEndpoint.endpoint,
  )

  def apply[F[_] : Async](appConfig: ServiceConfiguration, service: RetailProductService[F]): F[Unit] = {
    val interface = appConfig.httpServer.interface.getOrElse(DefaultInterface)
    val port = appConfig.httpServer.port.getOrElse(DefaultPort)
    val responseTimeout = appConfig.httpServer.responseTimeOutSecs.map(_.seconds).getOrElse(DefaultTimeout)
    val threads = appConfig.httpServer.threads.getOrElse(DefaultThreads)

    implicit val serverOptions: Http4sServerOptions[F] = Http4sServerOptions.default[F].prependInterceptor(decodeFailureResponse)

    val routesList = Seq(
      RetailProductEndpoint.routes(service)
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
  }

}
