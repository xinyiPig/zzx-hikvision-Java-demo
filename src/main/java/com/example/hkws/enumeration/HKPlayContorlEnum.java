package com.example.hkws.enumeration;

import com.example.hkws.service.Linux.HCNetSDK;

/**
 * 错误码
 *
 * @创建日期 : 2018年10月23日
 * @作者 : GS_MASTER
 */
public enum HKPlayContorlEnum {
    ZOOM_IN(HCNetSDK.ZOOM_IN, "焦距变大"),
    ZOOM_OUT(HCNetSDK.ZOOM_OUT, "焦距变小"),
    TILT_UP(HCNetSDK.TILT_UP, "云台以SS的速度上仰"),
    TILT_DOWN(HCNetSDK.TILT_DOWN, "云台以SS的速度下俯" ),
    PAN_LEFT(HCNetSDK.PAN_LEFT, "云台以SS的速度左转"),
    PAN_RIGHT(HCNetSDK.PAN_RIGHT, "云台以SS的速度右转"),
    UP_LEFT(HCNetSDK.UP_LEFT, "云台以SS的速度上仰和左转"),
    UP_RIGHT(HCNetSDK.UP_RIGHT, "云台以SS的速度上仰和右转"),
    DOWN_LEFT(HCNetSDK.DOWN_LEFT, "云台以SS的速度下俯和左转"),
    DOWN_RIGHT(HCNetSDK.DOWN_RIGHT, "云台以SS的速度下俯和右转"),
    PAN_AUTO(HCNetSDK.PAN_AUTO, "云台以SS的速度左右自动扫描") ,

    ;

    private Integer code;
    private String msg;

    private  Integer value;

    private HKPlayContorlEnum(Integer code, String msg) {
        this.code = code;
        this.msg = msg;
    }

    public Integer getCode() {
        return code;
    }

    public void setCode(Integer code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }
}
