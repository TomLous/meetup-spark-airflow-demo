package link

import org.apache.spark.sql.SaveMode
import util.SparkJob


object IIa_MatchByFacebookId extends SparkJob {

  def main(args: Array[String]): Unit = {

    import spark.implicits._

    val source1Path = args(0)
    val source2Path = args(1)
    val outputPath = args(2)

    val dataframe1 = spark.read.parquet(source1Path)
    val dataframe2 = spark.read.parquet(source2Path)

    val df = dataframe1.join(
      dataframe2
        .withColumnRenamed("facebook_id", "facebook_id2")
        .withColumnRenamed("address", "address2")
        .withColumnRenamed("postalCode", "postalCode2")
        .withColumnRenamed("city", "city2")
        .withColumnRenamed("address", "address2")
        .withColumnRenamed("name", "name2")
        .withColumnRenamed("hours", "hours2")
        .withColumnRenamed("website", "website2"),
      joinExprs =
        'facebook_id === 'facebook_id2,
      joinType = "inner")

    df.write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)

    println(s"\n\nProcessed ${df.count} lines")
  }

}