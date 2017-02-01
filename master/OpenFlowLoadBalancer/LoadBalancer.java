// Evelina Dumitrescu Tema 2 ATDS
package net.floodlightcontroller.atds;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import org.projectfloodlight.openflow.protocol.OFFlowMod;
import org.projectfloodlight.openflow.protocol.OFMessage;
import org.projectfloodlight.openflow.protocol.OFPacketIn;
import org.projectfloodlight.openflow.protocol.OFPacketOut;
import org.projectfloodlight.openflow.protocol.OFVersion;
import org.projectfloodlight.openflow.protocol.action.OFAction;
import org.projectfloodlight.openflow.protocol.match.Match;
import org.projectfloodlight.openflow.protocol.match.MatchField;

import org.projectfloodlight.openflow.types.EthType;
import org.projectfloodlight.openflow.types.IPv4Address;
import org.projectfloodlight.openflow.types.IpProtocol;
import org.projectfloodlight.openflow.types.MacAddress;
import org.projectfloodlight.openflow.types.OFBufferId;
import org.projectfloodlight.openflow.types.OFPort;
import org.projectfloodlight.openflow.types.TransportPort;

import net.floodlightcontroller.atds.BasicModule;

import net.floodlightcontroller.core.FloodlightContext;
import net.floodlightcontroller.core.IFloodlightProviderService;
import net.floodlightcontroller.core.IOFSwitch;

import net.floodlightcontroller.packet.Ethernet;
import net.floodlightcontroller.packet.IPv4;
import net.floodlightcontroller.packet.TCP;

public class LoadBalancer extends BasicModule {
	protected final Integer PORT1 = 1;
	protected final Integer PORT2 = 2;
	protected final Integer PORT3 = 3;
	int port = PORT2;

	protected PollServer pollServer1 = new PollServer(0, "192.168.56.3");
	protected PollServer pollServer2 = new PollServer(1, "192.168.56.4");

	public LoadBalancer() {
	}

	@Override
	protected void pseudoConstructor() {
		pollServer1.start();
		pollServer2.start();
	}

