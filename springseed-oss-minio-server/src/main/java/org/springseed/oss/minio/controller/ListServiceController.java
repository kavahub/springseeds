package org.springseed.oss.minio.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springseed.oss.minio.service.ListOptsService;

import io.minio.messages.Item;

/**
 * TODO
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@RestController
@RequestMapping("/v1/listservice")
public class ListServiceController {
    @Autowired
    private ListOptsService listOptsService;

    @GetMapping("/objects/item/{bucket}")
    @ResponseStatus(HttpStatus.OK)
    public List<Item> listItems(@PathVariable("bucket") String bucket,
            @RequestParam(value = "prefix", required = false) String prefix, 
            @RequestParam(value = "includeUserMetadata", required = false) Boolean includeUserMetadata) {
        return listOptsService.listItems(bucket, builder -> {
            if (StringUtils.hasText(prefix)) {
                builder.prefix(prefix);
            }
            
            if (includeUserMetadata != null) {
                builder.includeUserMetadata(includeUserMetadata);
            }
        });
    }

}
