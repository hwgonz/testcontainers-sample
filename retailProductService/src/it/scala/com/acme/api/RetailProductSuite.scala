package com.acme.api

import com.acme.BaseSpec
import cats.effect.{IO, Resource}
import com.acme.api.retailproduct.RetailProductSpec
import com.acme.api.utils.Containers
import com.acme.containers.helpers.KafkaHelper
import org.scalatest.{BeforeAndAfterAll, BeforeAndAfterEach}
import org.http4s.client.Client

import scala.concurrent.duration._

class RetailProductSuite extends BaseSpec with Containers
  with KafkaHelper
  with BeforeAndAfterAll with BeforeAndAfterEach
  with RetailProductSpec {

  override val httpClientResource: Resource[IO, Client[IO]] = BlazeHttpClient.client[IO](20.seconds, 20.seconds)

  "Bowtie API" should {
    behave like retailProductServiceTests()
  }

}
