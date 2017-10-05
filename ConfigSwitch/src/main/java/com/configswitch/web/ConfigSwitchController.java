package com.configswitch.web;


import java.net.InetAddress;

import javax.servlet.http.HttpSessionEvent;
import javax.servlet.http.HttpSessionListener;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

import com.configswitch.entities.Switch;
import com.configswitch.metier.IConfigSwitchMetier;

@Controller
public class ConfigSwitchController{
	
	@Autowired
	private IConfigSwitchMetier configSwitchMetier ;

	@RequestMapping("/index")
	public String index() {
		return "index";
	}
	
	@RequestMapping("/consulterSwitch")
	public String consulterSwitch(Model model, String adresseSwitch) {
		model.addAttribute("adresseSwitch", adresseSwitch);
		try {
			boolean result = configSwitchMetier.isReachable(InetAddress.getByName(adresseSwitch));
			if (! result) {
				throw new RuntimeException("Switch injoignable") ;
			}
			else {
				
				Switch switche = configSwitchMetier.getSwitchInformations(InetAddress.getByName(adresseSwitch));
				model.addAttribute("switche", switche);
				
			}
			
			
		} catch (Exception e) {
			
			model.addAttribute("exception",e);
		}
		return "index";
		
	}

}
