package com.spark.day7

import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * spark从Csv中读取数据，用的偏少
  */
object DataSource_Csv {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataSource_Csv")
      .master("local[*]")
      .getOrCreate()

    //指定以后读取json类型的数据
    val csv: DataFrame = spark.read.csv(" ")

    csv.printSchema()

    val pdf: DataFrame = csv.toDF("id", "name", "age")//否则会是默认的名字

    pdf.show()

    spark.stop()


  }
}
