package com.configswitch.web;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.snmp4j.PDU;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.entities.Trap;
import com.configswitch.metier.IConfigSwitchMetier;
import com.configswitch.snmp.TrapReceiver;

@Controller
public class ConfigSwitchController{

	//public static final String IP_ADDRESS_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	private static final Pattern PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@Autowired
	private IConfigSwitchMetier configSwitchMetier ;
	
	
	private TrapReceiver trapReceiver ;	
	

	@RequestMapping("/index")
	public String index(Model model, Trap trap) throws UnknownHostException {
		Collection<Switch> listeSwitch = configSwitchMetier.getListSwitch();
		model.addAttribute("listeSwitch", listeSwitch);
		trapReceiver = new TrapReceiver();
		
		try {
			trapReceiver.listen(new UdpAddress("10.0.0.1/1800"));
		} catch (IOException e) {
			 System.err.println("Error in Listening for Trap");
		      System.err.println("Exception Message = " + e.getMessage());
		}
		
		return "index";
	}

	@RequestMapping("/consulterSwitch")
	public String consulterSwitch(Model model, String adresseSwitch) {
		model.addAttribute("adresseSwitch", adresseSwitch);
		
		try {
			if(! traitementStyleIpAddresse(adresseSwitch)) {
				throw new RuntimeException("Adresse Ip non valide") ;
			}
			boolean result = configSwitchMetier.isReachable(InetAddress.getByName(adresseSwitch));
			if (! result) {
				throw new RuntimeException("Switch injoignable") ;
			}
			else {

				Switch switche = configSwitchMetier.getSwitchInformations(InetAddress.getByName(adresseSwitch));
				model.addAttribute("switche", switche);
				Collection<InterfaceSwitch> listInterfaces = configSwitchMetier.getListInterfaces(InetAddress.getByName(adresseSwitch));
				model.addAttribute("listInterfaces", listInterfaces);
				
				Collection<Switch> listeSwitch = configSwitchMetier.getListSwitch();
				model.addAttribute("listeSwitch", listeSwitch);
			}


		} catch (Exception e) {

			model.addAttribute("exception",e);
		}
		return "index";

	}
	
	private boolean traitementStyleIpAddresse(String adresseSwitch) {
		Matcher matcher = PATTERN.matcher(adresseSwitch) ;
		
		if(matcher.find()) {
			return true ;
		}
		else {
			return false;
		}
		
	}

	@RequestMapping(value="/configurerSwitch", method=RequestMethod.POST)
	public String configurerSwitch(Model model, String adresseSwitch, 
									@RequestParam("selectTypeInterface") String[] typeInterface)  {
		int i = 1 ;
		
		for (String s : typeInterface) {
			String[] traitementString = s.split("\\.");
			String vlanIdType = traitementString[0];
			String ifIndexString = traitementString[1];
			
			int vlanId = configSwitchMetier.getVlanId(vlanIdType);
			configSwitchMetier.setVlanConfiguration(adresseSwitch, Integer.valueOf(ifIndexString), vlanId);
			
		}
		return "redirect:/consulterSwitch?adresseSwitch="+adresseSwitch;
	}

}
