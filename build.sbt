name := "meetup-spark-airflow-demo"

version := "0.1"

scalaVersion := "2.11.11"

val sparkDependencyScope = sys.props.getOrElse("sparkDependencyScope", default = "compile")

// scala
//libraryDependencies += "org.scala-lang" % "scala-reflect" % "2.11.11" %
//libraryDependencies += "org.json4s" %% "json4s-native" % "3.3.0"
//libraryDependencies += "org.scalatest" %% "scalatest" % "3.0.0"


// spark
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0" % sparkDependencyScope
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0" % sparkDependencyScope
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.2.0" % sparkDependencyScope


// comparison
libraryDependencies += "info.debatty" % "java-string-similarity" % "1.0.1"
