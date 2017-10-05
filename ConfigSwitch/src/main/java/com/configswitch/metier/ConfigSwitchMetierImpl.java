package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;

import org.springframework.stereotype.Service;

import com.configswitch.entities.Switch;

@Service
public class ConfigSwitchMetierImpl implements IConfigSwitchMetier {
	
	private Switch switche ;

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

}
