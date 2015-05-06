package a3.a3;

import a3.kmap165Engine.network.*;
import a3.kmap165Engine.npc.NPCcontroller;

import java.io.IOException;

public class TestNetworkingServer {
	
	private NPCcontroller npcCtrl;
	private long startTime;
	private long lastUpdateTime;
	private GameServerTCP testTCPServer;
	
	public TestNetworkingServer(int id) throws IOException
	{
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		npcCtrl = new NPCcontroller();
		
		testTCPServer = new GameServerTCP(8065);
		
		npcCtrl.setupNPC();
		npcLoop();
	}
	
	public void npcLoop()
	{
		while (true)
		{
			long frameStartTime = System.nanoTime();
			float elapMilSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			if (elapMilSecs >= 50.0f)
			{
				lastUpdateTime = frameStartTime;
				npcCtrl.updateNPCs();
				testTCPServer.sendNPCinfo();
			}
			
			Thread.yield();
		}
	}
	
	public static void main(String[] args) throws IOException {
		try {
			GameServerTCP testTCPServer = new GameServerTCP(8065);

		} catch (IOException e) {
			e.printStackTrace();
		}
	}
}