package etl

import org.apache.spark.sql.SaveMode
import util.SparkJob


object Ia_Facebook extends SparkJob {

  def main(args: Array[String]): Unit = {

    val inputPath = args(0)
    val outputPath = args(1)

    import spark.implicits._
    val df = spark.read.json(inputPath)
      .withColumn("facebook_id", 'id)
      .withColumn("address", $"location.street")
      .withColumn("postalCode", $"location.zip")
      .withColumn("city", $"location.city")


      df.write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)

    println(s"\n\nProcessed ${df.count} lines")
  }

}
