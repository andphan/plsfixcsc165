package a3.games.fighter2015;

import sage.scene.Controller;
import a3.kmap165Engine.action.*;
import a3.kmap165Engine.camera.*;
import a3.kmap165Engine.custom_objects.*;
import a3.kmap165Engine.custom_objects.event_listener_objects.*;
import a3.kmap165Engine.display.*;
import a3.kmap165Engine.event.*;
import a3.kmap165Engine.network.*;
import a3.kmap165Engine.network.ghost_avatar.*;
import a3.kmap165Engine.players.PlayerOne;
import a3.kmap165Engine.scene_node_controller.*;
import sage.app.BaseGame;
import sage.physics.IPhysicsEngine;
import sage.physics.IPhysicsObject;
import sage.physics.PhysicsEngineFactory;
import sage.renderer.*;
import sage.scene.Group;
import sage.display.*;
import sage.camera.*;
import sage.input.*;
import sage.scene.SceneNode;
import sage.scene.shape.*;
import sage.scene.HUDString;
import sage.scene.Model3DTriMesh;
import sage.scene.TriMesh;
import net.java.games.input.*;
import sage.input.action.AbstractInputAction;
import sage.input.action.IAction;
import graphicslib3D.Point3D;
import graphicslib3D.Matrix3D;
import graphicslib3D.Vector3D;

import java.awt.event.*;
import java.util.List;
import java.util.Random;
import java.awt.Color;
import java.text.DecimalFormat;

import sage.scene.shape.Line;
import sage.scene.shape.Cube;
import sage.scene.shape.Cylinder;
import sage.scene.shape.Pyramid;
import sage.scene.shape.Teapot;
import sage.scene.shape.Sphere;
import sage.scene.shape.Cube;
import sage.scene.shape.Rectangle;
import sage.scene.state.RenderState;
import sage.scene.state.TextureState;

import java.nio.*;
import java.util.ArrayList;
import java.awt.image.BufferedImage;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.net.UnknownHostException;
import java.net.URL;

import sage.model.loader.OBJLoader;
import sage.model.loader.OBJMaterial;
import sage.model.loader.ogreXML.OgreXMLParser;
import sage.networking.IGameConnection.ProtocolType;

import java.net.InetAddress;

import javax.imageio.ImageIO;
import javax.script.Invocable;
import javax.script.ScriptEngine;
import javax.script.ScriptEngineFactory;
import javax.script.ScriptEngineManager;
import javax.script.ScriptException;

import a3.kmap165Engine.action.BackwardAction;
import a3.kmap165Engine.action.ForwardAction;
import a3.kmap165Engine.action.LeftAction;
import a3.kmap165Engine.action.QuitAction;
import a3.kmap165Engine.action.RightAction;
import a3.kmap165Engine.camera.Camera3Pcontroller;
import a3.kmap165Engine.custom_objects.event_listener_objects.TheChest;
import a3.kmap165Engine.event.CrashEvent;
import a3.kmap165Engine.scene_node_controller.ClockwiseRotationController;
import sage.camera.ICamera;
import sage.display.IDisplaySystem;
import sage.scene.SkyBox;
import sage.terrain.*;
import sage.texture.Texture;
import sage.texture.TextureManager;
import sage.model.loader.ogreXML.*;
import sage.scene.Model3DTriMesh;
//import com.jme.scene.state.TextureState;
import sage.event.*;
import sage.animation.Joint;
import sage.audio.*;

import com.jogamp.openal.ALFactory;

public class FightingGame extends BaseGame implements KeyListener {
	private Camera3Pcontroller c0c, c1c, c2c;
	// adding Scripting stuff
	private ScriptEngine engine;
	private String sName = "src/a3/games/fighter2015/TestScriptColor.js", gpName, Keyboard, mouseName;
	private File scriptFile;

	// test

	private IRenderer renderer;
	private int score1 = 0, score2 = 0, numCrashes = 0;
	private float time1 = 0, time2 = 0;
	private HUDString player1ScoreString, player1TimeString, player1HealthString,
			player2ScoreString, player2TimeString;
	private boolean isOver = false, inFSEM = false;
	private IDisplaySystem fullDisplay, display;
	private Point3D origin;
	private Random rng;
	private ICamera camera1, camera2;
	private IInputManager im;
	private IEventManager eventMgr;
	private Cylinder cyl;
	private Teapot tpt;
	private Sphere sph;
	private TriMesh fightingRingTriMesh;
	private TextureState playerOneTextureState;
	private Group model;
	private Model3DTriMesh playerOne;

	private Cube p2;
	private MyDiamond jade;
	private TheChest chest;
	private boolean collidedWTeapot = false, collidedWPyramid = false,
			collidedWCylinder = false, collidedWDiamond = false,
			isConnected = false, firstRun = true;

	private Texture tf;
	private Group scene;
	private final String dir = "." + File.separator + "a3" + File.separator
			+ "images" + File.separator, serverAddress;
	private final int serverPort;
	private final ProtocolType serverProtocol;
	private MyClient thisClient;

	private TerrainBlock parkingLot1, parkingLot2, parkingLot3, parkingLot4;
	private TerrainBlock hillTerr;

	private SceneNode lineNodes;
	private SkyBox skybox;
	// physics
	private boolean running;
	private IPhysicsEngine physicsEngine;
	private IPhysicsObject powerUpP, terrainP;
	private Sphere powerUp;
	private OBJLoader objectLoader;

	private Model3DTriMesh myObject;
   private IAudioManager audioMgr;
   private Sound punchSwooshSound, punchHitSound, kickSwooshSound, kickHitSound, crowdSound;
   private ArrayList<SceneNode> gameWorldX = new ArrayList<SceneNode>(); 
    
   // creating flags
    private boolean p1isPunching, p1isKicking, p1isBlocking, p2isPunching, p2isKicking, p2isBlocking, p1LosesTrade, p2LosesTrade,
    p1WinsTrade, p2WinsTrade, p1isNeutral, p2isNeutral; 
    private boolean isIdle, isActive;
    
    private boolean isMoving, isPunching, isKicking, isBlocking, isPunched, isKicked, isKnockedOut, startAnimProcess;
    
    private PlayerOne p1Data;
    
    // trying out NPCs
    private GhostNPC npc;
    
//    private PlayerOne playerOne;
    
