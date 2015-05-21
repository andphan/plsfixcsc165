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
	private GhostNPC npc;
	private boolean nearFlag;
	//private GhostNPC[] NPClist = new GhostNPC[1];
	private int numNPCs = 1;
   private Vector3D playerSpot;
   //private Vector<GhostNPC> ghostNPCs;
   
   public NPCcontroller(GameServerTCP serveThis){
      server = serveThis;
      nearFlag = false;
   }
   
	public void startNPCControl() {
		bt = new BehaviorTree(BTCompositeType.SELECTOR);
		startTime = System.nanoTime();
		lastUpdateTime = startTime;
		setupNPC();
      
		setupBehaviorTree();
		npcLoop();
	}
   public void setPlayerLocation(Vector3D playerLocation){
      this.playerSpot = playerLocation;
   }
	public void setupNPC() {
		npc = new GhostNPC(new Vector3D(70.0,2.0,50.0), playerSpot);
      //NPClist[0] = npc;
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
		//for (int i = 0; i < numNPCs; i++)
		//{
			//NPClist[i].updateLocation();
         npc.updateLocation();
         npc.setPlayerPosition(playerSpot);
         server.sendNPCinfo();
         //if(NPClist[i].isClose()) setNearFlag(true);
         //else setNearFlag(false);
		//}
	}
	public void setupBehaviorTree() {
	   bt.insertAtRoot(new BTSequence(10));
		bt.insertAtRoot(new BTSequence(20));
      bt.insertAtRoot(new BTSequence(30));
		bt.insert(10, new OneSecPassed(this, npc, false));
		bt.insert(10, new MopeAround(npc));
		bt.insert(20, new AvatarNear(server, this, npc, false));
		bt.insert(20, new AttackAvatar(npc));
      bt.insert(30, new AvatarFar(server, this, npc, false));
	   bt.insert(30, new ApproachAvatar(npc));
	}
   public void setPlayerSpot(Vector3D pos){
      playerSpot = pos;
   }
	public boolean getNearFlag() {
		return nearFlag;
	}
   public void setNearFlag(boolean nearFlag) {
		this.nearFlag = nearFlag;
	}
	public GhostNPC getNPC() {
		//GhostNPC nz = new GhostNPC();
		/*for (int x = 0; x <= NPClist.length; x++)
		{
         //System.out.println(NPClist[0]);
         if(x == i) return NPClist[x];
			//NPClist[x] = nz;
			//i++;
		}
		return null;*/
      return npc;
	}
	public int getNumOfNPCs() {
		// TODO Auto-generated method stub
		return numNPCs;
	}
}
