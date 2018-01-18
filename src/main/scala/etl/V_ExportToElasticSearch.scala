package etl

import org.apache.spark.sql.functions._
import org.apache.spark.sql.types.DoubleType
import org.elasticsearch.spark.sql._
import util.SparkJob

import scalaj.http.Http


object V_ExportToElasticSearch extends SparkJob {

  def main(args: Array[String]): Unit = {

    import spark.implicits._

    val inputPath = args(0)
    val outputIndex = args(1)

    val node = spark.conf.getOption("spark.es.nodes").get
    val baseUrl = s"http://$node:9200/"
    val (indexName, mappingName) = outputIndex.split('/').toList match {
      case List(i, m) => (i,m)
      case _ => ("unknown","unknown")
    }

    println(s"\nDeleting index $baseUrl$indexName")
    delete(baseUrl + indexName)


    println(s"\nCreate index $baseUrl$indexName")
    put(baseUrl + indexName, "")

    println(s"\nCreate mapping $baseUrl$indexName/_mapping/$mappingName")
    val indexJson =s""""{"properties": {"geo": {"type": "geo_point"}}}"""
    val indexJson2 =s""""{"mappings": { "$mappingName": {"properties": {"geo": {"type": "geo_point"}}}}}"""
    put(s"$baseUrl$indexName/_mapping/$mappingName", indexJson)



    spark.read.parquet(inputPath)
      .select(
        'facebook_id,
        'name,
        'address,
        'postalCode,
        'city,
        'keywords,
        'category,
        'parsed_category_labels.as('categoryLabel),
        'description,
        'website,
        map(lit("lat"),'latitude.cast(DoubleType),lit("lon").cast(DoubleType),'longitude).as("geo"),
        'latitude,
        'longitude
      ).saveToEs(outputIndex, Map("es.mapping.id" -> "facebook_id"))



  }

  def post(url: String, jsonData: String): Boolean ={
    val data = Http(url).compress(false).postData(jsonData).header("content-type", "application/json").asString
      println(data)
      data.is2xx
  }

  def put(url: String, jsonData: String): Boolean ={
    val data = Http(url).compress(false).put(jsonData).header("content-type", "application/json").asString
    println(data)
    data.is2xx
  }

  def delete(url: String): Boolean ={
    val data = Http(url).compress(false).method("DELETE").asString
    println(data)
    data.is2xx
  }

}