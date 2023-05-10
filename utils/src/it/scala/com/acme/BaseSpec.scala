package com.acme

import org.scalatest.concurrent.Eventually
import org.scalatest.matchers.must.Matchers
import org.scalatest.wordspec.AnyWordSpec

import scala.concurrent.duration._
trait BaseSpec extends AnyWordSpec with Matchers

trait BaseSpecWithEventually extends BaseSpec with Eventually {
  override implicit val patienceConfig: PatienceConfig = PatienceConfig(timeout = 5.seconds)
}
