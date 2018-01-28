package com.github.dmgcodevil.aw.endpoint

import akka.http.scaladsl.model.{ContentTypes, HttpEntity, StatusCodes}
import akka.http.scaladsl.server.Route
import akka.http.scaladsl.testkit.ScalatestRouteTest
import akka.stream.Materializer
import com.github.dmgcodevil.aw.Mongo
import com.github.dmgcodevil.aw.endpoint.resource.UserResource
import com.github.dmgcodevil.aw.entity.User
import com.github.dmgcodevil.aw.repo.UserRepository
import com.github.fakemongo.async.FongoAsync
import com.mongodb.async.client.MongoDatabase
import com.typesafe.config.ConfigFactory
import org.bson.types.ObjectId
import org.mongodb.scala.MongoCollection
import org.scalatest.{BeforeAndAfterAll, FeatureSpec, Matchers}

import scala.concurrent.ExecutionContext

/**
  * Created by dmgcodevil on 1/27/2018.
  */
class UserEndpointTest extends FeatureSpec
  with Matchers
  with ScalatestRouteTest
  with BeforeAndAfterAll
  with UserEndpoint {

  override val mat: Materializer = materializer
  override val ec: ExecutionContext = executor

  val db: MongoDatabase = {
    val fongo = new FongoAsync("akka-http-mongodb-microservice")
    val db = fongo.getDatabase(ConfigFactory.load().getString("mongo.database"))
    db.withCodecRegistry(Mongo.codecRegistry)
  }
  override val repository: UserRepository = new UserRepository(MongoCollection(db.getCollection("userCol", classOf[User])))
  val httpEntity: (String) => HttpEntity.Strict = (str: String) => HttpEntity(ContentTypes.`application/json`, str)


  val validUser =
    """
        {
          "name": "user1",
          "email": "user1@mail.com"
        }
    """

  feature("user api") {
    scenario("success creation") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created
      }
    }

    scenario("success get after success creation") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        Get(header("Location").orNull.value()) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.OK
        }
      }
    }


    scenario("invalid id on get") {
      Get(s"/api/users/1") ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    scenario("no body") {
      Post(s"/api/users", httpEntity("{}")) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    scenario("body without email") {
      val invalidUser =
        """
        {
          "name": "user1"
        }
        """

      Post(s"/api/users", httpEntity(invalidUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    scenario("body without name") {
      val invalidUser =
        """
        {
          "email": "user1@email.com"
        }
        """

      Post(s"/api/users", httpEntity(invalidUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    scenario("success delete") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        Delete(header("Location").orNull.value()) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.OK
        }
      }
    }

    scenario("invalid id on delete") {
      Delete("/api/users/1") ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.BadRequest
      }
    }

    scenario("delete nonexistent user") {
      Delete(s"/api/users/${ObjectId.get().toHexString}") ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.NotFound
      }
    }

    scenario("successful update of user name and email") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        val update =
          """
          {
            "name" : "user999",
            "email": "user999@email.com"
          }
          """

        val userUri = header("Location").orNull.value()
        Put(userUri, httpEntity(update)) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.OK
        }

        Get(userUri) ~> Route.seal(userRoute) ~> check {
          val entity = entityAs[UserResource]
          entity.username shouldBe "user999"
          entity.useremail shouldBe "user999@email.com"
        }
      }

    }

    scenario("successful update of user name only") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        val update =
          """
          {
            "name" : "user999"
          }
          """

        val userUri = header("Location").orNull.value()
        Put(userUri, httpEntity(update)) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.OK
        }

        Get(userUri) ~> Route.seal(userRoute) ~> check {
          val entity = entityAs[UserResource]
          entity.username shouldBe "user999"
          entity.useremail shouldBe "user1@mail.com"
        }
      }

    }

    scenario("successful update of user email only") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        val update =
          """
        {
          "email": "user999@mail.com"
        }
        """

        val userUri = header("Location").orNull.value()
        Put(userUri, httpEntity(update)) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.OK
        }

        Get(userUri) ~> Route.seal(userRoute) ~> check {
          val entity = entityAs[UserResource]
          entity.username shouldBe "user1"
          entity.useremail shouldBe "user999@mail.com"
        }
      }

    }

    scenario("user update with empty body") {
      Post(s"/api/users", httpEntity(validUser)) ~> Route.seal(userRoute) ~> check {
        status shouldBe StatusCodes.Created

        val update =
          """
          { }
          """

        val userUri = header("Location").orNull.value()
        Put(userUri, httpEntity(update)) ~> Route.seal(userRoute) ~> check {
          status shouldBe StatusCodes.BadRequest
        }
      }
    }
  }
}
