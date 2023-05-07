import sbt._
object Dependencies {

  val coreDependencies = Seq(
    // Circe JSON
    "io.circe" %% "circe-core" % "0.14.5",
    "io.circe" %% "circe-generic" % "0.14.5",
    "io.circe" %% "circe-generic-extras" % "0.14.3",
    "io.circe" %% "circe-literal" % "0.14.5",
    "io.circe" %% "circe-parser" % "0.14.5",
    "io.circe" %% "circe-parser" % "0.14.5",

    "com.beachape" %% "enumeratum-circe" % "1.7.2",

    // Cats
    "org.typelevel" %% "cats-core" % "2.9.0",
    "org.typelevel" %% "cats-effect" % "3.4.8",
    "com.github.cb372" %% "cats-retry" % "3.1.0",

    // Configuration
    "com.typesafe" % "config" % "1.4.2",
    "com.github.pureconfig" %% "pureconfig" % "0.17.3",
    "com.github.pureconfig" %% "pureconfig-cats-effect" % "0.17.2",
    "com.github.pureconfig" %% "pureconfig-enum" % "0.17.2"
  )

  val apiDependencies = Seq(
    // http server dependencies
    "com.softwaremill.sttp.tapir" %% "tapir-core" % "1.2.10",
    "com.softwaremill.sttp.tapir" %% "tapir-json-circe" % "1.2.10",
    "com.softwaremill.sttp.tapir" %% "tapir-openapi-docs" % "1.2.10",
    "com.softwaremill.sttp.apispec" %% "openapi-circe-yaml" % "0.3.2",
    "com.softwaremill.sttp.tapir" %% "tapir-http4s-server" % "1.2.10",
    "com.softwaremill.sttp.tapir" %% "tapir-swagger-ui-bundle" % "1.2.10",
    "com.softwaremill.sttp.tapir" %% "tapir-sttp-client" % "1.2.10",
    "com.softwaremill.sttp.client3" %% "http4s-backend" % "3.8.13",
    "com.softwaremill.sttp.client3" %% "core" % "3.8.13",
    "org.http4s" %% "http4s-blaze-client" % "0.23.14",
    "org.http4s" %% "http4s-blaze-server" % "0.23.14",
    "org.http4s" %% "http4s-circe" % "0.23.18",
  ) ++ loggingDependencies


  lazy val loggingDependencies = Seq(
    "org.typelevel" %% "log4cats-core" % "2.5.0", // Only if you want to Support Any Backend
    "org.typelevel" %% "log4cats-slf4j" % "2.5.0", // Direct Slf4j Support - Recommended

    "ch.qos.logback" % "logback-classic" % "1.4.6",
    "org.slf4j" % "slf4j-api" % "2.0.6",
  )

  lazy val databaseDependencies = Seq(
    // Database
    "org.tpolecat" %% "doobie-core" % "1.0.0-RC2",
    "org.tpolecat" %% "doobie-hikari" % "1.0.0-RC2",
    "mysql" % "mysql-connector-java" % "8.0.32",
  )

  lazy val streamingDependencies = Seq(
    // https://mvnrepository.com/artifact/com.github.fd4s/fs2-kafka
    "com.github.fd4s" %% "fs2-kafka" % "2.5.0",
  )

  lazy val utilDependencies = Seq(
    // https://mvnrepository.com/artifact/com.github.f4b6a3/uuid-creator
    "com.github.f4b6a3" % "uuid-creator" % "5.2.0",
    "org.gnieh" %% "diffson-circe" % "4.3.0", // https://github.com/gnieh/diffson
  )

  lazy val lensDependencies = Seq(
    "com.github.julien-truffaut" %% "monocle-core" % "2.1.0",
    "com.github.julien-truffaut" %% "monocle-macro" % "2.1.0",
    //    "dev.optics" %% "monocle-core" % "3.2.0", TODO replace with this once circe-optics updates it
    //    "dev.optics" %% "monocle-macro" % "3.2.0",
  )

  lazy val apiIntegrationUtilDependencies = Seq(
    "org.systemfw" %% "upperbound" % "0.4.0",
  )

  private def commonTestDependencies = Seq(
    "org.scalatest" %% "scalatest" % "3.2.15",
    "org.tpolecat" %% "doobie-scalatest" % "1.0.0-RC2"
  )

  lazy val testDependencies = commonTestDependencies.map(_ % Test)

  lazy val testAndITTestDependencies = (commonTestDependencies ++ Seq(
    "com.dimafeng" %% "testcontainers-scala" % "0.40.12",
    "com.dimafeng" %% "testcontainers-scala-mysql" % "0.40.12",
    "com.dimafeng" %% "testcontainers-scala-kafka" % "0.40.12",
    "com.dimafeng" %% "testcontainers-scala-mockserver" % "0.40.12",
    "org.mock-server" % "mockserver-client-java" % "5.14.0", // Not latests, but it needs to match the server's version on testcontainers
  )).map(_ % "it,test")

}
