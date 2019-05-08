import org.apache.spark.sql.{DataFrame, SparkSession}

/**
  * 读取Json格式数据
  */
object DataSource_Json {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataSource_Json")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    //指定以后读取json类型的数据(有表头)  可以保存丰富的信息
    val jsons: DataFrame = spark.read.json(" ")

    val filtered: DataFrame = jsons.where($"age" <=500)
    filtered.printSchema()

    filtered.show()

    spark.stop()


  }
}
