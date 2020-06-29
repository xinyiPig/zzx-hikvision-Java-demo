package com.example.hkws.schedule;

import com.example.hkws.controller.web;
import com.example.hkws.data.CommandTasker;
import com.google.common.collect.Lists;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.EnableScheduling;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;


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

	@Autowired
	private RestTemplate restTemplate;

	/**
	 * @Description:通过访问摄像头ip去监控摄像头是否可访问
	 * @Param:
	 * @Author: zzx 774286887@qq.com
	 * @Date: 2020/6/12
	 * @Time: 11:22
	 * @return:
	 */
	@Scheduled(initialDelay = 1000 * 1, fixedRate = 1000 * 10)
	public void autoKillTask() {
		Collection<CommandTasker> commandTaskers = web.manager.queryAll();
		log.info("定时执行删除task任务");
		for(CommandTasker c:commandTaskers){
			// id就是ip
			String id = c.getId();
			// 因为历史任务就会有 history 前缀
			String ip = id.replace("history","");
			try {
				restTemplate.getForObject("http://"+ip, String.class);
			}catch (Exception e){
				web.manager.stop(ip);
				log.info(ip+"摄像头访问异常，请检查");
				log.info(e.getMessage()+ip);
			}

		}
		Iterator taskId = taskIdSet.iterator();
		while (taskId.hasNext()){
			log.info("摄像头异常停止：Failed to update header with correct filesize");
			web.manager.stop(taskId.next().toString());
			taskId.remove();
		}
	}


}
