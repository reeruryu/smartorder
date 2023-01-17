package com.example.smartorder.common.component;

import com.example.smartorder.common.configuration.JobConfig;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.batch.core.JobExecution;
import org.springframework.batch.core.JobParameter;
import org.springframework.batch.core.JobParameters;
import org.springframework.batch.core.JobParametersInvalidException;
import org.springframework.batch.core.launch.JobLauncher;
import org.springframework.batch.core.repository.JobExecutionAlreadyRunningException;
import org.springframework.batch.core.repository.JobInstanceAlreadyCompleteException;
import org.springframework.batch.core.repository.JobRestartException;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Slf4j
@RequiredArgsConstructor
@Component
public class JobScheduler {

	private final JobLauncher jobLauncher;
	private final JobConfig jobConfig;

	@Scheduled(cron = "0 30 * * * *")
	public void runJob() {
		Map<String, JobParameter> map = new HashMap<>();

		String time = getDateFormatString();
		map.put("time", new JobParameter(time));
		JobParameters jobParameters = new JobParameters(map);

		try {
			JobExecution jobExecution = jobLauncher.run(
				jobConfig.soldOutForOneDayChangeJob(), jobParameters);

			log.info("Job Execution: " + jobExecution.getStatus());
			log.info("Job getJobConfigurationName: " + jobExecution.getJobConfigurationName());
			log.info("Job getJobId: " + jobExecution.getJobId());
			log.info("Job getExitStatus: " + jobExecution.getExitStatus());
			log.info("Job getJobInstance: " + jobExecution.getJobInstance());
			log.info("Job getStepExecutions: " + jobExecution.getStepExecutions());
			log.info("Job getLastUpdated: " + jobExecution.getLastUpdated());
			log.info("Job getFailureExceptions: " + jobExecution.getFailureExceptions());

		} catch (JobExecutionAlreadyRunningException |
				 JobInstanceAlreadyCompleteException |
				 JobParametersInvalidException | JobRestartException e) {

			log.error(e.getMessage());
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	private static String getDateFormatString() {
		SimpleDateFormat format = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss:SSS");

		return format.format(new Date());
	}

}