package mygame;

import com.jme3.app.SimpleApplication;
import com.jme3.light.DirectionalLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
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

    @Override
    public void simpleInitApp() {
         
    DirectionalLight sun = new DirectionalLight();
    sun.setDirection((new Vector3f(-0.3f, -0.3f, -0.3f)).normalizeLocal());
    sun.setColor(ColorRGBA.White);
    
    rootNode.addLight(sun);    
        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(0,-1,0));
        
        Box a = new Box(10, 0.1f, 10);
        Geometry geom = new Geometry("Box", a);
        
        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/piso.jpg"); 
        mat.setTexture("ColorMap", monkeyTex); 
        geom.setMaterial(mat);
    
        Spatial myModel = assetManager.loadModel("/Textures/Mesa/PoolTable.j3o");
         myModel.scale(0.09f);
         rootNode.attachChild(myModel);
    
        geom.setLocalTranslation(0, -4, -10.5f);
        myModel.setLocalTranslation(0, -4, -10.5f);


        rootNode.attachChild(geom);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //TODO: add update code
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }
}
