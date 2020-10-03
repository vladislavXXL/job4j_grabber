package quartz;

import org.quartz.Job;
import org.quartz.JobDataMap;
import org.quartz.JobDetail;
import org.quartz.JobExecutionContext;
import org.quartz.JobExecutionException;
import org.quartz.Scheduler;
import org.quartz.SimpleScheduleBuilder;
import org.quartz.Trigger;
import org.quartz.impl.StdSchedulerFactory;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import propsloader.PropertyLoader;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.util.Properties;

import static org.quartz.JobBuilder.newJob;
import static org.quartz.SimpleScheduleBuilder.simpleSchedule;
import static org.quartz.TriggerBuilder.newTrigger;

/**
 * Class AlertRabbit.
 * @author v.ivanov
 * @version 1
 * @since 24.08.2020
 */
public class AlertRabbit {
    /** Field logger.*/
    private static final Logger LOG = LoggerFactory.getLogger(AlertRabbit.class.getName());

    /**
     * Entry point.
     * @param args args
     */
    public static void main(String[] args) {
        Properties props = PropertyLoader.getProps("rabbit.properties");
        try (
            Connection conn = DriverManager.getConnection(
                    props.getProperty("db.url"),
                    props.getProperty("db.user"),
                    props.getProperty("db.password"))
        ) {
            Scheduler scheduler = StdSchedulerFactory.getDefaultScheduler();
            scheduler.start();
            JobDataMap data = new JobDataMap();
            data.put("db", conn);
            JobDetail job = newJob(Rabbit.class)
                    .usingJobData(data)
                    .build();
            SimpleScheduleBuilder times = simpleSchedule()
                    .withIntervalInSeconds(Integer.parseInt(
                            props.getProperty("rabbit.interval"))
                    ).repeatForever();
            Trigger trigger = newTrigger()
                    .startNow()
                    .withSchedule(times)
                    .build();
            scheduler.scheduleJob(job, trigger);
            Thread.sleep(20000);
            scheduler.shutdown();
        } catch (Exception e) {
            LOG.error(e.getMessage(), e);
        }
    }

    /**
     * Rabbit class to implement Job interface to run job.
     */
    public static class Rabbit implements Job {
        /**
         * Class Rabbit constructor.
         */
        public Rabbit() {

        }

        @Override
        public void execute(JobExecutionContext context) throws JobExecutionException {
            LOG.info("Job execution started");
            Connection conn = (Connection) context.getJobDetail().getJobDataMap().get("db");
            try (PreparedStatement pst = conn.prepareStatement("insert into rabbit(created) values(?)")) {
                pst.setTimestamp(1, new Timestamp(System.currentTimeMillis()));
                pst.executeUpdate();
            } catch (SQLException e) {
                LOG.error(e.getMessage(), e);
            }
        }
    }
}
