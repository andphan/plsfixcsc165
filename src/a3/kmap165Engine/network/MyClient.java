package a3.kmap165Engine.network;

import java.io.IOException;
import java.net.InetAddress;
import java.util.UUID;

import sage.networking.client.GameConnectionClient;
import sage.networking.server.IClientInfo;

import java.util.Vector;

import a3.games.fighter2015.FightingGame;
import a3.kmap165Engine.network.ghost_avatar.GhostAvatar;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import a3.games.fighter2015.*;
import a3.kmap165Engine.network.ghost_avatar.*;
import java.lang.Double;

public class MyClient extends GameConnectionClient {
	private FightingGame game;
	private UUID id;
	private Vector<GhostAvatar> ghostAvatars;
    private GhostNPC newNPC;

	public MyClient(InetAddress remAddr, int remPort, ProtocolType pType,
			FightingGame game) throws IOException {

		super(remAddr, remPort, pType);
		this.game = game;
		this.id = UUID.randomUUID();
		this.ghostAvatars = new Vector<GhostAvatar>();
	}

	protected void processPacket(Object msg) { // override
		String message = (String) msg;
		String[] messageTokens = message.split(",");
		// extract incoming message into substrings. Then process:
		if (messageTokens[0].compareTo("join") == 0) { // receive �join�
			// format: join, success or join, failure
			if (messageTokens[1].compareTo("success") == 0) {
				game.setIsConnected(true);
				System.out.println("success obtained");
				sendCreateMessage(game.getPlayerPosition());
				createGhostNPC(new Vector3D(100, 2, 70));
			}
			if (messageTokens[1].compareTo("failure") == 0)
				System.out.println("failure obtained");
			game.setIsConnected(false);
		}
		if (messageTokens[0].compareTo("bye") == 0) { // receive �bye�
			// format: bye, remoteId
			UUID ghostID = UUID.fromString(messageTokens[1]);
			sendByeMessage();
			System.out.println("bye obtained");
			removeGhostAvatar(ghostID);
		}
		if (messageTokens[0].compareTo("dsfr") == 0) { // receive �details for�
			// format: create, remoteId, x,y,z or dsfr, remoteId, x,y,z
			UUID ghostID = UUID.fromString(messageTokens[1]);
			// extract ghost x,y,z, position from message
			Vector3D ghostPosition = new Vector3D(
					Double.parseDouble(messageTokens[2]),
					Double.parseDouble(messageTokens[3]),
					Double.parseDouble(messageTokens[4]));
			   System.out.println("dsfr obtained");
			// System.out.println(ghostPosition.getX() +"," +
			// ghostPosition.getY() +"," + ghostPosition.getZ());
			createGhostAvatar(ghostID, ghostPosition, this);
		}
		
		if (messageTokens[0].compareTo("create") == 0) { // receive �create��
			System.out.println("create obtained");
			// format: create, remoteId, x,y,z
			UUID ghostID = UUID.fromString(messageTokens[1]);

			// extract ghost x,y,z, position from message:
			Vector3D ghostPosition = new Vector3D(
					Double.parseDouble(messageTokens[2]),
					Double.parseDouble(messageTokens[3]),
					Double.parseDouble(messageTokens[4]));
			System.out.println("ghost poscreate: " + ghostPosition.getX() + ","
					+ ghostPosition.getY() + "," + ghostPosition.getZ());
			createGhostAvatar(ghostID, ghostPosition, this);
		}
		if (messageTokens[0].compareTo("wsds") == 0) { // receive �wants��
			//System.out.println("wsds obtained");
			// format: wsds, remoteID
			UUID remoteID = UUID.fromString(messageTokens[1]);
			sendDetailsForMessage(remoteID, game.getPlayerPosition());
		}
		if (messageTokens[0].compareTo("move") == 0) { // receive �move�
			// format: move, remoteId, x,y,z
			UUID remoteID = UUID.fromString(messageTokens[1]);
			Vector3D ghostPosition = new Vector3D(
					Double.parseDouble(messageTokens[2]),
					Double.parseDouble(messageTokens[3]),
					Double.parseDouble(messageTokens[4]));
			System.out.println("move obtained for ghost position: " + ghostPosition.getX() +"," +
			ghostPosition.getY() +"," + ghostPosition.getZ());
			updateGhostAvatar(remoteID, ghostPosition);
		}
		// here is where you're updating the npc
		if (messageTokens[0].compareTo("mnpc") == 0)
		{  
			Vector3D ghostPosition = new Vector3D(
			   Double.parseDouble(messageTokens[1]),
			   Double.parseDouble(messageTokens[2]),
			   Double.parseDouble(messageTokens[3]));
         System.out.println("move npc");
			updateGhostNPC(ghostPosition);
		}
	
	}

