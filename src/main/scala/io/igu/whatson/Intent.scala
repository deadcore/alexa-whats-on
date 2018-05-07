package io.igu.whatson

import com.amazon.speech.speechlet.{IntentRequest, SpeechletResponse}
import com.amazon.speech.ui.{OutputSpeech, PlainTextOutputSpeech, Reprompt, SimpleCard}
import io.igu.whatson.Intent.Receiver

trait Intent {

  def receive: Receiver

  def apply(speechletRequest: IntentRequest): SpeechletResponse = {
    val maybeIntent = Option(speechletRequest.getIntent)

    maybeIntent.map(_.getName) match {
      case Some(intent) if receive.isDefinedAt(intent) => receive(intent)
      case _                                           => askResponse("HelloWorld", "This is unsupported. Please try something else.")
    }

  }

  def :+(right: Intent): Intent = {
    val left = this

    new Intent {
      def receive: Receiver = left.receive.orElse(right.receive)
    }
  }

  protected def getSimpleCard(title: String, content: String): SimpleCard = {
    val card = new SimpleCard
    card.setTitle(title)
    card.setContent(content)
    card
  }

  protected def getPlainTextOutputSpeech(speechText: String): PlainTextOutputSpeech = {
    val speech = new PlainTextOutputSpeech
    speech.setText(speechText)
    speech
  }

  protected def getReprompt(outputSpeech: OutputSpeech): Reprompt = {
    val reprompt = new Reprompt
    reprompt.setOutputSpeech(outputSpeech)
    reprompt
  }

  protected def askResponse(cardTitle: String, speechText: String): SpeechletResponse = {
    val card = getSimpleCard(cardTitle, speechText)
    val speech = getPlainTextOutputSpeech(speechText)
    val reprompt = getReprompt(speech)
    SpeechletResponse.newAskResponse(speech, reprompt, card)
  }

}

object Intent {
  type Receiver = PartialFunction[String, SpeechletResponse]
}