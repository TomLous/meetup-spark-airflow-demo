package link

import info.debatty.java.stringsimilarity.JaroWinkler
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import info.debatty.java.stringsimilarity.JaroWinkler
import util.SparkJob


object IIb_MatchByNameAddress extends SparkJob {

  def main(args: Array[String]): Unit = {
    import spark.implicits._

    val source1Path = args(0)
    val source2Path = args(1)
    val outputPath = args(2)

    val dataframe1 = spark.read.parquet(source1Path)
    val dataframe2 = spark.read.parquet(source2Path)



    dataframe1.join(
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
        lower(regexp_replace('postalCode," ","")) === lower(regexp_replace('postalCode2," ","")) && compareString(0.8)('name, 'name2) && compareString(0.6)('address, 'address2),
      joinType = "inner")
      .write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)
  }




  lazy val jaroWinkler = new JaroWinkler

  def compareString(treshhold: Double) = udf((s1: String, s2:String) => {
    if(s1 == null || s2 == null) false
    else jaroWinkler.similarity(s1.toLowerCase.trim , s2.toLowerCase.trim) > treshhold
  })

}