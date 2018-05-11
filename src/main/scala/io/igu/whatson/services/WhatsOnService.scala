package io.igu.whatson.services

import io.igu.meetup.v2.ConciergeClientComponent
import io.igu.meetup.v2.model.{ConciergeRequest, Event}
import io.igu.meetup.v3.member.ProfileClientComponent

class WhatsOnService {
  self: ProfileClientComponent with ConciergeClientComponent =>

  def eventsInUsersArea(accessToken: String): Seq[Event] = {

    val profile = self.profileClient.self(accessToken)

    val request = ConciergeRequest(city = Some(profile.city))

    conciergeClient.concierge(request, accessToken).results

  }

}
