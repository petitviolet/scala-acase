package net.petitviolet.acase.example

import net.petitviolet.meta.acase.{Copy, NoCopy, ToString}

@ToString @Copy
class CopyApp(val n: Int, val s: String)

object CopyAppApp extends App {
  val c = new CopyApp(10, "hoge")
  c.toString
  assert(c.copy().n == 10)
  assert(c.copy(n = 100).n == 100)
  assert(c.copy(s = "foo").s == "foo")
  val x = c.copy(n = 99, s = "foo")
  assert(x.n == 99 && x.s == "foo")
}

@NoCopy case class NoCopyTarget(value: Int)

object NoCopyApp extends App {
  val c = NoCopyTarget(100)
  // cannot compile
//  println(c.copy(value = 200))
}
