package com.github.dmgcodevil.aw.endpoint.json

import akka.http.scaladsl.marshallers.sprayjson.SprayJsonSupport
import com.github.dmgcodevil.aw.endpoint.request.FindByIdRequest
import com.github.dmgcodevil.aw.endpoint.request.user.{CreateUserRequest, UpdateUserRequest}
import com.github.dmgcodevil.aw.endpoint.resource.UserResource
import spray.json.{DefaultJsonProtocol, JsObject, JsValue, RootJsonFormat}

/**
  * Created by dmgcodevil on 1/27/2018.
  */
trait JsonSupport extends SprayJsonSupport with DefaultJsonProtocol {
  implicit val findByIdRequestFormat = jsonFormat1(FindByIdRequest)
  implicit val deleteByIdRequest = jsonFormat1(FindByIdRequest)
  implicit val createUserRequestFormat = jsonFormat2(CreateUserRequest)
  implicit val userResourceFormat = jsonFormat3(UserResource)

  implicit object ProductItemFormat extends RootJsonFormat[UpdateUserRequest] {
    override def read(json: JsValue): UpdateUserRequest = {
      implicit val jsObject = json.asJsObject
      UpdateUserRequest(getStringField("name"), getStringField("email"))
    }

    override def write(obj: UpdateUserRequest): JsValue =
      throw new UnsupportedOperationException(s"serialization of ${classOf[UpdateUserRequest]} isn't not supported")
  }

  def getStringField(fieldName: String)(implicit jsObject: JsObject): Option[String] =
    jsObject.getFields(fieldName) match {
      case Seq(value) => Option(value.convertTo[String])
      case Seq() => None
    }
}
