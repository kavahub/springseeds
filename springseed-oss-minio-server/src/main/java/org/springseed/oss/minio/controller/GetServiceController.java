package org.springseed.oss.minio.controller;

import java.io.IOException;
import java.io.InputStream;
import java.util.List;
import java.util.Set;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

import javax.servlet.http.HttpServletResponse;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.FileCopyUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springseed.oss.minio.bean.MinioUserMetadata;
import org.springseed.oss.minio.service.GetOptService;
import org.springseed.oss.minio.service.StatOptService;
import org.springseed.oss.minio.util.FileNameCounter;
import org.springseed.oss.minio.util.OSSMinioInternalException;

import io.minio.http.Method;

/**
 * get接口
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/getservice")
public class GetServiceController {
    @Autowired
    private GetOptService getOptService;
    @Autowired
    private StatOptService statOptService;

    @GetMapping("/objects/data/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public void getObject(@PathVariable("bucket") String bucket, @PathVariable("objectId") String objectId,
            final HttpServletResponse response) {

        // 元数据
        final MinioUserMetadata userMetadata = statOptService.getUserMetadata(bucket, objectId);
        // 文件数据
        final InputStream inputStream = getOptService.getObject(bucket, objectId);

        // 设置响应
        response.setContentType(userMetadata.getContentType());
        response.setHeader("Content-Disposition",
                String.format("inline; filename=\"" + userMetadata.getFileName() + "\""));
        try {
            FileCopyUtils.copy(inputStream, response.getOutputStream());
        } catch (IOException e) {
            throw new OSSMinioInternalException("下载文件失败", e);
        }
    }

    @GetMapping("/objects/data-zip/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public void getObjects(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds,
            final HttpServletResponse response) {
        final FileNameCounter fileNameCounter = new FileNameCounter();
        // 元数据
        final List<MinioUserMetadata> userMetadatas = statOptService.getUserMetadata(bucket, objectIds);

        try (ZipOutputStream zipOut = new ZipOutputStream(response.getOutputStream())) {

            for (final MinioUserMetadata userMetadata : userMetadatas) {
                // 读取文件
                final InputStream fileData = getOptService.getObject(bucket, userMetadata.getObjectId());
                // 避免重复的文件名
                final ZipEntry zipEntry = new ZipEntry(fileNameCounter.convert(userMetadata.getFileName()));
                zipOut.putNextEntry(zipEntry);
                FileCopyUtils.copy(fileData, zipOut);
                zipOut.closeEntry();
            }
            zipOut.finish();
        } catch (IOException e) {
            throw new OSSMinioInternalException("批量下载文件失败", e);
        }
    }

    @GetMapping("/objects/presigned-url/{bucket}/{objectId}/{method}")
    @ResponseStatus(HttpStatus.OK)
    public String getPresignedObjectUrl(@PathVariable("bucket") String bucket, @PathVariable("objectId") String objectId, @PathVariable("method") Method method) {
        return getOptService.getPresignedObjectUrl(bucket, objectId, method);
    }
}
