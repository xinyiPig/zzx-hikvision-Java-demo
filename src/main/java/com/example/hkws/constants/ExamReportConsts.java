package com.example.hkws.constants;

/**
 * 检验报告常量类
 * @author qinguangrui
 *
 */
public interface ExamReportConsts {
	/**
	 * 检验报告URL
	 */
	public static final String URL = "http://192.168.100.84:1506/services/WSInterface";
	
	/**
	 * redis中同一IP验证码错误统计的key前缀
	 */
	public static final String IP_LIMIT_COUNT = "IP_LIMIT_COUNT";
	
	/**
	 * 验证码最大错误次数
	 */
	public static final Integer IP_LIMIT_SUM = 5;
	
	/**
	 * IP限制时间
	 */
	public static final Integer IP_LIMIT_TIME = 60;
	/**
	 * 查询结果失败
	 */
	public static final String QUERY_FAIL = "查询结果为空";
	/**
	 * 身份证号码格式不正确
	 */
	public static final String IDCARD_ERROR = "身份证号码格式不正确";
	/**
	 * 没有输入身份证号码
	 */
	public static final String QUERY_NO_IDCARD = "请输入身份证号码";
	/**
	 * 请选择查询时间段
	 */
	public static final String QUERY_NO_TIME = "请选择查询时间段";
	/**
	 * 请输入正确的身份证号码
	 */
	public static final String QUERY__IDCARD_FALE = "请输入正确的身份证号码";
	/**
	 * 请输入正确的验证码
	 */
	public static final String QUERY_VERIFYCODE_FALE = "请输入正确的验证码";

	/**
	 * IP开始进行时间限制的key前缀
	 */
    public static final String IP_BEGIN_LIMIT_TIME = "IP_BEGIN_LIMIT_TIME";

    /**
     * 验证码
     */
    public static final String SESSION_NAME_EXAMREPORT_VERIFY_CODE = "SESSION_NAME_EXAMREPORT_VERIFY_CODE";

    public static final String CONTENT_TYPE = "text/xml; charset=utf-8";
}
