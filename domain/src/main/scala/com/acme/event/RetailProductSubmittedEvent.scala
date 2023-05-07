package com.acme.event

import com.acme.model.RetailProduct

case class RetailProductSubmittedEvent(
                           header: Header,
                           data: RetailProduct,
                         ) extends BusinessEvent[RetailProduct]
