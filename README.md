# @Case annotation let a class to case class

This repository provides a utility annotation.

# @Case annotation

`@Case` is an annotation to insert whole methods as a case class to instance itself and companion object.

Before expansion.

```scala
@Case class CaseApp(val n: Int, val s: String)
```

After compilation and expansion.

```scala
class CaseApp(val n: Int, val s: String) {
  override def toString: String = {
    "CaseApp" + "(" + ("n" + ": " + n.toString + ", " + "s" + ": " + s.toString) + ")"
  }

  override def equals(obj: Any): Boolean = {
    if (!obj.isInstanceOf[CaseApp]) false else {
      val other = obj.asInstanceOf[CaseApp]
      this.n == other.n && this.s == other.s
    }
  }
}
object CaseApp {
  def unapply(arg: CaseApp): Option[(Int, String)] = {
    Some((arg.n, arg.s))
  }

  def apply(n: Int, s: String): CaseApp = new CaseApp(n, s)
}
```

# each annotations as a case class

- @Apply
- @Unapply
- @ToString
- @Equals
