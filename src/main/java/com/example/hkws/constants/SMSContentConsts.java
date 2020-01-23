package com.example.hkws.constants;

/**
 * 项目状态常量
 * 
 * @创建日期 : 2018年10月25日
 * @作者 : GS_MASTER
 * //  0 已创建但未提交；1 提交待督查部门审核 ； 2 督查部门已审核，驳回给项目负责人； 3 督查部门已审核，提交领导审核 ，4 领导已审核，驳回给督查部门；
 * //   5：督查部门将领导的驳回意见转发给项目负责人；6 领导已审核，等待督查部门完善项目资料  7入库成功；
 */
public final class SMSContentConsts {
	/**
	 *
	 */
	public static final String PRINCIAL2INSPECTOR = "";
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
	/**
	 *
	 */
	/**
	 *
	 */
	public static final Integer PASS = 7;

	public static final Integer STORAGE = 8;

}
