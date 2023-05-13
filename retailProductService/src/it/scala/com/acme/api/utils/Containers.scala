package com.acme.api.utils

import com.dimafeng.testcontainers._
import com.acme.containers.BaseContainers
import org.scalatest.Suite
import com.dimafeng.testcontainers.lifecycle.and
import org.testcontainers.lifecycle.Startables

trait Containers extends BaseContainers {
  self: Suite =>

  override type Containers = KafkaContainer and GenericContainer and MySQLContainer

  protected lazy val retailProductServiceContainer: GenericContainer = baseAppContainer(
    name = "retail-product-app",
    jarName = "run.jar",
    mainClass = "com.acme.service.RetailProductApp",
    baseFolder = "retailProductService/target/scala-2.13",
    envVars = Map(
      "ENVIRONMENT" -> "local",
    )
  )

  override def startContainers: Containers = {
    Startables.deepStart(kafkaContainer, mySQLContainer).join()
    retailProductServiceContainer.start()
    kafkaContainer and retailProductServiceContainer and mySQLContainer
  }

}
