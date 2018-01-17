package helper

import util.SparkJob

object ParquetShow extends SparkJob {

  def main(args: Array[String]) {

    val inputPath = args(0)

    val ds = spark.read
      .parquet(inputPath)

    ds.printSchema()

    ds.show(20)

    println(s"\n\n# Records: ${ds.count}")
  }



}
