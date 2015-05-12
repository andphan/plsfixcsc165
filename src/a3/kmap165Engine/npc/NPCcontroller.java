package a3.kmap165Engine.npc;

import graphicslib3D.Point3D;

import graphicslib3D.Vector3D;

import java.util.Random;

import sage.ai.behaviortrees.*;
import a3.kmap165Engine.network.*;
import a3.kmap165Engine.network.ghost_avatar.*;

public class NPCcontroller {

	private Random rn;
	private BehaviorTree bt;
	private long lastUpdateTime;
	private long startTime;
	private GameServerTCP server;
	private NPC npc;
	private GhostNPC npC;
	private boolean nearFlag;
	private GhostNPC[] NPClist = new GhostNPC[1];
	private int numNPCs = 1;
   //private Vector<GhostNPC> ghostNPCs;
   
   public NPCcontroller(){
   }
   
	public void startNPCControl() {
		bt = new BehaviorTree(BTCompositeType.SELECTOR);
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
		setupBehaviorTree();
		npcLoop();
	}

	public void setupNPC() {
		npC = new GhostNPC(1, new Vector3D(50,1,80));
      NPClist[0] = npC;
		/*try {
		Point3D newPoint = new Point3D(50, 0, 80);
		npc.randomizeLocation(newPoint.getX(), newPoint.getZ());
		}
		catch (Exception e)
		{
			e.printStackTrace();
		}*/
	}

	public void npcLoop() {
		while (true) {
			long frameStartTime = System.nanoTime();
			float elapsedMiliSecs = (frameStartTime - lastUpdateTime) / (1000000.0f);

			if (elapsedMiliSecs >= 50.0f) {
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
	public void setupBehaviorTree() {
		bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
		bt.insert(10, new OneSecPassed(this, npc, false));
		bt.insert(10, new MopeAround(npc));
		bt.insert(20, new AvatarNear(server, this, npc, false));
		bt.insert(20, new AttackAvatar(npc));
	}

	public boolean getNearFlag() {
		// TODO Auto-generated method stub
		return nearFlag;
	}

	public void setNearFlag(boolean b) {
		nearFlag = b; // test
	}
	public GhostNPC getNPC(int i) {
		//GhostNPC nz = new GhostNPC();
		for (int x = 0; x < NPClist.length; x++)
		{
         if(x == i) return NPClist[x];
			//NPClist[x] = nz;
			//i++;
		}
		return NPClist[i];
	}
	public int getNumOfNPCs() {
		// TODO Auto-generated method stub
		return numNPCs;
	}
}
