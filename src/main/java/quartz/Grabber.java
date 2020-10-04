package quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import propsloader.PropertyLoader;
import storage.PsqlStore;
import storage.Store;

import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class Grabber.
 * @author v.ivanov
 * @version 1
 * @since 04.10.2020
 */
public class Grabber implements Grab {
    /** Field to load in properties file.*/
    static final Properties CFG = PropertyLoader.getProps("app.properties");;

    /**
     /** Method returns Store instance.
     * @return Sheduler instance
     */
    public Store store() {
        return new PsqlStore("db.properties");
    }

    /**
     * Method returns Scheduler instance.
     * @return Sheduler instance
     * @throws SchedulerException can be thrown
     */
    public Scheduler scheduler() throws SchedulerException {
        Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
        scheduler.start();
        return scheduler;
    }

    /**
     * Method init.
     * @param parse - Parse instance
     * @param store - Store instance
     * @param scheduler - Scheduler instance
     * @throws SchedulerException can be thrown
     */
    @Override
    public void init(Parse parse, Store store, Scheduler scheduler) throws SchedulerException {
        JobDataMap data = new JobDataMap();
        data.put("store", store);
        data.put("parse", parse);
        JobDetail job = newJob(GrabJob.class)
                .usingJobData(data)
                .build();
        SimpleScheduleBuilder times = SimpleScheduleBuilder.simpleSchedule()
                .withIntervalInSeconds(Integer.parseInt(CFG.getProperty("time")))
                .repeatForever();
        Trigger trigger = newTrigger()
                .startNow()
                .withSchedule(times)
                .build();
        scheduler.scheduleJob(job, trigger);
    }

    /**
     * GrabJob class to implement Job interface execute method to run job.
     */
    public static class GrabJob implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            JobDataMap map = context.getJobDetail().getJobDataMap();
            Store store = (Store) map.get("store");
            Parse parse = (Parse) map.get("parse");
            parse.list(CFG.getProperty("parse.link")).forEach(store::save);
        }
    }

    /**
     *
     * Entry point.
     * @param args args
     * @throws Exception can be thrown
     */
    public static void main(String[] args) throws Exception {
        Grabber grab = new Grabber();
        Store store = grab.store();
        Scheduler scheduler = grab.scheduler();
        grab.init(new SqlRuParse(), store, scheduler);
    }
}
