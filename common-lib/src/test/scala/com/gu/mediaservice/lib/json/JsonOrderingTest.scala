package com.gu.mediaservice.lib.json

import com.gu.mediaservice.model.{Asset, FileMetadata, Handout, Image, ImageMetadata, UploadInfo}
import org.joda.time.{DateTime, DateTimeZone}
import org.scalatest.Inside.inside
import org.scalatest.{FreeSpec, Matchers}
import play.api.libs.json.{JsObject, Json}

import java.net.URI

class JsonOrderingTest extends FreeSpec with Matchers {
  /**
    * The order of JSON documents is not strictly in accordance with the RFC but the Play library did maintain it
    * until 2.6.11 and does again from 2.8.0. See https://github.com/playframework/play-json/pull/253
    * This is helpful for debugging and for the super-power-users that look at the API as it means that related fields
    * are grouped together throughout our API.
    * This test is here to prevent a further regression. It does mean that we'll need to jump to Play 2.8 for our next
    * upgrade (which is likely to be what we do anyway...)
    */
  "Play Json writes maintain ordering" in {
    val dt = new DateTime(2021,1,20,12,0,0, DateTimeZone.forID("America/New_York"))
    val image = Image(id = "id",
      uploadTime = dt,
      uploadedBy = "Biden",
      lastModified = None,
      identifiers = Map.empty,
      uploadInfo = UploadInfo(None),
      source = Asset(new URI("fileUri"), None, None, None),
      thumbnail = None,
      optimisedPng = None,
      fileMetadata = FileMetadata(),
      userMetadata = None,
      metadata = ImageMetadata(),
      originalMetadata = ImageMetadata(),
      usageRights = Handout(None),
      originalUsageRights = Handout(None))
    val json = Json.toJson(image)
    inside(json) {
      case jso: JsObject => jso.fields.map(_._1) shouldBe Seq(
        "id",
        "uploadTime",
        "uploadedBy",
        "identifiers",
        "uploadInfo",
        "source",
        "fileMetadata",
        "metadata",
        "originalMetadata",
        "usageRights",
        "originalUsageRights",
        "exports",
        "usages",
        "leases",
        "collections"
      )
    }
  }

}
