package org.springseed.oauth2.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.MessageSource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindException;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;
import org.springseed.oauth2.operator.EmailValidateExcepiton;
import org.springseed.oauth2.operator.OperatorNotFoundExcepiton;
import org.springseed.oauth2.operator.PhoneNumberValidateExcepiton;
import org.springseed.oauth2.util.CommonRuntimeException;
import org.springseed.oauth2.util.ErrorResponse;


/**
 * 
 * 请求异常处理器
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@ControllerAdvice
public class RestResponseEntityExceptionHandler extends ResponseEntityExceptionHandler {
    @Autowired
    private MessageSource messages;

    @Override
    protected ResponseEntity<Object> handleBindException(final BindException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("400 Status Code ", ex);
        }
        final BindingResult result = ex.getBindingResult();
        final ErrorResponse bodyOfResponse = new ErrorResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @Override
    protected ResponseEntity<Object> handleMethodArgumentNotValid(final MethodArgumentNotValidException ex, final HttpHeaders headers, final HttpStatus status, final WebRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("400 Status Code ", ex);
        }
        final BindingResult result = ex.getBindingResult();
        final ErrorResponse bodyOfResponse = new ErrorResponse(result.getAllErrors(), "Invalid" + result.getObjectName());
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ PhoneNumberValidateExcepiton.class })
    public ResponseEntity<Object> handlePhoneNumberValidateExcepiton(RuntimeException ex, WebRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("PhoneNumberValidateExcepiton " + ex.getMessage());
        }
        final ErrorResponse bodyOfResponse = new ErrorResponse(
          messages.getMessage("message.phoneNumberValidate", new String[] { ex.getMessage() }, request.getLocale()), "PhoneNumberValidateExcepiton");
        
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ EmailValidateExcepiton.class })
    public ResponseEntity<Object> handleEmailValidateExcepiton(RuntimeException ex, WebRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("EmailValidateExcepiton " + ex.getMessage());
        }
        final ErrorResponse bodyOfResponse = new ErrorResponse(
          messages.getMessage("message.emailValidate", new String[] { ex.getMessage() }, request.getLocale()), "EmailValidateExcepiton");
        
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ OperatorNotFoundExcepiton.class })
    public ResponseEntity<Object> handleOperatorNotFoundExcepiton(RuntimeException ex, WebRequest request) {
        if (logger.isDebugEnabled()) {
            logger.debug("OperatorNotFoundExcepiton " + ex.getMessage());
        }
        final ErrorResponse bodyOfResponse = new ErrorResponse(
          messages.getMessage("message.operatorNotFound", new String[] { ex.getMessage() }, request.getLocale()), "OperatorNotFoundExcepiton");
        
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.NOT_FOUND, request);
    }

    @ExceptionHandler({ CommonRuntimeException.class })
    public ResponseEntity<Object> handleCommonRuntimeException(RuntimeException ex, WebRequest request) {
        logger.error(ex);
        final ErrorResponse bodyOfResponse = new ErrorResponse(
          messages.getMessage("message.common", new String[] { ex.getMessage() }, request.getLocale()), "CommonRuntimeException");
        
        return handleExceptionInternal(ex, bodyOfResponse, new HttpHeaders(), HttpStatus.BAD_REQUEST, request);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        logger.error(ex);
        final ErrorResponse bodyOfResponse = new ErrorResponse(messages.getMessage("message.error", new String[] { ex.getMessage() }, request.getLocale()), "InternalError");
        return new ResponseEntity<>(bodyOfResponse, new HttpHeaders(), HttpStatus.INTERNAL_SERVER_ERROR);
    }
}
