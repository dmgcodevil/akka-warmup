package com.github.dmgcodevil.aw.endpoint.request

import org.bson.types.ObjectId

/**
  * Created by dmgcodevil on 1/27/2018.
  */
final case class FindByIdRequest(id: String) {
  require(ObjectId.isValid(id), "the informed id is not a representation of a valid hex string")
}
