package com.acme.model

import io.circe.generic.extras.semiauto.deriveConfiguredCodec

trait InventoryCodecs {

  implicit val codecRetailProduct = deriveConfiguredCodec[RetailProduct]
  implicit val codecStockItem = deriveConfiguredCodec[StockItem]
  implicit val codecWarehouse = deriveConfiguredCodec[Warehouse]

}
