package io.igu.whatson

import java.io.{ByteArrayInputStream, ByteArrayOutputStream}

import com.typesafe.scalalogging.LazyLogging
import io.circe.parser.parse

object WhatsOnRequestStreamHandlerIntergrationSpec extends App with LazyLogging {

  private val handler = new WhatsOnRequestStreamHandler
  private val outputStream = new ByteArrayOutputStream()

  handler.handleRequest(
    new ByteArrayInputStream(
      s"""{
        |	"version": "1.0",
        |	"session": {
        |		"new": true,
        |		"sessionId": "amzn1.echo-api.session.0",
        |		"application": {
        |			"applicationId": "amzn1.ask.skill.0"
        |		},
        |		"user": {
        |			"userId": "amzn1.ask.account.AB"
        |		}
        |	},
        |	"context": {
        |		"AudioPlayer": {
        |			"playerActivity": "IDLE"
        |		},
        |		"Display": {},
        |		"System": {
        |			"application": {
        |				"applicationId": "amzn1.ask.skill.0"
        |			},
        |			"user": {
        |				"userId": "amzn1.ask.account.AB"
        |			},
        |			"device": {
        |				"deviceId": "amzn1.ask.device.CD",
        |				"supportedInterfaces": {
        |					"AudioPlayer": {},
        |					"Display": {
        |						"templateVersion": "1.0",
        |						"markupVersion": "1.0"
        |					}
        |				}
        |			},
        |			"apiEndpoint": "https://api.eu.amazonalexa.com",
        |			"apiAccessToken": "AAA.BBB.CCC-DDD-EEE-FFF-GGG"
        |		}
        |	},
        |	"request": {
        |		"type": "IntentRequest",
        |		"requestId": "amzn1.echo-api.request.0",
        |		"timestamp": "2018-05-08T06:34:20Z",
        |		"locale": "en-GB",
        |		"intent": {
        |			"name": "Status",
        |			"confirmationStatus": "NONE"
        |		}
        |	}
        |}""".stripMargin.getBytes()),
    outputStream,
    null)

  val json = parse(new String(outputStream.toByteArray)).getOrElse(throw new RuntimeException("Error parsing"))

  logger.info("Json Output: {}", json.toString())

}
