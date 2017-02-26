package net.petitviolet.meta.acase

@Case
class CaseApp(val n: Int, val s: String)

object CaseAppApp extends App {
  val app = CaseApp(10, "hoge")
  println(app.toString)
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
 * }
 * object CaseApp {
 *   def unapply(arg: CaseApp): Option[(Int, String)] = {
 *     Some((arg.n, arg.s))
 *   }
 *
 *   def apply(n: Int, s: String): CaseApp = new CaseApp(n, s)
 * }
 */
