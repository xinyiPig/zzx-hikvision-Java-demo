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
    ZOOM_OUT(HCNetSDK.ZOOM_OUT, "焦距变小");

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
