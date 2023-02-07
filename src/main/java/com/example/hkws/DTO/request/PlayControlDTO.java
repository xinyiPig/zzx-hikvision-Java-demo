package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 查询条件
 *
 */
@Data
public class PlayControlDTO {

    /**
     * channelName是指的login后返回的channelList中的值
     * Camera1
     */
    @NotNull(message = "channelName不能为空")
    @ApiModelProperty(name = "channelName", value = "channelName", required = true, dataType = "String")
    public String channelName;

    @NotNull(message = "command不能为空")
    @ApiModelProperty(name = "command", value = "command", required = true, dataType = "String")
    public String command;

}