    /*
     * PUNCHING
     * if p1 throws a punch and p2 is not doing anything then p1 hits
     * 		p1ispunching = true;
     * 		
     * if p1 throws a punch and p2 is pressing punch == parry? 
     * if p1 throws a punch and p2 is pressing kick = p2 kick wins
     * if p1 throws a punch and p2 is pressing block = bounce back

     *  KICKING
     * if p1 throws a kick and p2 is not doing anything then p1 hits
     * if p1 throws a kick and p2 is pressing punch == kick wins
     * if p1 throws a kick and p2 is pressing kick = parry or mini game
     * if p1 throws a kick and p2 is pressing block = bounce back no damage
     
     
     * BLOCKING
     * if p1 press block and p2 is not doing anything then nothing happens
     * if p1 press block and p2 is pressing punch == block wins = no damage
     * if p1 press block and p2 is pressing kick = bounce back but no damage
     * if p1 press block and p2 is pressing block = put on ippo music
     * 

 
     */
   
   
	public FightingGame(String serverAddr, int sPort) throws IOException {
		super();
		this.serverAddress = serverAddr;
		this.serverPort = sPort;
		this.serverProtocol = ProtocolType.TCP;
	}

	protected void initGame() {
		getDisplaySystem().setTitle("Fighting Game");
		renderer = getDisplaySystem().getRenderer();
		im = getInputManager();
		eventMgr = EventManager.getInstance();
		// adding scripting stuff
		ScriptEngineManager factory = new ScriptEngineManager();
		List<ScriptEngineFactory> list = factory.getEngineFactories();
		engine = factory.getEngineByName("js");
		scriptFile = new File(sName);
		this.runScript();
		objectLoader = new OBJLoader();

		initGameObjects();
		initTerrain();
		createPlayers();
		initPhysicsSystem();
		createSagePhysicsWorld();
      
		if(firstRun){
         
         initInputA();
         initAudio(); // error
      }
		startAnimProcess = false;
		setIdle(true);
		try {
			thisClient = new MyClient(InetAddress.getByName(serverAddress),
					serverPort, serverProtocol, this);
		} catch (UnknownHostException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		if (thisClient != null) {
			thisClient.sendJoinMessage();
		}
      if(firstRun){
         firstRun = false;
         initInputB();
      }
		// playerOne.startAnimation("Idle_Stance");
      isBlocking = true;
	}

	public MyClient getClient() {
		return thisClient;
	}

	private void initInputA() {

		gpName = im.getFirstGamepadName();
		Keyboard = im.getKeyboardName();
		mouseName = im.getMouseName();

		// physicsTest
		IAction startAction = new StartAction();
		im.associateAction(Keyboard, Component.Identifier.Key.Q, startAction,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		// scriptTestInput
		UpdatePlayerColor updateColor = new UpdatePlayerColor();
		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.P,
				updateColor, IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		c1c = new Camera3Pcontroller(camera1, playerOne, im, mouseName, gpName);
   }
   private void initInputB() {
		// c2c = new Camera3Pcontroller(camera2,p2,im,mouseName);
		// c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
		// Controls for P1
		ForwardAction mvForward = new ForwardAction(playerOne, hillTerr,
				thisClient, this);
		// c2c = new Camera3Pcontroller(camera2,p2,im,mouseName);
		// c2c = new Camera3Pcontroller(camera2,p2,im,gpName);
		// Controls for P1

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.W, mvForward,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		BackwardAction mvBackward = new BackwardAction(playerOne, hillTerr,
				thisClient, this);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.S, mvBackward,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		LeftAction mvLeft = new LeftAction(playerOne, hillTerr, thisClient, this);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.A, mvLeft,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		RightAction mvRight = new RightAction(playerOne, hillTerr, thisClient, this);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.D, mvRight,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);

		KickAction kick = new KickAction(playerOne, thisClient, kickSwooshSound, this);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.K, kick,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		PunchAction punch = new PunchAction(playerOne, thisClient, this, punchSwooshSound);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.I, punch,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		BlockAction block = new BlockAction(playerOne, thisClient, this);

		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.SPACE, block,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            
      SwitchAction switchNow = new SwitchAction(this);
      
		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.F11, switchNow,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
            
      // Quit Action
		QuitAction stop = new QuitAction(this);
		im.associateAction(Keyboard,
				net.java.games.input.Component.Identifier.Key.ESCAPE, stop,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		// Controls for P2
		
		 X_Action_Controller xControl = new X_Action_Controller(playerOne, hillTerr, thisClient, this);
       
		  im.associateAction(gpName,
		  net.java.games.input.Component.Identifier.Axis.X, xControl,
		 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		  
		  Z_Action_Controller zControl = new Z_Action_Controller(playerOne, hillTerr, thisClient, this);
		  im.associateAction(gpName,
		  net.java.games.input.Component.Identifier.Axis.Y, zControl,
		  IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN); 
        
        /*ForwardAction mvForward2 = new ForwardAction(playerOne, hillTerr, thisClient, this); 
        im.associateAction(Keyboard,
		  net.java.games.input.Component.Identifier.Key.K, mvForward2,
		  IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		  
		 BackwardAction mvBackward2 = new BackwardAction(playerOne, hillTerr, thisClient, this);
		  im.associateAction(Keyboard,
		  net.java.games.input.Component.Identifier.Key.I, mvBackward2,
		  IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		  
		 LeftAction mvLeft2 = new LeftAction(playerOne, hillTerr, thisClient, this); 
       im.associateAction(Keyboard,
		  net.java.games.input.Component.Identifier.Key.J, mvLeft2,
		  IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
		  
		  RightAction mvRight2 = new RightAction(playerOne, hillTerr, thisClient, this);
        
		  im.associateAction(Keyboard,
		  net.java.games.input.Component.Identifier.Key.L, mvRight2,
		 IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);*/
		
      //KickAction kick2 = new KickAction(playerOne, thisClient, kickSwooshSound, this);

		im.associateAction(gpName,
				net.java.games.input.Component.Identifier.Button._1, kick,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		//PunchAction punch = new PunchAction(playerOne, thisClient, this, punchSwooshSound);

		im.associateAction(gpName,
				net.java.games.input.Component.Identifier.Button._2, punch,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);

		//BlockAction block = new BlockAction(playerOne, thisClient, this);

		im.associateAction(gpName,
				net.java.games.input.Component.Identifier.Button._3, block,
				IInputManager.INPUT_ACTION_TYPE.REPEAT_WHILE_DOWN);
            
      //SwitchAction switchNow = new SwitchAction(this);
      
		im.associateAction(gpName,
				net.java.games.input.Component.Identifier.Button._4, switchNow,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
      
      im.associateAction(gpName,
				net.java.games.input.Component.Identifier.Button._5, stop,
				IInputManager.INPUT_ACTION_TYPE.ON_PRESS_ONLY);
		
	}

   private void initAudio(){
      AudioResource punchResource1, punchResource2, kickResource1, kickResource2, blockResource, koResource, 
         bellResource, crowdResource, cheerResource;
      audioMgr = AudioManagerFactory.createAudioManager("sage.audio.joal.JOALAudioManager");
      if(!audioMgr.initialize()){  // error
         System.out.println("Audio Manager failed to initialize!");
         return;
      }
      //setup for punchResource1
      punchResource1 = audioMgr.createAudioResource("src/a3/kmap165Engine/sound/punch/punch_init.mp3", AudioResourceType.AUDIO_SAMPLE);
      //System.out.println(punchResource1);
      punchSwooshSound = new Sound(punchResource1, SoundType.SOUND_EFFECT, 50, false);
      //System.out.println(punchSwooshSound);
      punchSwooshSound.initialize(audioMgr);
      //System.out.println(punchSwooshSound);
      //punchSwooshSound.setMaxDistance(null);

      /*punchSwooshSound.setMaxDistance(10.0f);
      punchSwooshSound.setMinDistance(0.0f);
      punchSwooshSound.setRollOff(5.0f);
      punchSwooshSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));*/
      
      //setup for punchResource2
      punchResource2 = audioMgr.createAudioResource("src/a3/kmap165Engine/sound/punch/punch_hit.wav", AudioResourceType.AUDIO_SAMPLE);
      punchHitSound = new Sound(punchResource2, SoundType.SOUND_EFFECT, 70, false);
      punchHitSound.initialize(audioMgr);

      /*punchHitSound.setMaxDistance(10.0f);
      punchHitSound.setMinDistance(0.0f);
      punchHitSound.setRollOff(1.0f);
      punchHitSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));*/
      
      //setup for kickResource1
      kickResource1 = audioMgr.createAudioResource("src/a3/kmap165Engine/sound/kick/kick_init.wav", AudioResourceType.AUDIO_SAMPLE);
      kickSwooshSound = new Sound(kickResource1, SoundType.SOUND_EFFECT, 50, false);
      kickSwooshSound.initialize(audioMgr);

      /*kickSwooshSound.setMaxDistance(10.0f);
      kickSwooshSound.setMinDistance(0.0f);
      kickSwooshSound.setRollOff(5.0f);
      kickSwooshSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));*/
      
      //setup for kickResource2
      kickResource2 = audioMgr.createAudioResource("src/a3/kmap165Engine/sound/kick/kick_hit.wav", AudioResourceType.AUDIO_SAMPLE);
      kickHitSound = new Sound(kickResource2, SoundType.SOUND_EFFECT, 70, false);
      kickHitSound.initialize(audioMgr);

      /*kickHitSound.setMaxDistance(10.0f);
      kickHitSound.setMinDistance(0.0f);
      kickHitSound.setRollOff(1.0f);
      kickHitSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));*/
      
      //setup for crowdResource
      crowdResource = audioMgr.createAudioResource("src/a3/kmap165Engine/sound/background/ambience/sports_crowd.wav", AudioResourceType.AUDIO_SAMPLE);
      crowdSound = new Sound(crowdResource, SoundType.SOUND_EFFECT, 50, true);
      crowdSound.initialize(audioMgr);

      /*crowdSound.setMaxDistance(50.0f);
      crowdSound.setMinDistance(5.0f);
      crowdSound.setRollOff(3.0f);
      crowdSound.setLocation(new Point3D(chest.getWorldTranslation().getCol(3)));*/
      
      setEarParameters(); // okay
      crowdSound.play();
   }
   public void setEarParameters(){
      Matrix3D avDir = (Matrix3D) (playerOne.getWorldRotation().clone());
      float camAz = c1c.getAzimuth();
      avDir.rotateY(180.0f-camAz);
      Vector3D camDir = new Vector3D(0,0,1);
      camDir = camDir.mult(avDir);
      audioMgr.getEar().setLocation(camera1.getLocation());
      audioMgr.getEar().setOrientation(camDir, new Vector3D(0,1,0));
   }
	private void createScene() {
		scene = new Group("Root Node");

		skybox = new SkyBox("SkyBox", 100.0f, 100.0f, 100.0f);

		Texture northTex = TextureManager
				.loadTexture2D("src/a3/images/heightMapTest.JPG");
		Texture southTex = TextureManager
				.loadTexture2D("src/a3/images/heightMapTest.JPG");
		Texture eastTex = TextureManager
				.loadTexture2D("src/a3/images/lotTest.jpg");
		Texture westTex = TextureManager
				.loadTexture2D("src/a3/images/lotTest.jpg");
		Texture upTex = TextureManager.loadTexture2D("src/a3/images/clouds.jpg");
		Texture downTex = TextureManager
				.loadTexture2D("src/a3/images/lot_floor.jpg");
		Texture testTerr = TextureManager
				.loadTexture2D("src/a3/images/squaresquare.bmp");

		Texture testMountain = TextureManager
				.loadTexture2D("src/a3/images/mountains512.jpg");

		skybox.setTexture(SkyBox.Face.North, northTex);
		skybox.setTexture(SkyBox.Face.South, southTex);

		skybox.setTexture(SkyBox.Face.East, eastTex);
		skybox.setTexture(SkyBox.Face.West, westTex);

		skybox.setTexture(SkyBox.Face.Up, upTex);
		skybox.setTexture(SkyBox.Face.Down, downTex);

		scene.addChild(skybox);

		addGameWorldObject(scene, true);

		
		
		// testing out npc creation
		
	}

	public void initOgre() {
		OgreXMLParser loader = new OgreXMLParser();
		try {
			model = loader.loadModel(
							"src/a3/kmap165Engine/external_models/avatar/Cube.001.mesh.xml",
							"src/a3/kmap165Engine/external_models/avatar/backup/materialMesh.material",
							"src/a3/kmap165Engine/external_models/avatar/Cube.001.skeleton.xml");
			model.updateGeometricState(0, true);
			java.util.Iterator<SceneNode> modelIterator = model.iterator();
			playerOne = (Model3DTriMesh) modelIterator.next();

			Matrix3D playerOneT = playerOne.getLocalTranslation();
			playerOneT.translate(245, 10, 250);
			playerOne.setLocalTranslation(playerOneT);
         playerOne.setWorldTranslation(playerOneT);

			Matrix3D playerOneR = playerOne.getLocalRotation();
		//	playerOneR.rotateX(90.0);
		//	playerOneR.rotateY(180);
		//	playerOneR.rotateZ(180);
			playerOne.setLocalRotation(playerOneR);
         playerOne.setWorldRotation(playerOneR);
         
			Matrix3D playerOneS = playerOne.getLocalScale();
			playerOneS.scale(.75, 0.75, .75);
			playerOne.setLocalScale(playerOneS);
         playerOne.setWorldScale(playerOneS);
         
		} catch (Exception eea) {
			eea.printStackTrace();
			System.exit(1);
		}

		addGameWorldObject(playerOne, true);
		System.out.println("playerone : " + playerOne.getAnimations());

	}
	
   /*private void initJoints(){
      for (Joint joint : playerOne.getJoints()){ //initialize the matrix that moves the parent�s axes to this joint�s axes
         float [] initialRot = joint.getInitialRotation(); //data from model file
         float [] initialTrans = joint.getInitialTranslation() ; //data from model file
         Matrix3D mat = new Matrix3D();
         mat.translate(initialTrans[0], initialTrans [1], initialTrans [2]);
         mat.rotateZ(initialRot[2]); //rotate order is significant; this is the order
         mat.rotateY(initialRot[1]); // assumed by MS3D
         mat.rotateX(initialRot[0]);
         joint.setFromParentSpace(mat);
         //initialize the matrix that moves the world (model) axes all the way to this joint�s axes
         if (!joint.hasParentJoint()){ //there is no parent joint; the from-Model-Space transform is just the local transform
            Matrix3D mat1 = new Matrix3D(joint.getFromParentSpace().getValues());
            joint.setFromModelSpace(mat1);
         } 
         else{ //set this joint's from-model-space transform to be the concatenation of the parent's
               // from-model-space transform with this joint's from-parent-space transform
            Joint parentJoint = joint.getParent();
            Matrix3D mat2 = new Matrix3D(parentJoint.getFromModelSpace().getValues());
            mat2.concatenate(joint.getFromParentSpace());
            joint.setFromModelSpace(mat2);
         }
      }
   }*/
	private void createPlayers() {
		initOgre();
      //initJoints();
		
		 Texture playerOneTexture = TextureManager.loadTexture2D("src/a3/kmap165Engine/external_models/albertUV.png");
		 playerOneTexture.setApplyMode
		 (sage.texture.Texture.ApplyMode.Replace);
        
       playerOneTextureState =(TextureState)getDisplaySystem().getRenderer().createRenderState(RenderState.
		 RenderStateType.Texture);
		 playerOneTextureState.setTexture(playerOneTexture,0);
		 playerOneTextureState.setEnabled(true);
		 playerOne.setRenderState(playerOneTextureState);

		playerOne.updateRenderStates();

		playerOne.updateWorldBound();
		playerOne.updateLocalBound();
		playerOne.updateGeometricState(0, true);

		//addGameWorldObject(playerOne);

		
		camera1 = new JOGLCamera(renderer);
		camera1.setPerspectiveFrustum(60, 1, 1, 1000);
		camera1.setViewport(0.0, 1.0, 0.0, 1.0);
		p1Data = new PlayerOne(this);
		p1Data.setScore(0);
		p1Data.setHealth(100);
		score1 = p1Data.getScore();
		createPlayerHUDs();

	}

	private void createPlayerHUDs() {
		// Player 1 identity HUD
		HUDString player1ID = new HUDString("Player1");
		player1ID.setName("Player1ID");
		player1ID.setLocation(0.01, 0.12);
		player1ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player1ID.setColor(Color.red);
		player1ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera1.addToHUD(player1ID);
		
		// Player 1 Health HUD
		player1HealthString = new HUDString("Health = " + p1Data.getHealth());
		player1HealthString.setLocation(0.01, 0.09);
		player1HealthString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player1HealthString.setColor(Color.red);
		player1HealthString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera1.addToHUD(player1HealthString);
		// Player 1 time HUD
		player1TimeString = new HUDString("Time = " + time1);
		player1TimeString.setLocation(0.01, 0.06); // (0,0) [lower-left] to
													// (1,1)
		player1TimeString.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player1TimeString.setColor(Color.red);
		player1TimeString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera1.addToHUD(player1TimeString);
		// Player 1 score HUD
		player1ScoreString = new HUDString("Score = " + p1Data.getScore()); // default is
																	// (0,0)
		player1ScoreString.setLocation(0.01, 0.00);
		player1ScoreString
				.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		player1ScoreString.setColor(Color.red);
		player1ScoreString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		camera1.addToHUD(player1ScoreString);

		// Player 2 identity HUD
		/*
		 * HUDString player2ID = new HUDString("Player2");
		 * player2ID.setName("Player2ID"); player2ID.setLocation(0.01, 0.12);
		 * player2ID.setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		 * player2ID.setColor(Color.yellow);
		 * player2ID.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		 * camera2.addToHUD(player2ID); // Player 2 time HUD player2TimeString =
		 * new HUDString("Time = " + time2);
		 * player2TimeString.setLocation(0.01,0.06); // (0,0) [lower-left] to
		 * (1,1)
		 * player2TimeString.setRenderMode(sage.scene.SceneNode.RENDER_MODE
		 * .ORTHO); player2TimeString.setColor(Color.yellow);
		 * player2TimeString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		 * camera2.addToHUD(player2TimeString); // Player 2 score HUD
		 * player2ScoreString = new HUDString ("Score = " + score2); //default
		 * is (0,0) player2ScoreString.setLocation(0.01,0.00);
		 * player2ScoreString
		 * .setRenderMode(sage.scene.SceneNode.RENDER_MODE.ORTHO);
		 * player2ScoreString.setColor(Color.yellow);
		 * player2ScoreString.setCullMode(sage.scene.SceneNode.CULL_MODE.NEVER);
		 * camera2.addToHUD(player2ScoreString);
		 */
	}

	private void initGameObjects() {
		createScene();
		// configure game display
		/*
		 * display.setTitle(); //display.addKeyListener(this);
		 * 
		 * camera = new JOGLCamera(renderer); camera.setPerspectiveFrustum(60,
		 * 2, 1, 1000); camera.setLocation(new Point3D(0,0,30));
		 * camera.setViewport(0.0, 1.0, 0.0, 0.45);
		 */

		// origin = new Point3D();
		rng = new Random();

		lineNodes = (SceneNode) engine.get("lineNodes");
		addGameWorldObject(lineNodes, true);

		/*
		 * // add some lines Line xLine = new Line(origin, new Point3D(100,0,0),
		 * new Color(255,0,0), 1); addGameWorldObject(xLine);
		 * 
		 * Line yLine = new Line(origin, new Point3D(0,100,0), new
		 * Color(0,255,0), 1); addGameWorldObject(yLine);
		 * 
		 * Line zLine = new Line(origin, new Point3D(0,0,100), new
		 * Color(0,0,255), 1); addGameWorldObject(zLine);
		 */
		// add a rectangle, and turn it into a plane
		/*
		 * Rectangle plane = new Rectangle(1000, 1000); plane.rotate(90, new
		 * Vector3D(1,0,0)); plane.translate(0.0f,-2f,0.0f); plane.setColor(new
		 * Color(0,255,130)); addGameWorldObject(plane);
		 */

		// add cube that will grow bigger later

		// adding objectloader
		try {

			createRing();

			// needs to fix the export for this object. doesn't look like
			// objloader will take it.

			// fightingRingTriMesh =
			// loader.loadModel("src/a3/kmap165Engine/external_models/fightingRing.obj");

		} catch (Exception e11) {
			e11.printStackTrace();
		}

		chest = new TheChest(); // testing for sound
		Matrix3D chestM = chest.getLocalScale();
		chestM.scale(5.5f, 5.5f, 5.5f);
		Matrix3D chestT = chest.getLocalTranslation();
		chestT.translate(250, 0, 250);
		chest.setLocalTranslation(chestT);
		chest.setLocalScale(chestM);
		addGameWorldObject(chest, true);
		eventMgr.addListener(chest, CrashEvent.class);

		// create some treasure
		cyl = new Cylinder();
		Matrix3D cylM = cyl.getLocalTranslation();
		cylM.translate(200, 0, 250);
		cyl.setLocalTranslation(cylM);
		addGameWorldObject(cyl, true);
		cyl.updateWorldBound();

		// physics
		powerUp = new Sphere();
		Matrix3D puT = powerUp.getLocalTranslation();
		puT.translate(30, 50, 40);
		powerUp.setLocalTranslation(puT);
		addGameWorldObject(powerUp, true);
		powerUp.updateGeometricState(1.0f, true);

		sph = new Sphere();
		Matrix3D sphM = sph.getLocalTranslation();
		sphM.translate(200, 0, 200);
		sph.setLocalTranslation(sphM);
		addGameWorldObject(sph, true);
		sph.updateWorldBound();

		tpt = new Teapot();
		Matrix3D tptM = tpt.getLocalTranslation();
		tptM.translate(25, 0, 40);
		tpt.setLocalTranslation(tptM);
		addGameWorldObject(tpt, true);
		tpt.updateWorldBound();

		jade = new MyDiamond();
		Matrix3D jadeM = jade.getLocalTranslation();
		jadeM.translate(350, 1, 0);
		jade.setLocalTranslation(jadeM);
		addGameWorldObject(jade, true);
		jade.updateWorldBound();

		eventMgr.addListener(chest, CrashEvent.class);

		// create a group
		createPillar();

		// a HUD
		/*
		 * timeString = new HUDString("Time = " + time);
		 * timeString.setLocation(0,0.05); // (0,0) [lower-left] to (1,1)
		 * addGameWorldObject(timeString); scoreString = new HUDString
		 * ("Score = " + score); //default is (0,0)
		 * addGameWorldObject(scoreString);
		 */

	}

	// going to test out animation stances
	
	public void startAnimationProcess()
	{
		
		System.out.println("start Anim Process being called: " + startAnimProcess);
		for (SceneNode s : getGameWorld())
		{
			if (s instanceof Model3DTriMesh)
			{
				if (isIdle)
				{
			//		System.out.println("i'm calling back!");
					((Model3DTriMesh) s).startAnimation("Idle_Animation");
				   isMoving = false;
				   isPunching = false; 
				   isKicking = false;
				   isBlocking = false; 
				   isPunched = false; 
				   isKicked = false;
				   isKnockedOut = false;
					// set 
		
				}
				
				if (isMoving)
				{
					// this refers to w,a,s,d
					System.out.println("moving");
					((Model3DTriMesh) s).startAnimation("Running_Animation");
					   isIdle = false;
					   isPunching = false; 
					   isKicking = false;
					   isBlocking = false; 
					   isPunched = false; 
					   isKicked = false;
					   isKnockedOut = false;
				}
				
			}
		}
			

	}
	
	
	public void update(float elapsedTimeMS) {

		if (running) {
			Matrix3D mat;
			Vector3D translateVec;
			Vector3D rotateVec;
			physicsEngine.update(20.0f);
			for (SceneNode s : getGameWorld()) {
				if (s.getPhysicsObject() != null) {
					mat = new Matrix3D(s.getPhysicsObject().getTransform());
					translateVec = mat.getCol(3);
					s.getLocalTranslation().setCol(3, translateVec);
				}
			}
		}
	//	System.out.println("blocking is " + isBlocking);
/*		if (startAnimProcess = true) {
			// this should work
			startAnimationProcess();
			startAnimProcess = false;
			System.out.println("WHY");
		}
		*/
		// Update skybox's location
		Point3D camLoc = c1c.getLocation();
		Matrix3D camTranslation = new Matrix3D();
		camTranslation.translate(camLoc.getX(), camLoc.getY(), camLoc.getZ());
		skybox.setLocalTranslation(camTranslation);

		// Update skybox2's location
		/*
		 * Point3D camLoc2 = c2c.getLocation(); Matrix3D camTranslation2 = new
		 * Matrix3D(); camTranslation2.translate(camLoc2.getX(), camLoc2.getY(),
		 * camLoc2.getZ()); skybox2.setLocalTranslation(camTranslation2);
		 */
		// parkingLot.setLocalTranslation(camTranslation);
		// Player 1's crash events
		/*
		 * 
		 * HERE IS THE TEST SO FAR 
		 */
		if (cyl.getWorldBound().intersects(playerOne.getWorldBound()) // error
				&& collidedWCylinder == false && isBlocking == false) {

			if (isKicking == true)
			{
				System.out.println("kicked");
			collidedWCylinder = true;
			isKicking = false;
			numCrashes++;
			score1 += 100;
			p1Data.setHealth(p1Data.getHealth()-10);
			CrashEvent newCrash = new CrashEvent(numCrashes);
			removeGameWorldObject(cyl);
			eventMgr.triggerEvent(newCrash);
			}
			else if (isPunching == true)
			{
				System.out.println("punched");
				collidedWCylinder = true;
				isPunching = false;
				numCrashes++;
				score1 += 500;
				CrashEvent newCrash = new CrashEvent(numCrashes);
				removeGameWorldObject(cyl);
				eventMgr.triggerEvent(newCrash);
					
			}
			/*
			else if (isPunching == true && isBlocking == true)
			{
				collidedWCylinder = true;
				System.out.println("punched but blocked");
				isPunching = false;
				numCrashes++;
				score1 += 10; // test
				CrashEvent newCrash = new CrashEvent(numCrashes);
		//		removeGameWorldObject(cyl);
				eventMgr.triggerEvent(newCrash);
				
			}
			else if (isKicking == true && isBlocking == true)
			{
				System.out.println("kicked but blocked");
				collidedWCylinder = true;
				isKicking = false;
				numCrashes++;
				score1 += 20; // test
				CrashEvent newCrash = new CrashEvent(numCrashes);
		//		removeGameWorldObject(cyl);
				eventMgr.triggerEvent(newCrash);
				
			}
			*/
		}
		// Player 1's crash events
	
		 if (tpt.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWTeapot == false){
         collidedWTeapot = true;
         numCrashes++;
         score1 += 100;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(tpt);
         eventMgr.triggerEvent(newCrash);
      }
      if (cyl.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWCylinder == false){
         collidedWCylinder = true; 
         numCrashes++;
         score1 += 500;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(cyl);
         eventMgr.triggerEvent(newCrash);
      }
      if (sph.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWPyramid == false){
         collidedWPyramid = true; 
         numCrashes++;
         score1 += 250;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(sph);
         eventMgr.triggerEvent(newCrash);
      }
      if (jade.getWorldBound().intersects(playerOne.getWorldBound()) && collidedWDiamond == false){
         collidedWDiamond = true; 
         numCrashes++;
         score1 += 1000;
         CrashEvent newCrash = new CrashEvent(numCrashes);
         removeGameWorldObject(jade);
         eventMgr.triggerEvent(newCrash);
      }

		 
		// update player 1's HUD
		player1ScoreString.setText("Score = " + score1);
		time1 += elapsedTimeMS;
		DecimalFormat df1 = new DecimalFormat("0.0");
		player1TimeString.setText("Time = " + df1.format(time1 / 1000));
		player1HealthString.setText("Health: " + p1Data.getHealth()); 

		// update player 2's HUD
		/*
		 * player2ScoreString.setText("Score = " + score2); time2 +=
		 * elapsedTimeMS; DecimalFormat df2 = new DecimalFormat("0.0");
		 * player2TimeString.setText("Time = " + df2.format(time2/1000));
		 */

		// tell BaseGame to update game world state
		// c0c.update(elapsedTimeMS);
		c1c.update(elapsedTimeMS);
		// c2c.update(elapsedTimeMS);
      
      punchSwooshSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));
      punchHitSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));
      kickSwooshSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));
      kickHitSound.setLocation(new Point3D(playerOne.getWorldTranslation().getCol(3)));
      crowdSound.setLocation(new Point3D(chest.getWorldTranslation().getCol(3)));
      setEarParameters();
      
