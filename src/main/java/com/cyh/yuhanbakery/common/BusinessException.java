package com.cyh.yuhanbakery.common;

import lombok.Getter;

@Getter
public class BusinessException extends RuntimeException {
    private ResultCode resultCode = ResultCode.BUSINESS_ERROR;

    public BusinessException(String message) {
        super(message);
    }

    public BusinessException(ResultCode resultCode) {
        super(resultCode.getMessage());
        this.resultCode = resultCode;
    }

    public BusinessException(ResultCode resultCode, String message) {
        super(message);
        this.resultCode = resultCode;
    }
}
