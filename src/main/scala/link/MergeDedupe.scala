package link

import link.MatchByFacebookId.spark
import org.apache.spark.sql.SaveMode
import util.SparkJob

/**
  * Created by Tom Lous on 15/01/2018.
  * Copyright © 2018 Datlinq B.V..
  */
object MergeDedupe  extends SparkJob {

  def main(args: Array[String]): Unit = {

    val matchesIdPath = args(0)
    val matchesNamePath = args(1)
    val outputPath = args(2)

    val matchesIdDataFrame = spark.read.parquet(matchesIdPath)
    val matchesNameDataFrame = spark.read.parquet(matchesNamePath)

    matchesIdDataFrame
      .union(matchesNameDataFrame)
      .dropDuplicates("facebook_id")
      .write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)
  }
}
