package etl

import org.apache.spark.sql.SaveMode
import util.SparkJob


object Facebook extends SparkJob {

  def main(args: Array[String]): Unit = {

    val inputPath = args(0)
    val outputPath = args(1)

    import spark.implicits._
    spark.read.json(inputPath)
      .withColumn("address", $"location.street")
      .withColumn("postalCode", $"location.zip")
      .withColumn("city", $"location.city")
      .write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)
  }

}
