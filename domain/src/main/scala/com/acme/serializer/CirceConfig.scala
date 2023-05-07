package com.acme.serializer

object CirceConfig {

  import io.circe.generic.extras.Configuration

  val Discriminator: String = "type"

  implicit val defaults: Configuration = Configuration
    .default
    .withDefaults

  implicit val defaultsWithSnakeCase: Configuration = defaults
    .withSnakeCaseMemberNames

  implicit val defaultsWithDiscriminator: Configuration = defaults
    .withDiscriminator(Discriminator)

  implicit val defaultsSnakeCaseWithDiscriminator: Configuration = defaults
    .withSnakeCaseMemberNames
    .withDiscriminator(Discriminator)

}

