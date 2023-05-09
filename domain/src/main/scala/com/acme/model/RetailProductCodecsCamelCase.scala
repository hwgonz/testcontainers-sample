package com.acme.model

import com.acme.event.BusinessEventCodecs
import io.circe.generic.extras.Configuration
object RetailProductCodecsCamelCase extends BusinessEventCodecs {

  override implicit lazy val config: Configuration = com.acme.serializer.CirceConfig.defaultsWithDiscriminator

}
