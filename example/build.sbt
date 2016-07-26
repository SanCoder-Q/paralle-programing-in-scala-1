scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.10" % "test",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test"
)