#爬取新浪新闻首页

import urllib.request
import re

data=urllib.request.urlopen("https://news.sina.com.cn/").read()
data2=data.decode("utf-8","ignore")#编码

#.匹配任何字符  *匹配前面的子表达式零次或多次  ？匹配前面的子表达式零次或一次
pat='href="(http://news.sina.com.cn/.*?)"'
allurl=re.compile(pat).findall(data2)
for i in range(0,len(allurl)):
    try:
        print("第"+str(i)+"次爬取")
        thisurl=allurl[i]
        file="E:/0Download/jiepai/"+str(i)+".html"
        urllib.request.urlretrieve(thisurl,file)
        print("------")
    except urllib.error.URLError as e:
        if hasattr(e,"code"):
            print(e.code)
        if hasattr(e,"reason"):
            print(e.reason)