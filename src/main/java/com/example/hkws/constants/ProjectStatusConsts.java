package com.example.hkws.constants;

/**
 * 项目状态常量
 * 
 * @创建日期 : 2019年8月25日
 * @作者 : zzx
 * //  0 已创建但未提交；1 提交待督查部门审核 ； 2 督查部门已审核，驳回给项目负责人； 3 督查部门已审核，提交领导审核 ，4 领导已审核，驳回给督查部门；
 * //   5：督查部门将领导的驳回意见转发给项目负责人；6 领导已审核，等待督查部门完善项目资料  7入库成功；
 */
public final class ProjectStatusConsts {
	/**入库流程 分类
	 *
	 */
	public static final Integer CREATED_BUT_NOT_SUBMITTED = 0;
	/**
	 *
	 */
	public static final Integer SUBMIT_TO_SUPERVISION_DEPARTMENT= 1;
	/**
	 *
	 */
	public static final Integer REJECTED_THE_PROJECT_LEADER = 2;
	/**
	 *
	 */
	public static final Integer SUBMITTED_THE_LEADERSHIP= 3;
	/**
	 *
	 */
	public static final Integer REJECTED_TO_SUPERVISION_DEPARTMENT = 4;

	public static final Integer PASS = 7;

	public static final Integer STORAGE = 8;

	/**
	 * @Description:  //	汇报填报阶段
	 * @Param:
	 * @return:
	 * @Author: zzx
	 * @Date: 2019-08-30
	 **/

	public static final Integer REPORT= 9;
	//	汇报-督查部门审核阶段
	public static final Integer REPORT_INSPECTOR= 10;

	//	汇报-督查部门退回给牵头部门阶段
	public static final Integer REPORT_REJECT2LEADUNIT= 11;

	//	汇报-领导审核审核阶段
	public static final Integer REPORT_LEADER= 12;

	//	汇报-领导退回阶段
	public static final Integer REPORT_REJECT2INSPECTOR= 13;
	//	汇报审核完成
	public static final Integer REPORT_PASS=14;

	//	wo需解决的
	public static final Integer NEED_TO_BE_SOLVED=15;

	/**
	* @Description: 这个指的是 入参时候projectType 分类,谁能看啥种项目
	* @Param:
	* @return:
	* @Author: zzx
	* @Date: 2019-08-30
	**/

	public static final Integer PRINCIAL = 1;

	public static final Integer INSPECTE= 2;

	public static final Integer COOPERATE= 3;

	public static final Integer LEADUNIT= 4;

	public static final Integer CONTRACTE= 5;

	public static final Integer MINOR_IN_CHARGE= 6;

	public static final Integer IN_CHARGE= 7;

	public static final  Integer SOLVING_UNIT = 8;



	/**
	* @Description: 项目类型 这个是指数据库中的projectType
	* @Param:
	* @return:
	* @Author: zzx
	* @Date: 2019-09-02
	**/
	public  static  final String[] PROJECTCATEGORYLIST ={"市重点建设项目新动工","市重点建设项目续建","市重点建设项目前期预备",
		"县十件民生工程","县十件民生实事","省重点建设项目新动工","省重点建设项目续建","省重点建设项目前期预备"};
	
	/** 
	* @Description: 导出excel前要根据projectType再进行一次分类 
	* @Param:  
	* @return:  
	* @Author: zzx
	* @Date: 2019-09-03 
	**/




}
