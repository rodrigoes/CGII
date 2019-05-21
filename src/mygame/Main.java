package mygame;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.AmbientLight;
import com.jme3.light.DirectionalLight;
import com.jme3.light.PointLight;
import com.jme3.light.SpotLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
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
import com.jme3.shadow.DirectionalLightShadowFilter;
import com.jme3.shadow.DirectionalLightShadowRenderer;
import com.jme3.texture.Texture;

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
private Spatial VenetianBlind;
private BulletAppState physicAppState;
private AudioNode music;

public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }

    private Node shootables;
    private Node wall;

    @Override
    public void simpleInitApp() {
         //initLight();
       initKeys();
       initAudio();
       initPhysics();

        SpotLight spot = new SpotLight();
        spot.setSpotRange(1000f);                           // distance
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD); // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD); // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(15));         // light color
        spot.setPosition(new Vector3f(-1, 5, 5));               // shine from camera loc
        spot.setDirection(cam.getDirection());             // shine forward from camera loc
        rootNode.addLight(spot);

        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(0, 1f, -3f));

        shootables = new Node("Shootables");
        shootables.attachChild(makeFloor(0, -4, -10.5f));
        physicAppState.getPhysicsSpace().add(shootables);
        rootNode.attachChild(shootables);
 
        shootables = new Node("Shootables");
        shootables.attachChild(makeFloor(0, 6, -10.5f));
        physicAppState.getPhysicsSpace().add(shootables);
        rootNode.attachChild(shootables);
        
        wall = new Node("wall");
        wall.attachChild(makeWall(10, 1, -10.5f, 0.2f, 5, 15));
        physicAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(-10, 1, -10.5f, 0.2f, 5, 15));  
        physicAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);
        
        wall = new Node("wall");
        wall.attachChild(makeWall(25, 1, 0, 0.2f, 5, 10));
        wall.rotate(0, 1.5708f, 0); //90 graus 1.5708 rad
        physicAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);
        
        Table = assetManager.loadModel("/Models/Table/Table.j3o");
       // Table.setLocalScale(3);
       Table.setLocalTranslation(0, -4, -10.5f);
      // RigidBodyControl r = new RigidBodyControl();
       //Table.addControl(r);
        
       // r.setPhysicsLocation(Table.getLocalTranslation());
      //physicAppState.getPhysicsSpace().add(r);
        rootNode.attachChild(Table);
      
        
        /*
        Spatial Wardrobe = assetManager.loadModel("/Models/Others/Sideboard.obj");
        Wardrobe.scale(20);
        rootNode.attachChild(Wardrobe);*/
        
        PoolCue = assetManager.loadModel("/Models/Others/Poolcue.j3o");
  
        PoolCue.scale(3);
        PoolCue.move(0, -3.9f, -10.5f);
        RigidBodyControl r2 = new RigidBodyControl();
        PoolCue.addControl(r2);
        r2.setPhysicsLocation(PoolCue.getLocalTranslation());
        physicAppState.getPhysicsSpace().add(r2);
        rootNode.attachChild(PoolCue);
        
        
        Ball = assetManager.loadModel("/Models/Others/ball.j3o");
        Ball.setLocalScale(3);
        Ball.move(0, -4, -10.5f);
        RigidBodyControl r3 = new RigidBodyControl();
        Ball.addControl(r3);
        r3.setPhysicsLocation(Ball.getLocalTranslation());
        physicAppState.getPhysicsSpace().add(r3);
        rootNode.attachChild(Ball);
        
        VenetianBlind = assetManager.loadModel("/Models/Others/VenetianBlind.j3o");
        VenetianBlind.scale(0.3f);
        VenetianBlind.rotate(0, 1.5708f, 0);
        physicAppState.getPhysicsSpace().add(VenetianBlind);
        rootNode.attachChild(VenetianBlind);
        
        // Wardrobe.setLocalTranslation(0, -4, -10.5f);
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

    protected Geometry makeFloor(float x, float y, float z) {

        Box a = new Box(10, 0.1f, 15);
        Geometry geom = new Geometry("Box", a);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/piso2.jpg");
        mat.setTexture("ColorMap", monkeyTex);
        geom.setMaterial(mat);

        geom.setLocalTranslation(x, y, z);
        RigidBodyControl r = new RigidBodyControl(0);
            
        geom.addControl(r);

       
        r.setPhysicsLocation(geom.getLocalTranslation());
        
        rootNode.attachChild(geom);
        physicAppState.getPhysicsSpace().add(r);
        return geom;

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
          
        AmbientLight al = new AmbientLight();
        al.setColor(ColorRGBA.White.mult(1.3f));
        rootNode.addLight(al);
        
        DirectionalLight sun = new DirectionalLight();
        sun.setColor(ColorRGBA.White);
        sun.setDirection(cam.getDirection().setY(10f));
        rootNode.addLight(sun);
        
        final int SHADOWMAP_SIZE = 1024;
        DirectionalLightShadowRenderer dlsr = new DirectionalLightShadowRenderer(assetManager, SHADOWMAP_SIZE, 3);
        dlsr.setLight(sun);
        viewPort.addProcessor(dlsr);

        DirectionalLightShadowFilter dlsf = new DirectionalLightShadowFilter(assetManager, SHADOWMAP_SIZE, 3);
        dlsf.setLight(sun);
        dlsf.setEnabled(true);
        FilterPostProcessor fpp = new FilterPostProcessor(assetManager);
        fpp.addFilter(dlsf);
        viewPort.addProcessor(fpp);
        
        FilterPostProcessor fpp2 = new FilterPostProcessor(assetManager);
        SSAOFilter ssaoFilter = new SSAOFilter(12.94f, 43.92f, 0.33f, 0.61f);
        fpp2.addFilter(ssaoFilter);
        viewPort.addProcessor(fpp2);
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
        
        private void initKeys(){
            inputManager.addMapping("play", new MouseButtonTrigger(mouseInput.BUTTON_LEFT));
            inputManager.addMapping("play2", new MouseButtonTrigger(mouseInput.BUTTON_RIGHT));
            inputManager.addListener(actionListener, "play");
            inputManager.addListener(actionListener, "play2");
        }
        
        private ActionListener actionListener = new ActionListener() { 
        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {
            
            if (name.equals("play") && !keyPressed) {
                hitPoolCue(0,0,2);
            }
            if (name.equals("play2") && !keyPressed) {
                hitPoolCue(0,0,-2);
            }
           
        }
    };
        
        private void hitPoolCue(int x,int y,int z){
          
            PoolCue.move(x, y, z);
            
        }
        
        private void initPhysics() {
        physicAppState = new BulletAppState();
      //  physicAppState.setDebugEnabled(true);
        stateManager.attach(physicAppState);
    }
}
