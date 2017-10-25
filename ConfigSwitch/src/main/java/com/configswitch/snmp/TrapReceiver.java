package com.configswitch.snmp;

import java.io.IOException;
import java.net.UnknownHostException;
import java.time.LocalDateTime;
import java.util.concurrent.Callable;

import org.snmp4j.CommandResponder;
import org.snmp4j.CommandResponderEvent;
import org.snmp4j.CommunityTarget;
import org.snmp4j.MessageDispatcher;
import org.snmp4j.MessageDispatcherImpl;
import org.snmp4j.MessageException;
import org.snmp4j.PDU;
import org.snmp4j.Snmp;
import org.snmp4j.log.LogFactory;
import org.snmp4j.mp.MPv1;
import org.snmp4j.mp.MPv2c;
import org.snmp4j.mp.StateReference;
import org.snmp4j.mp.StatusInformation;
import org.snmp4j.security.Priv3DES;
import org.snmp4j.security.SecurityProtocols;
import org.snmp4j.smi.Address;
import org.snmp4j.smi.OctetString;
import org.snmp4j.smi.TcpAddress;
import org.snmp4j.smi.TransportIpAddress;
import org.snmp4j.smi.UdpAddress;
import org.snmp4j.tools.console.SnmpRequest;
import org.snmp4j.transport.AbstractTransportMapping;
import org.snmp4j.transport.DefaultTcpTransportMapping;
import org.snmp4j.transport.DefaultUdpTransportMapping;
import org.snmp4j.util.MultiThreadedMessageDispatcher;
import org.snmp4j.util.ThreadPool;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.ApplicationEventPublisher;
import org.springframework.context.event.EventListener;
import org.springframework.http.MediaType;
import org.springframework.scheduling.annotation.Async;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;
import org.springframework.ui.Model;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.configswitch.entities.Trap;
import com.configswitch.web.ConfigSwitchController;

@Component
public class TrapReceiver implements CommandResponder {
	
	
	public Trap pduReceived ;
	
	public String message ;
	
	@Autowired
	private ApplicationEventPublisher publisher;
	
	public TrapReceiver(ApplicationEventPublisher publisher) {
        this.publisher = publisher;
    }
	
	private final SseEmitter emitter = new SseEmitter();
	
	public TrapReceiver() {
		
	}
	
	public SseEmitter getInfiniteMessages() {
        return emitter;
    }
	
	
	 /**
	   * This method will listen for traps and response pdu's from SNMP agent.
	   */
	  public synchronized void listen(TransportIpAddress address) throws IOException
	  {
	    AbstractTransportMapping transport;
	    if (address instanceof TcpAddress)
	    {
	      transport = new DefaultTcpTransportMapping((TcpAddress) address);
	    }
	    else
	    {
	      transport = new DefaultUdpTransportMapping((UdpAddress) address);
	    }

	    ThreadPool threadPool = ThreadPool.create("DispatcherPool", 10);
	    MessageDispatcher mtDispatcher = new MultiThreadedMessageDispatcher(threadPool, new MessageDispatcherImpl());

	    // add message processing models
	    mtDispatcher.addMessageProcessingModel(new MPv1());
	    mtDispatcher.addMessageProcessingModel(new MPv2c());

	    // add all security protocols
	    SecurityProtocols.getInstance().addDefaultProtocols();
	    SecurityProtocols.getInstance().addPrivacyProtocol(new Priv3DES());

	    //Create Target
	    CommunityTarget target = new CommunityTarget();
	    target.setCommunity( new OctetString("public"));
	    
	    Snmp snmp = new Snmp(mtDispatcher, transport);
	    snmp.addCommandResponder(this);
	    
	    transport.listen();
	    System.out.println("Listening on " + address);

	  }

	  /**
	   * This method will be called whenever a pdu is received on the given port specified in the listen() method
	   */
	  public synchronized void processPdu(CommandResponderEvent cmdRespEvent)
	  {
	    System.out.println("Received PDU...");
	    PDU pdu = cmdRespEvent.getPDU();
	    String[] sourceAddress = cmdRespEvent.getPeerAddress().toString().split("\\/");
		String[] traitementPDUInterfaceName = pdu.getVariableBindings().get(3).toString().split("=");
		message =LocalDateTime.now()+" : "+traitementPDUInterfaceName[1]+" down from "+sourceAddress[0];
	    
	    publisher.publishEvent(cmdRespEvent);
	    if (pdu != null)
	    {	
	      System.out.println("Trap Type = " + pdu.getType());
	      System.out.println("Variable Bindings = " + pdu.getVariableBindings());
	      int pduType = pdu.getType();
	      if ((pduType != PDU.TRAP) && (pduType != PDU.V1TRAP) && (pduType != PDU.REPORT)
	      && (pduType != PDU.RESPONSE))
	      {
	        pdu.setErrorIndex(0);
	        pdu.setErrorStatus(0);
	        pdu.setType(PDU.RESPONSE);
	        StatusInformation statusInformation = new StatusInformation();
	        StateReference ref = cmdRespEvent.getStateReference();
	        try
	        {
	          System.out.println(cmdRespEvent.getPDU());
	          cmdRespEvent.getMessageDispatcher().returnResponsePdu(cmdRespEvent.getMessageProcessingModel(),
	          cmdRespEvent.getSecurityModel(), cmdRespEvent.getSecurityName(), cmdRespEvent.getSecurityLevel(),
	          pdu, cmdRespEvent.getMaxSizeResponsePDU(), ref, statusInformation);
	        }
	        catch (MessageException ex)
	        {
	          System.err.println("Error while sending response: " + ex.getMessage());
	          LogFactory.getLogger(SnmpRequest.class).error(ex);
	        }
	      }
	    }
	  }	
	}
