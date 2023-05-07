package com.acme.api.response.failure

object ErrorCode extends Enumeration {
  val
  e11, // Internal error
  e12, // Timeout
  e14, // Internal error: database problem
  e20, // not authorized
  e30, // Not found error, e.g. 404
  e31, // Not found error, no integration found
  e40, // Input problem, general
  e50, // Service Unavailable
  e100 // Missing field
  = Value
}
