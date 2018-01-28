package com.github.dmgcodevil.aw.endpoint

/**
  * Created by dmgcodevil on 1/27/2018.
  */
import akka.http.scaladsl.marshalling.Marshal
import akka.http.scaladsl.model._
import akka.http.scaladsl.model.headers.Location
import akka.http.scaladsl.server.Directives._
import akka.stream.Materializer
import com.github.dmgcodevil.aw.endpoint.json.JsonSupport
import com.github.dmgcodevil.aw.endpoint.request.{DeleteByIdRequest, FindByIdRequest}
import com.github.dmgcodevil.aw.endpoint.request.user.{CreateUserRequest, UpdateUserRequest}
import com.github.dmgcodevil.aw.endpoint.resource.{UserResource, UserResourceFactory}
import com.github.dmgcodevil.aw.repo.UserRepository

import scala.concurrent.Future
import scala.concurrent.ExecutionContext

trait UserEndpoint extends JsonSupport {
  implicit val mat:Materializer
  implicit val ec: ExecutionContext

  val repository: UserRepository

  val userRoute = {
    pathPrefix("api" / "users") {
      get {
        path(Segment).as(FindByIdRequest) { request =>
          complete {
            repository
              .findById(request.id)
              .map { optionalUser => optionalUser.map { UserResourceFactory.create } }
              .flatMap {
                case None => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                case Some(user) => Marshal(user).to[ResponseEntity].map { e => HttpResponse(entity = e) }
              }
          }
        }
      } ~put {
        path(Segment) {
          id => {
            entity(as[UpdateUserRequest]) {
              u => complete {
                repository.update(u.asDomain(id)).map {
                  case true => Future.successful(HttpResponse(status = StatusCodes.custom(200, s"user[id=$id] has been updated")))
                  case false => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                }
              }
            }
          }
        }
      } ~post {
        entity(as[CreateUserRequest]) { user =>
          complete {
            repository
              .save(user.asDomain)
              .map { id =>
                HttpResponse(status = StatusCodes.Created, headers = List(Location(s"/api/users/$id")))
              }
          }
        }
      } ~delete {
        path(Segment).as(DeleteByIdRequest) {
          request => {
            complete {
              repository.deleteById(request.id)
                .map {
                  case true => Future.successful(HttpResponse(status = StatusCodes.custom(200, s"user[id=${request.id}] has been deleted")))
                  case false => Future.successful(HttpResponse(status = StatusCodes.NotFound))
                }
            }
          }
        }
      }
    }
  }
}
