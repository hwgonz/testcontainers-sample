package com.acme.service

import com.acme.logging.Logging
import cats.effect.{ExitCode, IO, IOApp}
import com.acme.api.HttpServer
import com.acme.event.RetailProductSubmittedEvent
import com.acme.kafka.serdes.DomainSerDes
import fs2.kafka.Serializer
import com.acme.event.RetailProductCodecsCamelCase._

object RetailProductApp extends IOApp with DomainSerDes with Logging {

  implicit val serializer: Serializer[IO, RetailProductSubmittedEvent] = serializer[IO, RetailProductSubmittedEvent]

  override def run(args: List[String]): IO[ExitCode] =
    ServiceResources
      .resources[IO]
      .use { r =>
        val service = new RetailProductService[IO]

        val processor = new RetailProductProcessor[IO](service, r.kafkaClient)
        val processorStream = processor.start(r.serviceConfiguration.outputTopic)

        val httpServer = HttpServer[IO](r.serviceConfiguration, service)

        IO(log.info(s"ENVIRONMENT=${r.serviceConfiguration.environment}")) *>
          (httpServer &>
            processorStream)
      }
      .as(ExitCode.Success)

}
