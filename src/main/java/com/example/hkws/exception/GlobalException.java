package com.example.hkws.exception;

import com.example.hkws.enumeration.ResultEnum;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.util.Map;
import java.util.Objects;

/**
 * 应用异常
 * @创建日期 : 2018年10月24日
 * @作者 : GS_MASTER
 */
@Data
@EqualsAndHashCode(callSuper = true)
public class GlobalException extends Exception {
    
	private static final long serialVersionUID = -766379244857861681L;
	
	private Integer code;
	
	private String msg;

    public GlobalException(ResultEnum errCode) {
        this.code = errCode.getCode();
        this.msg = errCode.getMsg();
    }

	public GlobalException(Integer code, String msg) {
		this.code = code;
		this.msg = msg;
	}

	public GlobalException(Integer code, Map<String, Object> errorMaps) {
		this.code = code == null ? 500 : code;
		this.msg = "";
		StringBuilder msg1 = new StringBuilder();
		if (Objects.nonNull(errorMaps) && !errorMaps.isEmpty()) {
			for (Map.Entry<String, Object> entry : errorMaps.entrySet()) {
				msg1.append((String)entry.getValue()).append(",");
			}
			if (msg1.length() > 0) {
				msg = msg1.substring(0, msg1.length() - 1);
			}else {
				msg = "参数校验失败";
			}
		}
	}

}
