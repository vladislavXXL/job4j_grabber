package quartz;

import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import storage.Store;

/**
 * Interface Grab.
 * @author v.ivanov
 * @version 1
 * @since 28.09.2020
 */
public interface Grab {

    /**
     * Method init.
     * @param parse - Parse instance
     * @param store - Store instance
     * @param scheduler - Scheduler instance
     * @throws SchedulerException can be thrown
     */
    void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException;
}
