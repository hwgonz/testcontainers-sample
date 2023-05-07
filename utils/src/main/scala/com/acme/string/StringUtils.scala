package com.acme.string

object StringUtils {

  def notNullOrEmpty(s: String): Boolean = s != null && s.nonEmpty

  def nullOrEmpty(s: String): Boolean = !notNullOrEmpty(s)

  def capitalize(s: String): String =
    if (notNullOrEmpty(s)) s.substring(0, 1).toUpperCase + s.substring(1)
    else s

}
