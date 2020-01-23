package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 查询条件
 *
 */
@Data
public class LiveDTO {
    @ApiModelProperty(name = "channelName", value = "channelName", required = true, dataType = "String")
    public String channelName;

    @ApiModelProperty(name = "channelStream", value = "channelStream", required = true, dataType = "String")
    public String channelStream;

}
