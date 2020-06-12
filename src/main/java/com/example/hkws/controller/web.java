package com.example.hkws.controller;

import com.example.hkws.CommandManager;
import com.example.hkws.CommandManagerImpl;
import com.example.hkws.DTO.ResultDTO;
import com.example.hkws.DTO.request.*;
import com.example.hkws.constants.ErrorCodeConsts;
import com.example.hkws.data.CommandTasker;
import com.example.hkws.enumeration.HKPlayContorlEnum;
import com.example.hkws.enumeration.ResultEnum;
import com.example.hkws.exception.GlobalException;

 import com.example.hkws.service.window.HCNetSDK;
//import com.example.hkws.service.Linux.HCNetSDK;
import io.swagger.annotations.ApiOperation;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.*;
import io.swagger.annotations.Api;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import com.sun.jna.ptr.IntByReference;
import com.sun.jna.ptr.NativeLongByReference;

import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;
import javax.servlet.http.HttpSession;
import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.Properties;

@RestController
@RequestMapping("/camera")
@Api(description = "海康摄像头模块")
@Slf4j
public class web {
    // 如果要打包到linux 记得把HCNetSDK 也要换成 linux版的
    static HCNetSDK hCNetSDK = null;//HCNetSDK.INSTANCE;

    @Value("${fileUploadPath}")
    private String fileUploadPath;

    @Value("${ipc.account}")
    private String ipcAccount;

    @Value("${ipc.password}")
    private String ipcPassword;

    @Value("${nvr.account}")
    private String nvrAccount;

    @Value("${nvr.password}")
    private String nvrPassword;

    @Value("${nvr.ip}")
    private String nvrIp;


    @Value("${currentserver}")
    private String currentserver;

    @Value("${videoStreamPort}")
    private String videoStreamPort;


    public static NativeLong g_lVoiceHandle;//全局的语音对讲句柄

    //       设置最多十个视频转码，可以设置大一些，随意的
    public  static   CommandManager manager=new CommandManagerImpl(5);
    /*windows*/
    private  String winAccountInfo= "admin:linghong2019@192.168.123.200";
//    linux
    private  String linuxAccountInfo= "admin:asdf1234@";

    boolean bRealPlay;//是否在预览.

    NativeLong lPreviewHandle;//预览句柄
    NativeLongByReference m_lPort;//回调预览时播放库端口指针

    NativeLong lAlarmHandle;//报警布防句柄
    NativeLong lListenHandle;//报警监听句柄

   @PostMapping("/login")
    public ResultDTO login(@RequestBody LoginDTO loginDTO, HttpServletRequest request) throws GlobalException {
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true)
        {
            return ResultDTO.of(ResultEnum.ERROR).setData("初始化失败");
        }
        String m_sDeviceIP = loginDTO.getIp();//设备ip地址
        HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP,
                (short) loginDTO.getPort(), loginDTO.getAccount(), loginDTO.getPassword(), m_strDeviceInfo);

        long userID = lUserID.longValue();
        if (userID == -1)
        {
            m_sDeviceIP = "";//登录未成功,IP置为空
            return ResultDTO.of(ResultEnum.ERROR).setData("登录失败");
        }
        HttpSession session = request.getSession();
        session.setAttribute("m_sDeviceIP",m_sDeviceIP);
        session.setAttribute("lUserID",lUserID);
        List<String>  channelList =  CreateDeviceChannel(lUserID,m_strDeviceInfo);
        System.out.println("channelList"+channelList);
        return ResultDTO.of(ResultEnum.SUCCESS).setData(channelList);
    }

