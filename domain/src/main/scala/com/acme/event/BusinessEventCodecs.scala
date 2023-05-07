package com.acme.event

import io.circe.generic.extras.semiauto.deriveConfiguredCodec
trait BusinessEventCodecs {

  implicit lazy val codecRetailProductSubmittedEvent = deriveConfiguredCodec[RetailProductSubmittedEvent]

}
