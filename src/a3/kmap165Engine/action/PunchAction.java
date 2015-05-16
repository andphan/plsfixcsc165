package a3.kmap165Engine.action;

import sage.input.action.AbstractInputAction;
import sage.app.AbstractGame;
import net.java.games.input.Event;
import sage.camera.*;
import graphicslib3D.Point3D;
import graphicslib3D.Vector3D;
import graphicslib3D.Matrix3D;
import sage.scene.SceneNode;
import sage.scene.shape.*;
import sage.terrain.TerrainBlock;
import a3.kmap165Engine.network.*;
import sage.scene.Model3DTriMesh;
import a3.games.fighter2015.*;

import sage.audio.*;
import com.jogamp.openal.ALFactory;

public class PunchAction extends AbstractInputAction {
	private Model3DTriMesh s;
	private Matrix3D sM;
	private MyClient client;
	private TerrainBlock terrain;
   private Sound punchSwooshSound;
   private FightingGame myGame;

	public PunchAction(Model3DTriMesh n, MyClient thisClient, FightingGame game, Sound thePunchSwooshSound) {
		s = n;
		sM = s.getLocalTranslation();
		client = thisClient;
      myGame = game;
      punchSwooshSound = thePunchSwooshSound;
	}

	public void performAction(float time, Event e) {
		myGame.setPunching(true);
		s.stopAnimation();
      punchSwooshSound.play();
		s.startAnimation("Punch_Animation");
      //s.stopAnimation();
		System.out.println("Punch");
      
		// s.updateWorldBound();
		// client.sendMoveMessage(s.getLocalTranslation().getCol(3));
		//s.startAnimation("Idle_Stance_Updated");
	}
   
}