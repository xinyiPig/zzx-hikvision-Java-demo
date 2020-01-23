package com.example.hkws.constants.log;

/**
 * 
 * @Descrption 操作日志类型，技术有限，暂时针对类型编写日志代码
 * @class_name LogOperationType
 * @author Ergou
 * @Date 2018年8月10日 下午2:27:13
 */
public interface LogOperationType {
	
	/**	添加	*/
	String CREATE = "添加";
	
	/**	修改基本内容	*/
	String UPDATE = "修改";
	
	/**	发布	*/
	String PUBLISH = "发布";
	
	/**	删除	*/
	String DELETE = "删除";

	/**	授权	*/
	String AUTHORIZATION = "授权";
	
}
