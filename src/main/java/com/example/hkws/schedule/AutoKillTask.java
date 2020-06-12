package com.example.hkws.schedule;

import com.example.hkws.controller.web;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;


import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneOffset;
import java.util.*;

/**
 *
 * @author
 * @Date
 */
@Slf4j
@Component
@EnableScheduling
public class AutoKillTask {

	public static HashSet<String> taskIdSet =new HashSet<>();

	@Scheduled(initialDelay = 1000 * 1, fixedRate = 1000 * 10)
//	@Scheduled(cron = "0 0 0 * * ?")
	public void autoKillTask() {

		Iterator ite  = taskIdSet.iterator();
		while(ite.hasNext()){
			String taskId = ite.next().toString();
			log.info("定时执行删除task任务"+taskId);
			Boolean isStop = web.manager.stop(taskId);
			ite.remove();

		}
	}


}
