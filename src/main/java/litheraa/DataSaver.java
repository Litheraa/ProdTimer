package litheraa;

import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class DataSaver implements Job {

	private static ProdTimerController controller;

	public static void setController(ProdTimerController controller) {
		DataSaver.controller = controller;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		controller.saveData();
	}

	public static void saveData() {
		String[] time = SettingsController.getDeadLineTime().split(":");
		int updateInterval = SettingsController.getUpdateInterval();

		JobDetail dayJob = JobBuilder.newJob(DataSaver.class).withIdentity("myJob", "group1").build();
		JobDetail routineJob = JobBuilder.newJob(DataSaver.class).withIdentity("Job", "group1").build();

		CronTrigger everyDayTrigger = TriggerBuilder.newTrigger().withIdentity("dayTrigger", "group1")
				.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(Integer.parseInt(time[0]), Integer.parseInt(time[1]))
						.withMisfireHandlingInstructionIgnoreMisfires())
				.build();
		Trigger routineTrigger = TriggerBuilder.newTrigger().withIdentity("routineTrigger", "group1")
				.startNow()
						.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(updateInterval))
				.build();
		SchedulerFactory factory = new StdSchedulerFactory();
		try {
			Scheduler scheduler = factory.getScheduler();
			scheduler.start();
			scheduler.scheduleJob(dayJob, routineTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
		try {
			Scheduler scheduler = factory.getScheduler();
			scheduler.start();
			scheduler.scheduleJob(routineJob, everyDayTrigger);
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
