package com.acme.containers

import com.dimafeng.testcontainers._
import com.acme.containers.BaseContainers.{AppPort, WaitStrategyForAPI}
import org.scalatest.Suite
import org.testcontainers.containers.wait.strategy.{HttpWaitStrategy, LogMessageWaitStrategy, WaitAllStrategy, WaitStrategy}
import org.testcontainers.containers.{Network, GenericContainer => JavaGenericContainer}
import org.testcontainers.images.builder.ImageFromDockerfile
import org.testcontainers.utility.DockerImageName

import java.nio.file.Path
import scala.jdk.CollectionConverters._

trait BaseContainers extends ForAllTestContainer {
  self: Suite =>

  protected val sharedNetwork: Network = Network.newNetwork()

  protected lazy val kafkaContainer: KafkaContainer = new KafkaContainer(DockerImageName.parse("confluentinc/cp-kafka:7.0.3")) {
    container.withNetwork(sharedNetwork)
    container.withNetworkAliases("kafka")
  }

  protected def baseAppContainer(
                                  name: String,
                                  jarName: String,
                                  mainClass: String,
                                  baseFolder: String,
                                  port: Int = AppPort,
                                  //waitStrategy: WaitStrategy = WaitStrategyForAPI,
                                  envVars: Map[String, String] = Map.empty
                                ): GenericContainer = new GenericContainer({
    val c = new JavaGenericContainer(
      new ImageFromDockerfile()
        .withFileFromPath(jarName, Path.of(s"$baseFolder/$jarName").toAbsolutePath)
        .withDockerfileFromBuilder { builder =>
          builder
            .from("openjdk:11-jre-slim")
            .copy(jarName, s"/$jarName")
            .cmd("java", "-Xmx256m", "-cp", s"/$jarName", mainClass)
            .build()
        }
    )
    c.dependsOn(kafkaContainer)
    c.withEnv(envVars.asJava)

    c.withEnv("ENVIRONMENT", "local")
    c.withEnv("APP_PORT", port.toString)
    c.withEnv("KAFKA_BOOTSTRAP_SERVERS", "kafka:9092")
    //c.setWaitStrategy(waitStrategy)
    c.withExposedPorts(port)
    c.withNetwork(sharedNetwork)
    c.withNetworkAliases(name)

    c
  })

}

object BaseContainers {

  val AppPort = 9000

  val WaitStrategyForAPI: WaitStrategy = new HttpWaitStrategy().forPath("/health").forStatusCode(200)

  val WaitStrategyForService: WaitStrategy = new WaitAllStrategy()
    .withStrategy(WaitStrategyForAPI)
    .withStrategy(new LogMessageWaitStrategy().withRegEx("^.*Resetting offset for partition.*$"))

}
