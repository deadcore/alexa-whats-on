package io.igu.whatson


import com.amazon.speech.json.SpeechletRequestEnvelope
import com.amazon.speech.speechlet.{IntentRequest, LaunchRequest, SessionEndedRequest, SessionStartedRequest, SpeechletResponse, _}
import com.amazon.speech.ui.{OutputSpeech, PlainTextOutputSpeech, Reprompt, SimpleCard}
import com.typesafe.scalalogging.LazyLogging
import io.igu.meetup.v3.StatusClientComponent

trait WhatsOnSpeechletComponent {

  val whatsOnSpeechlet: WhatsOnSpeechlet

  trait WhatsOnSpeechlet extends LazyLogging with SpeechletV2 {
    self: StatusClientComponent =>


    def onSessionStarted(requestEnvelope: SpeechletRequestEnvelope[SessionStartedRequest]): Unit = {
      logger.info("onSessionStarted requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any initialization logic goes here
    }

    def onLaunch(requestEnvelope: SpeechletRequestEnvelope[LaunchRequest]): SpeechletResponse = {
      logger.info("onLaunch requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      getWelcomeResponse
    }

    def onIntent(requestEnvelope: SpeechletRequestEnvelope[IntentRequest]): SpeechletResponse = {
      val request = requestEnvelope.getRequest
      logger.info("onIntent requestId={}, sessionId={}", request.getRequestId, requestEnvelope.getSession.getSessionId)
      val intent = Option(request.getIntent)
      val intentName = intent.map(_.getName)

      intentName map {
        case "HelloWorldIntent"  => getHelloResponse
        case "Status"     => getStatusResponse
        case "AMAZON.HelpIntent" => getHelloResponse
        case _                   => askResponse("HelloWorld", "This is unsupported. Please try something else.")
      } getOrElse askResponse("HelloWorld", "This is unsupported. Please try something else.")

    }

    def onSessionEnded(requestEnvelope: SpeechletRequestEnvelope[SessionEndedRequest]): Unit = {
      logger.info("onSessionEnded requestId={}, sessionId={}", requestEnvelope.getRequest.getRequestId, requestEnvelope.getSession.getSessionId)
      // any cleanup logic goes here
    }

    private def getWelcomeResponse = {
      val speechText = "Welcome to the Alexa Skills Kit, you can say hello"
      askResponse("HelloWorld", speechText)
    }

    private def getStatusResponse = {
      val isHealthy = statusClient.status.isHealthy
      val speechText = if (isHealthy) "Everything looks healthy" else "We are experiencing problems with Meetup at the moment"
      askResponse("WATSON.Status", speechText)
    }

    private def getHelloResponse = {
      val speechText = "Hello world"
      // Create the Simple card content.
      val card = getSimpleCard("HelloWorld", speechText)
      // Create the plain text output.
      val speech = getPlainTextOutputSpeech(speechText)
      SpeechletResponse.newTellResponse(speech, card)
    }

    private def getHelpResponse = {
      val speechText = "You can say hello to me!"
      askResponse("HelloWorld", speechText)
    }

    private def getSimpleCard(title: String, content: String) = {
      val card = new SimpleCard
      card.setTitle(title)
      card.setContent(content)
      card
    }

    private def getPlainTextOutputSpeech(speechText: String) = {
      val speech = new PlainTextOutputSpeech
      speech.setText(speechText)
      speech
    }

    private def getReprompt(outputSpeech: OutputSpeech) = {
      val reprompt = new Reprompt
      reprompt.setOutputSpeech(outputSpeech)
      reprompt
    }

    private def askResponse(cardTitle: String, speechText: String) = {
      val card = getSimpleCard(cardTitle, speechText)
      val speech = getPlainTextOutputSpeech(speechText)
      val reprompt = getReprompt(speech)
      SpeechletResponse.newAskResponse(speech, reprompt, card)
    }

  }

}

object WhatsOnSpeechletComponent extends WhatsOnSpeechletComponent {
  val whatsOnSpeechlet: WhatsOnSpeechlet = new WhatsOnSpeechlet with StatusClientComponent {
    override val statusClient: StatusClient = new StatusClient {}
  }
}