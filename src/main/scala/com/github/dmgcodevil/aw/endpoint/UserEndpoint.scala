package com.github.dmgcodevil.aw.endpoint


import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.github.dmgcodevil.aw.endpoint.json.JsonSupport
import com.github.dmgcodevil.aw.endpoint.request.user.{CreateUserRequest, UpdateUserRequest}
import com.github.dmgcodevil.aw.endpoint.request.HexString
import com.github.dmgcodevil.aw.endpoint.resource.UserResourceFactory
import com.github.dmgcodevil.aw.repo.UserRepository
import com.github.dmgcodevil.aw.util.BsonConversions._

import scala.concurrent.{ExecutionContext, Future}


trait UserEndpoint extends JsonSupport {
  implicit val mat: Materializer
  implicit val ec: ExecutionContext

  val repository: UserRepository

  val userRoute = {
    pathPrefix("api" / "users") {
      get {
        path(Segment).as(HexString) { id =>
          complete {
            repository
              .findById(id.value)
              .map { optionalUser => optionalUser.map { UserResourceFactory.create } }
              .flatMap {
                case None => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                case Some(user) => Marshal(user).to[ResponseEntity].map { e => HttpResponse(entity = e) }
              }
          }
        }
      } ~ put {
        path(Segment).as(HexString) {
          id => {
            entity(as[UpdateUserRequest]) {
              request =>
                complete {
                  repository.update(request.asDomain(id.value)).map {
                    case true => Future.successful(HttpResponse(status = StatusCodes.custom(200, s"user[id=$id] has been updated")))
                    case false => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                  }
                }
            }
          }
        }
      } ~ post {
        entity(as[CreateUserRequest]) { user =>
          complete {
            repository
              .save(user.asDomain)
              .map { id =>
                HttpResponse(status = StatusCodes.Created, headers = List(Location(s"/api/users/$id")))
              }
          }
        }
      } ~ delete {
        path(Segment).as(HexString) {
          id => {
            complete {
              repository.deleteById(id.value)
                .map {
                  case true => Future.successful(HttpResponse(status = StatusCodes.custom(200, s"user[id=${id.value}] has been deleted")))
                  case false => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                }
            }
          }
        }
      }
    }
  }
}
