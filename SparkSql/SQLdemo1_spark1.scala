package com.spark.sql

import org.apache.spark.rdd.RDD
import org.apache.spark.sql.{DataFrame, SQLContext}
import org.apache.spark.{SparkConf, SparkContext}

/**

spark 1.x SQL的基本用法（两种）
1.创建SparkContext
2.创建SQLContext
3.创建RDD
4.创建一个类，并定义类的成员变量
5.整理数据并关联class
6.将RDD转换成DataFrame（导入隐式转换）
7.将DataFrame注册成临时表
8.书写SQL（Transformation）
9.执行Action
---------------------------------
1.创建SparkContext
2.创建SQLContext
3.创建RDD
4.创建StructType（schema）
5.整理数据将数据跟Row关联
6.通过rowRDD和schema创建DataFrame
7.将DataFrame注册成临时表
8.书写SQL（Transformation）
9.执行Action
---------------------------------


Spark Core : RDD 
Spark SQL : DataFrame , DataSet

DataFrame是关联的schema信息的RDD
DataSet相当于是优化过了的RDD

  */
object SQLdemo1_spark1 {

  def main(args: Array[String]): Unit = {

    //提交的这个程序可以连接到Spark集群中
    val conf = new SparkConf().setAppName("SQLdemo1_spark1").setMaster("local[2]")

    //创建SparkSQL的连接（程序执行的入口）
    val sc = new SparkContext(conf)
    //1 sparkContext不能创建特殊的RDD（DataFrame）
    
    //将SparkContext包装进而增强
    val sqlContext = new SQLContext(sc)
    //2 创建特殊的RDD（DataFrame），就是有schema信息的RDD

    //先有一个普通的RDD，然后在关联上schema，进而转成DataFrame

    val lines = sc.textFile("hdfs://hadoop//person")//1 tob 98 69  数据格式
    //将数据进行整理  将非结构化转化为架构化数据
    val boyRDD: RDD[Boy] = lines.map(line => {
      val fields = line.split(",")
      val id = fields(0).toLong
      val name = fields(1)
      val age = fields(2).toInt
      val fv = fields(3).toDouble
      Boy(id, name, age, fv)
    })
    //3 该RDD装的是Boy类型的数据，有了shcma信息，但是还是一个RDD
    //将RDD转换成DataFrame
    //导入隐式转换
    import sqlContext.implicits._
    val bdf: DataFrame = boyRDD.toDF

    //变成DF后就可以使用两种API进行编程了
    
    //下面是使用sql的方式
    //4 把DataFrame先注册临时表
    bdf.registerTempTable("t_boy")

    //5 书写SQL（SQL方法应其实是Transformation）
    val result: DataFrame = sqlContext.sql("SELECT * FROM t_boy ORDER BY fv desc, age asc")

    //6 查看结果（触发Action）
    result.show()

    sc.stop()


    
  }
}

case class Boy(id: Long, name: String, age: Int, fv: Double)
