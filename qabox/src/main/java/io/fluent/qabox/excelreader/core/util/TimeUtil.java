package io.fluentqa.qaplugins.excelreader.core.util;

import com.intellij.openapi.diagnostic.Logger;


public class TimeUtil {

    private static final Logger LOG = Logger.getInstance(TimeUtil.class);
    private final static TimeUtil INSTANCE = new TimeUtil();
    private long start;
    private long end;
    private TimeUtil() {

    }

    public TimeUtil start(long start) {
        this.start = start;
        return this;
    }

    public TimeUtil end(long end) {
        this.end = end;
        return this;
    }

    public static TimeUtil getInstance() {
        return INSTANCE;
    }

    public void time(String msg) {
        if (start == 0 || end == 0) {
            return;
        }
        if (start > end) {
            return;
        }
        LOG.info(msg + ", cost: " + (end-start) + "ms");
    }
}
