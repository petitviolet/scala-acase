package net.petitviolet.acase.example

import net.petitviolet.meta.acase.{Apply, NoApply}

@Apply
class ApplyApp(value: Int)

object ApplyAppApp extends App {
  println(ApplyApp(100))
}

@NoApply case class NoApplyTarget private (value: Int)

object NoApplyTargetApp extends App {
  // cannot compile
//  println(NoApplyTarget(100))
}