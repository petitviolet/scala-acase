package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

// https://github.com/scalameta/tutorial/tree/master/macros/src/main/scala/scalaworld/macros
@compileTimeOnly("not expanded")
class Apply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _), companion: Defn.Object)) =>
        // companion object exists
        val newCompanion = Apply.insert(cls)(Some(companion))
        Term.Block(Seq(cls, newCompanion))
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        // companion object does not exist
        val newCompanion = Apply.insert(cls)(None)
        Term.Block(Seq(cls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@Apply must annotate a class.")
    }
  }
}

object Apply extends CompanionMethodHelper {
  override protected val METHOD_NAME: String = "apply"

  override protected def create(cls: Defn.Class)(companionOpt: Option[Defn.Object]): Defn.Def = {
    val (name, paramss) = (cls.name, cls.ctor.paramss)
    val args = paramss.map { _.map { param => Term.Name(param.name.value) } }
    val defArgs = paramss.map { _.map { param => param.copy(mods = Nil) }}
    q"""def apply(...$defArgs): $name =
            new ${Ctor.Ref.Name(name.value)}(...$args)"""
  }
}
