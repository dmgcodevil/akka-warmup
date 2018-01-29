package com.github.dmgcodevil.aw.endpoint.request.user

import com.github.dmgcodevil.aw.entity.User
import org.bson.types.ObjectId

/**
  * Request to update an user.
  *
  * Created by dmgcodevil on 1/27/2018.
  */
case class UpdateUserRequest(name: Option[String], email: Option[String]) {

  require(Seq(name, email).exists(op => op.isDefined), "at least one field should be non empty for update")

  private def validateString = (opt: Option[String]) => opt match {
    case Some(value) => require(value.nonEmpty, "user name cannot be empty")
    case _ =>
  }

  validateString(name)
  validateString(email)

  def asDomain(id: ObjectId): User = {
    User(id, name.orNull, email.orNull)
  }

}
