package a3.kmap165Engine.players;

import graphicslib3D.Matrix3D;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.scene.Group;
import sage.scene.Model3DTriMesh;
import sage.scene.SceneNode;
import sage.scene.state.RenderState;
import sage.scene.state.TextureState;
import sage.texture.Texture;
import sage.texture.TextureManager;
import a3.games.fighter2015.FightingGame;

public class PlayerOne extends Model3DTriMesh {
	
	private int health =  100;
	private int score = 0;
	private boolean isPunching;
	private boolean isKicking;
	private boolean isBlocking;
	private boolean isIdle;
	private boolean isMoving;
	private FightingGame mg;
	private Group model;
	private OgreXMLParser loader = new OgreXMLParser();
	private Model3DTriMesh playerOne;
	private TextureState playerOneTextureState;
	
	
	public PlayerOne(FightingGame g)
	{
		
		mg = g;
		health = 100;
		score = 0;
		isPunching = false;
		isKicking = false;
		isBlocking = false;
		isIdle = false;
		isMoving = false;
	/*	
		try {
			model = loader.loadModel(
							"src/a3/kmap165Engine/external_models/avatar/Cube.001.mesh.xml",
							"src/a3/kmap165Engine/external_models/avatar/backup/materialMesh.material",
							"src/a3/kmap165Engine/external_models/avatar/Cube.001.skeleton.xml");
			model.updateGeometricState(0, true);
			java.util.Iterator<SceneNode> modelIterator = model.iterator();
			playerOne = (Model3DTriMesh) modelIterator.next();
         
		} catch (Exception eea) {
			eea.printStackTrace();
			System.exit(1);
		}
	*/

	}


	public int getHealth() {
		return health;
	}


	public void setHealth(int health) {
		this.health = health;
	}


	public int getScore() {
		return score;
	}


	public void setScore(int score) {
		this.score = score;
	}


	public boolean isPunching() {
		return isPunching;
	}


	public void setPunching(boolean isPunching) {
		this.isPunching = isPunching;
	}


	public boolean isKicking() {
		return isKicking;
	}


	public void setKicking(boolean isKicking) {
		this.isKicking = isKicking;
	}


	public boolean isBlocking() {
		return isBlocking;
	}


	public void setBlocking(boolean isBlocking) {
		this.isBlocking = isBlocking;
	}


	public boolean isIdle() {
		return isIdle;
	}


	public void setIdle(boolean isIdle) {
		this.isIdle = isIdle;
	}


	public boolean isMoving() {
		return isMoving;
	}


	public void setMoving(boolean isMoving) {
		this.isMoving = isMoving;
	}


	public Model3DTriMesh getPlayerOne() {
		return playerOne;
	}


	public void setPlayerOne(Model3DTriMesh playerOne) {
		this.playerOne = playerOne;
	}
	
}
