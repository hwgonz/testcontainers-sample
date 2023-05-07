package com.acme.configuration

import scala.concurrent.duration.Duration

case class HttpServerConfiguration private(
                                     port: Option[Int],
                                     interface: Option[String],
                                     threads: Option[Int],
                                     maxEntitySizeBytes: Option[Int],
                                     responseTimeOutSecs: Option[Long],
                                   )

object HttpServerConfiguration {

  val DefaultPort: Int = 8080
  val DefaultInterface: String = "0.0.0.0"
  val DefaultThreads: Int = 10
  val DefaultTimeout: Duration = org.http4s.server.defaults.ResponseTimeout

  def apply(
             port: Option[Int] = Some(DefaultPort),
             interface: Option[String] = Some(DefaultInterface),
             threads: Option[Int] = Some(DefaultThreads),
             maxEntitySizeBytes: Option[Int] = None,
             responseTimeOutSecs: Option[Long] = Some(DefaultTimeout.toSeconds),
           ): HttpServerConfiguration = {
    val finalPort = if (port.contains(0)) Some(scala.util.Random.between(1025, 65535)) else port
    new HttpServerConfiguration(
      port = finalPort,
      interface = interface,
      threads = threads,
      maxEntitySizeBytes = maxEntitySizeBytes,
      responseTimeOutSecs = responseTimeOutSecs,
    )
  }

}
