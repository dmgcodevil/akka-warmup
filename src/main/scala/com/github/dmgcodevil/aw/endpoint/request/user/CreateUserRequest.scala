package com.github.dmgcodevil.aw.endpoint.request.user

import com.github.dmgcodevil.aw.entity.User
import org.bson.types.ObjectId

/**
  * Request to create new user.
  *
  * Created by dmgcodevil on 1/27/2018.
  */
final case class CreateUserRequest(name: String, email: String) {

  require(name != null, "user name not informed")
  require(name.nonEmpty, "user name cannot be empty")
  require(email != null, "user email not informed")
  require(email.nonEmpty, "user email cannot be empty")

  def asDomain = User(ObjectId.get(), name, email)
}
