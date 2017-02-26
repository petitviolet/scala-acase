package net.petitviolet.meta.acase

@Unapply
class UnapplyApp(protected val n: Int, val s: String)

object UnapplyAppApp extends App {
  val target = new UnapplyApp(n = 100, s = "hoge")
  target match {
    case UnapplyApp(n, s) => println(s"$n, $s")
    case _ => sys.error("out!")
  }
}
