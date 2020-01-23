package com.example.hkws.constants.log;

/**
 * 
 * @Descrption 操作日志类型，技术有限，暂时针对类型编写日志代码
 * @class_name LogOperationType
 * @author Ergou
 * @Date 2018年8月10日 下午2:27:13
 */
public interface LogNavbarType {
	
	/**
	 * 文章
	 */
	String ARTICLE = "文章";

	/**	科室简介	*/
	String DEPARTMENT_INTRODUCTION = "科室简介";

	/**	科室子网站简介	*/
	String DEPARTMENT_WEBSITE = "科室子网站简介";

	/**	科室子网站文章	*/
	String DEPARTMENT_ARTICLE = "科室子网站文章";

	/**	栏目管理-医院概况	*/
	String NAVBAR_HOSPITAL_SITUATION = "栏目管理-医院概况";
	
	/**	栏目管理-招标采购	*/
	String NAVBAR_BIDDING_PURCHASING = "栏目管理-招标采购";

	/**	栏目管理-科研教学	*/
	String NAVBAR_RESEARCH_TEACHING = "栏目管理-科研教学";

	/**	栏目管理-科室导航	*/
	String NAVBAR_DEPARTMENT = "栏目管理-科室导航";

	/**	普通用户*/
	String NORMAL_USER = "普通用户管理";

	/**	管理员用户*/
	String USER = "管理员用户管理";

	/**	身份权限	*/
	String ROLE_PERMISSION = "身份权限";

	/**	*/
	String LEAVE_A_MESSAGE = "留言管理";

	/**	*/
	String EMPLOYMENT_APPLICATION = "人才招聘处理";

}
