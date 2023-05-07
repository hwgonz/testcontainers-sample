package com.acme.api.response

trait Response

object Response

trait RichResponse extends Response {
  def message: String

  def code: Option[String]
}