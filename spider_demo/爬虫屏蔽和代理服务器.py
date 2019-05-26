#爬虫屏蔽和代理服务器
import urllib.request
import re
url="http://blog.csdn.net/"
headers = ("User-Agent",
           "Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
opener=urllib.request.build_opener()
opener.add_handler=[headers]#伪装
urllib.request.install_opener(opener)
data=urllib.request.urlopen(url).read().decode("utf-8","ignore")
pat='<a href="(https://blog.csdn.net.*?)"'#正则提取文章
allurl=re.compile(pat).findall(data)
print(len(data))
for i in range(len(allurl)):#网址存储
    file="E:/0Download/jiepai/"+str(i)+".html"
    urllib.request.urlretrieve(allurl[i],filename=file)
    print("第"+str(i)+"次成功")

#代理ip
def  use_proxy(url,proxy_addr):
    proxy=urllib.request.ProxyHandler({"http":proxy_addr})#https://www.xicidaili.com/
    opener=urllib.request.build_opener(proxy,urllib.request.HTTPHandler)
    urllib.request.install_opener(opener)#注册为全局
    urllib.request.urlopen(url)
    data = urllib.request.urlopen(url).read().decode("utf-8", "ignore")
    return data
proxy_addr="112.85.171.102:9999"
url="http://www.baidu.com"
datas=use_proxy(url,proxy_addr)
print(len(datas))#[WinError 10061] 表示ip被封了