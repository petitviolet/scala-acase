package net.petitviolet.acase.example

import net.petitviolet.meta.acase.ToString

class ToStringClass(n: Int, label: String)

@ToString
class ToStringClassA(n: Int, label: String)

@ToString
class HasToString(n: Int, label: String) {
  override def toString: String = "yes"
}

private object ToStringApp extends App {
  val no = new ToStringClass(1, "non-annotated")
  println(no.toString)

  val annotated = new ToStringClassA(2, "annotated")
  println(annotated.toString)
}
