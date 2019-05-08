import java.util.Properties
import org.apache.spark.sql.{DataFrame, Dataset, Row, SparkSession}
/*
 * 通过jdbc读取数据
 */
object DataSource_Jdbc {
  def main(args: Array[String]): Unit = {

    val spark = SparkSession.builder().appName("DataSource_Jdbc")
      .master("local[*]")
      .getOrCreate()

    import spark.implicits._

    //load这个方法会读取真正mysql的数据吗？  不会，而是连接获取表头数据信息
    val logs: DataFrame = spark.read.format("jdbc").options(//从数据库中读取些数据
      Map("url" -> "jdbc:mysql://localhost:3306/spark",
        "driver" -> "com.mysql.jdbc.Driver",
        "dbtable" -> "logs",
        "user" -> "root",
        "password" -> "123568")
    ).load()

    //logs.printSchema()//运行 发现只是建立连接   
    //logs.show()

//    val filtered: Dataset[Row] = logs.filter(r => {//过滤操作
//      r.getAs[Int]("age") <= 13
//    })
//    filtered.show()

    //lambda表达式
    val r = logs.filter($"age" <= 13)

    //val r = logs.where($"age" <= 13)//在这里的where也是调用fileter的方法

    val reslut: DataFrame = r.select($"id", $"name", $"age" * 10 as "age") 
    
    //写数据的方式*********************************
    //val props = new Properties()
    //props.put("user","root")
    //props.put("password","123568")
    //reslut.write.mode("ignore").jdbc("jdbc:mysql://localhost:3306/bigdata", "logs1", props)

    //DataFrame保存成text时出错(只能保存一列)
    //reslut.write.text("/Users/zx/Desktop/text")

    //reslut.write.json("/Users/zx/Desktop/json")

    //reslut.write.csv("/Users/zx/Desktop/csv")

    //reslut.write.parquet("hdfs://node-4:9000/parquet")


    //reslut.show()

    spark.close()
  }
}
