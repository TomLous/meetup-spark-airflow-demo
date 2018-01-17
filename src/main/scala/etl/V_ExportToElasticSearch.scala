package etl

import org.apache.spark.sql.SQLContext
import org.elasticsearch.spark.sql._
import util.SparkJob


object V_ExportToElasticSearch extends SparkJob {

  def main(args: Array[String]): Unit = {


    val inputPath = args(0)
    val outputIndex = args(1)

    spark.read.parquet(inputPath).saveToEs(outputIndex)



  }

}