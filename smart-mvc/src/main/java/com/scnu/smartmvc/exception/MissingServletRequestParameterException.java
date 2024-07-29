package com.scnu.smartmvc.exception;

import javax.servlet.ServletException;

public class MissingServletRequestParameterException extends ServletException {
    private String parameterName;
    private String parameterType;

    public MissingServletRequestParameterException(String parameterName, String parameterType) {
        super("Required " + parameterType + " parameter '" + parameterName + "' is not present");
        this.parameterName = parameterName;
        this.parameterType = parameterType;
    }

    public String getParameterName() {
        return parameterName;
    }

    public String getParameterTypeName() {
        return parameterType;
    }
}
