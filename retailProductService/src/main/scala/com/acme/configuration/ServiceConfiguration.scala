package com.acme.configuration

import com.acme.kafka.configuration.KafkaProducerConfiguration
import cats.effect._
import com.typesafe.config.ConfigFactory
import pureconfig.ConfigSource
import pureconfig.generic.auto._

case class ServiceConfiguration(
                                 environment: String,
                                 outputTopic: String,
                                 httpServer: HttpServerConfiguration,
                                 kafkaProducerConfiguration: KafkaProducerConfiguration,
                               )

object ServiceConfiguration {

  def apply[F[_] : Sync](configFile: String = "application.conf"): Resource[F, ServiceConfiguration] =
    Resource.eval(Sync[F].delay(sys.env("ENVIRONMENT")))
      .flatMap(env => load(env, configFile))

  def load[F[_] : Sync](environment: String, configFile: String): Resource[F, ServiceConfiguration] = {
    val fullPath = s"configuration/$environment/$configFile"

    Resource.eval(
      Sync[F].blocking(ConfigSource.fromConfig(ConfigFactory.load(fullPath)).loadOrThrow[ServiceConfiguration])
    )

  }

}
