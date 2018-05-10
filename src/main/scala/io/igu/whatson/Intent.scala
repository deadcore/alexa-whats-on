package io.igu.whatson

import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.{IntentRequest, SpeechletResponse}

trait Intent {
  def ++(right: Intent): Intent = (request: SpeechletRequestEnvelope[IntentRequest]) =>
    this (request).orElse(right(request))

  def apply(request: SpeechletRequestEnvelope[IntentRequest]): Option[SpeechletResponse]

}

object Intent {
  type IntentResolver = SpeechletRequestEnvelope[IntentRequest] => Receiver
  type Receiver = PartialFunction[String, SpeechletResponse]

  def apply(func: Receiver): Intent = (request: SpeechletRequestEnvelope[IntentRequest]) => {
    val intentName = request.getRequest.getIntent.getName

    func.lift(intentName)
  }

  def withRequest(func: SpeechletRequestEnvelope[IntentRequest] => Receiver): Intent = (request: SpeechletRequestEnvelope[IntentRequest]) => {
    val intentName = request.getRequest.getIntent.getName

    func(request).lift(intentName)
  }

}