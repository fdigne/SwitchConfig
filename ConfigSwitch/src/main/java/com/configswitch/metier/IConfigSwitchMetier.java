package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;

import com.configswitch.entities.Switch;

public interface IConfigSwitchMetier {
	
	public boolean isReachable(InetAddress adresseSwitch) throws IOException;
	public Switch getSwitchInformations(InetAddress adresseSwitch);

}