//    @PostMapping("/getLiveStream")
//    @ApiOperation(value="window,获取实时视频流",notes = "channelStream:101 代表通道一 主码流。102代表通道一 子码流；子码流比主码流小，但画质会有所下降 ，channelName是通道名，能保证唯一即可")
    public ResultDTO live(@RequestBody LiveDTO liveDTO, HttpServletRequest request, HttpServletResponse response) throws  GlobalException{
        HttpSession session = request.getSession();
        NativeLong lUserID = (NativeLong) session.getAttribute("lUserID");
        if(lUserID.intValue()==-1){
            return ResultDTO.of(ResultEnum.REQUIRE_LOGIN);
        }
        String liveUrl = "";
        String channelName = liveDTO.getChannelName();

        //通过id查询这个任务
        CommandTasker info=manager.query(channelName);
//       如果任务没存在，开启视频流
        if(Objects.isNull(info)){
            //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
            manager.start(channelName, "ffmpeg -re  -rtsp_transport tcp -i \"rtsp://"+winAccountInfo+"/Streaming/Channels/"+liveDTO.getChannelStream()+"?transportmode=unicast\" -f flv -vcodec h264 -vprofile baseline -acodec aac -ar 44100 -strict -2 -ac 1 -f flv -s 1480*500  -max_muxing_queue_size 1024 -crf 15 \"rtmp://localhost:1935/live/\""+channelName);
        }
        // 如果是window rtmp版就返回 rtmp://localhost:1935/live/\""+channelName
        liveUrl = "rtmp://localhost:1935/live/"+channelName;
        // 下面这个是http-flv版的流
//        liveUrl= "/live?port=1935&app=myapp&stream="+channelName;

        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
    }

    @PostMapping("/getLiveStreamByIp")
    @ApiOperation(value="windows 获取实时视频流",notes = "channelStream:101 代表通道一 主码流。102代表通道一 子码流；子码流比主码流小，但画质会有所下降 ，channelName是通道名，能保证唯一即可")
    public ResultDTO getLiveStreamByIp(@RequestBody LiveDTO liveDTO, HttpServletRequest request, HttpServletResponse response) throws  GlobalException{

        String liveUrl = "";
        String channelName = liveDTO.getChannelName();
        String ip = liveDTO.getIp();
        String channelStream = liveDTO.getChannelStream();

        //通过id查询这个任务
        CommandTasker info=manager.query(channelName);
//       如果任务没存在，开启视频流
        if(Objects.isNull(info)){
            //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
            try {
               String result= manager.start(channelName, "ffmpeg   -rtsp_transport tcp -i \"rtsp://"+ipcAccount+":"+ipcPassword+"@"+ip+"/Streaming/Channels/"+channelStream+"?transportmode=unicast\" -f flv -vcodec h264 -preset ultrafast -b:v 500k  -tune zerolatency -acodec copy -ar 44100 -strict -2 -ac 1  -s 1480*500 -crf 15 \"rtmp://localhost:1935/live/\""+channelName);
                log.info("result"+result);
            }catch (Exception e){
                log.info("windows:"+e.getMessage());
                throw new GlobalException(ErrorCodeConsts.ERROR,e.getMessage());
            }
        }else{
            log.info(channelName+"任务已存在，从任务列表中取出数据返回");
        }
        // 如果是window rtmp版就返回 rtmp://localhost:1935/live/\""+channelName
        liveUrl = "rtmp://"+currentserver+":1935/live/"+channelName;
        // 下面这个是http-flv版的流
//        liveUrl= "/live?port=1935&app=myapp&stream="+channelName;

        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
    }

    @PostMapping("/getHistoryStream")
    @ApiOperation(value="windows 获取历史视频流",notes = "channelStream:101 通道一 码流一，channelName是通道名，能保证唯一即可,跟前端约定就以 history为前缀，避免跟上面的channelName冲突了")
    public ResultDTO history(@RequestBody HistoryDTO historyDTO, HttpServletRequest request, HttpServletResponse response) throws  GlobalException{
        String liveUrl = "";
        String channelName = historyDTO.getChannelName();
        String ip = historyDTO.getIp();
        //
        String channelStream = winGetChannelNumByIp(ip)+"01";
        String starttime = historyDTO.getStarttime();
        String endtime = historyDTO.getEndtime();

        //通过id查询这个任务
        CommandTasker info=manager.query(channelName);
//       如果任务没存在，开启视频流
        if(Objects.isNull(info)) {
            //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
            try{
             String result = manager.start(channelName, "ffmpeg -re  -rtsp_transport tcp -i " +
                    "\"rtsp://"+nvrAccount+":"+nvrPassword+"@"+nvrIp+"/Streaming/tracks/" + channelStream + "?starttime=" + starttime + "&endtime=" + endtime + "\" " +
                    " -f flv -vcodec h264 -acodec copy  -s 1480*500 -crf 15 \"rtmp://localhost:1935/live/\"" + channelName);
                log.info("result"+result);

            }catch (Exception e){
                log.info("windows history:"+e.getMessage());

                throw new GlobalException(ErrorCodeConsts.ERROR,e.getMessage());
                }
        }else{
            log.info(channelName+"任务已存在，从任务列表中取出数据返回");
        }
        // 如果是window rtmp版就返回 rtmp://localhost:1935/live/\""+channelName
        liveUrl = "rtmp://"+currentserver+":1935/live/"+channelName;
        // 下面这个是http-flv版的流
//        liveUrl= "/live?port=1935&app=myapp&stream="+channelName;

        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
    }

    @PostMapping("/closeLiveStream")
    @ApiOperation(value="windows 关闭视频流")
    public ResultDTO closeLive(@RequestBody CloseLiveDTO closeLiveDTO){
       log.info("关闭视频流");
        List<String> channelList = closeLiveDTO.getChannelList();
        if(channelList.size()!=0){
            for(String channelName:channelList){
                //       设置最多十个视频转码，可以设置大一些，随意的
                // CommandManager manager=new CommandManagerImpl(10);
                //通过id查询这个任务
                CommandTasker info=manager.query(channelName);

                //       如果任务存在
                if(!Objects.isNull(info)){
//                    System.out.println(info);
                    try {
                        manager.stop(channelName);
                    }catch (Exception e){
                        log.info("关闭视频流异常"+e.getMessage());
                    }
                }
            }
        }
        return ResultDTO.of(ResultEnum.SUCCESS);
    }


    @PostMapping("/linux/getLiveStreamByIp")
    @ApiOperation(value="linux，获取实时视频流",notes = "channelStream:101 代表通道一 主码流。102代表通道一 子码流；子码流比主码流小，但画质会有所下降 ，channelName是通道名，能保证唯一即可")
    public ResultDTO linuxGetLiveStreamByIp(@RequestBody LiveDTO liveDTO, HttpServletRequest request, HttpServletResponse response) throws  GlobalException{

        String liveUrl = "";
        String channelName = liveDTO.getChannelName();
        String ip = liveDTO.getIp();

        //通过id查询这个任务
        CommandTasker info=manager.query(channelName);
//       如果任务没存在，开启视频流
        if(Objects.isNull(info)) {
            try{
            //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
                manager.start(channelName, "ffmpeg -re  -rtsp_transport tcp -i \"rtsp://"+ipcAccount+":"+ipcPassword+"@" + ip + "/Streaming/Channels/" + liveDTO.getChannelStream() + "?transportmode=unicast\" -f flv -vcodec h264 -preset ultrafast -b:v 500k  -tune zerolatency -acodec copy -ar 44100 1480trict -2 -ac 1 -s 1480*500 -max_muxing_queue_size 1024 -crf 15 \"rtmp://localhost:1935/live/\"" + channelName);
             }catch (Exception e){
                log.info("linux:"+e.getMessage());

                throw new GlobalException(ErrorCodeConsts.ERROR,e.getMessage());
            }
        }
//        liveUrl = "rtmp://localhost:1935/live/"+channelName;
        // 下面这个是http-flv版的流,前端显示时加上ip地址即可
        liveUrl= "http://"+currentserver+":"+videoStreamPort+"/live?port=1935&app=live&stream="+channelName;

        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
    }

    @PostMapping("/linux/getHistoryStream")
    @ApiOperation(value="linux，获取历史视频流",notes = "channelStream:101 通道一 码流一，channelName是通道名，能保证唯一即可,跟前端约定就以 history为前缀，避免跟上面的channelName冲突了")
    public ResultDTO linuxHistory(@RequestBody HistoryDTO historyDTO, HttpServletRequest request, HttpServletResponse response) throws  GlobalException{
        String liveUrl = "";
        String channelName = historyDTO.getChannelName();
        String starttime = historyDTO.getStarttime();
        String endtime = historyDTO.getEndtime();
        String ip = historyDTO.getIp();
        String channelStrem = linuxGetChannelNumByIp(ip)+"01";
        //通过id查询这个任务
        CommandTasker info=manager.query(channelName);
//       如果任务没存在，开启视频流
        if(Objects.isNull(info)){
           try {
               //执行原生ffmpeg命令（不包含ffmpeg的执行路径，该路径会从配置文件中自动读取）
               //172.32.251.66 只能从nvr设备上去取历史rtsp流
               manager.start(channelName, "ffmpeg -re  -rtsp_transport tcp -i " +
                       "\"rtsp://"+nvrAccount+":"+nvrPassword+"@"+nvrIp+"/Streaming/tracks/" + channelStrem + "?transportmode=unicast&startime=" + starttime + "&endtime=" + endtime + "\" " +
                       "-f flv -vcodec h264 -acodec copy -preset veryfast -b:v 500k  -tune zerolatency -ar 44100 -strict -2 -ac 1  -s 1480*500  -max_muxing_queue_size 1024 -crf 15 \"rtmp://localhost:1935/live/\"" + channelName);
           }catch (Exception e){
               log.info("linux history:"+e.getMessage());

               throw new GlobalException(ErrorCodeConsts.ERROR,e.getMessage());
            }
        }
        // 如果是window rtmp版就返回 rtmp://localhost:1935/live/\""+channelName
//        liveUrl = "rtmp://localhost:1935/live/"+channelName;
        // 下面这个是http-flv版的流，前端显示时加上ip地址即可
        liveUrl= "http://"+currentserver+":"+videoStreamPort+"/live?port=1935&app=live&stream="+channelName;

        return ResultDTO.of(ResultEnum.SUCCESS).setData(liveUrl);
    }

    @PostMapping("/linux/closeLiveStream")
    @ApiOperation(value="linux，关闭视频流")
    public ResultDTO linuxCloseLive(@RequestBody CloseLiveDTO closeLiveDTO){
        List<String> channelList = closeLiveDTO.getChannelList();
        if(channelList.size()!=0){
            for(String channelName:channelList){
                //       设置最多十个视频转码，可以设置大一些，随意的
                // CommandManager manager=new CommandManagerImpl(10);
                //通过id查询这个任务
                CommandTasker info=manager.query(channelName);

                //       如果任务存在
                if(!Objects.isNull(info)){
                    try {
                        manager.stop(channelName);
                    }catch (Exception e){
                        log.info("关闭视频流异常"+e.getMessage());
                    }
                }
            }
        }
        return ResultDTO.of(ResultEnum.SUCCESS);
    }

