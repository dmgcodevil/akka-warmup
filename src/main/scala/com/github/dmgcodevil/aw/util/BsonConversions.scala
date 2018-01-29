package com.github.dmgcodevil.aw.util

import org.mongodb.scala.bson.ObjectId

/**
  * Implicit conversions for BSON types.
  *
  * Created by dmgcodevil on 1/28/2018.
  */
object BsonConversions {
  implicit def hexStringToObjectId(hexString: String): ObjectId = new ObjectId(hexString)

  implicit def objectIdToHexString(objectId: ObjectId): String = objectId.toHexString
}