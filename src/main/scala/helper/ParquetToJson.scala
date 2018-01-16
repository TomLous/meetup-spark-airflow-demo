package helper

import java.io.File
import java.nio.file.{Files, StandardCopyOption}

import org.apache.spark.sql.SaveMode
import util.SparkJob


object ParquetToJson extends SparkJob {

  def main(args: Array[String]) {
    val dir = "data"
    val from = "fbparquet"
    val to = "facebook-nl"
    val city = "ROTTERDAM"

    import spark.implicits._
    val df = spark.read.parquet(s"$dir/$from")
//      .filter('address_locality === city)
      .drop("_unix_timestamp", "meta_originalFileName", "meta_originalFileTimestamp", "meta_originalFileVersion", "meta_originalRecordTimestamp", "id_uuid", "id_counter", "id_sourceId", "id_name", "id_sourceType", "id_crossWalks", "geo_latitude", "geo_longitude", "geo_hash", "location_mainBusinessType", "location_businessTypes", "location_emailAddress", "location_website", "location_phoneNumber", "address_street", "address_houseNumber", "address_houseNumberTo", "address_houseLetter", "address_houseNumberAddition", "address_houseName", "address_streetHouseNumber", "address_country", "address_countryCode", "address_administrativeAreaLevel1", "address_administrativeAreaLevel1Code", "address_administrativeAreaLevel2", "address_administrativeAreaLevel3", "address_administrativeAreaLevel4", "address_administrativeAreaLevel5", "address_postalCode", "address_locality", "address_neighborhood", "address_string")
//        .limit(100)

      df.repartition(1)
      .write
      .mode(SaveMode.Overwrite)
      .json(s"$dir/$to.parquet")

    val fromFile = new File(s"$dir/$to.parquet").listFiles().filter(_.getName.endsWith(".json")).head.toPath
    val toFile = new File(s"$dir/$to.json").toPath
    Files.move(fromFile, toFile, StandardCopyOption.ATOMIC_MOVE)

    println(s"Saved ${df.count} to $dir/$to.json")
  }

}
