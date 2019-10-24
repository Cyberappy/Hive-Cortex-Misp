package org.thp.thehive.connector.cortex.models

import java.util.Date

import play.api.libs.json.JsObject

import gremlin.scala.{Edge, Graph, Vertex}
import org.thp.scalligraph.VertexEntity
import org.thp.scalligraph.models._

@VertexEntity
case class Action(
    responderId: String,
    responderName: Option[String],
    responderDefinition: Option[String],
    status: JobStatus.Value,
    objectType: String,
    objectId: String,
    parameters: JsObject,
    startDate: Date,
    endDate: Option[Date],
    report: Option[JsObject],
    cortexId: Option[String],
    cortexJobId: Option[String],
    operations: Seq[JsObject]
)

case class RichAction(action: Action with Entity, context: Entity) {
  val _id: String                         = action._id
  val _createdAt: Date                    = action._createdAt
  val _createdBy: String                  = action._createdBy
  val responderId: String                 = action.responderId
  val responderName: Option[String]       = action.responderName
  val responderDefinition: Option[String] = action.responderDefinition
  val status: JobStatus.Value             = action.status
  val startDate: Date                     = action.startDate
  val endDate: Option[Date]               = action.endDate
  val report: Option[JsObject]            = action.report
  val cortexId: Option[String]            = action.cortexId
  val cortexJobId: Option[String]         = action.cortexJobId
  val operations: Seq[JsObject]           = action.operations
}

case class ActionContext()

object ActionContext extends HasEdgeModel[ActionContext, Action, Product] {

  override val model: Model.Edge[ActionContext, Action, Product] = new EdgeModel[Action, Product] { thisModel =>
    override type E = ActionContext
    override val label: String                                = "ActionContext"
    override val fromLabel: String                            = "Action"
    override val toLabel: String                              = ""
    override val indexes: Seq[(IndexType.Value, Seq[String])] = Nil

    override val fields: Map[String, Mapping[_, _, _]] = Map.empty
    override def toDomain(element: Edge)(implicit db: Database): ActionContext with Entity = new ActionContext with Entity {
      override val _id: String                = element.id().toString
      override val _model: Model              = thisModel
      override val _createdBy: String         = db.getProperty(element, "_createdBy", UniMapping.string)
      override val _updatedBy: Option[String] = db.getProperty(element, "_updatedBy", UniMapping.string.optional)
      override val _createdAt: Date           = db.getProperty(element, "_createdAt", UniMapping.date)
      override val _updatedAt: Option[Date]   = db.getProperty(element, "_updatedAt", UniMapping.date.optional)
    }
    override def create(e: ActionContext, from: Vertex, to: Vertex)(implicit db: Database, graph: Graph): Edge = from.addEdge(label, to)
  }
}