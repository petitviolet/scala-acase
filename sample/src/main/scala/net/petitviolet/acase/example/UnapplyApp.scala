package net.petitviolet.acase.example

import net.petitviolet.meta.acase.Unapply

@Unapply
class MyClass(val n: Int, val s: String)

object UnapplyApp extends App {
  val target = new MyClass(n = 100, s = "hoge")
  target match {
    case MyClass(n, s) => println(s"$n, $s")
    case _ => sys.error("out!")
  }
}
