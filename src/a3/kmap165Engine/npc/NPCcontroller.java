package a3.kmap165Engine.npc;

import java.util.Random;

import a3.kmap165Engine.network.GameServerTCP;
import sage.ai.behaviortrees.BTCompositeType;
import sage.ai.behaviortrees.BTSequence;
import sage.ai.behaviortrees.BehaviorTree;

public class NPCcontroller {

	BehaviorTree bt = new BehaviorTree(BTCompositeType.SELECTOR);
	
	//
	private NPC npc;
	private long startTime;
	private long lastUpdateTime;
	private Random rn = new Random();
	private GameServerTCP server;
	
	public void startNPCcontrol()
	{
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		setupBehaviorTree();
		npcLoop();
	}
	
	public void setupNPC()
	{
		npc = new NPC();
		npc.randomizeLocation(rn.nextInt(100), rn.nextInt(100));
	}
	public void npcLoop()
	{
		while (true)
		{
			long frameStartTime = System.nanoTime();
			float elapsedMiliSecs = (frameStartTime-lastUpdateTime)/(1000000.0f);
			
			if (elapsedMiliSecs >= 50.0f)
			{
				lastUpdateTime = frameStartTime;
				npc.updateLocation();
				server.sendNPCinfo();
				bt.update(elapsedMiliSecs);
			}
			Thread.yield();
		}
	}
	public void setupBehaviorTree()
	{
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
		bt.insert(10, new OneSecPassed(this, npc, false));
		bt.insert(10, new GetSmall(npc));
		bt.insert(20, new AvatarNear(server, this, npc, false));
		bt.insert(20, new ThrowPowerUps(npc));
		
	}

	public void setNearFlag(boolean b) {
		// TODO Auto-generated method stub
		
	}

	public boolean getNearFlag() {
		// TODO Auto-generated method stub
		return false;
	}

}
