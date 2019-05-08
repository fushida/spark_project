

import com.big.spark.MyUtils
import org.apache.spark.broadcast.Broadcast
import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, Dataset, SparkSession}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 运用sparksql
  */
object IpLoactionSQL {

  def main(args: Array[String]): Unit = {


    val spark = SparkSession
      .builder()
      .appName("JoinTest")
      .master("local[*]")
      .getOrCreate()

    //取到HDFS中的ip规则
    import spark.implicits._
    val rulesLines:Dataset[String] = spark.read.textFile(args(0))
    //整理ip规则数据()
    val ruleDataFrame: DataFrame = rulesLines.map(line => {
      val fields = line.split("[|]")
      val startNum = fields(2).toLong
      val endNum = fields(3).toLong
      val province = fields(6)
      (startNum, endNum, province)
    }).toDF("snum", "enum", "province")//规则


    //创建RDD，读取访问日志
    val accessLines: Dataset[String] = spark.read.textFile(args(1))

    //整理数据
    val ipDataFrame: DataFrame = accessLines.map(log => {
      //将log日志的每一行进行切分
      val fields = log.split("[|]")
      val ip = fields(1)
      //将ip转换成十进制
      val ipNum = MyUtils.ip2Long(ip)
      ipNum
    }).toDF("ip_num")
  
    
    //注册视图
    ruleDataFrame.createTempView("v_rules")
    ipDataFrame.createTempView("v_ips")
    
    
    //不需要二分法查找
    //是一条一条查找，当数据量比较大的时候则会耗时比较长  
    val r = spark.sql("SELECT province, count(*) counts FROM v_ips JOIN v_rules ON (ip_num >= snum AND ip_num <= enum) GROUP BY province ORDER BY counts DESC")

    r.show()

    spark.stop()



  }
}
