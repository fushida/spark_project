#街拍2 代码源自
import json
from urllib.parse import urlencode
import requests
import re
import os


cookie = "tt_webid=6694223721472001544; UM_distinctid=16ae50459b319d-0ef4b55aca775b-7a1437-100200-16ae50459b41bc; csrftoken=a13c263693a7a490b4b783633ac61ed9; tt_webid=6694223721472001544; WEATHER_CITY=%E5%8C%97%E4%BA%AC; __tasessionId=h4x0uv5nn1558710063019; CNZZDATA1259612802=132349129-1558616690-%7C1558709375; s_v_web_id=cc160e0b32bd49346dfaca19a16a9679"
header = {'User-agent': "Mozilla/5.0 (Windows NT 6.1; rv:2.0.1) Gecko/20100101 Firefox/4.0.1", 'cookie': cookie}

#请求页索引
def get_page_index(keyword, offset):#Network  XHR  Header
    """这个我是不能粘贴的哈，自己登陆后用谷歌浏览器查看"""

    data = {"aid": 24,
              "app_name": "web_search",
              "offset":offset,
              "format": "json",
              "keyword": keyword,
              "autoload": "true",
              "count": "20",
              "en_qc": 1,
              "cur_tab": 1,
              "from": "search_tab",
              "pd": "synthesis",
              "timestamp": 1558711234366
              }
    url = "https://www.toutiao.com/api/search/content/?" + urlencode(data)#　这里用到urlib库的将参数转换为网址后缀
    try:
          r = requests.get(url, headers=header, timeout=20)
          if r.status_code == 200:
          # print(r.encoding)  # 从httpheader推测
          #  print(r.apparent_encoding)  # 从内容中分析的解析编码格式
              r.encoding = "json"# 定义对此文档的解析编码格式,这里上面两种方法返回的编码格式都不对，
              return r.text
    except:
         print("索引页获取失败！")

# 解析索引页ｊｓｏｎ数据
def parse_page_index(html):
    data = json.loads(html)# 此处将json数据转为字典
    if data and 'data' in data.keys(): # data 是否存在，data的键值对是否有‘data’   data.keys为返回的所有键名
        for item in data.get('data'):
            yield item.get('article_url')# 构造生成器。参考https://blog.csdn.net/mieleizhi0522/article/details/82142856

# 获取详情页
def get_page_detail(url):
    try:
        r = requests.get(url, headers=header)
        return r.text
    except:
        print("获取详情页出错！")

# 处理详情页
def parse_page_detail(html):
     gallery = re.findall(r"gallery: JSON.parse(.*?)siblingList", html, re.S) # r 是为了无视转义字符
     if len(gallery) != 0:
        for need_deal_gallery in gallery: # 因为提取到的是个只有一项的列表，所以需要遍历。
            deal_gallery = re.sub(r"(\\)", '', need_deal_gallery) # 去掉字符串的转义字符，仍然是字符串
            url_list = re.findall('(http.*?)"', deal_gallery, re.S) # 返回列表
            return url_list

    # soup=BeautifulSoup(html,'lxml')
    # title=soup.select('title')[0].get_text()
    # print(title)
    # images_pattern=re.compile('var gallery=(.*?);',re.S)
    # result=re.search(images_pattern.html)
    # if  result:
    #     print(result.group(1))
# 下载图片
def download(url):
    #图片保存md5,可以去重
     root = "E:/0Download/jiepai/"
     path = root + url.split('/')[-1]
     try:
         if not os.path.exists(root):
              os.mkdir(root)
              if not os.path.exists(path):
                  r = requests.get(url, headers=header)
                  with open(path, 'wb') as f:
                        f.write(r.content)
                        print("保存成功！")
     except:
          print("保存失败！")

def main(key, pages):
    i = 0
    while i <= pages:
       offset = 20 * i
       i += 1
       dict_html = get_page_index(key, offset)
       all_url_list = []
       for url in parse_page_index(dict_html):
            if url != None: # 过滤不是网址的返回
               html = get_page_detail(url)
               url_lists = parse_page_detail(html)
               if url_lists != None: # 过滤不是组图的返回。因为组图函数处理不了，函数return中那块不能过滤。
                   for lis in url_lists:
                       all_url_list.append(lis)
       for url in all_url_list:
             download(url)

if  __name__=='__main__':
    main("街拍", 3)  # 关键词，和页数

