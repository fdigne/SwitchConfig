package com.configswitch.web;


import java.io.IOException;
import java.net.InetAddress;
import java.time.LocalDateTime;
import java.util.Collection;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.smi.UdpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.entities.Trap;
import com.configswitch.metier.IConfigSwitchMetier;
import com.configswitch.snmp.TrapReceiver;

@Controller
@Component
@EnableAsync
public class ConfigSwitchController  {

	//public static final String IP_ADDRESS_PATTERN = "\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}\\.\\d{1,3}";
	private static final Pattern PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@Autowired
	private IConfigSwitchMetier configSwitchMetier ;
	
	
	@Autowired
	private TrapReceiver trapReceiver = new TrapReceiver();
	
	private Trap trap = new Trap();
	private Model model ;
	
	
	@RequestMapping("/index")
	public String index(Model model, Trap trap) {
		this.model = model ;
		Collection<Switch> listeSwitch = configSwitchMetier.getListSwitch();
		model.addAttribute("listeSwitch", listeSwitch);
		this.startTrapReceiver();
		model.addAttribute("trapReceived", this.trap.getMessage());
		
		return "index";
	}

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

	@RequestMapping("/consulterSwitch")
	public String consulterSwitch(Model model, String adresseSwitch) {
		model.addAttribute("adresseSwitch", adresseSwitch);
		model.addAttribute("trapReceived", this.trap.getMessage());
		
		
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
		
		for (String s : typeInterface) {
			String[] traitementString = s.split("\\.");
			String vlanIdType = traitementString[0];
			String ifIndexString = traitementString[1];
			
			int vlanId = configSwitchMetier.getVlanId(vlanIdType);
			configSwitchMetier.setVlanConfiguration(adresseSwitch, Integer.valueOf(ifIndexString), vlanId);
			
		}
		return "redirect:/consulterSwitch?adresseSwitch="+adresseSwitch;
	}
	
	@Async 
	@EventListener
	public String trapReceived(CommandResponderEvent cmdRespEvent) {
		
		PDU pdu = cmdRespEvent.getPDU();
		String[] sourceAddress = cmdRespEvent.getPeerAddress().toString().split("\\/");
		this.trap.setSourceAdress(sourceAddress[0]);
		String[] traitementPDUInterfaceName = pdu.getVariableBindings().get(3).toString().split("=");
		this.trap.setInterfaceName(traitementPDUInterfaceName[1]);
		String message =LocalDateTime.now()+" : "+traitementPDUInterfaceName[1]+" down from "+sourceAddress[0];
		this.trap.setMessage(message);
		this.updateView(this.model, message);
		return message;
	}

	@RequestMapping("/redirect")
	private String updateView(Model model, @ModelAttribute("trapReceived") String message) {
		return "redirect:/index";
	}


}
