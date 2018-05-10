package io.igu.meetup.v2.model

case class ConciergeRequest(category_id: Option[String] = None,
                            city: Option[String] = None,
                            country: Option[String] = None,
                            fields: Option[String] = None,
                            lat: Option[String] = None,
                            lon: Option[String] = None,
                            page_token: Option[String] = None,
                            radius: Option[String] = None,
                            self_groups: Option[String] = None,
                            state: Option[String] = None,
                            text_format: Option[String] = None,
                            time: Option[String] = None,
                            topic_id: Option[String] = None,
                            with_friends: Option[String] = None,
                            zip: Option[String] = None) {

  def flatternToMap: Map[String, String] = Map(
    "category_id" -> category_id,
    "city" -> city,
    "country" -> country,
    "fields" -> fields,
    "lat" -> lat,
    "lon" -> lon,
    "page_token" -> page_token,
    "radius" -> radius,
    "self_groups" -> self_groups,
    "state" -> state,
    "text_format" -> text_format,
    "time" -> time,
    "topic_id" -> topic_id,
    "with_friends" -> with_friends,
    "zip" -> zip,
  ).
    filter(_._2.isDefined).
    mapValues(_.get)

}