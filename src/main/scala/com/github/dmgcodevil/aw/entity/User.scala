package com.github.dmgcodevil.aw.entity

import org.bson.types.ObjectId

/**
  * Created by dmgcodevil on 1/26/2018.
  */
case class User(id: ObjectId, name: String, email: String) {
}