val scala3Version = "3.7.4"

lazy val root = project
  .in(file("."))
  .settings(
    name := "listening-dna",
    version := "0.1.0-SNAPSHOT",

    scalaVersion := scala3Version,

    libraryDependencies ++= Seq(
      // JSON
      "io.circe" %% "circe-core" % "0.14.15",
      "io.circe" %% "circe-generic" % "0.14.15",
      "io.circe" %% "circe-parser" % "0.14.15",

      // Cats
      "org.typelevel" %% "cats-effect" % "3.7.0",

      "com.monovore" %% "decline" % "2.6.2",

      // Testing
      "org.scalameta" %% "munit" % "1.3.0" % Test
    )
  )
