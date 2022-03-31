package org.springseed.core.util;

import cn.hutool.core.lang.Assert;
import cn.hutool.core.lang.Snowflake;
import cn.hutool.core.util.IdUtil;
import cn.hutool.core.util.StrUtil;
import lombok.Getter;
import lombok.extern.slf4j.Slf4j;

/**
 * 
 * ID生成工具。
 * 
 * <p>
 * 设置环境变量：
 * <ul>
 * <li>SBS_WORKER_ID：必须，范围1~32 </li>
 * <li>SBS_DATACENTER_ID：可选的，范围1~32 </li>
 * </ul>
 *
 * @author PinWei Wan
 * @since 1.0.0
 */
@Slf4j
public class SnowflakeUtil {
    public final static String PRO_WORKER_ID = "SBS_WORKER_ID";
    public final static String PRO_DATACENTER_ID = "SBS_DATACENTER_ID";
    public final static SnowflakeUtil INSTANCE = new SnowflakeUtil();

    private final Snowflake snowflake;

    @Getter
    private long workerId = 1;
    @Getter
    private long datacenterId = 1;

    private SnowflakeUtil() {
        final String workerIdStr = System.getProperty(PRO_WORKER_ID);
        final String datacenterIdStr = System.getProperty(PRO_DATACENTER_ID);
        if (StrUtil.isNotBlank(workerIdStr)) {
            this.workerId = Long.valueOf(workerIdStr);
            
            if (StrUtil.isNotBlank(datacenterIdStr)) {
                this.datacenterId = Long.valueOf(workerIdStr);
            }
        }

        snowflake = IdUtil.getSnowflake(this.workerId, this.datacenterId);
        log.info("SnowflakeUtil initialized [workerId={}, datacenterId={}]", this.workerId, this.datacenterId);
    }

    public long nextId() {
        return snowflake.nextId();
    }
    
    public String nextIdStr() {
        return snowflake.nextIdStr();
    }

    public long[] nextId(final int size) {
        Assert.isTrue(size > 0, "The value must be greater than zero");

        final long[] ids = new long[size];
        for(int i = 0; i < size; i++) {
            ids[i] = snowflake.nextId();
        }
        return ids;
    }
    
    public String[] nextIdStr(final int size) {
        Assert.isTrue(size > 0, "The value must be greater than zero");

        final String[] ids = new String[size];
        for(int i = 0; i < size; i++) {
            ids[i] = snowflake.nextIdStr();
        }

        return ids;
    }
}
