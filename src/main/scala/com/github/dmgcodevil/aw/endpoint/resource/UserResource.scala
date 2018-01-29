package com.github.dmgcodevil.aw.endpoint.resource

import com.github.dmgcodevil.aw.entity.User

/**
  * User resource.
  *
  * Created by dmgcodevil on 1/27/2018.
  */
final case class UserResource(id: String, username: String, useremail: String) {}

object UserResourceFactory {
  def create: User => UserResource = (user: User) => UserResource(user.id.toHexString, user.name, user.email)
}