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
	private NPC[] NPClist = new NPC[3];
	private int numNPCs = 3;
	private boolean nearFlag;
	
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
	
	public void updateNPCs()
	{
		for (int i = 0; i < numNPCs; i++)
		{
			NPClist[i].updateLocation();
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
		nearFlag = b;
		
	}

	public boolean getNearFlag() {
		// TODO Auto-generated method stub
		return nearFlag;
	}

	public NPC getNPC(int i) {
		NPC nz = new NPC();
		for (int x = 0; x < NPClist.length; x++)
		{
			NPClist[x] = nz;
			i++;
		}
		return NPClist[i];
	}

	public int getNumOfNPCs() {
		// TODO Auto-generated method stub
		return numNPCs;
	}

}
