import org.apache.spark.sql.{DataFrame, SparkSession}
/**
 *  Parquet格式数据读取       经常用   可以获取表头
 */
//保存数据  和文件信息      偏移量信息
object DataSource_Parquet {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataSource_Parquet")
      .master("local[*]")
      .getOrCreate()

    //指定以后读取json类型的数据  
    val parquetLine: DataFrame = spark.read.parquet("../parquet")
    //val parquetLine: DataFrame = spark.read.format("parquet").load("..")

    parquetLine.printSchema()

    //show是Action
    parquetLine.show()

    spark.stop()


  }
}
