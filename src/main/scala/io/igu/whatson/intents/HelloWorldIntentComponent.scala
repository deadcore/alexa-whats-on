package io.igu.whatson.intents

import com.amazon.speech.speechlet.SpeechletResponse
import io.igu.whatson.Intent
import io.igu.whatson.Intent.Receiver

trait HelloWorldIntentComponent {

  val helloWorldIntent: HelloWorldIntent

  trait HelloWorldIntent extends Intent {
    def receive: Receiver = {
      case "HelloWorld"        => getHelloResponse
      case "AMAZON.HelpIntent" => getHelloResponse
    }

    private def getHelloResponse = {
      val speechText = "Hello world"
      // Create the Simple card content.
      val card = getSimpleCard("HelloWorld", speechText)
      // Create the plain text output.
      val speech = getPlainTextOutputSpeech(speechText)
      SpeechletResponse.newTellResponse(speech, card)
    }
  }


}