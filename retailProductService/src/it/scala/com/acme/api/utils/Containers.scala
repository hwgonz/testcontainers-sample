package com.acme.api.utils

import com.dimafeng.testcontainers._
import com.acme.containers.BaseContainers
import org.scalatest.Suite

trait Containers extends BaseContainers {
  self: Suite =>

  protected lazy val appContainer: GenericContainer = baseAppContainer(
    name = "retail-product-app",
    jarName = "run.jar",
    mainClass = "com.acme.service.RetailProductApp",
    baseFolder = "retailProductService/target/scala-2.13",
    envVars = Map(
      "ENVIRONMENT" -> "local",
    )
  )

  override val container: Container = MultipleContainers(
    kafkaContainer,
    appContainer,
  )

}
