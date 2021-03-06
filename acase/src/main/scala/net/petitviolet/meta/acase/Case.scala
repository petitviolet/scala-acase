package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

@compileTimeOnly("not expanded")
class Case extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, template), companion: Defn.Object)) =>
        // companion object exists
        val (newCls, newCompanion) = Case.insertMethods(cls, Some(companion))
        Term.Block(Seq(newCls, newCompanion))
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        // companion object does not exists
        val (newCls, newCompanion) = Case.insertMethods(cls, None)
        Term.Block(Seq(newCls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@Unapply must annotate a class.")
    }
  }
}

object Case {
  private def insertInstanceMethods(cls: Defn.Class): Defn.Class = {
    (ToString.insert _ compose Equals.insert compose Copy.insert)(cls)
  }
  private def insertCompanionMethods(cls: Defn.Class, companionOpt: Option[Defn.Object]): Defn.Object = {
    (Apply.insert(cls) _ andThen
      Option.apply andThen
      Unapply.insert(cls))(companionOpt)
  }

  def insertMethods(cls: Defn.Class, companionOpt: Option[Defn.Object]): (Defn.Class, Defn.Object) = {
    (insertInstanceMethods(cls), insertCompanionMethods(cls, companionOpt))
  }

}
