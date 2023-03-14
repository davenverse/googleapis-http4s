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
      )
    )

// Projects
lazy val `googleapis-http4s` = tlCrossRootProject
  .aggregate(
    java,
    common,
    cloudBigQueryConnectionV1,
    cloudBigQueryDataPolicyV1,
    cloudBigQueryDataTransferV1,
    cloudBigQueryMigrationV2,
    cloudBigQueryReservationV1,
    cloudBigQueryStorageV1,
    cloudBigtableV2,
    cloudKmsV1,
    cloudMemcacheV1,
    cloudPubsubV1,
    cloudFirestoreV1,
    cloudRedisV1,
    cloudSpannerV1,
    cloudStorageV2,
    iamV1,
  )

// Core projects

lazy val java =
  mkProject("java", "protobuf-java", "3.21.7", "com.google.protobuf")

lazy val common =
  mkProject("common", "proto-google-common-protos", "2.9.6")
  .dependsOn(java)

// Everything else, alphabetically

lazy val cloudBigQueryConnectionV1 =
  mkProject("cloud-bigqueryconnection-v1", "proto-google-cloud-bigqueryconnection-v1", "2.14.0")
  .dependsOn(common, iamV1)

lazy val cloudBigQueryDataPolicyV1 =
  mkProject("cloud-bigquerydatapolicy-v1", "proto-google-cloud-bigquerydatapolicy-v1", "0.9.0")
  .dependsOn(common, iamV1)

lazy val cloudBigQueryDataTransferV1 =
  mkProject("cloud-bigquerydatatransfer-v1", "proto-google-cloud-bigquerydatatransfer-v1", "2.12.0")
  .dependsOn(common)

lazy val cloudBigQueryMigrationV2 =
  mkProject("cloud-bigquerymigration-v2", "proto-google-cloud-bigquerymigration-v2", "0.15.0")
  .dependsOn(common)

lazy val cloudBigQueryReservationV1 =
  mkProject("cloud-bigqueryreservation-v1", "proto-google-cloud-bigqueryreservation-v1", "2.13.0")
  .dependsOn(common)

lazy val cloudBigQueryStorageV1 =
  mkProject("cloud-bigquerystorage-v1", "proto-google-cloud-bigquerystorage-v1", "2.34.0")
  .dependsOn(common)

lazy val cloudBigtableV2 =
  mkProject("cloud-bigtable-v2", "proto-google-cloud-bigtable-v2", "2.20.0")
    .dependsOn(common)

lazy val cloudFirestoreV1 =
  mkProject("cloud-firestore-v1", "proto-google-cloud-firestore-v1", "3.9.0")
    .dependsOn(common)

lazy val cloudKmsV1 =
  mkProject("cloud-kms-v1", "proto-google-cloud-kms-v1", "0.106.0")
    .dependsOn(common)

lazy val cloudMemcacheV1 =
  mkProject("cloud-memcache-v1", "proto-google-cloud-memcache-v1", "2.12.0")
    .dependsOn(common)

lazy val cloudPubsubV1 =
  mkProject("cloud-pubsub-v1", "proto-google-cloud-pubsub-v1", "1.105.5")
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