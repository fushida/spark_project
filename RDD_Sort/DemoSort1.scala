package com.big.spark.sparkTest

/***
 * Array("tom 19 99", "kobe 20 98", "jack 20 99", "bob 17 97")数据类型在RDD中完成使用不同方式排序
 */
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}
/**
 * 
 * 放到类里面
 */
object DemoSort1 {
   def main(args: Array[String]): Unit = {
     val conf=new SparkConf().setAppName("DemoSort1").setMaster("local[*]")
     
     val sc=new SparkContext(conf)
     
     //排序规则：首先按照成绩的降序，如果成绩相等，再按照年龄的升序
    val users= Array("tom 19 99", "kobe 20 98", "jack 20 99", "bob 17 97")

    //将Driver端的数据并行化变成RDD
    val lines: RDD[String] = sc.parallelize(users)
    
    //切分数据
      val userRDD: RDD[User] = lines.map(line => {
      val fields = line.split(" ")
      val name = fields(0)
      val age = fields(1).toInt
      val score= fields(2).toInt
      //(name, age, fv)
      new User(name, age,score)//自定义一个类
    })

    //不满足要求,因为多个标准
    //tpRDD.sortBy(tp => tp._3, false)

    //将RDD里面装的User类型的数据进行排序
    val sorted: RDD[User] = userRDD.sortBy(u => u)

    val r = sorted.collect()

    println(r.toBuffer)

    sc.stop()
   }
}

//定义user类  
class User(val name: String, val age: Int, val score: Int) extends Ordered[User] with Serializable {//为什么要序列化，因为要走网络，封装数据！！
   //重写
  override def compare(that: User): Int = {
    if(this.score == that.score) {  //成绩相等的情况
      this.age - that.age  //为正数升序
    } else {
      -(this.score - that.score)
    }
  }
  override def toString: String = s"name: $name, age: $age, score: $score"
}
