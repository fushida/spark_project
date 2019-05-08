package com.big.spark.sparkTest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 通过传入排序规则
  */
object DemoSort4 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("DemoSort4").setMaster("local[*]")

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

    //排序(传入了一个排序规则，不会改变数据的格式，只会改变顺序)
    /**
     * 
    implicit object Orderingst extends Ordering[st] {
    override def compare(x: st, y: st): Int = {
        if(x.fv == y.fv) {
          x.age - y.age
        } else {
          y.fv - x.fv
        }
     * 
     */
    import SortRules.Orderingst//调入
    val sorted: RDD[(String, Int, Int)] = tpRDD.sortBy(tp => st(tp._2, tp._3))

    println(sorted.collect().toBuffer)

    sc.stop()

  }

}
   case class st(age: Int, fv: Int)
