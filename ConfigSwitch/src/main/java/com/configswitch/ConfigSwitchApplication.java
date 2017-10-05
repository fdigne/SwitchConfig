package com.configswitch;

import java.net.Inet4Address;
import java.net.InetAddress;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import com.configswitch.entities.Switch;

@SpringBootApplication
public class ConfigSwitchApplication implements CommandLineRunner {
	
	private Switch switche ;

	public static void main(String[] args)  {
		SpringApplication.run(ConfigSwitchApplication.class, args);
	}

	

	@Override
	public void run(String... arg0) throws Exception {
		
		InetAddress adresse= Inet4Address.getByName("192.168.1.10") ;
		
		switche = new Switch("SwitchFlorian", adresse) ; 
		
		if (switche.getAdressSwitch().isReachable(15)) {
			
			System.out.println("Le switch " + switche.getNameSwitch()+ " est joignable !");
		}
		else {
			throw new RuntimeException("Le switch " + switche.getNameSwitch()+ " n'est pas joignable !");
		}
		
		
	}
}
