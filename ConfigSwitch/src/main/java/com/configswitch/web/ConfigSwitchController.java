package com.configswitch.web;


import java.io.IOException;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.List;
import java.util.concurrent.CopyOnWriteArrayList;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.PDU;
import org.snmp4j.smi.UdpAddress;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.event.EventListener;
import org.springframework.stereotype.Component;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.context.request.async.AsyncRequestTimeoutException;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;
import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.entities.Trap;
import com.configswitch.metier.IConfigSwitchMetier;
import com.configswitch.snmp.TrapReceiver;


/** Classe Controller de la page Web
 * 
 * @author Florian Digne
 *
 */
@Controller
@Component
public class ConfigSwitchController  {

	
	private static final Pattern PATTERN = Pattern.compile(
	        "^(([01]?\\d\\d?|2[0-4]\\d|25[0-5])\\.){3}([01]?\\d\\d?|2[0-4]\\d|25[0-5])$");
	@Autowired
	private IConfigSwitchMetier configSwitchMetier ;
	
	private final CopyOnWriteArrayList<SseEmitter> emitters = new CopyOnWriteArrayList<>();
	
	private HashMap<String, String> sourceSwitche = new HashMap<String, String>();
	
	
	
	private Trap trap = new Trap();
	
	private String message = "" ;
	
	@RequestMapping("/")
	public String indexRacine(Model model) {
	
		return "redirect:index";
	}
	
	/**Page d'accueil
	 * 
	 * @param model
	 * @param trap
	 * @return
	 */
	@RequestMapping("index")
	public String index(Model model) {
		Collection<Switch> listeSwitch = configSwitchMetier.getListSwitch();
		for (Switch s : listeSwitch) {
			this.sourceSwitche.put(s.getAdressSwitchString(), s.getNameSwitch());
		}
		model.addAttribute("listeSwitch", listeSwitch);
		return "index";
	}

	/**Modifications des données du switch lors de l'envoie de la requête via Bouton "modifier"
	 * 
	 * @param model
	 * @param adresseSwitch
	 * @param typeInterface
	 * @return
	 */
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
		return "redirect:index";
	}
	
	
	/**
	 * Initialisation de la configuration du switch
	 * @param model
	 * @param adresseSwitch
	 * @param nomSwitch
	 * @return
	 */
	@PostMapping("/initialiserSwitch")
	public String initialiserSwitch(Model model, String adresseSwitch, String nomSwitch) {
		
		
		return "redirect:index";
	}
	
	
	 
	/** Reception des données envoyées sur le port d'écoute des traps.
	 * 
	 * @param cmdRespEvent
	 */
	@EventListener
	public void trapReceived(CommandResponderEvent cmdRespEvent) {
		
		List<SseEmitter> deadEmitters = new ArrayList<>();
		PDU pdu = cmdRespEvent.getPDU();
		String[] sourceAddress = cmdRespEvent.getPeerAddress().toString().split("\\/");
		//////////////////////////////////A ENLEVER EN CAS REEL 
		
		sourceAddress[0] = "172.31.10.254";
		////////////////////////////////////////////
		this.trap.setSourceSwitch(this.sourceSwitche.get(sourceAddress[0]));	
		String[] traitementPDUInterfaceName = pdu.getVariableBindings().get(3).toString().split("=");
		this.trap.setInterfaceName(traitementPDUInterfaceName[1].trim());
		this.trap.setTypeTrap(pdu.getType());
		String[] traitementPDUStatusInterface = pdu.getVariableBindings().get(5).toString().split("=");
		message = LocalDateTime.now()+" : "+traitementPDUInterfaceName[1]+" "+traitementPDUStatusInterface[1]+" from "+sourceAddress[0]+"\n";		
		this.trap.setMessage(message);
		
	    this.emitters.forEach(emitter -> {
	      try {
	        emitter.send(trap);
	      }
	      catch (Exception e) {
	        deadEmitters.add(emitter);
	      }
	    });

	    this.emitters.remove(deadEmitters);
	}


	/** Envoie du message à la page HTML
	 * 
	 * @param response
	 * @return
	 */
	@GetMapping("/trapAlarm")
	public SseEmitter sendMessage() {
		
		SseEmitter emitter = new SseEmitter();
	    this.emitters.add(emitter);

	    emitter.onCompletion(() -> this.emitters.remove(emitter));
	    emitter.onTimeout(() -> this.emitters.remove(emitter));

		return emitter;
	  }
	
	/**
	 * Modifie le nom du Switch 
	 * 
	 * @param model
	 * @param adresseSwitch
	 * @param nameSwitch
	 * @return
	 */
	@PostMapping("/changeSwitchName")
	public String changeSwitchName(Model model, String adresseSwitch, String nameSwitch) {
		configSwitchMetier.setSwitchName(adresseSwitch, nameSwitch.toUpperCase());
		
		return "redirect:index";
	}
	
	
}

