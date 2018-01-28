package com.github.dmgcodevil.aw.endpoint.request.user

import com.github.dmgcodevil.aw.entity.User
import org.bson.types.ObjectId

/**
  * Created by dmgcodevil on 1/27/2018.
  */
case class UpdateUserRequest(name: Option[String], email: Option[String]) {

  require(Seq(name, email).exists(op => op.isDefined), "at least one field should be non empty for update")

  name match {
    case Some(value) => require(value.nonEmpty, "user name cannot be empty")
    case _ =>
  }

  email match {
    case Some(value) => require(value.nonEmpty, "user email cannot be empty")
    case _ =>
  }

  def asDomain(id: String): User = {
    require(ObjectId.isValid(id), "the informed id is not a representation of a valid hex string")
    User(new ObjectId(id), name.orNull, email.orNull)
  }

}
