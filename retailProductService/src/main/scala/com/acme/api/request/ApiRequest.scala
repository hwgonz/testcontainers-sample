package com.acme.api.request

case class ApiRequest(
                       headers: List[sttp.model.Header],
                       json: String
                     )
