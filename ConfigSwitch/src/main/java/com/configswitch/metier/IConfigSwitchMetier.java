package com.configswitch.metier;

import java.io.IOException;
import java.net.InetAddress;
import java.util.Collection;
import com.configswitch.entities.InterfaceSwitch;
import com.configswitch.entities.Switch;

public interface IConfigSwitchMetier {
	
	/** Vérifie que l'adresse Ip soit accessible
	 * 
	 * @param adresseSwitch
	 * @return boolean 
	 * @throws IOException
	 */
	public boolean isReachable(InetAddress adresseSwitch) throws IOException;
	
	/** Obtient les informations du switch par une requête SNMP
	 * 
	 * @param adresseSwitch
	 * @return Switch
	 */
	public Switch getSwitchInformations(InetAddress adresseSwitch);
	
	/** Permet d'obtenir la liste des interfaces d'un switch
	 * 
	 * @param adresseSwitch
	 * @return Collection<InterfaceSwitch>
	 */
	public Collection<InterfaceSwitch> getListInterfaces(String adresseSwitch) ;
	
	/** Permet d'obtenir la liste des switchs présents sur le réseau 172.31.10.0/24
	 * 
	 * @return Collection<Switch>
	 */
	public Collection<Switch> getListSwitch();
	
	/** Permet d'obtenir le Vlan Id en fonction du type d'interface (Eclairage, Son, Vidéo)
	 * 
	 * @param typeInterface
	 * @return int
	 */
	public int getVlanId(String typeInterface);
	
	/** Permet de configurer l'interface à l'index du switch sur la valeur vlanValue
	 *  
	 * @param adresseSwitch
	 * @param index
	 * @param vlanValue
	 */
	public void setVlanConfiguration(String adresseSwitch, int index, int vlanValue);
	
	/** Permet d'obtenir le statut d'une interface en fonction de la valeur du PDU reçu
	 * 
	 * @param statusInterfaceSnmp
	 * @return boolean
	 */
	public boolean getStatusInterface(String statusInterfaceSnmp);
	
	/** Permet d'obtenir le type d'une interface (Eclairage, Son, Video) en fonction de son numéro de VLAN
	 * 
	 * @param vlanId
	 * @return String
	 */
	public String getTypeInterface(String vlanId);


}
