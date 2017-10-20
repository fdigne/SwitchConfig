package com.configswitch.metier;

import java.lang.management.ManagementFactory;
import java.lang.management.MemoryMXBean;
import java.lang.management.MemoryUsage;

import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

import com.configswitch.entities.TrapInformation;

@Service
public class TrapObserverJob {
	
	public final ApplicationEventPublisher eventPublisher;
	
	public TrapObserverJob(ApplicationEventPublisher eventPublisher) {
		this.eventPublisher = eventPublisher;
	}
	
	@Scheduled(fixedRate = 1000)
	  public void doSomething() {
	    

	    
	  }

}
