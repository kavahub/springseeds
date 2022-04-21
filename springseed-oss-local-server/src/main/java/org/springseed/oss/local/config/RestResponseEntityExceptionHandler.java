package org.springseed.oss.local.config;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springseed.core.util.ErrorResponse;
import org.springseed.oss.local.util.FileNotFoundException;
import org.springseed.oss.metadata.MetadataNotFoundException;
import org.springseed.oss.util.OSSRuntimeException;

import cn.hutool.core.exceptions.ExceptionUtil;
import lombok.extern.slf4j.Slf4j;


/**
 * 
 * 请求异常处理器
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
//@ControllerAdvice
public class RestResponseEntityExceptionHandler {
    @ExceptionHandler({ MetadataNotFoundException.class, FileNotFoundException.class })
    public ResponseEntity<ErrorResponse> handleMetadataNotFoundException(RuntimeException ex) {
        if (log.isDebugEnabled()) {
            log.debug("{} - {}", ex.getClass().getName(), ex.getMessage());
        }
        return ResponseEntity.notFound().build();
    }

    @ExceptionHandler({ OSSRuntimeException.class })
    public ResponseEntity<ErrorResponse> handleOSSRuntimeException(RuntimeException ex) {
        if (log.isDebugEnabled()) {
            log.debug("{} - {}", ex.getClass().getName(), ex.getMessage());
        }
        final ErrorResponse bodyOfResponse = new ErrorResponse(ex.getMessage(), ex.getClass().getName(), ExceptionUtil.stacktraceToString(ex));
        
        return ResponseEntity.badRequest().body(bodyOfResponse);
    }

    @ExceptionHandler({ Exception.class })
    public ResponseEntity<Object> handleInternal(final RuntimeException ex, final WebRequest request) {
        log.error("服务异常", ex);
        final ErrorResponse bodyOfResponse = new ErrorResponse(ex.getMessage(), Exception.class.getName(), ExceptionUtil.stacktraceToString(ex));
         return ResponseEntity.internalServerError().body(bodyOfResponse);
    }
}
