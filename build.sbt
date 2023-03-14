ThisBuild / tlBaseVersion := "0.0" // your current series x.y

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport"),
  tlGitHubDev("armanbilge", "Arman Bilge")
)
ThisBuild / tlCiReleaseBranches := Seq("main")
ThisBuild / tlSonatypeUseLegacyHost := true


val Scala213 = "2.13.7"

ThisBuild / crossScalaVersions := Seq(Scala213, "3.2.2")
ThisBuild / scalaVersion := Scala213

ThisBuild / testFrameworks += new TestFramework("munit.Framework")

def mkProject(
    id: String,
    module: String,
    version: String,
    org: String = "com.google.api.grpc"
) =
  Project(id, file(id)) // todo make cross
    .enablePlugins(Http4sGrpcPlugin)
    .settings(
      name := module.replace("proto", "http4s-grpc"), // set name to original plus http4s
      Compile / PB.targets ++= Seq(
        scalapb.gen(grpc = false) -> (Compile / sourceManaged).value / "scalapb"
      ),
      libraryDependencies ++= Seq(
        org % module % version % "protobuf-src" intransitive ()
      )
    )


// Projects
lazy val `googleapis-http4s` = tlCrossRootProject
  .aggregate(
    java, common, iamV1, cloudStorageV2, spannerV1, bigTableV2, fireStoreV1
  )

lazy val java =
  Project("java", file("java")) // todo make cross
    .enablePlugins(Http4sGrpcPlugin)
    .settings(
      name := "http4s-grpc-protobuf-java", // set name to original plus http4s
      Compile / PB.targets ++= Seq(
        scalapb.gen(grpc = false) -> (Compile / sourceManaged).value / "scalapb"
      ),
      libraryDependencies ++= Seq(
        "com.google.protobuf" % "protobuf-java" % "3.21.7" % "protobuf-src" intransitive ()
      )
    )

lazy val common =
  mkProject("common", "proto-google-common-protos", "2.9.6").dependsOn(java)

lazy val iamV1 =
  mkProject("iam-v1", "proto-google-iam-v1", "1.9.3").dependsOn(common)

lazy val cloudStorageV2 =
  mkProject("cloud-storage-v2", "proto-google-cloud-storage-v2", "2.9.3-alpha")
    .dependsOn(iamV1)

lazy val spannerV1 =
  mkProject("spanner-v1", "proto-google-cloud-spanner-v1", "6.37.0")
    .dependsOn(common)

lazy val bigTableV2 =
  mkProject("bigtable-v2", "proto-google-cloud-bigtable-v2", "2.20.0")
    .dependsOn(common)

lazy val fireStoreV1 =
  mkProject("firestore-v1", "proto-google-cloud-firestore-v1", "3.9.0")
    .dependsOn(common)

lazy val site = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
