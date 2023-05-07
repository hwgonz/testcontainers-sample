

val sharedSettings: Seq[Def.Setting[_]] = Seq(
  organization := "com.acme",
  version := "0.1.0-SNAPSHOT",
  scalaVersion := "2.13.10",
  scalacOptions ++= Seq(
    "-deprecation",
    "-unchecked",
    "-feature",
    "-language:implicitConversions",
    "-language:higherKinds",
    "-Ymacro-annotations",
    "-Ywarn-dead-code",
    "-Xlint:unused",
    "-Wdead-code",
  ),
  fork := true,
  Compile / fork := true,
  Test / fork := true,
  IntegrationTest / baseDirectory := file("."),
  Test / publishArtifact := true,
  assembly / test := {},
  assembly / assemblyMergeStrategy := {
    case PathList("META-INF", "maven", "org.webjars", "swagger-ui", "pom.properties") => MergeStrategy.singleOrError
    case x if x.endsWith("module-info.class") => MergeStrategy.last
    case x =>
      val oldStrategy = (assembly / assemblyMergeStrategy).value
      oldStrategy(x)
  }
)

lazy val root = (project in file("."))
  .settings(
    name := "testcontainers-sample",
    sharedSettings
  )
  .aggregate(
    domain,
    kafkaSupport,
    retailProductService,
    rpValidationService,
  )

lazy val domain = project
  .settings(
    name := "domain",
    sharedSettings,
    libraryDependencies ++= Dependencies.coreDependencies,
    libraryDependencies ++= Dependencies.testDependencies,
  )

lazy val kafkaSupport = project
  .settings(
    name := "kafkaSupport",
    sharedSettings,
    libraryDependencies ++= Dependencies.coreDependencies,
    libraryDependencies ++= Dependencies.streamingDependencies,
  )

lazy val utils = project
  .configs(IntegrationTest)
  .settings(
    name := "utils",
    sharedSettings,
    libraryDependencies ++= Dependencies.coreDependencies,
    libraryDependencies ++= Dependencies.streamingDependencies,
    libraryDependencies ++= Dependencies.databaseDependencies,
    libraryDependencies ++= Dependencies.utilDependencies,
    libraryDependencies ++= Dependencies.testAndITTestDependencies,
    libraryDependencies ++= Dependencies.loggingDependencies,
    Defaults.itSettings,
  )

lazy val retailProductService = project
  .settings(
    name := "retailProductService",
    sharedSettings,
    libraryDependencies ++= Dependencies.coreDependencies,
    libraryDependencies ++= Dependencies.apiDependencies,
    libraryDependencies ++= Dependencies.streamingDependencies,
    assembly / mainClass := Some("com.acme.service.RetailProductService"),
    assembly / assemblyJarName := "run.jar",
  )
  .dependsOn(
    domain % "compile->compile;test->test;it->test",
    utils % "compile->compile;test->test;it->it",
    kafkaSupport
  )

lazy val rpValidationService = project
  .settings(
    name := "retailProductValidationService",
    sharedSettings,
    libraryDependencies ++= Dependencies.coreDependencies,
    libraryDependencies ++= Dependencies.streamingDependencies,
    assembly / mainClass := Some("com.acme.service.RPValidationService"),
    assembly / assemblyJarName := "run.jar",
  )
  .dependsOn(
    domain % "compile->compile;test->test;it->test",
    utils % "compile->compile;test->test;it->it",
    kafkaSupport
  )
