package enrich


import org.apache.spark.ml.Pipeline
import org.apache.spark.ml.feature._
import org.apache.spark.ml.linalg.SparseVector
import org.apache.spark.sql.SaveMode
import org.apache.spark.sql.functions._
import util.SparkJob

/**
  * Created by Tom Lous on 15/01/2018.
  * Copyright © 2018 Datlinq B.V..
  */
object Keywords extends SparkJob {

  def main(args: Array[String]): Unit = {

    val inputPath = args(0)
    val outputPath = args(1)

    import spark.implicits._

    val orgData = spark.read.parquet(inputPath).cache()

    val textData = orgData.select(
      'facebook_id,
      regexp_replace(concat_ws(" ", 'about, 'category, 'company_overview, 'description, 'food_styles, 'general_info), """(?i)[^a-z]+""", " ").as("text")
    )

    val stopWords = StopWordsRemover.loadDefaultStopWords("english") ++ StopWordsRemover.loadDefaultStopWords("dutch") ++ List("wij", "onze", "www", "kunt", "alle", "staat", "waar", "maken", "nederland", "rotterdam", "nl", "jaar", "uur", "company", "nieuwe", "graag", "welkom", "werken", "terecht", "bieden", "biedt", "zoals", "grote", "sinds", "com", "vanaf", "zowel", "alleen", "per", "bent", "naast")

    val tokenizer = new RegexTokenizer()
      .setInputCol("text")
      .setOutputCol("words")
      .setPattern("""\w+""")
      .setGaps(false)
      .setMinTokenLength(3)

    val remover = new StopWordsRemover()
      .setInputCol(tokenizer.getOutputCol)
      .setOutputCol("filteredWords")
      .setStopWords(stopWords)

    val countVectorizer = new CountVectorizer()
      .setInputCol(remover.getOutputCol)
      .setOutputCol("features")
      .setVocabSize(40)
      .setMinDF(2)

    val pipeline = new Pipeline()
      .setStages(Array(tokenizer, remover, countVectorizer))

    val model = pipeline.fit(textData)

    val keywordList = model.stages.last.asInstanceOf[CountVectorizerModel].vocabulary.toList

    val combi = model.transform(textData).select(
      'facebook_id,
      keywords(keywordList)('features).as("keywords")
    )

    orgData.join(combi, "facebook_id")
      .write
      .mode(SaveMode.Overwrite)
      .parquet(outputPath)

  }

  def keywords(keywordsList: List[String]) = udf((v: SparseVector) => {
    v.toArray.zipWithIndex.filter(_._1 == 1.0).map(x => keywordsList(x._2))
  })

}
