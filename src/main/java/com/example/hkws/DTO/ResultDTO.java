package com.example.hkws.DTO;

import com.example.hkws.enumeration.ResultEnum;
import com.example.hkws.enumeration.ShiroErrorEnum;
import io.swagger.annotations.ApiModel;
import io.swagger.annotations.ApiModelProperty;
import lombok.AllArgsConstructor;
import lombok.Data;
//import org.springframework.data.domain.Page;

import java.util.Objects;

/**
 * API接口返回数据包装类
 *
 */
@Data
@AllArgsConstructor
@ApiModel(value = "返回实体", description = "用于封装返回结果")
public class ResultDTO<T> {

	// 错误码
	@ApiModelProperty(value = "状态码")
	private Integer code;

	// 错误信息
	@ApiModelProperty(value = "状态信息")
	private String msg;

	// 返回数据
	@ApiModelProperty(value = "响应数据")
	private T data;

	// 页码
	@ApiModelProperty(value = "【分页】记录总页数")
	private Integer totalPage;

	// 分页大小
	@ApiModelProperty(value = "【分页】记录总条数")
	private Long totalSize;

	public ResultDTO() {
		super();
	}

	public ResultDTO(ResultEnum errCode) {
		this.code = errCode.getCode();
		this.msg = errCode.getMsg();
	}

	public static ResultDTO of(ResultEnum code) {
		ResultDTO result = new ResultDTO();
		result.code = code.getCode();
		result.msg = code.getMsg();
		return result;
	}

	public static ResultDTO of(ShiroErrorEnum code) {
		ResultDTO result = new ResultDTO();
		result.code = Integer.valueOf(code.getErrorCode());
		result.msg = code.getErrorMsg();
		return result;
	}

	public static ResultDTO of(ResultEnum code, Object data) {
		ResultDTO result = new ResultDTO();
		result.code = code.getCode();
		result.msg = code.getMsg();
		result.data = data;
		return result;
	}

	public static ResultDTO of(Integer code, String msg) {
		ResultDTO result = new ResultDTO();
		result.code = code;
		result.msg = msg;
		return result;
	}



	@SuppressWarnings("unchecked")
	public ResultDTO setData(Object data) {
		if (Objects.nonNull(data)) {
			this.data = (T) data;
		}
		return this;
	}

//	public ResultDTO setData(JSONObject jsonObject) {
//		List<T> list = JSONObject.parseArray(jsonObject.getString("data"),T);
//		Long totalSize = jsonObject.getLong("totalSize");
//		if (Objects.nonNull(data)) {
//			this.data = (T)list;
//			this.totalSize = totalSize;
//		}
//		return this;
//	}

}
