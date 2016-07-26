scalaVersion := "2.11.7"

scalacOptions ++= Seq("-deprecation")

libraryDependencies ++= Seq(
  "junit" % "junit" % "4.10" % "test",
  "com.storm-enroute" %% "scalameter-core" % "0.6",
  "com.github.scala-blitz" %% "scala-blitz" % "1.1",
  "org.scalatest" %% "scalatest" % "2.2.4" % "test",
  "com.storm-enroute" %% "scalameter" % "0.6" % "test"
)