	@Override
	protected void receivePacketIn(IOFSwitch sw, OFPacketIn msg, FloodlightContext cntx) {
		// logger.info("Received packet in");
		OFPort ingressPort = msg.getVersion().compareTo(OFVersion.OF_12) < 0 ? msg.getInPort()
				: msg.getMatch().get(MatchField.IN_PORT);

		Ethernet eth = IFloodlightProviderService.bcStore.get(cntx, IFloodlightProviderService.CONTEXT_PI_PAYLOAD);
		MacAddress dest_mac = eth.getDestinationMACAddress();
		MacAddress src_mac = eth.getSourceMACAddress();
		EthType eth_type = eth.getEtherType();
		if (eth_type == EthType.IPv4) {
			IPv4 ipv4 = (IPv4) eth.getPayload();
			IpProtocol protocol = ipv4.getProtocol();
			IPv4Address src_ipv4 = ipv4.getSourceAddress();
			IPv4Address dest_ipv4 = ipv4.getDestinationAddress();
			if (protocol.equals(IpProtocol.TCP)) {
				TCP tcp = (TCP) ipv4.getPayload();
				TransportPort src_tcp = tcp.getSourcePort();
				TransportPort dest_tcp = tcp.getDestinationPort();
				if (ipv4.getDestinationAddress().toString().equals("10.1.0.2")
						&& tcp.getDestinationPort().getPort() == 80 && ingressPort.getPortNumber() == PORT1) {
					logger.info("Received an HTTP packet:\n" + " ingress port = " + ingressPort + " source endpoint = "
							+ src_ipv4 + ":" + src_tcp + " destination endpoint = " + dest_ipv4 + ":" + dest_tcp
							+ " egress port = " + port + "\n");

					List<OFAction> actions = new ArrayList<>(1);

					OFAction action = sw.getOFFactory().actions().buildOutput().setPort(OFPort.of(port))
							.setMaxLen(0xffFFffFF).build();
					actions.add(action);

					int loadServer1 = 0;
					int loadServer2 = 0;
					loadServer1 = pollServer1.getLoad();
					loadServer2 = pollServer2.getLoad();

					System.out.println(loadServer1 + " " + loadServer2);
					/*
					 * if (port == PORT2) { port = PORT3; } else if (port ==
					 * PORT3) { port = PORT2; }
					 */
					if (loadServer1 < loadServer2) {
						port = PORT2;
					} else {
						port = PORT3;
					}

					OFPacketOut.Builder pob = sw.getOFFactory().buildPacketOut();
					pob.setBufferId(msg.getBufferId());
					pob.setInPort(ingressPort);
					pob.setActions(actions);

					if (msg.getBufferId() == OFBufferId.NO_BUFFER) {
						pob.setData(msg.getData());
					}

					sw.write(pob.build());

					Match.Builder mb = sw.getOFFactory().buildMatch();
					mb.setExact(MatchField.IN_PORT, ingressPort).setExact(MatchField.ETH_SRC, src_mac)
							.setExact(MatchField.ETH_DST, dest_mac).setExact(MatchField.ETH_TYPE, eth_type)
							.setExact(MatchField.IP_PROTO, protocol).setExact(MatchField.IPV4_SRC, src_ipv4)
							.setExact(MatchField.IPV4_DST, dest_ipv4).setExact(MatchField.TCP_SRC, src_tcp)
							.setExact(MatchField.TCP_DST, dest_tcp);

					Match match = mb.build();

					OFFlowMod.Builder fmb = sw.getOFFactory().buildFlowAdd();
					fmb.setIdleTimeout(0);
					fmb.setHardTimeout(0);
					fmb.setMatch(match);
					fmb.setActions(actions);

					sw.write(fmb.build());

					return;
				} else if (protocol.equals(IpProtocol.TCP) && src_tcp.getPort() == 80 && dest_ipv4.equals("10.1.0.1")
						&& ingressPort.getPortNumber() != PORT1) {
					List<OFAction> actions = new ArrayList<>(1);
					OFAction action = sw.getOFFactory().actions().buildOutput().setPort(OFPort.of(PORT1))
							.setMaxLen(0xffFFffFF).build();
					actions.add(action);

					OFPacketOut.Builder pob = sw.getOFFactory().buildPacketOut();
					pob.setBufferId(msg.getBufferId());
					pob.setInPort(ingressPort);
					pob.setActions(actions);

					if (msg.getBufferId() == OFBufferId.NO_BUFFER) {
						pob.setData(msg.getData());
					}

					sw.write(pob.build());
					Match.Builder mb = sw.getOFFactory().buildMatch();
					mb.setExact(MatchField.IN_PORT, ingressPort).setExact(MatchField.ETH_SRC, src_mac)
							.setExact(MatchField.ETH_DST, dest_mac).setExact(MatchField.ETH_TYPE, eth_type)
							.setExact(MatchField.IP_PROTO, protocol).setExact(MatchField.IPV4_SRC, src_ipv4)
							.setExact(MatchField.IPV4_DST, dest_ipv4).setExact(MatchField.TCP_SRC, src_tcp);

					Match match = mb.build();

					OFFlowMod.Builder fmb = sw.getOFFactory().buildFlowAdd();
					fmb.setIdleTimeout(0);
					fmb.setHardTimeout(0);
					fmb.setMatch(match);
					fmb.setActions(actions);

					sw.write(fmb.build());
					return;
				}
			}
		}

		List<OFAction> actions = new ArrayList<>(1);
		OFAction action = sw.getOFFactory().actions().buildOutput().setPort(OFPort.FLOOD).setMaxLen(0xffFFffFF).build();
		actions.add(action);
		OFPacketOut.Builder pob = sw.getOFFactory().buildPacketOut();
		pob.setBufferId(msg.getBufferId());
		pob.setInPort(ingressPort);
		pob.setActions(actions);

		if (msg.getBufferId() == OFBufferId.NO_BUFFER) {
			pob.setData(msg.getData());
		}

		sw.write(pob.build());
	}

	@Override
	protected void handleSwitchUp(IOFSwitch sw) {
	}

	protected class PollServer extends Thread {
		protected int id;
		protected String ip;
		protected int load;

		protected PollServer(int id, String ip) {
			this.id = id;
			this.ip = ip;
		}

		protected HttpURLConnection createHttpConnection() throws Exception {
			URL url = new URL("http://" + this.ip + "/server-status?auto");
			HttpURLConnection connection = (HttpURLConnection) url.openConnection();

			connection.setRequestMethod("GET");
			connection.setUseCaches(false);
			connection.setDoInput(true);
			connection.setDoOutput(false);
			connection.setConnectTimeout(1000);
			connection.setReadTimeout(1000);

			return connection;
		}

		@Override
		public void run() {
			while (true) {
				try {
					sleep(1000);
					this.load = updateLoad();

				} catch (Exception e) {
					logger.warn(e.toString());
				}
			}
		}

		protected int getLoad() {
			return this.load;
		}

		protected int updateLoad() throws Exception {
			int load = 0;
			HttpURLConnection connection = createHttpConnection();
			connection.connect();
			try (BufferedReader reader = new BufferedReader(new InputStreamReader(connection.getInputStream()))) {
				String line;

				while ((line = reader.readLine()) != null) {
					// System.out.println(line);
					if (line.contains("ConnsTotal:")) {
						String[] tokens = line.split(" ");
						load = Integer.parseInt(tokens[1]);
					} else if (line.contains("ConnsAsyncKeepAlive:")) {
						String[] tokens = line.split(" ");
						load -= Integer.parseInt(tokens[1]);
					} else if (line.contains("ConnsAsyncClosing:")) {
						String[] tokens = line.split(" ");
						load -= Integer.parseInt(tokens[1]);
					}
				}
				connection.disconnect();
			}
			return load;
		}

	}

}

