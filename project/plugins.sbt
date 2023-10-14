lazy val sbtTypelevelVersion = "0.5.0-M10"
addSbtPlugin("org.typelevel" % "sbt-typelevel-ci" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-ci-signing" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-sonatype-ci-release" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-github" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-settings" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-site" % sbtTypelevelVersion)
addSbtPlugin("org.typelevel" % "sbt-typelevel-mergify" % sbtTypelevelVersion)

addSbtPlugin("org.scala-js" % "sbt-scalajs" % "1.14.0")
addSbtPlugin("org.portable-scala" % "sbt-scalajs-crossproject"      % "1.3.2")
addSbtPlugin("org.scala-native" % "sbt-scala-native" % "0.4.16")
addSbtPlugin("org.portable-scala" % "sbt-scala-native-crossproject" % "1.3.2")

addSbtPlugin("io.chrisdavenport" % "sbt-http4s-grpc" % "0.0.3")

libraryDependencies ++= Seq(
  "io.get-coursier" %% "coursier" % "2.1.7"
)
