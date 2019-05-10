import org.apache.spark.streaming.dstream.{DStream, ReceiverInputDStream}
import org.apache.spark.streaming.{Milliseconds, StreamingContext}
import org.apache.spark.{SparkConf, SparkContext}

/**
  * steaming实现wordcount
    但是只能实现对当前输入的计算，不能对历史数据累加，对于出现故障也不知道故障点
  */
object StreamingWordCount {

  def main(args: Array[String]): Unit = {

    //离线任务是创建SparkContext，现在要实现实时计算，用StreamingContext

    val conf = new SparkConf().setAppName("StreamingWordCount").setMaster("local[2]")
    val sc = new SparkContext(conf)
    //StreamingContext是对SparkContext的包装，包了一层就增加了实时的功能
    //第二个参数是小批次产生的时间间隔
    val ssc = new StreamingContext(sc, Milliseconds(5000))

    //有了StreamingContext，就可以创建SparkStreaming的抽象了DSteam
    //从一个socket端口中读取数据
    //在Linux上用yum安装nc    nc -lk 8888
    //yum install -y nc
    val lines: ReceiverInputDStream[String] = ssc.socketTextStream("47.107.133.129", 8888)
    //对DSteam进行操作，你操作这个抽象（代理，描述），就像操作一个本地的集合一样
    //切分压平
    val words: DStream[String] = lines.flatMap(_.split(" "))
    //单词和一组合在一起
    val wordAndOne: DStream[(String, Int)] = words.map((_, 1))
    //聚合
    val reduced: DStream[(String, Int)] = wordAndOne.reduceByKey(_+_)
    //打印结果(Action)
    reduced.print()

    //启动sparksteaming程序
    ssc.start()
    //等待优雅的退出
    ssc.awaitTermination()


  }
}
