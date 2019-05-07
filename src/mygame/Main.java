package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.texture.Texture;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 * @author normenhansen
 */
public class Main extends SimpleApplication {

    public static void main(String[] args) {
        Main app = new Main();
        app.start();
    }
    
    private Node shootables;
    private Node wall;
    
    @Override
    public void simpleInitApp() {
         
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-1f, -1f, -1f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    
    rootNode.addLight(sun);    
    
        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(0,1f,-3f));
        
    shootables = new Node("Shootables");
    shootables.attachChild(makeFloor());
    rootNode.attachChild(shootables);
    
    wall = new Node("wall");
    wall.attachChild(makeFloor());
    rootNode.attachChild(wall);
    
    
    
         Spatial Table = assetManager.loadModel("/Models/Table/Table.j3o");
         Table.scale(3);
         rootNode.attachChild(Table);
    
         Spatial PoolCue = assetManager.loadModel("/Models/Others/Poolcue.j3o");
         PoolCue.scale(3);
         rootNode.attachChild(PoolCue);
         
         Spatial Ball = assetManager.loadModel("/Models/Others/ball.j3o");
         Ball.scale(3);
         rootNode.attachChild(Ball);
    
       
        Table.setLocalTranslation(0, -4, -10.5f);
        PoolCue.setLocalTranslation(0, -4f, -10.5f);
        Ball.setLocalTranslation(0, -4, -10.5f);
        
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
    
     protected Geometry makeFloor() {
    
        Box a = new Box(10, 0.1f, 10);
        Geometry geom = new Geometry("Box", a);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/piso.jpg"); 
        mat.setTexture("ColorMap", monkeyTex); 
        geom.setMaterial(mat);
    
        geom.setLocalTranslation(0, -4, -10.5f);
    
    return geom;
    
  }
       
}
