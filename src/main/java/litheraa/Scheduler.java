package litheraa;

import litheraa.controller.ProdTimerController;
import litheraa.controller.SettingsController;
import litheraa.settings.DBSettings;
import org.quartz.*;
import org.quartz.impl.StdSchedulerFactory;

public class Scheduler implements Job {

	private static ProdTimerController controller;

	public static void setController(ProdTimerController controller) {
		Scheduler.controller = controller;
	}

	@Override
	public void execute(JobExecutionContext jobExecutionContext) {
		controller.saveData();
	}

	public static void saveData() {
		DBSettings settings = controller.getDbSettings();
		String[] time = SettingsController.getDeadLineTime().split(":");

		JobDetail dayJob = JobBuilder.newJob(Scheduler.class).withIdentity("myJob", "group1").build();
		JobDetail routineJob = JobBuilder.newJob(Scheduler.class).withIdentity("Job", "group1").build();

		CronTrigger everyDayTrigger = TriggerBuilder.newTrigger().withIdentity("dayTrigger", "group1")
				.withSchedule(CronScheduleBuilder.dailyAtHourAndMinute(Integer.parseInt(time[0]), Integer.parseInt(time[1]))
						.withMisfireHandlingInstructionIgnoreMisfires())
				.build();
		Trigger routineTrigger = TriggerBuilder.newTrigger().withIdentity("routineTrigger", "group1")
				.startNow()
						.withSchedule(SimpleScheduleBuilder.repeatMinutelyForever(settings.getUpdateInterval()))
				.build();
		SchedulerFactory factory = new StdSchedulerFactory();

		try {
			org.quartz.Scheduler scheduler1 = factory.getScheduler();
			scheduler1.start();
			scheduler1.scheduleJob(dayJob, routineTrigger);
			scheduler1.scheduleJob(routineJob, everyDayTrigger);

//			org.quartz.Scheduler scheduler2 = factory.getScheduler();
//			scheduler2.start();
		} catch (SchedulerException e) {
			throw new RuntimeException(e);
		}
	}
}
