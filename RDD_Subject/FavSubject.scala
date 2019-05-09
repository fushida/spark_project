package com.big.spark.sparkTest

import java.net.URL

import org.apache.spark.rdd.RDD
import org.apache.spark.{SparkConf, SparkContext}

/**
  * 数据读取->整理->聚合->排序->action
  * 最基本的方式，没有利用分组
  */
/***   切分规则
 val line = "http://python.org/stack"
    
     //学科，老师
//    val splits: Array[String] = line.split("/")
//
//    val subject = splits(2).split("[.]")(0)
//
//    val teacher = splits(3)
//
//    println(subject + " " + teacher)
    var index=line.lastIndexOf("/")
    var teacher =line.substring(index+1)
    var httpHost=line.substring(0,index)
    var subject=new URL(httpHost).getHost.split("[.]")(0)
    println(teacher+","+subject)
 * 
 */
object FavSubject {
  def main(args: Array[String]): Unit = { 
    val conf = new SparkConf().setAppName("FavSubject").setMaster("local[*]")
    val sc = new SparkContext(conf)

    //指定以后从哪里读取数据
    val lines: RDD[String] = sc.textFile(args(0))   //别忘记传入参数
    //整理数据
    val teacherAndOne = lines.map(line => {
      val index = line.lastIndexOf("/")
      val teacher = line.substring(index + 1)
      //val httpHost = line.substring(0, index)
      //val subject = new URL(httpHost).getHost.split("[.]")(0)
      (teacher, 1)//value 置为1
    })
    //聚合
    val reduced: RDD[(String, Int)] = teacherAndOne.reduceByKey(_+_)
    //排序
    val sorted: RDD[(String, Int)] = reduced.sortBy(_._2, false)
    //触发Action执行计算
    val reslut: Array[(String, Int)] = sorted.collect()//collect返回的是数组

    //打印
    println(reslut.toBuffer)
    sc.stop()
  }
}