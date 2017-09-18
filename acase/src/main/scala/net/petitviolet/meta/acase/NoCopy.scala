package net.petitviolet.meta.acase

import scala.annotation.compileTimeOnly
import scala.collection.immutable.Seq
import scala.meta._

@compileTimeOnly("not expanded")
class NoCopy extends scala.annotation.StaticAnnotation {
  inline def apply(defn: Any): Any = meta {
    NoCopy.insertFrom(defn)
  }
}

object NoCopy extends CopyBase {
  override protected def create(cls: Defn.Class): Defn.Def =
    createCopy(cls).copy(mods = Seq(mod"private"))
}
