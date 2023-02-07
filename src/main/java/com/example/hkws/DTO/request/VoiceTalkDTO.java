package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;

/**
 * Class:  VoiceTalkDTO
 * <p>
 * Author: zhaoyg
 * Date:   2023/2/6 13:55
 * Desc:   VoiceTalkDTO
 */
@Data
public class VoiceTalkDTO {

    /**
     * 语音通道号。对于设备本身的语音对讲通道，从1开始；对于设备的IP通道，为登录返回的
     * 起始对讲通道号(byStartDTalkChan) + IP通道索引 - 1，例如客户端通过NVR跟其IP Channel02所接前端IPC进行对讲，则dwVoiceChan=byStartDTalkChan + 1
     */
    @NotNull(message = "channelName不能为空")
    @ApiModelProperty(name = "channelName", value = "channelName", required = true, dataType = "Integer")
    public int channelName;

    @NotNull(message = "bret不能为空")
    @ApiModelProperty(name = "bret", value = "bret", required = true, dataType = "Boolean")
    public boolean bret;
}
