package com.big.spark.sparkTest

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 在demo1中创建的是user :class User(val name: String, val age: Int, val score: Int) extends Ordered[User] with Serializable
  * 在本例中创建的boy方法      ：class Boy(val age: Int, val score: Int) extends Ordered[Boy] with Serializable
  */
object DemoSort2 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("DemoSort2").setMaster("local[*]")

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
      val score = fields(2).toInt
      (name, age, score)
    })
    //*****************************************  区别一，放在元组里而不是放在类里
    //排序(传入了一个排序规则，不会改变数据的格式，只会改变顺序)   
    val sorted: RDD[(String, Int, Int)] = tpRDD.sortBy(tp => new Boy(tp._2, tp._3))

    println(sorted.collect().toBuffer)

    sc.stop()

  }

}
//排序规则
class Boy(val age: Int, val score: Int) extends Ordered[Boy] with Serializable {

  override def compare(that: Boy): Int = {
    if(this.score == that.score) {
      this.age - that.age
    } else {
      -(this.score - that.score)
    }
  }
}
