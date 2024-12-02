lazy val sbtTypelevelVersion = "0.6.7"
addSbtPlugin("org.typelevel" % "sbt-typelevel-ci" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-ci-signing" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-sonatype-ci-release" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-github" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-settings" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-mergify" % sbtTypelevelVersion)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.16.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.3.2")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.5.1")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")

addSbtPlugin("io.chrisdavenport" % "sbt-http4s-grpc" % "0.0.4")

libraryDependencies ++= Seq(
  "io.get-coursier" %% "coursier" % "2.1.10"
)
