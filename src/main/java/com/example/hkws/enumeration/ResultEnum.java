package com.example.hkws.enumeration;

import com.example.hkws.constants.ErrorCodeConsts;

/**
 * 错误码
 * 
 * @创建日期 : 2018年10月23日
 * @作者 : GS_MASTER
 */
public enum ResultEnum {
    SUCCESS(ErrorCodeConsts.SUCCESS, "成功"), 
    NOT_FONUD(ErrorCodeConsts.NOT_FOUND, "资源不存在或者已删除"),
    USER_NOT_FOUND(ErrorCodeConsts.NOT_FOUND, "用户不存在 "),
    UER_UNBIND(ErrorCodeConsts.UNBIND,"用户未绑定账号"),
    INVALID_APP_SORT(ErrorCodeConsts.FAIL, "无效的应用排序"), 
    DEPT_NAME_NOT_BLANK(ErrorCodeConsts.FAIL, "部门名称不能为空"), 
    GROUP_NAME_NOT_BLANK(ErrorCodeConsts.FAIL, "权限组名称不能为空"), 
    GROUP_NAME_EXIST(ErrorCodeConsts.FAIL, "权限组名称已存在"), 
    GROUP_NOT_FOUND(ErrorCodeConsts.FAIL, "权限组不存在"), 
    DEFAULT_DATA(ErrorCodeConsts.FORBIDEN, "系统默认数据，无法編輯和删除"), 
    INVALID_SORT_INFO(ErrorCodeConsts.FAIL, "无效的排序信息"), 
    ERROR(ErrorCodeConsts.ERROR, "系统异常"), 
    REDIS_CONNECTION_FAIL(ErrorCodeConsts.FAIL, "缓存服务器连接失败，请联系维护人员"),
    ENCRYPT_FAIL(ErrorCodeConsts.FORBIDEN, "请求数据解密失败"), 
    ACCOUNT_HAS_BOUND(ErrorCodeConsts.FORBIDEN, "该账号已被绑定"), 
    HAS_NOT_AUTH(ErrorCodeConsts.FORBIDEN, "请先完成认证"), 
    FORBIDEN_ACCESS(ErrorCodeConsts.FORBIDEN, "非法操作，禁止访问"), 
    REQUIRE_LOGIN(ErrorCodeConsts.AUTH, "请先登录"), 
    INVALID_DISTRIBUTION(ErrorCodeConsts.FORBIDEN, "权限管理栏目不能与其它栏目同时分配"),
    
    
    
    // 基本操作
    INSERT_SUCCESS(ErrorCodeConsts.SUCCESS, "新建成功"),
    UPDATE_SUCCESS(ErrorCodeConsts.SUCCESS, "更新成功"),
    DELETE_SUCCESS(ErrorCodeConsts.SUCCESS, "删除成功"),
    
    INSERT_FAILURE(ErrorCodeConsts.FAIL, "新建失败"),
    UPDATE_FAILURE(ErrorCodeConsts.FAIL, "更新失败"),
    DELETE_FAILURE(ErrorCodeConsts.FAIL, "删除失败"),
    
	
    /**
     * 无效的父栏目编号
     */
	INVALID_COLUMN_PARENT_ID(ErrorCodeConsts.FAIL, "无效的父栏目编号");

    private Integer code;

    private String msg;

    private ResultEnum(Integer code, String msg) {
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
