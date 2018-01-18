name := "meetup-spark-airflow-demo"

version := "0.1"

scalaVersion := "2.11.11"

val sparkDependencyScope = sys.props.getOrElse("sparkDependencyScope", default = "compile")



// spark
libraryDependencies += "org.apache.spark" %% "spark-core" % "2.2.0" % sparkDependencyScope
libraryDependencies += "org.apache.spark" %% "spark-sql" % "2.2.0" % sparkDependencyScope
libraryDependencies += "org.apache.spark" %% "spark-mllib" % "2.2.0" % sparkDependencyScope

// elastic
libraryDependencies += "org.elasticsearch" %% "elasticsearch-spark-20" % "6.1.1"
libraryDependencies += "org.scalaj" %% "scalaj-http" % "2.3.0"

// comparison
libraryDependencies += "info.debatty" % "java-string-similarity" % "1.0.1"
