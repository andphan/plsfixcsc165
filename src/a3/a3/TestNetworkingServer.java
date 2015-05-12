package a3.a3;

import a3.kmap165Engine.network.*;
import a3.kmap165Engine.npc.NPCcontroller;

import java.io.IOException;

public class TestNetworkingServer {
	
	static private NPCcontroller npcCtrl;
	static private long startTime;
	static private long lastUpdateTime;
   static private GameServerTCP TCPServer;
	public TestNetworkingServer(int id) throws IOException
	{
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		npcCtrl = new NPCcontroller();
		//GameServerTCP testTCPServer = new GameServerTCP(8080);
		TCPServer = new GameServerTCP(8065);
		npcCtrl.setupNPC();
		npcLoop();
		
		System.out.println("test networking server called");
	}
	
	public static void main(String[] args) throws IOException {
		try {
			startTime = System.nanoTime();
			lastUpdateTime = startTime;
			npcCtrl = new NPCcontroller();
			//GameServerTCP testTCPServer = new GameServerTCP(8080);
			TCPServer = new GameServerTCP(8065);
			npcCtrl.setupNPC();
//			npcLoop();			
			System.out.println("test networking server called");

		 TCPServer = new GameServerTCP(8065);
		
		} catch (IOException e) {
			e.printStackTrace();
		}
	}
	
	
	public static void npcLoop()
	{
		while (true)
		{
			long frameStartTime = System.nanoTime();
			float elapMilSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);
			if (elapMilSecs >= 50.0f)
			{
				lastUpdateTime = frameStartTime;
				npcCtrl.updateNPCs();
				TCPServer.sendNPCinfo();
				
			}
			Thread.yield();
		}
	}
	
	
}