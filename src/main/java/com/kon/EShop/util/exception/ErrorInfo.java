package com.kon.EShop.util.exception;

import lombok.Getter;
import lombok.Setter;

@Getter @Setter
public class ErrorInfo {
    private String url;
    private ErrorType type;
    private String typeMessage;
    private String[] details;

    public ErrorInfo() {
    }

    public ErrorInfo(CharSequence url, ErrorType type, String typeMessage, String... details) {
        this.url = url.toString();
        this.type = type;
        this.typeMessage = typeMessage;
        this.details = details;
    }
}