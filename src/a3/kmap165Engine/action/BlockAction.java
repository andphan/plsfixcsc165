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
import a3.games.fighter2015.FightingGame;
import a3.kmap165Engine.network.*;
import sage.scene.Model3DTriMesh;
import sage.audio.*;

import com.jogamp.openal.ALFactory;

public class BlockAction extends AbstractInputAction {
	private Model3DTriMesh s;
	private Matrix3D sM;
	private MyClient client;
	private TerrainBlock terrain;
	private FightingGame gg;
	public BlockAction(Model3DTriMesh n, MyClient thisClient, FightingGame g) {
		s = n;
		sM = s.getLocalTranslation();
		client = thisClient;
		gg = g;
	}

	public void performAction(float time, Event e) {
		gg.setBlocking(true);
		s.stopAnimation();
		s.startAnimation("Block_Animation");
		System.out.println("Block");

		s.updateWorldBound();
		// client.sendMoveMessage(s.getLocalTranslation().getCol(3));
		//s.startAnimation("Idle_Stance_Updated");
	}
}