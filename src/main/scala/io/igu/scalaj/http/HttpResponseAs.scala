package io.igu.scalaj.http

import io.circe.parser.decode
import io.circe.{Decoder, Error}
import scalaj.http.HttpResponse

trait HttpResponseAs {

  implicit class As(httpResponse: HttpResponse[String]) {
    def as[U: Decoder]: HttpResponse[U] = decode[U](httpResponse.body) match {
      case Left(error: Error) => throw error
      case Right(value)       => httpResponse.copy(body = value)
    }
  }

}
