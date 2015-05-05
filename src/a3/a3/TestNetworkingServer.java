package a3.a3;

import a3.kmap165Engine.network.*;
import a3.kmap165Engine.npc.NPCcontroller;

import java.io.IOException;

public class TestNetworkingServer {
	
	private NPCcontroller npcCtrl;
	private long startTime;
	private long lastUpdateTime;
	
	public TestNetworkingServer(int id)
	{
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		npcCtrl = new NPCcontroller();
		
	}
	
	public static void main(String[] args) throws IOException {
		try {
			GameServerTCP testTCPServer = new GameServerTCP(8065);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}