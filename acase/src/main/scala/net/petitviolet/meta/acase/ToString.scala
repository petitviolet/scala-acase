package net.petitviolet.meta.acase

import scala.collection.immutable.Seq
import scala.meta._

/**
 * - before
 * {{{
 * @ToString
 * class ToStringClassA(n: Int, label: String)
 * }}}
 *
 * - after
 * {{{
 * class ToStringClassA(n: Int, label: String) {
 *   override def toString: String = {
 *     "ToStringClassA" + "(" + ("n" + ":" + n.toString + ", " + "label" + ":" + label.toString) + ")"
 *   }
 * }
 * }}}
 */
class ToString extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    defn match {
      case cls @ Defn.Class(_, name, _, ctor, template) =>
        ToString.insert(cls)
      case _ =>
        println(defn.structure)
        abort("@ToString must annotate a class.")
    }
  }
}

object ToString extends InstanceMethodHelper {

  override protected val METHOD_NAME: String = "toString"

  override protected def create(cls: Defn.Class): Defn.Def = {
    val (name, paramss) = (cls.name, cls.ctor.paramss)
    val args: Seq[String] = paramss.flatMap { params: Seq[Term.Param] =>
      params.map { param: Term.Param =>
        // as just a string like `"n"`
        val paramName = s""""${param.name}""""
        // as a term like `n.toString`
        val value = s"${param.name}.toString".parse[Term].get

        // "\"n\" + \": \" + n.toString"
        s"""$paramName + ": " + $value"""
      }
    }

    // "\"n\" + \":\" + n.toString" +
    // ", " +
    // "\"x\" + \":\" + x.toString"
    val joinedParamStrings = args
      .mkString(""" + ", " + """).parse[Term].get

    q"""
       override def ${Term.Name(METHOD_NAME)}: String = {
         ${name.value} + "(" + $joinedParamStrings + ")"
       }
      """
  }
}
