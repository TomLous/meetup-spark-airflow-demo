name := "meetup-spark-airflow-demo"

version := "0.1"

scalaVersion := "2.11.11"


// scala
libraryDependencies += "org.scala-lang"              % "scala-reflect"             % "2.11.11"
libraryDependencies += "org.json4s"                  %% "json4s-native"            % "3.3.0"
libraryDependencies += "org.scalatest"               %% "scalatest"                % "3.0.0"




// spark
libraryDependencies += "org.apache.spark"            %% "spark-core"               % "2.2.0"
libraryDependencies += "org.apache.spark"            %% "spark-sql"                % "2.2.0"


////logging
//
//libraryDependencies += "org.apache.logging.log4j"    % "log4j-core"                % "2.6.2"
//libraryDependencies += "org.apache.logging.log4j"    % "log4j"                     % "2.6.2"
//libraryDependencies += "org.apache.logging.log4j"    % "log4j-api"                 % "2.6.2"
//libraryDependencies += "org.apache.logging.log4j"    % "log4j-to-slf4j"            % "2.6.2"
//
//
//// logging
//libraryDependencies += "org.slf4j" % "slf4j-log4j12" % "1.7.21"