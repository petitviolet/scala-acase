package net.petitviolet.meta.acase

import scala.collection.immutable.Seq
import scala.annotation.compileTimeOnly
import scala.meta._

@compileTimeOnly("not expanded")
class NoApply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _), companion: Defn.Object)) =>
        // companion object exists
        val newCompanion = NoApply.insert(cls)(Some(companion))
        Term.Block(Seq(cls, newCompanion))
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        // companion object does not exist
        val newCompanion = NoApply.insert(cls)(None)
        Term.Block(Seq(cls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@NoApply must annotate a class.")
    }
  }
}
object NoApply extends ApplyBase {
  override protected def create(cls: Defn.Class)(companionOpt: Option[Defn.Object]): Defn.Def = {
    createApply(cls)(companionOpt).copy(mods = Seq(mod"private"))
  }
}
