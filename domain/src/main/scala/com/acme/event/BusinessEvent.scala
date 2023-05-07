package com.acme.event

trait BusinessEvent[A] {

  def header: Header

  def data: A

}
