package com.configswitch.metier;


import org.springframework.context.ApplicationEventPublisher;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;


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
