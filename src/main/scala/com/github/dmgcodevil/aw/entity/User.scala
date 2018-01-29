package com.github.dmgcodevil.aw.entity

import org.mongodb.scala.bson.ObjectId

/**
  * User entity.
  *
  * Created by dmgcodevil on 1/26/2018.
  */
case class User(override val id: ObjectId, name: String, email: String) extends PersistenceEntity[ObjectId](id) {
}