//    下载文件版回放 耗时较长，建议使用rtsp协议版
    @PostMapping("/getVideoUrl")
    public ResultDTO playback(@RequestBody PlayBackConDTO playBackConDTO, HttpServletRequest request, HttpServletResponse response) throws GlobalException, InterruptedException {
        HttpSession session = request.getSession();
        NativeLong lUserID = (NativeLong) session.getAttribute("lUserID");
        String sDeviceIP = (String)session.getAttribute("m_sDeviceIP");
        // =====================按照开始时间和结束时间下载视频 开始====================================
        NativeLong m_lLoadHandle = new NativeLong(-1);
                 if (m_lLoadHandle.intValue() == -1) {
                     HCNetSDK.NET_DVR_TIME struStartTime;
                         HCNetSDK.NET_DVR_TIME struStopTime;

                        struStartTime = new HCNetSDK.NET_DVR_TIME();
                        struStopTime = new HCNetSDK.NET_DVR_TIME();
                         struStartTime.dwYear = (playBackConDTO.getStartYear());// 开始时间
                         struStartTime.dwMonth = (playBackConDTO.getStartMonth());
                        struStartTime.dwDay = playBackConDTO.getStartDay();
                         struStartTime.dwHour = playBackConDTO.getStartHour();
                         struStartTime.dwMinute =playBackConDTO.getStartMinute();
                         struStartTime.dwSecond = playBackConDTO.getStartSecond();

                         struStopTime.dwYear = playBackConDTO.getEndYear();// 结束时间
                         struStopTime.dwMonth = playBackConDTO.getEndMonth();
                         struStopTime.dwDay = playBackConDTO.getEndDay();
                         struStopTime.dwHour = playBackConDTO.getEndHour();
                         struStopTime.dwMinute = playBackConDTO.getEndMinute();
                         struStopTime.dwSecond = playBackConDTO.getEndSecond();
                         int m_iChanShowNum = getChannelNumber(playBackConDTO.getChannelName());// 通道（摄像头IP地址）
                         System.out.println(playBackConDTO.toString());
                         System.out.println("lUserID:"+lUserID);
                         System.out.println("m_iChanShowNum:"+m_iChanShowNum);
                         String fileName = sDeviceIP+"/"+m_iChanShowNum+"/"+struStartTime.toStringTitle() + ".mp4";
                         String sFileName = fileUploadPath +fileName;
                         System.out.println(sFileName);

                         // 视频下载调用 下载的文件是mpeg-ps 非标准的mpeg-4
                        m_lLoadHandle = hCNetSDK.NET_DVR_GetFileByTime(lUserID, new NativeLong(m_iChanShowNum), struStartTime,
                                         struStopTime, sFileName);
                         if (m_lLoadHandle.intValue() >= 0) {
//                             开始下载
                             hCNetSDK.NET_DVR_PlayBackControl(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYSTART, 0, null);
                             IntByReference nPos = new IntByReference(0);
                             while (m_lLoadHandle.intValue()>=0){
//                                 获取下载进度
                                 hCNetSDK.NET_DVR_PlayBackControl(m_lLoadHandle, HCNetSDK.NET_DVR_PLAYGETPOS, 0, nPos);
                                 if (nPos.getValue() == 100) {
                                     hCNetSDK.NET_DVR_StopGetFile(m_lLoadHandle);
                                     m_lLoadHandle.setValue(-1);
                                     System.out.println("按时间下载结束!");
                                     Integer error =  hCNetSDK.NET_DVR_GetLastError();
                                     System.out.println("last error " +error);


                                 }
                                 if (nPos.getValue() > 100) {
                                     Integer error =  hCNetSDK.NET_DVR_GetLastError();
                                     System.out.println("下载失败");// 按时间
                                     System.out.println("last error " +error);
                                     return ResultDTO.of(ResultEnum.ERROR).setData(error);

                                 }
                                 Thread.sleep(500);
                             }
                             return ResultDTO.of(ResultEnum.SUCCESS).setData(sFileName);


                             // System.out.println("视频下载成功！");
                             } else {
                                 Integer error =  hCNetSDK.NET_DVR_GetLastError();
                                 System.out.println("下载失败");// 按时间
                                 System.out.println("last error " +error);
                                 return ResultDTO.of(ResultEnum.ERROR).setData(error);
                             }
                     }
        return ResultDTO.of(ResultEnum.ERROR);
    }

