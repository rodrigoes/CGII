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
    private Spatial Barreira;
    private Spatial Ball;
    private Spatial Ball2;
    private Spatial Ball3;
    private Spatial Ball4;
    private Spatial VenetianBlind;
    private BulletAppState bulletAppState;
    private AudioNode music;
    private RigidBodyControl ball_phy;
    private RigidBodyControl taco;
    private static final Sphere sphere;
    Material stone_mat;
    Material mat;
    static {
    /** Initialize the cannon ball geometry */
     sphere = new Sphere(32, 32, 0.39f, true, false);
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
        makeTable();
        
        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(0, 1f, -3f));
       //cam.lookAt(new Vector3f(2, 2, 0), Vector3f.UNIT_Y);
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
        wall.attachChild(makeWall(15, 1, -10.5f, 0.2f, 5, 20));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(-15, 1, -10.5f, 0.2f, 5, 20));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(30, 1, 0, 0.f, 5, 15));
        wall.rotate(0, 1.5708f, 0); //90 graus 1.5708 rad
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);
        //////
        
     
        
/*
        Table = assetManager.loadModel("/Models/Table/agrvai7.j3o");
        Material gun_mat;
        gun_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture gun_text = assetManager.loadTexture("/Models/Table/wood032.jpg");
        gun_mat.setTexture("ColorMap", gun_text);
        Table.setMaterial(gun_mat);
        Table.setLocalScale(0.85f);
        Table.move(0, -3.63f, -10.5f);
        Table.rotate(0, 1.5708f, 0);
        rootNode.attachChild(Table);

        RigidBodyControl r5 = new RigidBodyControl(10);
        Table.addControl(r5);
        r5.setPhysicsLocation(Table.getLocalTranslation());
        bulletAppState.getPhysicsSpace().add(Table);*/
        
       /* Barreira = assetManager.loadModel("/Models/Table/barreira.j3o");
     
        Texture gun_textq = assetManager.loadTexture("/Models/Table/wood032.jpg");
        gun_mat.setTexture("ColorMap", gun_textq);
        Barreira.setMaterial(gun_mat);
        Barreira.setLocalScale(0.85f);
        Barreira.move(0, -3.10f, -18.5f);
        Barreira.rotate(0, 1.5708f, 0);
        rootNode.attachChild(Barreira);

        RigidBodyControl r7 = new RigidBodyControl(0);
        Barreira.addControl(r7);
        r7.setPhysicsLocation(Barreira.getLocalTranslation());
        bulletAppState.getPhysicsSpace().add(Barreira);
*/
        
        Spatial Wardrobe = assetManager.loadModel("/Models/Others/Sideboard.obj");
        Wardrobe.scale(20);
        rootNode.attachChild(Wardrobe);
      
        
        PoolCue = assetManager.loadModel("/Models/Others/Poolcue.j3o");

        PoolCue.scale(5);
        PoolCue.move(1.2f, -5.9f, -7.1f);
        rootNode.attachChild(PoolCue)
                ;
        taco = new RigidBodyControl(0.0f);  
        PoolCue.addControl(taco);
        bulletAppState.getPhysicsSpace().add(taco);
        
       float b = -2f ;
                  
        makeCannonBall(0, b, -14.5f,"Textures/1.jpg");
        makeCannonBall(0.195f, b, -14.5f,"Textures/2.jpg");
        makeCannonBall(0.195f * 2, b, -14.5f,"Textures/3.jpg");
        makeCannonBall(0.195f * 3  ,b, -14.5f,"Textures/4.jpg");
        makeCannonBall(0.195f * 4, b, -14.5f,"Textures/5.jpg");
        makeCannonBall(0 , b, -13.5f,"Textures/6.jpg");
        makeCannonBall(0.195f, b, -13.5f,"Textures/7.jpg");
        makeCannonBall(0.195f* 2,b, -13.5f,"Textures/8.jpg");
        makeCannonBall(0.195f * 3 ,b, -13.5f,"Textures/9.jpg");
        makeCannonBall(0, b, -12.5f,"Textures/10.jpg");
        makeCannonBall(0.195f, b, -12.5f,"Textures/11.jpg");
        makeCannonBall(0.195f * 2, b, -12.5f,"Textures/12.jpg");
        makeCannonBall(0, b, -11.5f,"Textures/13.jpg");
        makeCannonBall(0.195f, b, -11.5f,"Textures/14.jpg");
        makeCannonBall(0.195f * 3 ,b, -10.5f,"Textures/15.jpg");
       // Ball.move(0, -5, -10.1f);
       
        
        
    
        
        VenetianBlind = assetManager.loadModel("/Models/Others/VenetianBlind.j3o");
        VenetianBlind.scale(0.3f);
        VenetianBlind.rotate(0, 1.5708f, 0);
        bulletAppState.getPhysicsSpace().add(VenetianBlind);
        rootNode.attachChild(VenetianBlind);
        
        //Wardrobe.setLocalTranslation(0, -4, -10.5f);
         VenetianBlind.setLocalTranslation(0, 0, -29.6f);
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

        Box a = new Box(15, 0.1f, 20);
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
    protected void makeTable(){
        
        Box a = new Box(9.88f, 0.2f, 4.6f);
        Geometry geoma = new Geometry("Box", a);

        Material mata = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex4 = assetManager.loadTexture("Textures/Felt.jpg"); 
        mata.setTexture("ColorMap", monkeyTex4); 
        geoma.setMaterial(mata);
        geoma.setLocalTranslation(0, -2, -15.5f);

        rootNode.attachChild(geoma);
        
        RigidBodyControl r = new RigidBodyControl(0.0f);

        geoma.addControl(r);

        r.setPhysicsLocation(geoma.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r);

         Box b1 = new Box(11, 1, 5.5f);
        Geometry geomb = new Geometry("Box", b1);

        Material matb = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTexb = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        matb.setTexture("ColorMap", monkeyTexb); 
        geomb.setMaterial(matb);
        geomb.setLocalTranslation(0, -3.2f, -15.5f);

        rootNode.attachChild(geomb);
        
        RigidBodyControl rb = new RigidBodyControl(0.0f);

        geomb.addControl(rb);

        rb.setPhysicsLocation(geomb.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(rb);

        
         Box a1= new Box(4.5f, -0.35f, 0.5f);
        Geometry geoma1 = new Geometry("Box", a1);

        Material mata1 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex2 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata1.setTexture("ColorMap", monkeyTex2); 
        geoma1.setMaterial(mata1);
        geoma1.setLocalTranslation(-4.5f, -1.39f, -10.5f);

        rootNode.attachChild(geoma1);
        
                
        RigidBodyControl r20 = new RigidBodyControl(0.0f);

        geoma1.addControl(r20);

        r20.setPhysicsLocation(geoma1.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r20);

        
          Box a2= new Box(4, -0.35f, 0.5f);
        Geometry geoma2 = new Geometry("Box", a2);

        Material mata2 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex3 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata2.setTexture("ColorMap", monkeyTex3); 
        geoma2.setMaterial(mata2);
        geoma2.setLocalTranslation(5, -1.39f, -10.5f);

        rootNode.attachChild(geoma2);
        
        RigidBodyControl r21 = new RigidBodyControl(0.0f);

        geoma2.addControl(r21);

        r21.setPhysicsLocation(geoma2.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r21);
        
        Box a3= new Box(4.5f, -0.35f, 0.5f);
        Geometry geoma3 = new Geometry("Box", a3);

        Material mata3 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex5 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata3.setTexture("ColorMap", monkeyTex5); 
        geoma3.setMaterial(mata3);
        geoma3.setLocalTranslation(-4.5f, -1.39f, -20.5f);

        rootNode.attachChild(geoma3);
        
        
        RigidBodyControl r22 = new RigidBodyControl(0.0f);

        geoma3.addControl(r22);

        r22.setPhysicsLocation(geoma3.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r22);
        
        
        Box a4= new Box(4, -0.35f, 0.5f);
        Geometry geoma4 = new Geometry("Box", a4);

        Material mata4 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex6 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata4.setTexture("ColorMap", monkeyTex6); 
        geoma4.setMaterial(mata4);
        geoma4.setLocalTranslation(5, -1.39f, -20.5f);

        rootNode.attachChild(geoma4);
        
        
        RigidBodyControl r24 = new RigidBodyControl(0.0f);

        geoma4.addControl(r24);

        r24.setPhysicsLocation(geoma4.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r24);
        
        Box a5= new Box(0.5f, -0.35f, 4);
        Geometry geoma5 = new Geometry("Box", a5);

        Material mata5 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex7 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata5.setTexture("ColorMap", monkeyTex7); 
        geoma5.setMaterial(mata5);
        geoma5.setLocalTranslation(10.2f
                , -1.39f, -15.5f);

        rootNode.attachChild(geoma5);
        
        
        RigidBodyControl r25 = new RigidBodyControl(0.0f);

        geoma5.addControl(r25);

        r25.setPhysicsLocation(geoma5.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r25);
        
        
       Box a6= new Box(0.5f, -0.35f, 4);
        Geometry geoma6 = new Geometry("Box", a6);

        Material mata6 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex8 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata6.setTexture("ColorMap", monkeyTex8); 
        geoma6.setMaterial(mata6);
        geoma6.setLocalTranslation(-10.2f
                , -1.39f, -15.5f);

        rootNode.attachChild(geoma6);
        
        
        RigidBodyControl r27 = new RigidBodyControl(0.0f);

        geoma6.addControl(r27);

        r27.setPhysicsLocation(geoma6.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r27);
        
        
         Box a7= new Box(0.5f, -0.35f, 6);
        Geometry geoma7 = new Geometry("Box", a7);

        Material mata7 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex9 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata7.setTexture("ColorMap", monkeyTex9); 
        geoma7.setMaterial(mata7);
        geoma7.setLocalTranslation(11.2f
                , -1.39f, -15.5f);

        rootNode.attachChild(geoma7);
         RigidBodyControl r28 = new RigidBodyControl(0.0f);

        geoma6.addControl(r28);

        r28.setPhysicsLocation(geoma6.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r28);
        
        
            Box a8= new Box(0.5f, -0.35f, 6);
        Geometry geoma8 = new Geometry("Box", a8);

        Material mata8 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex10 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata8.setTexture("ColorMap", monkeyTex10); 
        geoma8.setMaterial(mata8);
        geoma8.setLocalTranslation(-11.2f
                , -1.39f, -15.5f);

        rootNode.attachChild(geoma8);
        
         RigidBodyControl r29 = new RigidBodyControl(0.0f);

        geoma6.addControl(r29);

        r29.setPhysicsLocation(geoma6.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r29);
        
        
        Box a9=new Box(11.7f, -0.35f, 0.5f);
        Geometry geoma9 = new Geometry("Box", a9);

        Material mata9 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex11 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata9.setTexture("ColorMap", monkeyTex11); 
        geoma9.setMaterial(mata9);
        geoma9.setLocalTranslation(0f, -1.39f, -21.5f);

        rootNode.attachChild(geoma9);
        
         RigidBodyControl r30 = new RigidBodyControl(0.0f);

        geoma6.addControl(r30);

        r30.setPhysicsLocation(geoma6.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r30);
        
        
         Box a11=new Box(11.7f, -0.35f, 0.5f);
        Geometry geoma11 = new Geometry("Box", a11);

        Material mata11 = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex12 = assetManager.loadTexture("Textures/Mahogany.jpg"); 
        mata11.setTexture("ColorMap", monkeyTex12); 
        geoma11.setMaterial(mata11);
        geoma11.setLocalTranslation(0f, -1.39f, -9.5f);

        rootNode.attachChild(geoma11);
        
         RigidBodyControl r31 = new RigidBodyControl(0.0f);

        geoma6.addControl(r31);

        r31.setPhysicsLocation(geoma6.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r31);
        
        
    }
//(10, 1, -10.5f)
    protected Geometry makeWall(int xpos, int ypos, float zpos, float xtam, int ytam, int ztam) {

        Box a = new Box(xtam, ytam, ztam);
        Geometry geom = new Geometry("Box", a);
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
    
      public void makeCannonBall(float x,float y,float z, String w) {
          
         stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey(w);
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);
          
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
     ball_phy.setLinearVelocity(cam.getDirection().mult(2.3f));
  
        
       
        
   
  }
     private void initTexture(){
        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/5.jpg");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);
        
        
        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/madeira.jpg");
        mat.setTexture("ColorMap", monkeyTex);
        
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
