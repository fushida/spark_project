import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}

/*
 * *sparksql 被注册成多个表操作后实现join
 * 对于复杂的操作可以用join简单化
  */

//完成两个表的join
object SparkJoinTest {

  def main(args: Array[String]): Unit = {

    val spark = SparkSession
      .builder()
      .appName("SparkJoinTest")
      .master("local[*]")//不在本地执行可以去掉
      .getOrCreate()

    import spark.implicits._    //隐式转化
    //读取数据可以在多种渠道中，本例中直接使用dataset
    val lines: Dataset[String] = spark.createDataset(List("1,yaomi,china", "2,jack,usa", "3,longzhe,jp"))
    
    //对数据进行整理
    val tpDs: Dataset[(Long, String, String)] = lines.map(line => {
      val fields = line.split(",")
      val id = fields(0).toLong
      val name = fields(1)
      val nationCode = fields(2)
      (id, name, nationCode)
    })
    //建立 表1
    val df1 = tpDs.toDF("id", "name", "nation")//非结构化数转化为结构化数据

    val nations: Dataset[String] = spark.createDataset(List("china,中国", "usa,美国"))
    //对数据进行整理
    val ndataset: Dataset[(String, String)] = nations.map(l => {
      val fields = l.split(",")
      val ename = fields(0)
      val cname = fields(1)
      (ename, cname)
    })
    //建立 表2
    val df2 = ndataset.toDF("ename","cname")
    df2.count()

    //第一种，创建视图
    df1.createTempView("v_users")
    df2.createTempView("v_nations")
    
    //实现join
    val r: DataFrame = spark.sql("SELECT name, cname FROM v_users JOIN v_nations ON nation = ename")

    //import org.apache.spark.sql.functions._
    //法二
   // val r = df1.join(df2, $"nation" === $"ename", "left_outer")//三个等号  left_outer显示在左边

   r.show()
    spark.stop()
  }
}
