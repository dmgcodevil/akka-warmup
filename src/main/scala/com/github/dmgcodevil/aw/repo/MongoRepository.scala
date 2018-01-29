package com.github.dmgcodevil.aw.repo

import com.github.dmgcodevil.aw.entity.PersistenceEntity
import org.mongodb.scala.MongoCollection
import org.mongodb.scala.bson.ObjectId
import org.mongodb.scala.model.Filters.equal

import scala.concurrent.{ExecutionContext, Future}
import scala.reflect.ClassTag

/**
  * Abstract MongoDB repo that provides implementations for common methods.
  *
  * Created by dmgcodevil on 1/28/2018.
  */
abstract class MongoRepository[A <: PersistenceEntity[ObjectId]](collection: MongoCollection[A])
                                                                (implicit ct: ClassTag[A], ec: ExecutionContext) extends Repository[A, ObjectId] {

  override def save(entity: A): Future[ObjectId] = collection.insertOne(entity).head.map { _ => entity.id }

  override def findById(id: ObjectId): Future[Option[A]] = collection.find(equal("_id", id)).first.head.map(Option(_))

  override def deleteById(id: ObjectId): Future[Boolean] = collection.deleteOne(equal("_id", id)).head.map { res => res.getDeletedCount == 1 }
}