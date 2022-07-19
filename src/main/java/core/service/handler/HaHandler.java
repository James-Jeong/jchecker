package core.service.handler;

import core.scheduler.job.Job;
import core.scheduler.job.JobContainer;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class HaHandler extends JobContainer {

    private static final Logger logger = LoggerFactory.getLogger(HaHandler.class);

    public HaHandler(Job haHandleJob) {
        setJob(haHandleJob);
    }

    public void start () {
        getJob().setRunnable(() -> {
            logger.debug("| thread=[{}]",
                    Thread.activeCount()
            );
        });
    }

}