//    @RequestMapping(value = "/videoPlay", method = RequestMethod.GET)
    public void videoPlay(HttpServletRequest request, HttpServletResponse response,@RequestParam String url) {
//        String path = request.getServletContext().getRealPath(url);
        String path = fileUploadPath+url;
        BufferedInputStream bis = null;
        System.out.println("url"+url);
        System.out.println("path"+path);
        try {
            File file = new File(path);
            if (file.exists()) {
                long p = 0L;
                long toLength = 0L;
                long contentLength = 0L;
                int rangeSwitch = 0; // 0,从头开始的全文下载；1,从某字节开始的下载（bytes=27000-）；2,从某字节开始到某字节结束的下载（bytes=27000-39000）
                long fileLength;
                String rangBytes = "";
                fileLength = file.length();

                // get file content
                InputStream ins = new FileInputStream(file);
                bis = new BufferedInputStream(ins);

                // tell the client to allow accept-ranges
                response.reset();
                response.setHeader("Accept-Ranges", "bytes");

                // client requests a file block download start byte
                String range = request.getHeader("Range");
                if (range != null && range.trim().length() > 0 && !"null".equals(range)) {
                    response.setStatus(javax.servlet.http.HttpServletResponse.SC_PARTIAL_CONTENT);
                    rangBytes = range.replaceAll("bytes=", "");
                    if (rangBytes.endsWith("-")) { // bytes=270000-
                        rangeSwitch = 1;
                        p = Long.parseLong(rangBytes.substring(0, rangBytes.indexOf("-")));
                        contentLength = fileLength - p; // 客户端请求的是270000之后的字节（包括bytes下标索引为270000的字节）
                    } else { // bytes=270000-320000
                        rangeSwitch = 2;
                        String temp1 = rangBytes.substring(0, rangBytes.indexOf("-"));
                        String temp2 = rangBytes.substring(rangBytes.indexOf("-") + 1, rangBytes.length());
                        p = Long.parseLong(temp1);
                        toLength = Long.parseLong(temp2);
                        contentLength = toLength - p + 1; // 客户端请求的是 270000-320000 之间的字节
                    }
                } else {
                    contentLength = fileLength;
                }

                // 如果设设置了Content-Length，则客户端会自动进行多线程下载。如果不希望支持多线程，则不要设置这个参数。
                // Content-Length: [文件的总大小] - [客户端请求的下载的文件块的开始字节]
//                response.setHeader("Content-Length", new Long(contentLength).toString());

                // 断点开始
                // 响应的格式是:
                // Content-Range: bytes [文件块的开始字节]-[文件的总大小 - 1]/[文件的总大小]
                if (rangeSwitch == 1) {
                    String contentRange = new StringBuffer("bytes ").append(new Long(p).toString()).append("-")
                            .append(new Long(fileLength - 1).toString()).append("/")
                            .append(new Long(fileLength).toString()).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else if (rangeSwitch == 2) {
                    String contentRange = range.replace("=", " ") + "/" + new Long(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                    bis.skip(p);
                } else {
                    String contentRange = new StringBuffer("bytes ").append("0-").append(fileLength - 1).append("/")
                            .append(fileLength).toString();
                    response.setHeader("Content-Range", contentRange);
                }

                String fileName = file.getName();
                response.setContentType("application/octet-stream");
                response.addHeader("Content-Disposition", "attachment;filename=" + fileName);

                OutputStream out = response.getOutputStream();
                int n = 0;
                long readLength = 0;
                int bsize = 1024;
                byte[] bytes = new byte[bsize];
                if (rangeSwitch == 2) {
                    // 针对 bytes=27000-39000 的请求，从27000开始写数据
                    while (readLength <= contentLength - bsize) {
                        n = bis.read(bytes);
                        readLength += n;
                        out.write(bytes, 0, n);
                    }
                    if (readLength <= contentLength) {
                        n = bis.read(bytes, 0, (int) (contentLength - readLength));
                        out.write(bytes, 0, n);
                    }
                } else {
                    while ((n = bis.read(bytes)) != -1) {
                        out.write(bytes, 0, n);
                    }
                }
                out.flush();
                out.close();
                bis.close();
            }
        } catch (IOException ie) {
            // 忽略 ClientAbortException 之类的异常
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

//    @PostMapping("/playControl")
    public ResultDTO playControl(@RequestBody PlayControlDTO playControlDTO, HttpServletRequest request) throws GlobalException{
        HttpSession session = request.getSession();
        NativeLong lUserID = (NativeLong) session.getAttribute("lUserID");
        int iCannleNum = getChannelNumber(playControlDTO.getChannelName());
        HKPlayContorlEnum controlEnum = HKPlayContorlEnum.valueOf(playControlDTO.getCommand());
        Boolean playHandle = false;
        System.out.println("controlEnum.getCode():"+controlEnum.getCode());
        System.out.println("playControlDTO:"+playControlDTO.toString());
//        开始控制
        playHandle = hCNetSDK.NET_DVR_PTZControl_Other(lUserID,new NativeLong(iCannleNum),controlEnum.getCode(),0);
        if(playHandle==false){
            Integer error =  hCNetSDK.NET_DVR_GetLastError();
            System.out.println("控制失败");// 按时间
            System.out.println("last error " +error);
            return ResultDTO.of(ResultEnum.ERROR).setData(error);
        }
        //        停止控制
        playHandle = hCNetSDK.NET_DVR_PTZControl_Other(lUserID,new NativeLong(iCannleNum),controlEnum.getCode(),1);
        if(playHandle==false){
            Integer error =  hCNetSDK.NET_DVR_GetLastError();
            System.out.println("控制失败");// 按时间
            System.out.println("last error " +error);
            return ResultDTO.of(ResultEnum.ERROR).setData(error);
        }
        return ResultDTO.of(ResultEnum.SUCCESS);
    }

    /**
     * @Description: 返回通道数，要根据这个通道数组装成 102,101这种通道流去找nvr要数据
     * @Param:
     * @Author: zzx 774286887@qq.com
     * @Date: 2020/5/28
     * @Time: 17:06
     * @return:
     */
    public String  winGetChannelNumByIp(String ip) throws GlobalException {
        String channelNum = "";
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true)
        {
            throw new GlobalException (ErrorCodeConsts.ERROR,"刻录机初始化失败");
        }
        String m_sDeviceIP = nvrIp;//设备ip地址
        HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP,
                (short) 8000, nvrAccount,nvrPassword, m_strDeviceInfo);

        long userID = lUserID.longValue();
        if (userID == -1)
        {
            m_sDeviceIP = "";//登录未成功,IP置为空
            throw new GlobalException (ErrorCodeConsts.ERROR,"刻录机登录失败");
        }
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        boolean bRet = false;

        HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparaCfg.write();
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
        m_strIpparaCfg.read();

        if (!bRet)
        {
            //设备不支持,则表示没有IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {


            }
        }
        else
        {
            //设备支持IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {
                if(m_strIpparaCfg.byAnalogChanEnable[iChannum] == 1)
                {

                }
            }
            for(int iChannum =0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++)
                if (m_strIpparaCfg.struIPChanInfo[iChannum].byEnable == 1)
                {
                    String channelIp = (new String(m_strIpparaCfg.struIPDevInfo[iChannum].struIP.sIpV4));
                    channelIp=channelIp.trim();
                    log.info("channelIp:"+channelIp.length());
                    log.info("ip:"+ip.length());
                    log.info(String.valueOf(channelIp.equals(ip)));
                    if(channelIp.equals(ip)){
                        channelNum=(String.valueOf(iChannum+1));
                    }

                }
        }
        if(Objects.isNull(channelNum)){
            throw new GlobalException(ErrorCodeConsts.ERROR,"获取通道号失败，历史回放不成功");
        }
        return channelNum;
    }

    /**
     * @Description: 返回通道数，要根据这个通道数组装成 102,101这种通道流去找nvr要数据
     * @Param:
     * @Author: zzx 774286887@qq.com
     * @Date: 2020/5/28
     * @Time: 17:06
     * @return:
     */
    public String  linuxGetChannelNumByIp(String ip) throws GlobalException {
        String channelNum = "";
        boolean initSuc = hCNetSDK.NET_DVR_Init();
        if (initSuc != true)
        {
            throw new GlobalException (ErrorCodeConsts.ERROR,"刻录机初始化失败");
        }
        String m_sDeviceIP = nvrIp;//设备ip地址
        HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo = new HCNetSDK.NET_DVR_DEVICEINFO_V30();
        NativeLong lUserID = hCNetSDK.NET_DVR_Login_V30(m_sDeviceIP,
                (short) 8000, nvrAccount,nvrPassword, m_strDeviceInfo);

        long userID = lUserID.longValue();
        if (userID == -1)
        {
            m_sDeviceIP = "";//登录未成功,IP置为空
            throw new GlobalException (ErrorCodeConsts.ERROR,"刻录机登录失败");
        }
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        boolean bRet = false;

        HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparaCfg.write();
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
        m_strIpparaCfg.read();

        if (!bRet)
        {
            //设备不支持,则表示没有IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {


            }
        }
        else
        {
            //设备支持IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {
                if(m_strIpparaCfg.byAnalogChanEnable[iChannum] == 1)
                {

                }
            }
            for(int iChannum =0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++)
                if (m_strIpparaCfg.struIPChanInfo[iChannum].byEnable == 1)
                {
                    String channelIp = (new String(m_strIpparaCfg.struIPDevInfo[iChannum].struIP.sIpV4));
                    channelIp=channelIp.trim();
                    log.info("channelIp:"+channelIp.length());
                    log.info("ip:"+ip.length());
                    log.info(String.valueOf(channelIp.equals(ip)));
                    if(channelIp.equals(ip)){
                        channelNum=(String.valueOf(iChannum+1));
                    }

                }
        }
        if(Objects.isNull(channelNum)){
            throw new GlobalException(ErrorCodeConsts.ERROR,"获取通道号失败，历史回放不成功");
        }
        return channelNum;
    }


    /*************************************************
     函数:    CreateDeviceTree
     函数描述:建立设备通道数
     *************************************************/
    private List<String> CreateDeviceChannel(NativeLong lUserID, HCNetSDK.NET_DVR_DEVICEINFO_V30 m_strDeviceInfo)
    {
        List<String>  channelList = new ArrayList<>();
        IntByReference ibrBytesReturned = new IntByReference(0);//获取IP接入配置参数
        boolean bRet = false;

        HCNetSDK.NET_DVR_IPPARACFG m_strIpparaCfg = new HCNetSDK.NET_DVR_IPPARACFG();
        m_strIpparaCfg.write();
        Pointer lpIpParaConfig = m_strIpparaCfg.getPointer();
        bRet = hCNetSDK.NET_DVR_GetDVRConfig(lUserID, HCNetSDK.NET_DVR_GET_IPPARACFG, new NativeLong(0), lpIpParaConfig, m_strIpparaCfg.size(), ibrBytesReturned);
        m_strIpparaCfg.read();

        if (!bRet)
        {
            //设备不支持,则表示没有IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {
                channelList.add("Camera" + (iChannum + m_strDeviceInfo.byStartChan));

            }
        }
        else
        {
            //设备支持IP通道
            for (int iChannum = 0; iChannum < m_strDeviceInfo.byChanNum; iChannum++)
            {
                if(m_strIpparaCfg.byAnalogChanEnable[iChannum] == 1)
                {
                    channelList.add("Camera" + (iChannum + m_strDeviceInfo.byStartChan));
                }
            }
            for(int iChannum =0; iChannum < HCNetSDK.MAX_IP_CHANNEL; iChannum++)
                if (m_strIpparaCfg.struIPChanInfo[iChannum].byEnable == 1)
                {
                    System.out.println(new String(m_strIpparaCfg.struIPDevInfo[iChannum].struIP.sIpV4));
                    channelList.add("IPCamera" + (iChannum + m_strDeviceInfo.byStartChan));

                }
        }
    return channelList;
    }
    /*************************************************
     函数:    getChannelNumber
     函数描述:从设备树获取通道号
     *************************************************/
    int getChannelNumber(String sChannelName)
    {
        int iChannelNum = -1;


            //获取选中的通道名,对通道名进行分析:

            if(sChannelName.charAt(0) == 'C')//Camara开头表示模拟通道
            {
                //子字符串中获取通道号
                iChannelNum = Integer.parseInt(sChannelName.substring(6));
            }
            else
            {
                if(sChannelName.charAt(0) == 'I')//IPCamara开头表示IP通道
                {
                    //子字符创中获取通道号,IP通道号要加32
                    iChannelNum = Integer.parseInt(sChannelName.substring(8)) + 32;
                }
                else
                {
                    return -1;
                }
            }

        return iChannelNum;
    }









}
