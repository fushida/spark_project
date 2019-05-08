# Spark  Project 

pom.xml帮助建立相关的连接

1)RDD_Subject 是利用RDD算子实现的一个实例，有助于对RDD的理解

             subiect.log 里面是1000条课程日志信息，类似：http://www.python.org.cn/kuli  python为课程名，kuli为任课老师
		 
		 SQLFavSubject.scala 运用Sparksql实现
		 
		 
2)ip_log  获取用户端的ip，通过ip规则归属地查询获取用户当前的位置信息IpLoaction.scala仅仅用RDD算子，IpLoactionSQL.scala运用SparkSQL
   
             ip_log.txt 是中国ip归属地划分规则
             user.log是用户行为日志采集的信息
   
   
   

 
3)RDD_Sorts 是RDD对于排序规则实例用法。里面有六种形式，主要的差别是在是否有函数方法构造、序列化、利用元组的规则等

4)SparkSql 是SparkSql中spark1.0和spark2.0的总结与demo。在SQLdemo1_spark1.scala中对于1.0的使用笔记，RDD实现DataSet和DataFrame

             SparkSql实现RDD转化为DataFrame操作和总结SQLdemo.scala
		 
		 DataSource_ .scala是spark从不同格式的文件中读取数据的样例
		 
		 Wordcunt 实现代码DateSetWordCount.scala 和SQLWordCount.scala
		 
		 SparkJoinTest 实现无结构表转化为架构表后,利用join实现多个表的复杂逻辑简单操作
		 
		 在Spark上使用Hive数据库HiveOnSpark.scala
		 
5)streaming 中有实现streaming实现wordcount;kafka实现对数据的实时读取数据：KafkaWordCount.scala，对历史数据累加：KafkaWordCountStateful.scala
         比较直接方式和receiver方式，以及两者的实现，编写直连方式偏移量。
		 