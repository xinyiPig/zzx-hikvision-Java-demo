package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;


/**
 * 查询条件
 *
 */
@Data
public class HistoryDTO {
    @ApiModelProperty(name = "通道名，以history为前缀", value = "channelName", required = true, dataType = "String")
    public String channelName;

    @ApiModelProperty(name = "如101  前面的1是通道一，后面的1表示码流", value = "channelStream", required = true, dataType = "String")
    public String channelStream;

    @ApiModelProperty(name = "开始时间", value = "20120802t093812z", required = true, dataType = "String")
    public String starttime;

    @ApiModelProperty(name = "结束时间", value = "20120802t103812z", required = true, dataType = "String")
    public String endtime;

}
