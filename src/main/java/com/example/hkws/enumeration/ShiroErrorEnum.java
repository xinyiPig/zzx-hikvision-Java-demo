package com.example.hkws.enumeration;

/**
 * 
 * @Descrption shiro错误信息枚举
 * @class_name ShiroErrorEnum
 * @author Ergou
 * @Date 2018年8月3日 下午1:40:36
 */
public enum ShiroErrorEnum {
    /*
     * 错误信息
     * */
	/**
     *请求处理异常，请稍后再试
     */
    E_400("400", "请求处理异常，请稍后再试"),
    /**
     *请求方式有误,请检查 GET/POST
     */
    E_500("500", "请求方式有误,请检查 GET/POST"),
    /**
     * 请求路径不存在
     */
    E_501("501", "请求路径不存在"),
    /**
     * 权限不足
     */
    E_502("502", "权限不足"),
    /**
     * 角色删除失败,尚有用户属于此角色
     */
    E_10008("10008", "角色删除失败,尚有用户属于此角色"),
    /**
     * 账户已存在
     */
    E_10009("10009", "账户已存在"),

    /**
     * 登陆已过期,请重新登陆
     */
    E_20011("20011", "登陆已过期,请重新登陆"),

    /**
     * "缺少必填参数"
     */
    E_90003("90003", "缺少必填参数"),
	
	/**
     * "账号或者密码错误"
     */
    E_416("416", "账号或者密码错误"),
	
	/**
     * "该账户已经登录"
     */
    E_417("417", "该账户已经登录"),
    
    /**
     * 	登录失败次数过多，请10分钟后再登录
     */
    E_418("418", "登录失败次数过多，请10分钟后再进行登录");

    private String errorCode;

    private String errorMsg;

    ShiroErrorEnum() {
    }

    ShiroErrorEnum(String errorCode, String errorMsg) {
        this.errorCode = errorCode;
        this.errorMsg = errorMsg;
    }

    public String getErrorCode() {
        return errorCode;
    }

    public void setErrorCode(String errorCode) {
        this.errorCode = errorCode;
    }

    public String getErrorMsg() {
        return errorMsg;
    }

    public void setErrorMsg(String errorMsg) {
        this.errorMsg = errorMsg;
    }
}
