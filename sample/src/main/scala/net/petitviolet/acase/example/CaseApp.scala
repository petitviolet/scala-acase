package net.petitviolet.acase.example

import net.petitviolet.meta.acase._

@Case class CaseApp(val n: Int, val s: String)

object CaseAppApp extends App {
  val app = CaseApp(10, "hoge")
  println(app.toString)
  val r = app.copy(n = 100) match {
    case c @ CaseApp(nn, ss) => c == CaseApp(nn, ss)
  }
  assert(r == true)
}
/**
 * class CaseApp(val n: Int, val s: String) {
 *   override def toString: String = {
 *     "CaseApp" + "(" + ("n" + ": " + n.toString + ", " + "s" + ": " + s.toString) + ")"
 *   }
 *
 *   override def equals(obj: Any): Boolean = {
 *     if (!obj.isInstanceOf[CaseApp]) false else {
 *       val other = obj.asInstanceOf[CaseApp]
 *       this.n == other.n && this.s == other.s
 *     }
 *   }
 *   def copy(n = this.n, s = this.s) = new CaseApp(n, s)
 * }
 * object CaseApp {
 *   def unapply(arg: CaseApp): Option[(Int, String)] = {
 *     Some((arg.n, arg.s))
 *   }
 *
 *   def apply(n: Int, s: String): CaseApp = new CaseApp(n, s)
 * }
 */

@Equals @ToString @Copy @Unapply
class Hoge(val n: Int, val s: String)

object HogeApp extends App {
  val h = new Hoge(10, "foo")
  println(h)
  h.copy(n = 100, s = "bar") match {
    case x @ Hoge(a, b) => assert(x == new Hoge(a, b))
  }
}
