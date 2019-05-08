package com.big.spark.sparkTest
import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**本例没有使用序列化  ：case class Man(age: Int, fv: Int) extends Ordered[Man]  
  * 用  case默认实现了序列化     但是这个方法耦合写死了
  */
object DemoSort3 {

  def main(args: Array[String]): Unit = {

    val conf = new SparkConf().setAppName("DemoSort3").setMaster("local[*]")

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

    //排序(传入了一个排序规则，不会改变数据的格式，只会改变顺序)
    val sorted: RDD[(String, Int, Int)] = tpRDD.sortBy(tp => Man(tp._2, tp._3))

    println(sorted.collect().toBuffer)

    sc.stop()
  }
}
case class Man(age: Int, score: Int) extends Ordered[Man] {//不用序列化，也可以用  case默认实现了序列化     但是这个方法耦合写死了

  override def compare(that: Man): Int = {
    if(this.score == that.score) {
      this.age - that.age
    } else {
      -(this.score - that.score)
    }
  }
}
