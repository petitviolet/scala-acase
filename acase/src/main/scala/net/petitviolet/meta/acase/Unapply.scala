package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

@compileTimeOnly("not expanded")
class Unapply extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      // companion object exists
      case Term.Block(
      Seq(cls @ Defn.Class(_, name, _, ctor, _),
      companion: Defn.Object)) =>
        val newCompanion = Unapply.insert(cls)(Some(companion))
        Term.Block(Seq(cls, newCompanion))
      // companion object does not exists
      case cls @ Defn.Class(_, name, _, ctor, _) =>
        val newCompanion = Unapply.insert(cls)(None)
        Term.Block(Seq(cls, newCompanion))
      case _ =>
        println(defn.structure)
        abort("@Unapply must annotate a class.")
    }
  }
}

object Unapply extends CompanionMethodHelper {
  override protected val METHOD_NAME: String = "unapply"

  override protected def create(cls: Defn.Class)(companionOpt: Option[Defn.Object]): Defn.Def = {
    val (name, paramss) = (cls.name, cls.ctor.paramss)
    val argName = Term.Name("arg")
    val inputParam: Seq[Seq[Term.Param]] = {
      val param: Term.Param = Term.Param(Nil, argName, Some(name), None)
      (param :: Nil) :: Nil
    }
    val resultParam: Type = {
      val types: Seq[Type] = paramss.flatMap { _.collect {
        case Term.Param(_ :: Mod.ValParam() :: Nil, _, Some(typeArg), _) =>
          // private val n: Int
          Type.Name(typeArg.syntax)
        case Term.Param(_ :: Mod.VarParam() :: Nil, _, Some(typeArg), _) =>
          // private var n: Int
          Type.Name(typeArg.syntax)
        case Term.Param(Mod.ValParam() :: Nil, _, Some(typeArg), _) =>
          // val n: Int
          Type.Name(typeArg.syntax)
        case Term.Param(Mod.VarParam() :: Nil, _, Some(typeArg), _) =>
          // var n: Int
          Type.Name(typeArg.syntax)
        case x =>
          println(s"invalid paramss: $paramss")
          abort(s"non-accessible constructor exists. `unapply` always returns None. cause => $x")
      }}

      val tupled = Type.Tuple(types)
      Type.Apply(Type.Name("Option"), tupled :: Nil)
    }

    val body: Term = {
      val select: Seq[Term.Select] = paramss.flatMap { _.map { param =>
        Term.Select(argName, Term.Name(param.name.value))
      } }
      Term.Block(q"Some((..$select))" :: Nil)
    }
    Defn.Def(Nil, Term.Name("unapply"), Nil, inputParam, Some(resultParam), body)
  }

}
