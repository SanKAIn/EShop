package com.kon.EShop;

import com.kon.EShop.util.ValidationUtil;
import com.kon.EShop.util.exception.ApplicationException;
import com.kon.EShop.util.exception.ErrorInfo;
import com.kon.EShop.util.exception.ErrorType;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.support.MessageSourceAccessor;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.HttpRequestMethodNotSupportedException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.multipart.MultipartException;
import org.springframework.web.servlet.ModelAndView;
import org.springframework.web.servlet.NoHandlerFoundException;

import javax.servlet.http.HttpServletRequest;
import java.util.Map;

@ControllerAdvice
public class GlobalExceptionHandler {
    private static final Logger log = LoggerFactory.getLogger(GlobalExceptionHandler.class);

    private final MessageSourceAccessor messageSourceAccessor;

    public GlobalExceptionHandler(MessageSourceAccessor messageSourceAccessor) {
        this.messageSourceAccessor = messageSourceAccessor;
    }

    @ExceptionHandler(MultipartException.class)
    public ModelAndView handleControllerException(HttpServletRequest request, Throwable ex) {
        return logAndGetExceptionView(
                request,
                (Exception) ex,
                false,
                ErrorType.WRONG_REQUEST,
                "Attachment size exceeds the allowable limit! (10MB)");
    }

    @ExceptionHandler(NoHandlerFoundException.class)
    public ModelAndView wrongRequest(HttpServletRequest req, NoHandlerFoundException e) {
        return logAndGetExceptionView(
                req,
                e,
                false,
                ErrorType.WRONG_REQUEST,
                null);
    }

    @ExceptionHandler(ApplicationException.class)
    public ModelAndView applicationErrorHandler(HttpServletRequest req, ApplicationException appEx) {
        return logAndGetExceptionView(
                req,
                appEx,
                true,
                appEx.getType(),
                messageSourceAccessor.getMessage(appEx.getMsgCode(), appEx.getArgs()));
    }

    @ResponseStatus(HttpStatus.UNAUTHORIZED)
    @ExceptionHandler(HttpRequestMethodNotSupportedException.class)
    public ErrorInfo authority(HttpServletRequest req, HttpRequestMethodNotSupportedException e) {
        return new ErrorInfo(
                req.getRequestURL(),
                ErrorType.APP_ERROR,
                e.getMessage(), e.getMessage());
    }

    @ExceptionHandler(Exception.class)
    public ModelAndView defaultErrorHandler(HttpServletRequest req, Exception e) {
        log.error("Exception at request " + req.getRequestURL(), e);
        return logAndGetExceptionView(
                req,
                e,
                true,
                ErrorType.APP_ERROR,
                null);
    }

    private ModelAndView logAndGetExceptionView(HttpServletRequest req, Exception e, boolean logException, ErrorType errorType, String msg) {
        Throwable rootCause = ValidationUtil.logAndGetRootCause(req, e, logException, errorType);

        HttpStatus httpStatus = errorType.getStatus();
        ModelAndView mav = new ModelAndView("exception",
                Map.of( "exception", rootCause,
                        "message", msg != null ? msg : ValidationUtil.getMessage(rootCause),
                        "typeMessage", messageSourceAccessor.getMessage(errorType.getErrorCode()),
                        "status", httpStatus));
        mav.setStatus(httpStatus);
        return mav;
    }
}
