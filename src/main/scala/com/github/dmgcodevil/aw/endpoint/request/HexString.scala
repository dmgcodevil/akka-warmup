package com.github.dmgcodevil.aw.endpoint.request

import org.bson.types.ObjectId

/**
  * Wrapper class for hex string.
  *
  * Created by dmgcodevil on 1/28/2018.
  */
case class HexString(value: String) {
  require(ObjectId.isValid(value), "the informed id is not a representation of a valid hex string")
}
