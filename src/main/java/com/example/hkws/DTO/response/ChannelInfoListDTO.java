package com.example.hkws.DTO.response;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;

/**
 * 通道信息
 */
@Data
public class ChannelInfoListDTO {

    @ApiModelProperty(name = "通道是否在录像,0-不录像,1-录像")
    public int byRecordStatic;

    @ApiModelProperty(name = "连接的信号状态,0-正常,1-信号丢失")
    public int bySignalStatic;

    @ApiModelProperty(name = "通道硬件状态,0-正常,1-异常,例如DSP死掉")
    public int byHardwareStatic;

    @ApiModelProperty(name = "实际码率")
    public int dwBitRate;

    @ApiModelProperty(name = "客户端连接的个数")
    public int dwLinkNum;

    @ApiModelProperty(name = "设备IP")
    public String IPAddress;

}
