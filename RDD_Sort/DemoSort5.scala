package com.big.spark.sparkTest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 充分利用元组的比较规则，元组的比较规则：先比第一，相等再比第二个      元组默认规则
  */
object DemoSort5 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("DemoSort5").setMaster("local[*]")

    val sc = new SparkContext(conf)

     //排序规则：首先按照成绩的降序，如果成绩相等，再按照年龄的升序
    val users= Array("tom 19 99", "kobe 20 98", "jack 20 99", "bob 17 97")

    //将Driver端的数据并行化变成RDD
    val lines: RDD[String] = sc.parallelize(users)

    //切分整理数据
    val tpRDD: RDD[(String, Int, Int)] = lines.map(line => {
      val fields = line.split(" ")
      val name = fields(0)
      val age = fields(1).toInt
      val fv = fields(2).toInt
      (name, age, fv)
    })
    //充分利用元组的比较规则，元组的比较规则：先比第一，相等再比第二个      元组默认规则
    val sorted: RDD[(String, Int, Int)] = tpRDD.sortBy(tp => (-tp._3, tp._2))//元组默认为升序

    println(sorted.collect().toBuffer)

    sc.stop()

  }

}
