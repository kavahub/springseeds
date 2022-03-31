package org.springseed.core.util;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;

public class SnowflakeUtilTests {
    static {
        System.setProperty(SnowflakeUtil.PRO_WORKER_ID, "2");
        System.setProperty(SnowflakeUtil.PRO_DATACENTER_ID, "2");
    }

    @Test
    public void thenInitOk() {
        SnowflakeUtil util = SnowflakeUtil.INSTANCE;
        assertThat(util.getWorkerId()).isEqualTo(2);
        assertThat(util.getDatacenterId()).isEqualTo(2);
    }

    @Test
    public void whenNextId_thenOk() {
        assertThat(SnowflakeUtil.INSTANCE.nextId()).isNotNegative();
    }

    @Test
    public void whenNextIdStr_thenOk() {
        assertThat(SnowflakeUtil.INSTANCE.nextIdStr()).isNotBlank();
    }

    @Test
    public void whenNextIds_thenOk() {
        final int size = 100;
        final long[] ids = SnowflakeUtil.INSTANCE.nextId(size);

        assertThat(ids.length).isEqualTo(size);
        for(int i = 0; i < size; i++) {
            assertThat(ids[i]).isNotNegative();
            System.out.println(ids[i]);
        }
    }

    @Test
    public void whenNextIdsStr_thenOk() {
        final int size = 100;
        final String[] ids = SnowflakeUtil.INSTANCE.nextIdStr(size);

        assertThat(ids.length).isEqualTo(size);
        for(int i = 0; i < size; i++) {
            assertThat(ids[i]).isNotBlank();
            System.out.println(ids[i]);
        }
    }

}
