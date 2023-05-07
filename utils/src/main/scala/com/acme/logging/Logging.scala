package com.acme.logging

import com.acme.string.StringUtils
import org.slf4j.Logger
import org.slf4j.LoggerFactory

import scala.util.Try
import java.nio.charset.Charset
import java.util.Base64

trait Logging {

  val log: Logger = LoggerFactory.getLogger(this.getClass)

  object LogLevel extends Enumeration {
    val error, debug, info, trace, warn = Value
  }

  def logBase64(msgFmt: String, message: String, charSet: Charset, level: LogLevel.Value): Either[Throwable, Unit] =
    if (StringUtils.nullOrEmpty(msgFmt) || !msgFmt.contains("%"))
      Left(new IllegalArgumentException(
        """MsgFmt must be a not null and non empty format string.
          |For more information: https://docs.oracle.com/en/java/javase/18/docs/api/java.base/java/util/Formatter.html
          |""".stripMargin
      ))
    else Try {
      val base64: String = new String(Base64.getEncoder.encode(message.getBytes(charSet)), charSet)
      val body: String = msgFmt.format(base64)
      level match {
        case LogLevel.error => log.error(body)
        case LogLevel.debug => log.debug(body)
        case LogLevel.info => log.info(body)
        case LogLevel.trace => log.trace(body)
        case LogLevel.warn => log.warn(body)
      }
    }.toEither

}
