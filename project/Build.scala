import sbt._, Keys._

object RootBuild extends Build {
  lazy val root = Project(
    id = "paralle-programing-in-scala-1",
    base = file("."))
    .aggregate(example, kmeans, reductions, scalashop, barneshut)

  def project(id: String) = Project(
    id = id,
    base = file(id))

  lazy val example = project("example")
  lazy val kmeans = project("kmeans")
  lazy val reductions = project("reductions")
  lazy val scalashop = project("scalashop")
  lazy val barneshut = project("barneshut")

  scalaVersion := "2.11.7"
  name := "paralle-programing-in-scala-1"
  version := "0.0.1"
}
