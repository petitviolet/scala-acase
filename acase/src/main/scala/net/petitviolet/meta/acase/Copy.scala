package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.meta._

@compileTimeOnly("not expanded")
class Copy extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    Copy.insertFrom(defn)
  }
}

trait CopyBase extends InstanceMethodHelper {
  import scala.collection.immutable.Seq

  override protected val METHOD_NAME: String = "copy"

  protected def createCopy(cls: Defn.Class): Defn.Def = {
    val (clsName, clsParamss) = (cls.name, cls.ctor.paramss)

    val mods: Seq[Mod] = Nil
    val name = Term.Name(METHOD_NAME)
    val tParams: Seq[Type.Param] = Nil
    val paramss: Seq[Seq[Term.Param]] =
      clsParamss map { _.map { clsParam =>
        val n: Term.Name = Term.Name(clsParam.name.value)
        val default: Term.Select = q"this.${n}"
        clsParam.copy(mods = Nil, default = Some(default))
      }}

    val body: Term = {
      val ctorName = Ctor.Name(clsName.value)
      val args: Seq[Term.Arg] = {
        paramss flatMap { _.map { p =>
          val termName = Term.Name(p.name.value)
          val arg = Term.Name(p.name.value)
          q"""$termName = $arg"""
        }}
      }
      q"""new $ctorName(..$args)"""
    }

    Defn.Def(mods, name, tParams, paramss, None, body)
  }
}

object Copy extends CopyBase {
  override protected def create(cls: Defn.Class): Defn.Def =
    createCopy(cls)
}
