package org.springseed.oss.minio.controller;

import java.io.IOException;
import java.io.InputStream;
import java.time.ZonedDateTime;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springseed.core.util.SnowflakeUtil;
import org.springseed.oss.minio.bean.UserMetadata;
import org.springseed.oss.minio.service.PutOptService;
import org.springseed.oss.minio.util.OSSMinioException;
import org.springseed.oss.minio.util.OSSMinioInternalException;
import org.springseed.oss.minio.util.SecurityUtils;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/putservice")
public class PutServiceController {
    @Autowired
    private PutOptService putOptService;

    @PostMapping("/objects/{bucket}")
    @ResponseStatus(HttpStatus.CREATED)
    public String put(@PathVariable("bucket") String bucket, @RequestParam("file") MultipartFile file,
            @RequestParam(value = "objectId", required = false) String reqObjectId) {

        if (file.isEmpty()) {
            throw new OSSMinioException("不能是空文件");
        }
        
        final String fileName = StringUtils.cleanPath(file.getOriginalFilename());
        if (!StringUtils.hasText(fileName)) {
            throw new OSSMinioException("文件名是必须的");
        }

        final String objectId = reqObjectId == null ? SnowflakeUtil.INSTANCE.nextIdStr() : reqObjectId;
        final UserMetadata umd = UserMetadata.of()
                .objectId(objectId)
                .bucket(bucket)
                .conentType(file.getContentType())
                .fileName(fileName)
                .fileSize(file.getSize())
                .createdBy(SecurityUtils.getCurrentUserInfo())
                .createdOn(ZonedDateTime.now());

        try (InputStream fileData = file.getInputStream()) {
            putOptService.put(umd, fileData);
            return objectId;
        } catch (IOException e) {
            throw new OSSMinioInternalException(e);
        }
    }
}
