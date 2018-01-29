package com.github.dmgcodevil.aw.repo

import scala.concurrent.Future

/**
  * Generic trait for repositories.
  *
  * Created by dmgcodevil on 1/28/2018.
  */
trait Repository[A, IdType] {
  def findById(id: IdType): Future[Option[A]]

  def save(a: A): Future[IdType]

  def deleteById(id: IdType): Future[Boolean]

  def update(a: A): Future[Boolean]
}