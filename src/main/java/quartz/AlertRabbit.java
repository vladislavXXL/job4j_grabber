package quartz;

import org.quartz.Job;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SchedulerException;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;

import java.io.InputStream;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class AlerRabbit.
 * @author v.ivanov
 * @version 1
 * @since 24.08.2020
 */
public class AlertRabbit {
    /**
     * Entry point.
     * @param args args
     */
    public static void main(String[] args) {
        try {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDetail job = newJob(Rabbit.class).build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(
                            getProps().getProperty("rabbit.interval"))
                    ).repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
        } catch (SchedulerException se) {
            se.printStackTrace();
        }
    }

    /**
     * Rabbit class to implement Job interface to run job.
     */
    public static class Rabbit implements Job {
        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            System.out.println("Rabbit runs here...");
        }
    }

    /**
     * Method to get properties to read parameters.
     * @return Properties instance
     */
    private static Properties getProps() {
        Properties props = new Properties();
        try (InputStream  in = AlertRabbit.class.getClassLoader().getResourceAsStream("rabbit.properties")) {
            props.load(in);
            return props;
        } catch (Exception e) {
            throw new IllegalStateException(e);
        }
    }
}
