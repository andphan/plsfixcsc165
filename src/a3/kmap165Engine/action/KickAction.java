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

import sage.audio.*;
import com.jogamp.openal.ALFactory;

public class KickAction extends AbstractInputAction {
	private Model3DTriMesh s;
	private Matrix3D sM;
	private MyClient client;
   private Sound kickSwooshSound;
	private TerrainBlock terrain;

	public KickAction(Model3DTriMesh n, MyClient thisClient, Sound theKickSwooshSound) {
		s = n;
		client = thisClient;
      kickSwooshSound = theKickSwooshSound;
	}

	public void performAction(float time, Event e) {
		s.stopAnimation();
      kickSwooshSound.play();
		s.startAnimation("Kick_Animation");
      //s.stopAnimation();
		System.out.println("Kick");

		// s.updateWorldBound();
		// client.sendMoveMessage(s.getLocalTranslation().getCol(3));
		//s.startAnimation("Idle_Stance_Updated");
	}
}