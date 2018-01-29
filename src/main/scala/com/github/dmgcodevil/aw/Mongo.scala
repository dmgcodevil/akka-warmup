package com.github.dmgcodevil.aw

import com.github.dmgcodevil.aw.bson.codec.UserCodec
import com.github.dmgcodevil.aw.entity.User
import com.mongodb.ConnectionString
import com.typesafe.config.ConfigFactory
import org.bson.codecs.configuration.CodecRegistries
import org.mongodb.scala.connection.ClusterSettings
import org.mongodb.scala.model.IndexOptions
import org.mongodb.scala.model.Sorts._
import org.mongodb.scala.{MongoClient, MongoClientSettings, MongoCollection, MongoDatabase, Observable}

/**
  * Created by dmgcodevil on 1/27/2018.
  */
object Mongo {
  lazy val config = ConfigFactory.load()
  lazy val codecRegistry = CodecRegistries.fromRegistries(MongoClient.DEFAULT_CODEC_REGISTRY, CodecRegistries.fromCodecs(new UserCodec()))
  lazy val clusterSettings = ClusterSettings.builder().applyConnectionString(new ConnectionString(config.getString("mongo.uri"))).build()
  lazy val settings = MongoClientSettings.builder().clusterSettings(clusterSettings).codecRegistry(codecRegistry).build()
  lazy val mongoClient: MongoClient = MongoClient(settings)

  lazy val database: MongoDatabase = mongoClient.getDatabase(config.getString("mongo.database"))
  lazy val userCollection: MongoCollection[User] = database.getCollection("users")

  def createUserCollectionIndexes(): Observable[Seq[String]] = {
    for {
      nameIndex <- userCollection.createIndex(ascending("name"), new IndexOptions().unique(true) )
      emailIndex <- userCollection.createIndex(ascending("email"), new IndexOptions().unique(true))
    } yield Seq(nameIndex, emailIndex)
  }
}
