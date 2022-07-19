package core.util;

import lombok.extern.slf4j.Slf4j;

import java.util.concurrent.TimeUnit;

@Slf4j
public class TimeUtil {

    public static void sleep(long seconds, TimeUnit timeUnit) {
        if (seconds <= 0) { return; }

        try {
            timeUnit.sleep(seconds);
        } catch (Exception e) {
            log.warn("TimeUtil.sleepBySeconds.Exception", e);
        }
    }

}
