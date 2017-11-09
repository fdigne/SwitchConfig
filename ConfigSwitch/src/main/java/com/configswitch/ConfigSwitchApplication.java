package com.configswitch;


import java.io.IOException;

import org.snmp4j.smi.UdpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.EnableAutoConfiguration;
import org.springframework.boot.autoconfigure.SpringBootApplication;

import com.configswitch.snmp.TrapReceiver;


@EnableAutoConfiguration
@SpringBootApplication
public class ConfigSwitchApplication implements CommandLineRunner{
	
	@Autowired
	private TrapReceiver trapReceiver = new TrapReceiver();
		
	
	
	public static void main(String[] args)  {
		SpringApplication.run(ConfigSwitchApplication.class, args);
		
	}
	
	@Override
	public void run(String... arg0) throws Exception {
		this.startTrapReceiver();		 
	}
	
	/**Lancement du listener du port UDP 1800 pour la réception des trap Cisco.
	 * 
	 */
	private void startTrapReceiver() {
		 try
		    {
		      trapReceiver.listen(new UdpAddress("10.0.0.1/1800"));
		    }
		    catch (IOException e)
		    {
		      System.err.println("Error in Listening for Trap");
		      System.err.println("Exception Message = " + e.getMessage());
		    }
	}
	
}
