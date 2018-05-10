package io.igu.whatson.intents

import com.amazon.speech.speechlet.SpeechletResponse
import io.igu.whatson.{Intent, ResponseSupport}

trait HelloWorldIntentComponent {

  val helloWorldIntent: HelloWorldIntent

  trait HelloWorldIntent {

    def helloWorld = Intent {
      case "HelloWorld"        => getHelloResponse
      case "AMAZON.HelpIntent" => getHelloResponse
    }

    private def getHelloResponse = {
      val speechText = "Hello world"
      // Create the Simple card content.
      val card = ResponseSupport.getSimpleCard("HelloWorld", speechText)
      // Create the plain text output.
      val speech = ResponseSupport.getPlainTextOutputSpeech(speechText)
      SpeechletResponse.newTellResponse(speech, card)
    }
  }


}