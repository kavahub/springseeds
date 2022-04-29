package org.springseed.oss.minio.controller;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springseed.core.util.SnowflakeUtil;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/commonservice")
public class CommonServiceController {
    @GetMapping("/generate-object-id")
    @ResponseStatus(HttpStatus.OK)
    public String generateObjectId() {
        return SnowflakeUtil.INSTANCE.nextIdStr();
    }
}
