package com.github.dmgcodevil.aw.repo

import com.github.dmgcodevil.aw.entity.User
import org.bson.types.ObjectId
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Created by dmgcodevil on 1/27/2018.
  */
class UserRepository(collection: MongoCollection[User])(implicit ec: ExecutionContext) {

  def save(user: User): Future[String] = collection.insertOne(user).head.map { _ => user.id.toHexString }

  def findById(id: String): Future[Option[User]] = collection.find(equal("_id", new ObjectId(id))).first.head.map(Option(_))

  def update(user: User): Future[Boolean] = {

    val update = combine(
      Seq(
        Option(user.name).map { _ => set("name", user.name) },
        Option(user.email).map { _ => set("email", user.email) }
      ).filter(op => op.isDefined).map { op => op.get }: _*)

    collection.updateOne(equal("_id", user.id), update).head().map { res => res.getModifiedCount == 1 }

  }

  def deleteById(id: String): Future[Boolean] = collection.deleteOne(equal("_id", new ObjectId(id))).head.map { res => res.getDeletedCount == 1 }

}
