package com.example.hkws.callback.sdk;

import com.example.hkws.service.window.HCNetSDK;
import com.sun.jna.NativeLong;
import com.sun.jna.Pointer;
import org.springframework.stereotype.Component;

/**
 * Class:  VoiceDataCallBack
 * <p>
 * Author: zhaoyg
 * Date:   2023/2/6 14:21
 * Desc:   VoiceDataCallBack
 */
@Component
public class VoiceDataCallBack implements HCNetSDK.FVoiceDataCallBack_V30 {

    public void invoke(NativeLong lVoiceComHandle, String pRecvDataBuffer, int dwBufSize, byte byAudioFlag, Pointer pUser) {
        //回调函数里保存语音对讲中双方通话语音数据
        System.out.println("语音对讲数据回调....");
    }
}
