package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.meta._

@compileTimeOnly("not expand")
class Equals extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    Equals.insertFrom(defn)
  }
}

object Equals extends InstanceMethodHelper {

  override protected val METHOD_NAME: String = "equals"

  override protected def create(cls: Defn.Class): Defn.Def = {
    val (name, paramss) = (cls.name, cls.ctor.paramss)
    val argName = Term.Name("obj")
    val arg = {
      val inputType = Type.Name("Any")
      Term.Param(Nil, argName, Some(inputType), None)
    }

    val castArgName = Term.Name("other")
    val castArg = q"val ${Pat.Var.Term(castArgName)} = $argName.asInstanceOf[$name]"

    val equalStats = {
      val selfName = Term.Name("this")
      paramss.flatMap { _.map { param: Term.Param =>
        val inputParam = Term.Name(param.name.syntax)
        val selfSelect = Term.Select(selfName, inputParam)
        val otherSelect = Term.Select(castArgName, inputParam)
        q"($selfSelect == $otherSelect)".syntax
      }}.mkString (" && ").parse[Term].get
    }

    val methodName = Term.Name("equals")
    val resultType= Type.Name("Boolean")
    q"""override def $methodName($arg): $resultType = {
        if (!$argName.isInstanceOf[$name]) false
        else {
          $castArg
          $equalStats
        }
       }
     """
  }

}

