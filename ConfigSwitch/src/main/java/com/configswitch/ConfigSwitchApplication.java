package com.configswitch;

import java.awt.Desktop;
import java.io.IOException;
import java.net.URI;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConfigSwitchApplication implements CommandLineRunner {
	
	public static void main(String[] args)  {
		SpringApplication.run(ConfigSwitchApplication.class, args);
	}
	
	private void openUrlInBrowser(String url)
	{
	 Runtime runtime = Runtime.getRuntime();
	 String[] args = { "osascript", "-e", "open location \"" + url + "\"" };
	 try
	 {
	  Process process = runtime.exec(args);
	 }
	 catch (IOException e)
	 {
	// do what you want with this
	 }
	}

	@Override
	public void run(String... arg0) throws Exception {
		
		
		openUrlInBrowser("http://localhost:8880/index");
		 
	}
	
}
