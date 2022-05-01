package org.springseed.oss.minio.service;

import java.util.List;
import java.util.function.Consumer;
import java.util.stream.Collectors;
import java.util.stream.StreamSupport;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springseed.oss.minio.util.MinioArgsUtils;
import org.springseed.oss.minio.util.MinioExceptionUtil;

import io.minio.ListObjectsArgs;
import io.minio.MinioClient;
import io.minio.Result;
import io.minio.messages.Item;
import lombok.extern.slf4j.Slf4j;

/**
 * list操作
 * 
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
@Service
public class ListOptsService {
    @Autowired
    private MinioClient client;

    /**
     * 所有对象信息。
     * 
     * @param bucket
     * @return
     */
    public List<Item> listItems(final String bucket, final Consumer<ListObjectsArgs.Builder> consumer) {
        final ListObjectsArgs.Builder builder = ListObjectsArgs.builder()
                .bucket(bucket)
                .recursive(false);

        if (consumer != null) {
            consumer.accept(builder);
        }

        final ListObjectsArgs args = builder.build();

        if (log.isDebugEnabled()) {
            log.debug("ListObjectsArgs={}", MinioArgsUtils.printListObjectsArgs(args));
        }

        return convert(client.listObjects(args));
    }

    private List<Item> convert(Iterable<Result<Item>> items) {
        return StreamSupport
                .stream(items.spliterator(), true)
                .map(itemResult -> {
                    try {
                        return itemResult.get();
                    } catch (Exception e) {
                        throw MinioExceptionUtil.wapper(e);
                    }
                })
                .collect(Collectors.toList());
    }

}
