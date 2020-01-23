package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import java.util.List;


/**
 * 查询条件
 *
 */
@Data
public class CloseLiveDTO {
    @ApiModelProperty(name = "channelList", value = "channelList", required = true, dataType = "List")
    public List<String> channelList;

}
