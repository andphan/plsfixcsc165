package a3.kmap165Engine.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import javax.swing.Timer;

import a3.kmap165Engine.npc.NPCcontroller;
import sage.networking.server.GameConnectionServer;
import sage.networking.server.IClientInfo;
//import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

public class GameServerTCP extends GameConnectionServer<UUID> {
	private NPCcontroller npcCtrl;
   private String ghostNPC_ID = "1";
   Vector3D playerPosition3D = new Vector3D();
	public GameServerTCP(int localPort) throws IOException {
		super(localPort, ProtocolType.TCP);
      npcCtrl = new NPCcontroller(this);
       
	}

	public void acceptClient(IClientInfo ci, Object o) { // override
		String message = (String) o;
		String[] messageTokens = message.split(",");
		if (messageTokens.length > 0) {
			if (messageTokens[0].compareTo("join") == 0) { // received “join”
				// format: join,localid
				UUID clientID = UUID.fromString(messageTokens[1]);
				addClient(ci, clientID);
				System.out.println("join obtained");
				sendJoinedMessage(clientID, true);
			}
		}
	}

	// Messages from Client to Server
	public void processPacket(Object o, InetAddress senderIP, int sndPort) {
		String message = (String) o;
		String[] messageTokens = message.split(",");
		if (messageTokens.length > 0) {
			if (messageTokens[0].compareTo("bye") == 0) { // receive “bye”
				// format: bye,localid
				UUID clientID = UUID.fromString(messageTokens[1]);
				System.out.println("bye obtained");
				sendByeMessages(clientID);
				removeClient(clientID);
			}
			if (messageTokens[0].compareTo("create") == 0) { // receive “create”
				// format: create,localid,x,y,z
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] ghostPosition = { messageTokens[2], messageTokens[3],
						messageTokens[4] };
                  
            Vector3D ghostPosition3D = new Vector3D(
					Double.parseDouble(messageTokens[2]),
					Double.parseDouble(messageTokens[3]),
					Double.parseDouble(messageTokens[4]));
            //npcCtrl.setPlayerLocation(ghostPosition3D);
            //npcCtrl.startNPCControl();    
				System.out.println("create obtained");
				sendCreateMessages(clientID, ghostPosition);
				sendWantsDetailsMessages(clientID);
            
        /*    sendCreateNPCMessages(clientID, ghostNPC_ID, ghostPosition);
            playerPosition3D = ghostPosition3D; 
            npcCtrl.setPlayerSpot(ghostPosition3D);
            npcCtrl.startNPCControl();
            int i = 1;
            while (i > 0)
            {
            if ( i % 4 == 0)
            {
            sendNPCinfo();
            }
            i++;
            }
            */
			}
			
			/*if (messageTokens[0].compareTo("createNPC") == 0) { // receive “create”
				// format: createNPC,localid, ghostNPC_id,x,y,z
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] ghostPosition = { messageTokens[2], messageTokens[3],
						messageTokens[4] };
				System.out.println("create NPC obtained");
				//sendCreateNPCMessages(clientID, ghostNPC_ID, ghostPosition);
				sendNPCinfo();
			}*/
			
			if (messageTokens[0].compareTo("dsfr") == 0) { // receive “details
															// for”
				// format: dsfr,remoteid,localid,x,y,z
				UUID remoteID = UUID.fromString(messageTokens[1]);
				UUID clientID = UUID.fromString(messageTokens[2]);
				String[] pos = { messageTokens[3], messageTokens[4],
						messageTokens[5] };
				//System.out.println("dsfr obtained");
				sndDetailsMsg(clientID, remoteID, pos);
			}
			if (messageTokens[0].compareTo("move") == 0) { // receive “move”
				// format: move,localid,x,y,z //look up sender name
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = { messageTokens[2], messageTokens[3],
						messageTokens[4] };
				//System.out.println("move obtained");
            Vector3D ghostPosition = new Vector3D(
					Double.parseDouble(messageTokens[2]),
					Double.parseDouble(messageTokens[3]),
					Double.parseDouble(messageTokens[4]));
           // npcCtrl.setPlayerSpot(ghostPosition);
				sendMoveMessages(clientID, pos);
			}
			/*
			if (messageTokens[0].compareTo("mnpc") == 0) { // receive “move”
				// format: move,localid,x,y,z //look up sender name
				UUID clientID = UUID.fromString(messageTokens[1]);
				String[] pos = { messageTokens[2], messageTokens[3],
						messageTokens[4] };
				// System.out.println("move obtained");
				sendNPCmoveMessages(clientID, pos);
			}
			*/
	
		}
	}

	public void sendJoinedMessage(UUID clientID, boolean success) {
		// format: join, success or join, failure
		try {
			String message = new String("join,");
			if (success)
				message += "success";
			else
				message += "failure";
			// System.out.println(message);
			sendPacket(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public void sendCreateNPCMessages(UUID clientID, String ghostNPC_ID, String[] position) {
		// format: create, remoteId, x, y, z
		try {
			String message = new String("createNPC");
         //message += "," + ghostNPC_ID;
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			//System.out.println("LPK " + message);
			sendPacket(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	public void sendCreateMessages(UUID clientID, String[] position) {
		// format: create, remoteId, x, y, z
		try {
			String message = new String("create," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			// System.out.println(message);
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sndDetailsMsg(UUID clientID, UUID remoteId, String[] position) {
		// format: dsfr, remoteId, x, y, z
		try {
			String message = new String("dsfr," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			// System.out.println(message);
			sendPacket(message, remoteId);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendWantsDetailsMessages(UUID clientID) {
		// format: wsds, remoteID
		try {
			String message = new String("wsds," + clientID.toString());
			// System.out.println(message);
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendMoveMessages(UUID clientID, String[] position) {
		try {
			String message = new String("move," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			// System.out.println(message);
			forwardPacketToAll(message, clientID);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	/*
	public void sendNPCmoveMessages(UUID clientID, String[] position) {
		// format: create, remoteId, x, y, z
		try {
			String message = new String("mnpc," + clientID.toString());
			message += "," + position[0];
			message += "," + position[1];
			message += "," + position[2];
			// System.out.println(message);
			sendPacketToAll(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	*/
	public void sendByeMessages(UUID clientID) {
		// format: bye, remoteID
		try {
			String message = new String("bye," + clientID.toString());
			// System.out.println(message);
			sendPacketToAll(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

/*	public void sendNPCinfo() {
			try
			{
				String message = new String("mnpc");
				message += "," + (npcCtrl.getNPC)).getX();
				message += "," + (npcCtrl.getNPC)).getY();
				message += "," + (npcCtrl.getNPC)).getZ();
				sendPacketToAll(message);
			} catch (Exception zzz)
			{
				zzz.printStackTrace();
			}
	}
	*/
/*	public void sendCheckForAvatarNear(Vector3D position) {
      if(Math.abs(position.getX() - playerPosition3D.getX()) <= 5 &&
         Math.abs(position.getZ() - playerPosition3D.getZ()) <= 5) npcCtrl.setNearFlag(true);
      else npcCtrl.setNearFlag(false); 
	}
*/
}