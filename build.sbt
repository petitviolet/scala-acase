import sbt.Keys._

val libVersion = "0.5.0"

val groupId = "net.petitviolet"

val projectName = "acase"

val scala = "2.12.2"

crossScalaVersions := Seq("2.11.11", "2.12.2")

// https://github.com/scalameta/sbt-macro-example/blob/master/build.sbt
lazy val metaMacroSettings: Seq[Def.Setting[_]] = Seq(
  resolvers += Resolver.sonatypeRepo("releases"),
  resolvers += Resolver.bintrayIvyRepo("scalameta", "maven"),
  addCompilerPlugin("org.scalameta" % "paradise" % "3.0.0-M9" cross CrossVersion.full),
  libraryDependencies ++= Seq(
    "org.scala-lang" % "scala-reflect" % scalaVersion.value % "provided",
    "org.scala-lang" % "scala-compiler" % scalaVersion.value % "provided"
  ),
  scalacOptions ++= Seq(
    "-Xplugin-require:macroparadise",
    "-Ymacro-debug-lite"
  )
)

def commonSettings(prjName: String) = Seq(
  name := prjName,
  scalaVersion := scala,
  version := libVersion,
  organization := groupId
)

lazy val root = (project in file("."))
  .aggregate(acase, sample)

lazy val acase = (project in file("acase"))
  .settings(commonSettings(projectName))
  .settings(metaMacroSettings)
  .settings(libraryDependencies += "org.scalameta" %% "scalameta" % "1.8.0")// % "provided")

lazy val sample = (project in file("./sample"))
  .settings(commonSettings("sample"))
  .settings(metaMacroSettings)
//    .settings(libraryDependencies += groupId %% projectName % libVersion % "provided")
  .dependsOn(acase)
