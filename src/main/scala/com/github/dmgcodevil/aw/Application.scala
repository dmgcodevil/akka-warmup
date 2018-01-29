package com.github.dmgcodevil.aw

import java.util.concurrent.Executors

import akka.actor.ActorSystem
import akka.http.scaladsl.Http
import akka.stream.ActorMaterializer
import com.github.dmgcodevil.aw.util.Helpers._
import com.github.dmgcodevil.aw.endpoint.UserEndpoint
import com.github.dmgcodevil.aw.repo.UserRepository
import com.github.dmgcodevil.aw.util.CustomThreadFactory

import scala.concurrent.ExecutionContext
import scala.util.{Failure, Success}

/**
  * Main application.
  *
  * Created by dmgcodevil on 1/27/2018.
  */
object Application extends App with UserEndpoint {
  implicit val sys: ActorSystem = ActorSystem("akka-http-app")
  implicit val mat: ActorMaterializer = ActorMaterializer()
  implicit val ec: ExecutionContext = sys.dispatcher

  val log = sys.log

  val mongoDbExecutionContext : ExecutionContext = ExecutionContext.fromExecutor(Executors.newFixedThreadPool(10, new CustomThreadFactory("mongodb")))

  log.info("user collection indexes have been created: {}", Mongo.createUserCollectionIndexes().results())

  override val repository: UserRepository = new UserRepository(Mongo.userCollection)(mongoDbExecutionContext)

  Http().bindAndHandle(userRoute, "127.0.0.1", 8080).onComplete {
    case Success(b) => log.info(s"application is up and running at ${b.localAddress.getHostName}:${b.localAddress.getPort}")
    case Failure(e) => log.error(s"could not start application: {}", e.getMessage)
  }
}