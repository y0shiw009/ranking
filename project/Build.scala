import sbt._
import Keys._
import play.Project._

object ApplicationBuild extends Build {

    val appName = "ranking"
    val appVersion = "1.0-SNAPSHOT"

    val appDependencies = Seq(
        "org.apache.hbase" % "hbase" % "0.94.11" excludeAll (
            ExclusionRule(organization = "org.slf4j")),
        "org.apache.hadoop" % "hadoop-core" % "1.0.4",
        jdbc,
        anorm)

    val main = play.Project(appName, appVersion, appDependencies).settings()

}
