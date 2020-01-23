package com.example.hkws.constants.log;


/**
 * 日志登录类型
 *
 * @author fengshuonan
 * @Date 2017年1月22日 下午12:14:59
 */
public enum LogType {

    LOGIN("登录"),
    LOGIN_FAIL("登录"),
    EXIT("退出"),
    EXCEPTION("异常日志"),
    BUSSINESS("业务日志");

    String message;

    LogType(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
