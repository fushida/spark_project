#对CSDN爬虫

import urllib.error
import urllib.request
import requests
try:
   # data=urllib.request.urlopen("http://blog.csdn.net").read().decode("utf-8","ignore")
   #print(data)
    url="https://blog.csdn.net/u012477144/article/details/85872941"
    headers=("User-Agent","Mozilla/5.0 (Windows NT 10.0; WOW64) AppleWebKit/537.36 (KHTML, like Gecko) Chrome/63.0.3239.132 Safari/537.36")
    data= requests.get(url,headers=headers)
    fh=open("E:/0Download/jiepai/result.html","wb")
    fh.write(data)
    fh.close()
except urllib.error.URLError as e :
    if hasattr(e,"code"):
        print(e.code)
    if hasattr(e,"reason"):
        print(e.reason)