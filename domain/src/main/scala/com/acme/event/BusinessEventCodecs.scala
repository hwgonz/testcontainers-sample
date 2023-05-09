package com.acme.event

import com.acme.model.RetailProduct
import io.circe.generic.extras.Configuration
import io.circe.generic.extras.semiauto.deriveConfiguredCodec
trait BusinessEventCodecs {

  implicit lazy val config: Configuration = com.acme.serializer.CirceConfig.defaultsWithDiscriminator

  implicit lazy val codecHeader = deriveConfiguredCodec[Header]
  implicit val codecRetailProduct = deriveConfiguredCodec[RetailProduct]
  implicit lazy val codecRetailProductSubmittedEvent = deriveConfiguredCodec[RetailProductEvent]

}
