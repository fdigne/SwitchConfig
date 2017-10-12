package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import org.snmp4j.event.ResponseEvent;
import org.snmp4j.smi.OID;
import org.snmp4j.smi.VariableBinding;
import org.snmp4j.util.TableEvent;
import org.springframework.stereotype.Service;

import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;
import com.configswitch.snmp.SNMPManager;

@Service
public class ConfigSwitchMetierImpl implements IConfigSwitchMetier {

	private Switch switche ;

	public static final String IFNAME = ".1.3.6.1.2.1.2.2.1.2.";
	public static final String IFNUMBER = ".1.3.6.1.2.1.2.1.0";
	private static final String IFDESCR = ".1.3.6.1.2.1.2.2.1.2";
	private static final String IFTYPE = ".1.3.6.1.2.1.2.2.1.3.";

	@Override
	public Switch getSwitchInformations(InetAddress adresseSwitch) {

		switche = new Switch(adresseSwitch.getHostName(), adresseSwitch);
		return switche;
	}

	@Override
	public boolean isReachable(InetAddress adresseSwitch) throws IOException {
		boolean result = adresseSwitch.isReachable(10);

		return result;
	}


	@Override
	public Collection<InterfaceSwitch> getListInterfaces(InetAddress adresseSwitch) {

		SNMPManager client = new SNMPManager("udp:"+adresseSwitch.toString()+"/161");
		ArrayList<InterfaceSwitch> liste = new ArrayList<InterfaceSwitch>();

		try {
			client.start();
			List<TableEvent> tableViewSnmp = client.getListInterfaces(new OID(IFDESCR));
			for (TableEvent te : tableViewSnmp) {				
				for(VariableBinding vb : te.getColumns()) {
					String nomInterface = vb.getVariable().toString();
					String typeInterface = getTypeInterface(client.getAsString(new OID(IFTYPE+te.getIndex())));

					InterfaceSwitch interfaceSwitch = new InterfaceSwitch(nomInterface);
					interfaceSwitch.setTypeInterface(typeInterface);
					liste.add(interfaceSwitch);
				}


			}
		} catch (Exception e) {
			e.printStackTrace();
		}

		return liste;

	}

	private String getTypeInterface(String typeInterfaceSnmp) {
		String typeInterface;
		if (typeInterfaceSnmp.equals("6")) {
			typeInterface = "Eclairage" ;
		}
		else if(typeInterfaceSnmp.equals("71")) {
			typeInterface = "Video" ;

		}
		else {
			typeInterface = "Son" ;
		}

		return typeInterface ;
	}
}
