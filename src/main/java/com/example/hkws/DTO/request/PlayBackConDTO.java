package com.example.hkws.DTO.request;

import io.swagger.annotations.ApiModelProperty;
import lombok.Data;

import javax.validation.constraints.NotNull;


/**
 * 查询条件
 *
 */
@Data
public class PlayBackConDTO {
    @NotNull(message = "channelName不能为空")
    @ApiModelProperty(name = "channelName", value = "channelName", required = true, dataType = "String")
    public String channelName;		//年

    @NotNull(message = "startYear不能为空")
    @ApiModelProperty(name = "startYear", value = "startYear", required = true, dataType = "Integer")
    public Integer startYear;		//年

    @NotNull(message = "startMonth不能为空")
    @ApiModelProperty(name = "startMonth", value = "startMonth", required = true, dataType = "Integer")
    public Integer startMonth;		//月

    @NotNull(message = "startDay不能为空")
    @ApiModelProperty(name = "startDay", value = "startDay", required = true, dataType = "Integer")
    public Integer startDay;		//日

    @NotNull(message = "startHour不能为空")
    @ApiModelProperty(name = "startHour", value = "startHour", required = true, dataType = "Integer")
    public Integer startHour;		//时

    @NotNull(message = "startMinute不能为空")
    @ApiModelProperty(name = "startMinute", value = "startMinute", required = true, dataType = "Integer")
    public Integer startMinute;		//分

    @NotNull(message = "startSecond不能为空")
    @ApiModelProperty(name = "startSecond", value = "startSecond", required = true, dataType = "Integer")
    public Integer startSecond;		//秒

    @NotNull(message = "endYear不能为空")
    @ApiModelProperty(name = "endYear", value = "endYear", required = true, dataType = "Integer")
    public Integer endYear;		//年

    @NotNull(message = "endMonth不能为空")
    @ApiModelProperty(name = "endMonth", value = "endMonth", required = true, dataType = "Integer")
    public Integer endMonth;		//月

    @NotNull(message = "endDay不能为空")
    @ApiModelProperty(name = "endDay", value = "endDay", required = true, dataType = "Integer")
    public Integer endDay;		//日

    @NotNull(message = "endHour不能为空")
    @ApiModelProperty(name = "endHour", value = "endHour", required = true, dataType = "Integer")
    public Integer endHour;		//时

    @NotNull(message = "endMinute不能为空")
    @ApiModelProperty(name = "endMinute", value = "endMinute", required = true, dataType = "Integer")
    public Integer endMinute;		//分

    @NotNull(message = "endSecond不能为空")
    @ApiModelProperty(name = "endSecond", value = "endSecond", required = true, dataType = "Integer")
    public Integer endSecond;		//秒

}
