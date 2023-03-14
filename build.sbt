ThisBuild / version := {
  git.gitCurrentTags.value.headOption.getOrElse(git.gitHeadCommit.value.get + "-SNAPSHOT")
}

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport"),
  tlGitHubDev("armanbilge", "Arman Bilge")
)
ThisBuild / tlCiReleaseBranches := Seq("main")
ThisBuild / tlSonatypeUseLegacyHost := true

val Scala213 = "2.13.10"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.2.2")
ThisBuild / scalaVersion := Scala213

def mkProject(
    id: String,
    module: String,
    version: String,
    org: String = "com.google.api.grpc",
) =
  sbtcrossproject.CrossProject(id, file(id))(JSPlatform, JVMPlatform, NativePlatform)
    .enablePlugins(Http4sGrpcPlugin)
    .settings(
      name := "http4s-grpc-" + module.replace("proto-", ""),
      Compile / PB.targets ++= Seq(
        scalapb.gen(grpc = false) -> (Compile / sourceManaged).value / "scalapb"
      ),
      libraryDependencies ++= Seq(
        org % module % version % "protobuf-src" intransitive ()
      ),
      mimaPreviousArtifacts := Set(),
    )

// Projects
lazy val `googleapis-http4s` = tlCrossRootProject
  .aggregate(
    java,
    common,
    cloudBigtableV2,
    cloudFirestoreV1,
    cloudRedisV1,
    cloudSpannerV1,
    cloudStorageV2,
    iamV1,
  )
  .settings(mimaPreviousArtifacts := Set())

// Core projects

lazy val java =
  mkProject("java", "protobuf-java", "3.21.7", "com.google.protobuf")

lazy val common =
  mkProject("common", "proto-google-common-protos", "2.9.6")
  .dependsOn(java)

// Everything else, alphabetically

lazy val cloudBigtableV2 =
  mkProject("cloud-bigtable-v2", "proto-google-cloud-bigtable-v2", "2.20.0")
    .dependsOn(common)

lazy val cloudFirestoreV1 =
  mkProject("cloud-firestore-v1", "proto-google-cloud-firestore-v1", "3.9.0")
    .dependsOn(common)

lazy val cloudRedisV1 =
  mkProject("cloud-redis-v1", "proto-google-cloud-redis-v1", "2.15.0")
    .dependsOn(common)

lazy val cloudSpannerV1 =
  mkProject("cloud-spanner-v1", "proto-google-cloud-spanner-v1", "6.37.0")
    .dependsOn(common)

lazy val cloudStorageV2 =
  mkProject("cloud-storage-v2", "proto-google-cloud-storage-v2", "2.9.3-alpha")
    .dependsOn(iamV1)

lazy val iamV1 =
  mkProject("iam-v1", "proto-google-iam-v1", "1.9.3").dependsOn(common)
