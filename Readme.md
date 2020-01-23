---
title:海康威视摄像头sdk-java demo
date: 2020-1-18 10:50
tags: 
- java springBoot
categories: 
- java springBoot


---

### 一：下载海康JAVA-DEMO

我开发环境是window10-64位 IDEA  JDK1.8 的 和  mac  IDEA  JDK1.8
demo下载地址 https://www.hikvision.com/cn/download_more_570.html

<!--more-->

1：下载完成以后，必须认真看完这个txt，不然后面可能会出现“dll缺失”和“sdk文件不存在”这种异常

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/1.jpg">



2：sdk使用手册【必看】

![](https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/4.jpg)

### 二：摄像设备ip，通道查看

海康摄像头设备的录像文件一般都是存储在录像机，找出录像机的ip及账号密码

在最新版IE11上访问 录像机ip

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/2.jpg">

输入账号密码就能看到这个录像机关联的摄像头，一般来讲第一个就是通道一，第二个就是通道二，ip暂时没用到，如果想看具体的摄像头ip ,可以下载个[4200工具](https://www.hikvision.com/cn/download_more_390.html)

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/3.jpg">

当你测试sdk中预览啊，回放啊什么功能出现问题的时候，可以在此管理平台检验一下是设备不支持，还是代码问题；我发现敝司用的监控摄像头就没有云台控制功能的；

### 三：rtmp实时预览

1：我想要在web端显示实时预览的画面，而海康提供的demo是用GUI 组件去解码视频流数据来显示；并且客服说只能提供IE和旧版chrome浏览器的解码插件；这不符合预期，那么接下来是使用ffmpeg【[ffmpeg的安装、环境变量配置及基本使用](https://www.cnblogs.com/sntetwt/p/11435564.html)】对海康视频流rtsp流进行转换，并以rtmp协议的流数据返回给前端，前端通过[video.js](https://videojs.com/)来展示

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/5.jpg">

端口默认8000，所以组装成如下格式

rtsp://admin:password@192.168.123.200/Streaming/Channels/101?transportmode=unicast

101主码流比102子码流更清晰，组装好以后可以用[vlc播放器](https://www.videolan.org/vlc/index.zh.html)检查一下地址

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/6.jpg">

2：下载已经集成nginx-rtmp插件的nginx，传送门：https://github.com/illuspas/nginx-rtmp-win32

解压以后，打开conf/nginx.conf文件

```
worker_processes  1;

error_log  logs/error.log info;

events {
    worker_connections  1024;
}

rtmp {
    server {
        listen 1935;

        application live {
            live on;
        }
		
        application hls {
            live on;
            hls on;  
            hls_path temp/hls;  
            hls_fragment 8s;  
        }
    }
}

http {
    server {
        listen      8080;
		

		
        location / {
            root html;
        }
		
        location /stat {
            rtmp_stat all;
            rtmp_stat_stylesheet stat.xsl;
        }

        location /stat.xsl {
            root html;
        }
		
        location /hls {  
            #server hls fragments  
            types{  
                application/vnd.apple.mpegurl m3u8;  
                video/mp2t ts;  
            }  
            alias temp/hls;  
            expires -1;  
        }  
    }
}
```

在文件中找到nginx.exe，双击启动

三：在cmd中使用 ffmpeg命令 ,'home'随便命名的



```
ffmpeg -re  -rtsp_transport tcp -i "rtsp://admin:password@192.168.123.200/Streaming/Channels/101?transportmode=unicast" -f flv -vcodec h264 -vprofile baseline -acodec aac -ar 44100 -strict -2 -ac 1 -f flv -s 640*360 -q 10 "rtmp://localhost:1935/live/home"
```

获取视频源并转码成功如下

<img src='https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/7.jpg'>

使用vlc播放器来测试视频流

<img src='https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/8.jpg'>

那么windows版海康摄像头rtsp源转rtmp源实时预览到此到此基本结束啦；

因为chrome浏览器2020年底就不再支持flash,而videojs是使用flash来播放 ,所以得换成http-flv协议的视频流，这个协议是我对比了其他协议的优劣（如hls）才最终决定的，又因为[nginx-flv-module](https://github.com/winshining/nginx-http-flv-module)我找不到在windows上衣编译好的模块，编译起码又特别麻烦；所以我换成在macbook上去实现 海康摄像头 rtsp源转http-flv源，前端使用[flv.js](https://github.com/bilibili/flv.js/blob/master/docs/api.md)实现播放

### 四：http-flv实时预览

1：openssl下载：因为openssl前不久爆出重大安全漏洞，所以mac系统不支持使用依赖管理工具来下载，只能收到下载，下载地址：https://github.com/openssl/openssl

2：nginx下载 ：http://nginx.org/  我一开始用1.8不行，后面改成1.12就可以了

3：nginx-flv下载：https://github.com/winshining/nginx-http-flv-module

4：找出nginx下configure文件所在路径

```
./configure --add-module=/path/to/nginx-http-flv-module --with-openssl=/pathto/openssl-master/

make

make install
```

5：编译完成以后，终端会输出一个nginx的安装路径，前往该目录修改nginx.conf,启动nginx

修改nginx.conf，请参考：https://github.com/winshining/nginx-http-flv-module 最下面有个example

6：输入ffmpeg 转码命令

```
ffmpeg -re  -rtsp_transport tcp -i "rtsp://admin:password@192.168.123.200/Streaming/Channels/101?transportmode=unicast" -f flv -vcodec h264 -vprofile baseline -acodec aac -ar 44100 -strict -2 -ac 1 -f flv -s 640*360 -q 10 "rtmp://localhost:1935/myapp/home"
```

7：http-flv 流地址格式，

```
http://yourdomain[:httpport]/dir?[rtmpport=xxx&]app=yyy&stream=zzz
```

其中httpport是Nginx的配置文件中`http`块中监听的端口，如果这个端口是80，那么可以省略不写；rtmpport是Nginx的配置文件中`rtmp`块中监听的端口，如果这个端口是1935，那么可以省略不写。`dir`是Nginx的配置文件中`http`块中`location`后的路径。参数app 是example中的

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/9.jpg">

 参数stream可以随便填

```
http://192.168.30.53/live?port=1935&app=myapp&stream=home
```

8：使用vlc播放器校验一下http-flv视频源是否正常播放就ok了



### 五：历史回放

就这么简单啦

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/10.jpg">

### 上传到git的代码经测试无误，flv测试跟rtmp类似，就不做演示了

<img src="https://zz--blog-oss.oss-cn-beijing.aliyuncs.com/%E6%B5%B7%E5%BA%B7java-demo/11.jpg">

注：转载请注明出处:[zzx-hikvision-java-demo](https://github.com/xinyiPig/zzx-hikvision-Java-demo/wiki/%E6%B5%B7%E5%BA%B7%E5%A8%81%E8%A7%86%E6%91%84%E5%83%8F%E5%A4%B4sdk-java)