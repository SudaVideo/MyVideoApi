# MyVideoApi

## 这是什么
MyVideoApi 是基于 Spring Boot 开发的应用，利用网络爬虫技术，实时解析互联网资源，提供api给前端
 
## 补充说明
    本项目仅用于学习,任何单位或个人不得将项目用于商业目的。
  
## api

### 查询视频源
    /video/source
接口返回
```
{
    "data":[
        {
            "source":0,
            "type":"film",
            "name":"电影1"
        },
        {
            "source":0,
            "type":"teleplay",
            "name":"剧集1"
        }
    ],
    "success":true
}
 ```

### 根据类别分页查询视频
    /video/page?source=0&type=film&pageIndex=1
参数
```
* @param source    源id
* @param type      类型
* @param pageIndex 页数
```  
    
接口返回
```
{
    "data":{
        "pageSize":25,
        "data":[
            {
                "title":"碟中谍5",
                "thumb":"https://img31.mtime.cn/mg/2015/08/26/172358.77099492_270X405X4.jpg",
                "videoId":"3704",
                "source":0
            },
            {
                "title":"碟中谍4",
                "thumb":"https://img5.mtime.cn/mg/2016/11/09/144341.13183872_270X405X4.jpg",
                "videoId":"3703",
                "source":0
            }
        ]
    },
    "success":true
}
 ```
 
### 查询视频详情
    /video/detail?source=0&videoId=3704
参数
```
* @param source    源id
* @param videoId   视频id
```  
    
接口返回
```
{
  "data": {
    "videoSeries": [
      {
        "seriesId": "Ac_01",
        "name": "天翼高清"
      }
    ],
    "desc": "　　资深特工伊森·亨特（汤姆·克鲁斯 Tom Cruise 饰）也有百密一疏时刻，他在接收最新任务时遭到神秘组织“辛迪加”的暗算落入对方手中。辛迪加是一支由全球各地前特工组成的秘密组织，此前一直被CIA视为空穴来风。在对方成员伊莎·福斯特（丽贝卡·弗格森 Rebecca Ferguson 饰）的帮助下，伊森逃出生天，并及时向威廉•布莱德（杰瑞米·雷纳 Jeremy Renner 饰）汇报了辛迪加确切存在的消息。然而此时布莱德的日子并不好过，他负责的IMF机构因俄罗斯核弹头等事件遭到CIA指控并责令解散。在得知该组织密谋刺杀奥地利总统时，伊森联系上了老搭档班吉·邓恩（西蒙·佩吉 Simon Pegg 饰）展开行动，并于谜样女郎伊莎再度相会。 　　接下来的一连串事件中，辛迪加的真面目逐渐揭开，而他们也终于显露出真实的目的……",
    "previewImgs": [
      "https://img31.mtime.cn/pi/2015/07/21/094148.72440524_1000X1000.jpg",
      "https://img31.mtime.cn/pi/2015/07/21/094014.34414121_1000X1000.jpg",
      "https://img31.mtime.cn/pi/2015/07/21/094009.38871136_1000X1000.jpg",
      "https://img31.mtime.cn/pi/2015/07/21/094041.54516242_1000X1000.jpg"
    ]
  },
  "success": true
}
 ```
 
 ### 查询视频播放地址
     /video/playUrl?source=0&videoId=3704&seriesId=Ac_01
 参数
 ```
 * @param source    源id
 * @param videoId   视频id
 * @param seriesId  集数id
 ```  
     
 接口返回
 ```
{
  "data": "http://vd3.bdstatic.com/mda-ih5un792ceu09ya0/mda-ih5un792ceu09ya0.mp4",
  "success": true
}
```

 ### 根据名称查视频
     /video/search?videoName=复仇
 参数
 ```
 * @param source    源id
 * @param videoId   视频id
 * @param seriesId  集数id
 ```  
     
 接口返回
 ```
{
  "data": [
    {
      "title": "复仇",
      "thumb": "https://img5.mtime.cn/mg/2018/04/03/182824.74161297_270X405X4.jpg",
      "videoId": "3509",
      "source": 0
    },
    {
      "title": "《政宗君的复仇》高清全集在线观看",
      "thumb": "https://ww2.sinaimg.cn/large/87c01ec7gy1fq6nwyonxej20qo0goaxf.jpg",
      "videoId": "anime+av449",
      "source": 4
    },
    {
      "title": "复仇者联盟2：奥创纪元",
      "thumb": "https://img31.mtime.cn/mg/2015/03/27/120537.13212993_270X405X4.jpg",
      "videoId": "movies+list02+3030.html",
      "source": 3
    },
    {
      "title": "复仇者联盟3",
      "thumb": "https://img5.mtime.cn/mg/2018/03/30/101318.97845092_270X405X4.jpg",
      "videoId": "3682",
      "source": 0
    },
    {
      "title": "复仇者联盟",
      "thumb": "https://img5.mtime.cn/mg/2017/01/24/105507.20986123_270X405X4.jpg",
      "videoId": "2585",
      "source": 0
    }
  ],
  "success": true
}
```

### 查询站点信息
    /video/source
接口返回
```
{
  "data": {
    "siteName": "xxxxx",
    "contactEmail": "xxxx@xx.com"
  },
  "success": true
}
```