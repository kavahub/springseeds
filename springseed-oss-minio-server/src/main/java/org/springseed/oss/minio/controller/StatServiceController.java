package org.springseed.oss.minio.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springseed.oss.minio.service.StatOptService;

import io.minio.StatObjectResponse;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/statservice")
public class StatServiceController {
    @Autowired
    private StatOptService statOptService;

    @GetMapping("/objects/stat/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public StatObjectResponse getStatObjectResponse(@PathVariable("bucket") String bucket,
            @PathVariable("objectId") String objectId) {
        return statOptService.getStatObjectResponse(bucket, objectId);
    }

    @GetMapping("/objects/stat/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, StatObjectResponse> getStatObjectResponse(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds) {
        return statOptService.getStatObjectResponse(bucket, objectIds);
    }

    @GetMapping("/objects/usermetadata/{bucket}/{objectId}")
    @ResponseStatus(HttpStatus.OK)
    public Map<String, String> getUserMetadata(@PathVariable("bucket") String bucket,
            @PathVariable("objectId") String objectId) {
        return statOptService.getStatObjectResponse(bucket, objectId).userMetadata();
    }

    @GetMapping("/objects/usermetadata/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public List<Map<String, String>> getUserMetadata(@PathVariable("bucket") String bucket,
            @RequestParam(value = "objectIds") Set<String> objectIds) {
        final List<Map<String, String>> rslt = new ArrayList<>();

        statOptService.getStatObjectResponse(bucket, objectIds)
                .values()
                .stream()
                .forEach(data -> rslt.add(data.userMetadata()));
        return rslt;
    }
}
