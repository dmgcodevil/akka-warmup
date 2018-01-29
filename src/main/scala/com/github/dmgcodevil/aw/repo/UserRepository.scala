package com.github.dmgcodevil.aw.repo

import com.github.dmgcodevil.aw.entity.User
import org.mongodb.scala._
import org.mongodb.scala.model.Filters._
import org.mongodb.scala.model.Updates._

import scala.concurrent.{ExecutionContext, Future}

/**
  * Repository for User entity.
  *
  * Created by dmgcodevil on 1/27/2018.
  */
class UserRepository(collection: MongoCollection[User])(implicit ec: ExecutionContext) extends MongoRepository[User](collection) {

  override def update(user: User): Future[Boolean] = {
    val updateCmd = combine(
      Seq(
        Option(user.name).map { _ => set("name", user.name) },
        Option(user.email).map { _ => set("email", user.email) }
      ).filter(op => op.isDefined).map { op => op.get }: _*)

    collection.updateOne(equal("_id", user.id), updateCmd).head().map { res => res.getModifiedCount == 1 }
  }

}
