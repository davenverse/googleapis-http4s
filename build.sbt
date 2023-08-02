ThisBuild / version := "0.0.5" // global version for "blend" defined in this build

ThisBuild / organization := "io.chrisdavenport"
ThisBuild / organizationName := "Christopher Davenport"
ThisBuild / licenses := Seq(License.MIT)
ThisBuild / developers := List(
  tlGitHubDev("christopherdavenport", "Christopher Davenport"),
  tlGitHubDev("armanbilge", "Arman Bilge")
)
ThisBuild / tlCiReleaseBranches := Seq("main")
ThisBuild / tlSonatypeUseLegacyHost := true

ThisBuild / tlSitePublishBranch := Some("main")

ThisBuild / mergifyStewardConfig ~= {
  _.map(_.copy(mergeMinors = true, author = "davenverse-steward[bot]"))
}

val Scala213 = "2.13.11"
ThisBuild / crossScalaVersions := Seq(Scala213, "3.3.0")
ThisBuild / scalaVersion := Scala213

ThisBuild / githubWorkflowJavaVersions := Seq(JavaSpec.temurin("17"))

ThisBuild / githubWorkflowBuild := Seq(
  WorkflowStep.Sbt(List("compile"), name = Some("Compile"))
)

def mkDep(org: String, mod: String, v: String) = {
  s"""  "$org" %%% "$mod" % "$v""""
}

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
      Keys.version := s"$version+${(ThisBuild / Keys.version).value}", // google v + build v
      docs / mdocVariables := {
        // this is an atrocious hack b/c no stable setting evaluation order in sbt
        val vars = (docs / mdocVariables).value
        val modules = vars.get("MODULES").toList.flatMap(_.split('\n')).toSet
        val dep = mkDep(organization.value, moduleName.value, Keys.version.value)
        val newModules = (modules + dep).toList.sorted.mkString("\n")
        vars.updated("MODULES", newModules)
      },
      Compile / PB.targets ++= Seq(
        scalapb.gen(grpc = false) -> (Compile / sourceManaged).value / "scalapb"
      ),
      libraryDependencies ++= Seq(
        org % module % version % "protobuf-src" intransitive ()
      ),
      mimaPreviousArtifacts := Set(),
      publish / skip := {
        import coursier._
        val mod = CrossVersion(crossVersion.value,  scalaVersion.value, scalaBinaryVersion.value)
          .get.apply(moduleName.value)
        val dep = Dependency(Module(Organization(organization.value), ModuleName(mod)), Keys.version.value)
        try { // if this dep already exists, skip publishing
          Resolve()
            .addDependencies(dep)
            .addRepositories(Repositories.sonatype("releases"))
            .run()
          true
        } catch {
          case _: error.ResolutionError => false
        }
      }
    )

// Projects
lazy val `googleapis-http4s` = tlCrossRootProject
  .aggregate(
    java,
    common,
    cloudAlloyDb,
    cloudBigQueryConnectionV1,
    cloudBigQueryDataPolicyV1,
    cloudBigQueryDataTransferV1,
    cloudBigQueryMigrationV2,
    cloudBigQueryReservationV1,
    cloudBigQueryStorageV1,
    cloudBigtableV2,
    cloudDatastreamV1,
    cloudKmsV1,
    cloudMemcacheV1,
    cloudPubsubV1,
    cloudFirestoreV1,
    cloudRedisV1,
    cloudSpannerV1,
    cloudStorageV2,
    cloudTraceV2,
    iamV1,
  )
  .settings(mimaPreviousArtifacts := Set())

// Core projects

lazy val java =
  mkProject("java", "protobuf-java", "3.23.4", "com.google.protobuf")

lazy val common =
  mkProject("common", "proto-google-common-protos", "2.23.0")
  .dependsOn(java)

// Everything else, alphabetically

lazy val cloudAlloyDb =
  mkProject("cloud-alloydb", "proto-google-cloud-alloydb-v1", "0.8.0")
  .dependsOn(common)

lazy val cloudBigQueryConnectionV1 =
  mkProject("cloud-bigqueryconnection-v1", "proto-google-cloud-bigqueryconnection-v1", "2.24.0")
  .dependsOn(common, iamV1)

lazy val cloudBigQueryDataPolicyV1 =
  mkProject("cloud-bigquerydatapolicy-v1", "proto-google-cloud-bigquerydatapolicy-v1", "0.16.0")
  .dependsOn(common, iamV1)

lazy val cloudBigQueryDataTransferV1 =
  mkProject("cloud-bigquerydatatransfer-v1", "proto-google-cloud-bigquerydatatransfer-v1", "2.22.0")
  .dependsOn(common)

lazy val cloudBigQueryMigrationV2 =
  mkProject("cloud-bigquerymigration-v2", "proto-google-cloud-bigquerymigration-v2", "0.22.0")
  .dependsOn(common)

lazy val cloudBigQueryReservationV1 =
  mkProject("cloud-bigqueryreservation-v1", "proto-google-cloud-bigqueryreservation-v1", "2.23.0")
  .dependsOn(common)

lazy val cloudBigQueryStorageV1 =
  mkProject("cloud-bigquerystorage-v1", "proto-google-cloud-bigquerystorage-v1", "2.41.0")
  .dependsOn(common)

lazy val cloudBigtableV2 =
  mkProject("cloud-bigtable-v2", "proto-google-cloud-bigtable-v2", "2.25.1")
  .dependsOn(common)

lazy val cloudDatastreamV1 =
  mkProject("cloud-datastream-v1", "proto-google-cloud-datastream-v1", "1.21.0")
  .dependsOn(common)

lazy val cloudFirestoreV1 =
  mkProject("cloud-firestore-v1", "proto-google-cloud-firestore-v1", "3.13.8")
    .dependsOn(common)

lazy val cloudKmsV1 =
  mkProject("cloud-kms-v1", "proto-google-cloud-kms-v1", "0.113.0")
    .dependsOn(common)

lazy val cloudMemcacheV1 =
  mkProject("cloud-memcache-v1", "proto-google-cloud-memcache-v1", "2.22.0")
    .dependsOn(common)

lazy val cloudPubsubV1 =
  mkProject("cloud-pubsub-v1", "proto-google-cloud-pubsub-v1", "1.106.0")
    .dependsOn(common)

lazy val cloudRedisV1 =
  mkProject("cloud-redis-v1", "proto-google-cloud-redis-v1", "2.25.0")
    .dependsOn(common)

lazy val cloudSpannerV1 =
  mkProject("cloud-spanner-v1", "proto-google-cloud-spanner-v1", "6.43.2")
    .dependsOn(common)

lazy val cloudStorageV2 =
  mkProject("cloud-storage-v2", "proto-google-cloud-storage-v2", "2.20.2-alpha")
    .dependsOn(iamV1)

lazy val cloudTraceV2 =
  mkProject("cloud-trace-v2", "proto-google-cloud-trace-v2", "2.22.0")
    .dependsOn(common)

lazy val iamV1 =
  mkProject("iam-v1", "proto-google-iam-v1", "1.18.0").dependsOn(common)

lazy val docs = project.in(file("site"))
  .enablePlugins(TypelevelSitePlugin)
  .settings(
    tlSiteIsTypelevelProject := true,
  )
