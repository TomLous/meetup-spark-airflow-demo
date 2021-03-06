package link

import org.apache.spark.sql.SaveMode
import util.SparkJob


object III_MergeDedupe  extends SparkJob {

  def main(args: Array[String]): Unit = {

    val matchesIdPath = args(0)
    val matchesNamePath = args(1)
    val outputPath = args(2)

    val matchesIdDataFrame = spark.read.parquet(matchesIdPath)
    val matchesNameDataFrame = spark.read.parquet(matchesNamePath)

    val df = matchesIdDataFrame
      .union(matchesNameDataFrame)
      .dropDuplicates("facebook_id")

     df.write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)

    println(s"\n\nProcessed ${df.count} lines")
  }
}
