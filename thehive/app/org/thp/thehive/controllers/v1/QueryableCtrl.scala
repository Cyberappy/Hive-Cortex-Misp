package org.thp.thehive.controllers.v1

import org.thp.scalligraph.EntityIdOrName
import org.thp.scalligraph.query.{ParamQuery, PublicProperties, Query}

trait QueryableCtrl {
  val entityName: String
  val publicProperties: PublicProperties
  val initialQuery: Query
  val pageQuery: ParamQuery[OutputParam]
  val outputQuery: Query
  val getQuery: ParamQuery[EntityIdOrName]
  val extraQueries: Seq[ParamQuery[_]] = Nil
}