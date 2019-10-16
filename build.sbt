name := "sgit"

version := "0.1"

scalaVersion := "2.13.1"

parallelExecution in Test := false

libraryDependencies ++= Seq(

  "org.scalactic" %% "scalactic" % "3.0.8",
  "org.scalatest" %% "scalatest" % "3.0.8" % "test",
  "org.mockito" %% "mockito-scala" % "1.5.18" % "test"

)

import sbtsonar.SonarPlugin.autoImport.sonarProperties

sonarProperties ++= Map(
  "sonar.host.url" -> "http://localhost:9000",
  "sonar.projectName" -> "Sgit",
  "sonar.sources" -> "src/main/scala",
  "sonar.tests" -> "src/test/scala"
)