		playerOne.updateAnimation(elapsedTimeMS);
		if (thisClient != null)
			thisClient.processPackets();

		super.update(elapsedTimeMS);
	}

	private void createPillar() {
		Cube pillarBase = new Cube();
		pillarBase.scale(1, 4, 1);
		Cube pillarEye = new Cube();
		pillarEye.translate(0, 8, 0);

		Pyramid upperCenterEyeSpike = new Pyramid();

		Pyramid lowerCenterEyeSpike = new Pyramid();

		Group group1 = new Group(); // sun pillar system position
		Group group2 = new Group(); // pillar piece system translation
		Group group3 = new Group(); // piece spike system rotation

		group1.addChild(pillarBase);
		group1.addChild(group2);

		group2.addChild(group3);
		group2.addChild(pillarEye);

		group3.addChild(upperCenterEyeSpike);

		group3.addChild(lowerCenterEyeSpike);

		group1.setIsTransformSpaceParent(true);
		group2.setIsTransformSpaceParent(true);
		group3.setIsTransformSpaceParent(true);

		pillarBase.setIsTransformSpaceParent(true);
		pillarEye.setIsTransformSpaceParent(true);

		upperCenterEyeSpike.setIsTransformSpaceParent(true);

		lowerCenterEyeSpike.setIsTransformSpaceParent(true);

		group1.translate(-10, 2, -10);

		Vector3D pillarSpinV = new Vector3D(0, 1, 0);
		ClockwiseRotationController pillarSpin = new ClockwiseRotationController(
				200, pillarSpinV);
		pillarSpin.addControlledNode(group1);
		group1.addController(pillarSpin);

		group2.translate(0, 5, 0);

		MyTranslate_RotateController eyeMove = new MyTranslate_RotateController();
		eyeMove.addControlledNode(pillarEye);
		pillarEye.addController(eyeMove);

		Vector3D spikeSpinV = new Vector3D(8, 0, 0);
		spikeSpinV.cross(new Vector3D(0, 0, 1));
		ClockwiseRotationController spikeSpin = new ClockwiseRotationController(
				200, spikeSpinV);
		spikeSpin.addControlledNode(group3);
		group3.addController(spikeSpin);

		upperCenterEyeSpike.translate(0, 12, 0);

		lowerCenterEyeSpike.translate(0, 4, 0);
		lowerCenterEyeSpike.rotate(180f, new Vector3D(5, 0, 0));

		addGameWorldObject(group1, true);
	}

	private void createRing() {
		// Creates the base of the fighting ring and uv-wraps it to a texure.
		TriMesh fightingRing = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRing_Pad.obj");

		Texture fightingRing_Filled = TextureManager
				.loadTexture2D("src/a3/kmap165Engine/external_models/texture/fightingRing_Pad_Filled.jpg");
		fightingRing.setTexture(fightingRing_Filled);

		// Creates the four fighting ring posts and uv-wraps them to a texure.
		Texture fightingRingPost_Filled = TextureManager
				.loadTexture2D("src/a3/kmap165Engine/external_models/texture/RingPole_Filled.jpg");

		TriMesh fightingRingPost1 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost1.obj");
		fightingRingPost1.setTexture(fightingRingPost_Filled);

		TriMesh fightingRingPost2 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost2.obj");
		fightingRingPost2.setTexture(fightingRingPost_Filled);

		TriMesh fightingRingPost3 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost3.obj");
		fightingRingPost3.setTexture(fightingRingPost_Filled);

		TriMesh fightingRingPost4 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/fightingRingPost4.obj");
		fightingRingPost4.setTexture(fightingRingPost_Filled);

		// Creates the twelve fighting ring post strings and uv-wraps them to a
		// texure.
		Texture ringBoundaryString_Filled = TextureManager
				.loadTexture2D("src/a3/kmap165Engine/external_models/texture/String_Filled.jpg");

		TriMesh ringBoundaryString1 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString1.obj");
		ringBoundaryString1.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString2 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString2.obj");
		ringBoundaryString2.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString3 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString3.obj");
		ringBoundaryString3.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString4 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString4.obj");
		ringBoundaryString4.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString5 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString5.obj");
		ringBoundaryString5.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString6 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString6.obj");
		ringBoundaryString6.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString7 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString7.obj");
		ringBoundaryString7.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString8 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString8.obj");
		ringBoundaryString8.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString9 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString9.obj");
		ringBoundaryString9.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString10 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString10.obj");
		ringBoundaryString10.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString11 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString11.obj");
		ringBoundaryString11.setTexture(ringBoundaryString_Filled);

		TriMesh ringBoundaryString12 = objectLoader
				.loadModel("src/a3/kmap165Engine/external_models/fighting_ring/ringBoundaryString12.obj");
		ringBoundaryString12.setTexture(ringBoundaryString_Filled);

		Group ringGroup1 = new Group(); // fighting ring system position
		Group ringGroup2 = new Group(); // ring boundary system position
		Group ringGroup3 = new Group(); // first string system position
		Group ringGroup4 = new Group(); // second string system position
		Group ringGroup5 = new Group(); // third string system position
		Group ringGroup6 = new Group(); // fourth string system position

		ringGroup1.addChild(fightingRing);
		ringGroup1.addChild(ringGroup2);

		ringGroup2.addChild(fightingRingPost1);
		ringGroup2.addChild(fightingRingPost2);
		ringGroup2.addChild(fightingRingPost3);
		ringGroup2.addChild(fightingRingPost4);
		ringGroup2.addChild(ringGroup3);
		ringGroup2.addChild(ringGroup4);
		ringGroup2.addChild(ringGroup5);
		ringGroup2.addChild(ringGroup6);

		ringGroup3.addChild(ringBoundaryString1);
		ringGroup3.addChild(ringBoundaryString2);
		ringGroup3.addChild(ringBoundaryString3);

		ringGroup4.addChild(ringBoundaryString4);
		ringGroup4.addChild(ringBoundaryString5);
		ringGroup4.addChild(ringBoundaryString6);

		ringGroup5.addChild(ringBoundaryString7);
		ringGroup5.addChild(ringBoundaryString8);
		ringGroup5.addChild(ringBoundaryString9);

		ringGroup6.addChild(ringBoundaryString10);
		ringGroup6.addChild(ringBoundaryString11);
		ringGroup6.addChild(ringBoundaryString12);

		ringGroup1.setIsTransformSpaceParent(true);
		ringGroup2.setIsTransformSpaceParent(true);
		ringGroup3.setIsTransformSpaceParent(true);
		ringGroup4.setIsTransformSpaceParent(true);
		ringGroup5.setIsTransformSpaceParent(true);
		ringGroup6.setIsTransformSpaceParent(true);

		fightingRingPost1.setIsTransformSpaceParent(true);
		fightingRingPost2.setIsTransformSpaceParent(true);
		fightingRingPost3.setIsTransformSpaceParent(true);
		fightingRingPost4.setIsTransformSpaceParent(true);

		ringBoundaryString1.setIsTransformSpaceParent(true);
		ringBoundaryString2.setIsTransformSpaceParent(true);
		ringBoundaryString3.setIsTransformSpaceParent(true);
		ringBoundaryString4.setIsTransformSpaceParent(true);
		ringBoundaryString5.setIsTransformSpaceParent(true);
		ringBoundaryString6.setIsTransformSpaceParent(true);
		ringBoundaryString7.setIsTransformSpaceParent(true);
		ringBoundaryString8.setIsTransformSpaceParent(true);
		ringBoundaryString9.setIsTransformSpaceParent(true);
		ringBoundaryString10.setIsTransformSpaceParent(true);
		ringBoundaryString11.setIsTransformSpaceParent(true);
		ringBoundaryString12.setIsTransformSpaceParent(true);

		Matrix3D R1T = ringGroup1.getLocalTranslation();
		R1T.translate(50, 0, 50);
		ringGroup1.setLocalTranslation(R1T);
		Matrix3D R1S = ringGroup1.getLocalScale();
		R1S.scale(3, 3, 3);
		ringGroup1.setLocalScale(R1S);

		ringGroup1.updateLocalBound();
		ringGroup1.updateGeometricState(0, true);
		ringGroup1.updateWorldBound();

		addGameWorldObject(ringGroup1, true);
	}

	protected void render() {
		renderer.setCamera(camera1);
		super.render();
		/*
		 * renderer.setCamera(camera2); super.render();
		 */
	}

	private IDisplaySystem createFullDisplaySystem() {
		display = new MyDisplaySystem(1920, 1200, 32, 60, false,
				"sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation...");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		while (!display.isCreated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException("Display creation interrupted");
			}
			count++;
			System.out.print("+");
			if (count % 80 == 0) {
				System.out.println();
			}
			if (count > 2000) { // 20 seconds (approx.)
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display;
	}
   private IDisplaySystem createWDisplaySystem() {
		display = new MyDisplaySystem(1280, 800, 32, 60, false,
				"sage.renderer.jogl.JOGLRenderer");
		System.out.print("\nWaiting for display creation...");
		int count = 0;
		// wait until display creation completes or a timeout occurs
		while (!display.isCreated()) {
			try {
				Thread.sleep(10);
			} catch (InterruptedException e) {
				throw new RuntimeException("Display creation interrupted");
			}
			count++;
			System.out.print("+");
			if (count % 80 == 0) {
				System.out.println();
			}
			if (count > 2000) { // 20 seconds (approx.)
				throw new RuntimeException("Unable to create display");
			}
		}
		System.out.println();
		return display;
	}
   public boolean isInFSEM(){
      return inFSEM;
   } 
   public void setInFSEM(boolean ght){
      inFSEM = ght;
      changeSystem();
   }
	protected void shutdown() {
		display.close();

		super.shutdown();
		if (thisClient != null) {
			thisClient.sendByeMessage();
			try {
				thisClient.shutdown(); // shutdown() is inherited
			} catch (IOException e) {
				e.printStackTrace();
			}
		}
	}

	
	 protected void initSystem(){
       //call a local method to create a DisplaySystem object 
       IDisplaySystem display = createWDisplaySystem();
   	 setDisplaySystem(display); 
       //create an Input Manager
   	 IInputManager inputManager = new InputManager(); 
       setInputManager(inputManager);
   	 //create an (empty) gameworld 
       ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>(); 
       setGameWorld(gameWorld); 
    }
	 
    protected void changeSystem(){
       //call a local method to create a DisplaySystem object 
       display.close();
       if(inFSEM) display = createFullDisplaySystem();
       else display = createWDisplaySystem();
   	 setDisplaySystem(display); 
       
       getDisplaySystem().setTitle("Fighting Game");
		 renderer = getDisplaySystem().getRenderer(); 
       //setGameWorld(gameWorldX); 
       
       ArrayList<SceneNode> gameWorld = new ArrayList<SceneNode>(); 
       setGameWorld(gameWorld); 
       initGame();
    }

	public Vector3D getPlayerPosition() {
		Vector3D position = playerOne.getLocalTranslation().getCol(3);

		return position;

	}
	public Vector3D getNPCPosition()
	{
		Vector3D position = cyl.getLocalTranslation().getCol(3);
				
		return position;
	}

	public boolean isConnected() {
		return isConnected();
	}

	public void setIsConnected(boolean x) {
		isConnected = x;
	}

	public void keyPressed(KeyEvent e) {
	}

	public void keyReleased(KeyEvent e) {
	}

	public void keyTyped(KeyEvent e) {
	}

	// more scripting stuff for players
	public class UpdatePlayerColor extends AbstractInputAction {

		public void performAction(float t, Event e) {
			Invocable invocable = (Invocable) engine;
			SceneNode testingobject = tpt;

			try {
				invocable.invokeFunction("updateCharacter", testingobject); // error
			} catch (ScriptException t1) {
				t1.printStackTrace();
			} catch (NoSuchMethodException t2) {
				t2.printStackTrace();
			} catch (NullPointerException t3) {
				t3.printStackTrace();
			}
		}

	}

	private void runScript() {
		try {
			FileReader fileReader = new FileReader(scriptFile);
			engine.eval(fileReader);
			fileReader.close();
		} catch (FileNotFoundException e1) {
			System.out.println(scriptFile + " not found " + e1);
		} catch (IOException e2) {
			System.out.println("IO problem with " + scriptFile + e2);
		} catch (ScriptException e3) {
			System.out.println("ScriptException in " + scriptFile + e3);
		} catch (NullPointerException e4) {
			System.out.println("Null ptr exception reading " + scriptFile + e4);
		}
	}

	private TerrainBlock createTerBlock(AbstractHeightMap heightmap) {
		float heightscale = .02f;
		Vector3D terrainScale = new Vector3D(1, heightscale, 1);

		int terrainsize = heightmap.getSize();

		float cornerheight = heightmap.getTrueHeightAtPoint(0, 0) * heightscale;
		Point3D terrainOrig = new Point3D(0, -cornerheight, 0);

		String name = "terrain" + heightmap.getClass().getSimpleName();
		TerrainBlock tb = new TerrainBlock(name, terrainsize, terrainScale,
				heightmap.getHeightData(), terrainOrig);
		return tb;
	}

	private void initTerrain() {

		ImageBasedHeightMap myHeightMap = new ImageBasedHeightMap(
				"src/a3/images/mountains512.jpg");
		/*
		 * HillHeightMap myHeightMap = new HillHeightMap(129, 2000, 5.0f, 20.0f,
		 * (byte)2, 12345); myHeightMap.setHeightScale(0.1f);
		 */
		hillTerr = createTerBlock(myHeightMap);
		TextureState groundState;
		Texture floorTexture = TextureManager
				.loadTexture2D("src/a3/images/lot_floor.jpg");
		floorTexture.setApplyMode(sage.texture.Texture.ApplyMode.Replace);
		hillTerr.setTexture(floorTexture);

		addGameWorldObject(hillTerr, true);
	}

	public void addNode(GhostAvatar avatar) {
		// TODO Auto-generated method stub
		System.out.println("i'm being called!");
		this.addGameWorldObject(avatar, true);

	}

	public void removeNode(GhostAvatar avatar) {
		// TODO Auto-generated method stub
		System.out.println("removenode is being called!");
		this.removeGameWorldObject(avatar);
	}

   public void addNPC(GhostNPC newNPC){
      System.out.println("addnode is being called!");
		this.addGameWorldObject(newNPC, true);
   }
	protected void initPhysicsSystem() {
		String engine = "sage.physics.JBullet.JBulletPhysicsEngine";
		physicsEngine = PhysicsEngineFactory.createPhysicsEngine(engine);
		physicsEngine.initSystem();
		float[] gravity = { 0, -1f, 0 };
		physicsEngine.setGravity(gravity);
	}

	private void createSagePhysicsWorld() {
		float mass = 1.0f;
		powerUpP = physicsEngine.addSphereObject(physicsEngine.nextUID(), mass,
				powerUp.getWorldTransform().getValues(), 1.0f);
		powerUpP.setBounciness(1.0f);
		powerUp.setPhysicsObject(powerUpP);

		// terrain
		float up[] = { -.05f, .95f, 0 };
		terrainP = physicsEngine.addStaticPlaneObject(physicsEngine.nextUID(),
				hillTerr.getWorldTransform().getValues(), up, 0.0f);
		terrainP.setBounciness(1.0f);
		hillTerr.setPhysicsObject(terrainP);
	}

	private class StartAction extends AbstractInputAction {
		public void performAction(float time, Event ee) {
			running = true;
//			startAnimProcess(true);
//			setIdle(true);
//			setBlocking(false);
		}
	}
	public void setIdle(Boolean b)
	{
		isIdle = b;
	}
	public void setMoving(Boolean b)
	{
		isMoving = b;
	}
	public void setPunching(Boolean b)
	{
		isPunching = b;
	}
	public void setKicking(Boolean b)
	{
		isKicking = b;
	}
	public void setBlocking(Boolean b)
	{
		isBlocking = b;
	}
	public void setPunched(Boolean b)
	{
		isPunched = b;
	}
	public void setKicked(Boolean b)
	{
		isKicked = b;
	}
	public void setKOed(Boolean b)
	{
		isKnockedOut = b;
	}
	public void startAnimProcess(Boolean b)
	{
		startAnimProcess = b;
	}
   private void addGameWorldObject(SceneNode s, boolean p){
      addGameWorldObject(s);
      gameWorldX.add(s);
      
   }
}