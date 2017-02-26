logLevel := Level.Warn

resolvers += Resolver.bintrayIvyRepo("scalameta", "maven")
resolvers += Resolver.typesafeRepo("releases")
resolvers += Resolver.jcenterRepo
resolvers += Classpaths.sbtPluginReleases

// JMH
addSbtPlugin("io.get-coursier" % "sbt-coursier" % "1.0.0-M15")

// Formatter plugins
addSbtPlugin("com.typesafe.sbt" % "sbt-scalariform" % "1.3.0")

// Assembly
addSbtPlugin("com.eed3si9n" % "sbt-assembly" % "0.14.4")

addSbtPlugin("org.xerial.sbt" % "sbt-sonatype" % "1.1")

addSbtPlugin("com.jsuereth" % "sbt-pgp" % "1.0.0") // fot sbt-0.13.5 or higher

