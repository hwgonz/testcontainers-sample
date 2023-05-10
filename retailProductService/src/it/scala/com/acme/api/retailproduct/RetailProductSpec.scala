package com.acme.api.retailproduct

import cats.effect.{IO, Resource}
import cats.effect.unsafe.implicits.global
import com.acme.api.utils.Containers
import com.acme.containers.BaseContainers.AppPort
import com.acme.BaseSpec
import com.acme.api.response.success.RetailProductAccepted
import com.acme.containers.helpers.KafkaHelper
import com.acme.event.RetailProductEvent
import com.acme.model.RetailProduct
import org.http4s.circe.CirceEntityCodec._
import com.acme.model.RetailProductCodecsCamelCase._
import com.acme.kafka.serdes.DomainSerDes.jsonDeserializer
import org.http4s.client.Client
import org.http4s._

import java.util.UUID

trait RetailProductSpec {
  _: BaseSpec with Containers with KafkaHelper =>

  val httpClientResource: Resource[IO, Client[IO]]

  private lazy val url = Uri.unsafeFromString(s"http://${appContainer.host}:${appContainer.mappedPort(AppPort)}/retailproduct")

  def retailProductServiceTests(): Unit =
    "Retail Product endpoint" must {

      "properly emit event" in {

        val retailProduct = RetailProduct(
          id = UUID.fromString("ebc295e1-a678-40e5-88cc-1541bcc40545"),
          name = "Test product",
          description = "A test product"
        )

        val request = Request[IO](
          method = Method.POST,
          uri = url,
        ).withEntity(retailProduct)

        val (response, status) = httpClientResource
          .use(_.run(request).use { response =>
            response.as[RetailProductAccepted].map(_ -> response.status)
          })
          .unsafeRunSync()

        status mustBe Status.Accepted

        //println(appContainer.container.getLogs)

        val event = kafkaConsumer[RetailProductEvent]("retail-product-submitted")
          .collectFirst {
            case record if record.data.id == response.id => record
          }
          .compile
          .lastOrError
          .unsafeRunSync()

      }

    }

}
