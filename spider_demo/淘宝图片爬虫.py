#爬取淘宝中的图片
import urllib.request
import re
keyname="书包"
key=urllib.request.quote(keyname)
headers = ("User-Agent",
           "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
opener=urllib.request.build_opener()
opener.add_handler=[headers]#伪装
urllib.request.install_opener(opener)

for i in range(0,10):#根据页码进行翻页
    url="https://uland.taobao.com/sem/tbsearch?refpid=mm_26632258_3504122_32538762&clk1=4ce0df90a4ffcf9dcf80e69f75ccab99&keyword="+keyname+"&page="+str(i)
    data = urllib.request.urlopen(url).read().decode("utf-8", "ignore")
    pat='pic_url":"//(.*?)'#进行修改，淘宝需要登录
    imagelist=re.compile(pat).findall(data)
    for j in range(0,len(imagelist)):
        thisimg=imagelist[j]
        thismgurl="http://"+thisimg
        file="E:/0Download/jiepai/"+str(i)+str(j)+".jpg"
        urllib.request.urlretrieve(thismgurl,filename=file)


#对于有些网页爬虫时无法直接获得，需要进行抓包
#抓包工具   Fidder web Debugger   https://www.bilibili.com/video/av22571713/?p=23  该视频中与讲解
