package cn.dmp.utils

object NBF {
      //把字符串转换的方法

    def toInt(str: String): Int = {
        try {
            str.toInt
        } catch {
            case _: Exception => 0
        }
    }

    def toDouble(str: String): Double = {
        try {
            str.toDouble
        } catch {
            case _: Exception => 0
        }

    }

}
