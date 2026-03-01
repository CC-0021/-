package com.dorm.manage.exception;

import com.dorm.manage.common.ResultCode;
import lombok.Getter;

/**
 * 业务异常
 *
 * @author dorm-manage
 */
@Getter
public class BusinessException extends RuntimeException {

    private final int code;

    public BusinessException(String message) {
        super(message);
        this.code = ResultCode.BAD_REQUEST;
    }

    public BusinessException(int code, String message) {
        super(message);
        this.code = code;
    }
}
