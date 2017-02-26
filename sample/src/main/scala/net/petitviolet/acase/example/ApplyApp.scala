package net.petitviolet.acase.example

import net.petitviolet.meta.acase.Apply

@Apply
class ApplyApp(value: Int)

object ApplyAppApp extends App {
  println(ApplyApp(100))
}
