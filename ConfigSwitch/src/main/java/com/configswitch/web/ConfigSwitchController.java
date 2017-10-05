package com.configswitch.web;


import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

public class ConfigSwitchController {
	
	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/consulterSwitch")
	public String consulterSwitch(Model model, String adresseSwitch) {
		model.addAttribute("adresseSwitch", adresseSwitch);
		try {
			
			
		} catch (Exception e) {
			
			model.addAttribute("exception",e);
		}
		return "index";
		
	}

}
