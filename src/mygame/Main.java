package mygame;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import static com.jme3.math.ColorRGBA.Black;
import com.jme3.math.FastMath;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.post.FilterPostProcessor;
import com.jme3.post.ssao.SSAOFilter;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.control.LightControl;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;
import static java.awt.Color.black;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    private Spatial PoolCue;
    private Spatial Table;
    private Spatial Ball;
    private Spatial Ball2;
    private Spatial Ball3;
    private Spatial Ball4;
    private Spatial VenetianBlind;
    private BulletAppState bulletAppState;
    private AudioNode music;
    private RigidBodyControl ball_phy;
    private static final Sphere sphere;
    Material stone_mat;
   
    static {
    /** Initialize the cannon ball geometry */
     sphere = new Sphere(32, 32, 0.1f, true, false);
     sphere.setTextureMode(TextureMode.Projected);
    /** Initialize the brick geometry */
    
  }
    

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private Node shootables;
    private Node wall;

    @Override
    public void simpleInitApp() {
        
        initPhysics();
        initLight();
        initKeys();
        
        initAudio();
        initTexture();
        
        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(0, 1f, -3f));
      //  cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
        shootables = new Node("Shootables");
        makeFloor(0, -4, -10.5f);
        bulletAppState.getPhysicsSpace().add(shootables);
        rootNode.attachChild(shootables);
        /*
        shootables = new Node("Shootables");
        shootables.attachChild(makeFloor(0, 6, -10.5f));
        bulletAppState.getPhysicsSpace().add(shootables);
        rootNode.attachChild(shootables);
         */
        wall = new Node("wall");
        wall.attachChild(makeWall(10, 1, -10.5f, 0.2f, 5, 15));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(-10, 1, -10.5f, 0.2f, 5, 15));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(25, 1, 0, 0.2f, 5, 10));
        wall.rotate(0, 1.5708f, 0); //90 graus 1.5708 rad
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        Table = assetManager.loadModel("/Models/Table/agrvai7.j3o");
        Material gun_mat;
        gun_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture gun_text = assetManager.loadTexture("/Models/Table/wood032.jpg");
        gun_mat.setTexture("ColorMap", gun_text);
      //  Table.setMaterial(gun_mat);
        Table.setLocalScale(0.81f);
        Table.move(0, -3.63f, -10.5f);
        Table.rotate(0, 1.5708f, 0);
        rootNode.attachChild(Table);

        RigidBodyControl r5 = new RigidBodyControl(10);
        Table.addControl(r5);
        r5.setPhysicsLocation(Table.getLocalTranslation());
        bulletAppState.getPhysicsSpace().add(Table);

        
        Spatial Wardrobe = assetManager.loadModel("/Models/Others/Sideboard.obj");
        Wardrobe.scale(20);
        rootNode.attachChild(Wardrobe);
      
        
        PoolCue = assetManager.loadModel("/Models/Others/Poolcue.j3o");

        PoolCue.scale(3);
        PoolCue.move(0.5f, -3.5f, -14.1f);
        RigidBodyControl r2 = new RigidBodyControl(0);  
        PoolCue.addControl(r2);
        r2.setPhysicsLocation(PoolCue.getLocalTranslation());
        bulletAppState.getPhysicsSpace().add(r2);
        rootNode.attachChild(PoolCue);
        
        makeCannonBall(0, -2, -10.5f);
     //   makeCannonBall(0, -2, -10.1f);
       //makeCannonBall(0, -5, -10.1f);
       // Ball.move(0, -5, -10.1f);
       
        
        
    
        
        VenetianBlind = assetManager.loadModel("/Models/Others/VenetianBlind.j3o");
        VenetianBlind.scale(0.3f);
        VenetianBlind.rotate(0, 1.5708f, 0);
        bulletAppState.getPhysicsSpace().add(VenetianBlind);
        rootNode.attachChild(VenetianBlind);
        
        //Wardrobe.setLocalTranslation(0, -4, -10.5f);
         VenetianBlind.setLocalTranslation(0, 0, -24.6f);
        //  Table.setLocalTranslation(0, -4, -10.5f);
        //  PoolCue.setLocalTranslation(0, -3.9f, -10.5f);
        //  Ball.setLocalTranslation(0, -4, -10.5f);

    }

    @Override
    public void simpleUpdate(float tpf) {
        //  setPoolCue();
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    protected void makeFloor(float x, float y, float z) {

        Box a = new Box(10, 0.1f, 15);
        Geometry geom = new Geometry("Box", a);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/piso2.jpg");
        mat.setTexture("ColorMap", monkeyTex);
        geom.setMaterial(mat);
        geom.setLocalTranslation(x, y, z);
        rootNode.attachChild(geom);

        RigidBodyControl r = new RigidBodyControl(0.0f);

        geom.addControl(r);

        r.setPhysicsLocation(geom.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r);

    }

//(10, 1, -10.5f)
    protected Geometry makeWall(int xpos, int ypos, float zpos, float xtam, int ytam, int ztam) {

        Box a = new Box(xtam, ytam, ztam);
        Geometry geom = new Geometry("Box", a);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/madeira.jpg");
        mat.setTexture("ColorMap", monkeyTex);
        geom.setMaterial(mat);

        geom.setLocalTranslation(xpos, ypos, zpos);

        return geom;

    }

    private void initLight() {

        SpotLight spot = new SpotLight();
        spot.setSpotRange(1000f);                           // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(5));         // light color
        spot.setPosition(new Vector3f(-1, 5, 5));               // shine from camera loc
        spot.setDirection(cam.getDirection());             // shine forward from camera loc
        rootNode.addLight(spot);

    }
    
      public void makeCannonBall(float x,float y,float z) {
          
    /** Create a cannon ball geometry and attach to scene graph. */
    Geometry ball_geo = new Geometry("cannon ball", sphere);
    ball_geo.setMaterial(stone_mat);
    rootNode.attachChild(ball_geo);
    /** Position the cannon ball  */
    ball_geo.setLocalTranslation(x, y, z);
    /** Make the ball physcial with a mass > 0.0f */
    ball_phy = new RigidBodyControl(1f);
    /** Add physical ball to physics space. */
    ball_geo.addControl(ball_phy);
    bulletAppState.getPhysicsSpace().add(ball_phy);
    /** Accelerate the physcial ball to shoot it. */
  //  ball_phy.setLinearVelocity(cam.getDirection().mult(0.2f));
   
  }
     private void initTexture(){
        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/5.jpg");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);
     } 
    private void setPoolCue() {

        Vector3f vectorDifference = new Vector3f(cam.getLocation().subtract(PoolCue.getWorldTranslation()));
        PoolCue.setLocalTranslation(vectorDifference.addLocal(PoolCue.getLocalTranslation()));

        Quaternion worldDiff = new Quaternion(cam.getRotation().mult(PoolCue.getWorldRotation().inverse()));
        PoolCue.setLocalRotation(worldDiff.multLocal(PoolCue.getLocalRotation()));

        PoolCue.move(cam.getDirection().mult(2.5f));
        PoolCue.move(cam.getUp().mult(-3.5f));
        PoolCue.move(cam.getLeft().mult(0.7f));
        //  PoolCue.rotate(0.1f, FastMath.PI * 1.45f, 0.13f);
    }

    private void initAudio() {
        music = new AudioNode(assetManager, "Sounds/inter.wav", AudioData.DataType.Buffer);
        music.setPositional(false);
        music.setLooping(true);
        music.setVolume(3);
        rootNode.attachChild(music);
        music.play();
    }

    private void initKeys() {
        inputManager.addMapping("play", new MouseButtonTrigger(mouseInput.BUTTON_LEFT));
        inputManager.addMapping("play2", new MouseButtonTrigger(mouseInput.BUTTON_RIGHT));

        inputManager.addListener(actionListener, "play");
        inputManager.addListener(actionListener, "play2");
     
    }

    private ActionListener actionListener = new ActionListener() {
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("play") && !keyPressed) {
                hitPoolCue(0, 0, 2);
            }
            if (name.equals("play2") && !keyPressed) {
                hitPoolCue(0, 0, -2);
            }
    


        }
    };

    private void hitPoolCue(int x, int y, int z) {

        PoolCue.move(x, y, z);

    }

    

    private void initPhysics() {

        bulletAppState = new BulletAppState();
        stateManager.attach(bulletAppState);

    }

}
