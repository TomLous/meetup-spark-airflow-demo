package util

import org.apache.spark.SparkConf
import org.apache.spark.sql.SparkSession


trait SparkJob {

  implicit val spark = SparkSession
    .builder().config(sparkConf)
    .appName(this.getClass.getSimpleName)
    .getOrCreate()

   def sparkConf: SparkConf = new SparkConf()
}

