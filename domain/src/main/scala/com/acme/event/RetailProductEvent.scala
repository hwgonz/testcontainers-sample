package com.acme.event

import com.acme.model.RetailProduct

import java.time.Instant
import java.util.UUID

case class RetailProductEvent(
                           header: Header,
                           data: RetailProduct,
                         ) extends BusinessEvent[RetailProduct]

object RetailProductEvent {

  def apply(
             retailProduct: RetailProduct,
           ): RetailProductEvent = RetailProductEvent(
    header = Header(
      eventTimestamp = Instant.now.toEpochMilli,
      eventId = Some(UUID.randomUUID()),
    ),
    data = retailProduct,
  )

}