	public void sendCreateMessage(Vector3D pos) {
		// format: (create, localId, x,y,z)
		try {
			String message = new String("create," + id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			;
			// System.out.println(message);
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendNPCCreateMessage(Vector3D pos) {
		// format: (create, localId, x,y,z)
		try {
			String message = new String("createNPC,");
			message += pos.getX() + "," + pos.getY() + "," + pos.getZ();
			;
			// System.out.println(message);
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendJoinMessage() {
		// format: join, localId
		try {
			sendPacket(new String("join," + id.toString()));
			System.out.println("joined");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendByeMessage() {
		// format: bye, localId
		try {
			sendPacket(new String("bye," + id.toString()));
			System.out.println("bye");
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	public void sendDetailsForMessage(UUID remId, Vector3D pos) {
		// format: (dsfr, remId, localID, x,y,z)
		try {
			String message = new String("dsfr," + remId.toString() + ","
					+ id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			sendPacket(message);
			// System.out.println(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/*
	 * public void sendDetailsForMessage(UUID remId, Vector3D pos){ // format:
	 * (dsfr, remId, localID, x,y,z)dd
	 * 
	 * try{ String message = new String("dsfr," + remId.toString()+"," +
	 * id.toString()); message += "," + pos.getX()+"," + pos.getY() + "," +
	 * pos.getZ(); sendPacket(message); //System.out.println(message); } catch
	 * (IOException e) { e.printStackTrace(); } }
	 */
	public void sendMoveMessage(Vector3D pos) {
		// format: (move, localId, x,y,z)
		try {
			String message = new String("move," + id.toString());
			message += "," + pos.getX() + "," + pos.getY() + "," + pos.getZ();
			//System.out.println(message);
			sendPacket(message);
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	private void removeGhostAvatar(UUID ghostID) {
		for (GhostAvatar avatar : ghostAvatars) {
			if (avatar.getGhostID() == ghostID) {
				ghostAvatars.remove(avatar);
				game.removeNode(avatar);
			}
		}
	}

	private void createGhostAvatar(UUID ghostID, Vector3D ghostPosition,
			MyClient client) {
		Vector3D pos = new Vector3D(70.0, 0, 50.0);
		ghostAvatars.add(new GhostAvatar(ghostID, pos, client));

		System.out.println(ghostAvatars.lastElement());
		game.addNode(ghostAvatars.lastElement());
	}

	private void updateGhostAvatar(UUID ghostID, Vector3D pos) {
		for (GhostAvatar avatar : ghostAvatars) {
			//System.out.println(" Ghost: " + avatar.getGhostID());
			if (avatar.getGhostID().equals(ghostID)) {
            System.out.println(" YES ");
				avatar.setGhostPosition(pos);
			}
		}
	}

	public Vector<GhostAvatar> getAvatars() {
		return ghostAvatars;
	}
	
	/*public Vector<GhostNPC> returnNPCs()
	{
		return ghostNPCs;
	}*/
	private void createGhostNPC(Vector3D position)
	{
		newNPC = new GhostNPC(position, game.getPlayerPosition());
		
		System.out.println("NPC being called");
		if (newNPC != null)
		{
         game.addNPC(newNPC);
			System.out.println("NPC does exist!");
		}
	}
	private void updateGhostNPC(Vector3D pos)
	{
		//System.out.println("the npc pos is " + pos);
      newNPC.setPosition(pos); //error 
	}
	public void askForNPCinfo()
	{
		try 
		{
			sendPacket(new String("needNPC," + id.toString()));
		}
		 catch (IOException p)
		{
			 p.printStackTrace();
		}
	}
	
}