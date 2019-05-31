package mygame;

import com.jme3.asset.AssetManager;
import com.jme3.asset.TextureKey;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Vector3f;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Sphere;
import com.jme3.texture.Texture;
import java.util.ArrayList;

public class Ball {

	private static final Sphere sphere;

	static {
		/** Initialize the cannon ball geometry */
		sphere = new Sphere(32, 32, 0.39f, true, false);
	}

	private Material ballMaterial;
	private RigidBodyControl ballPhysicsControl;
	private Geometry ballGeometry;
         public ArrayList<Geometry> bolas = new ArrayList<Geometry>();

	public Ball(AssetManager assetManager, Node rootNode,
			BulletAppState bulletAppState, Vector3f startLocation, String w) {
            Material stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
            TextureKey key2 = new TextureKey(w);
            key2.setGenerateMips(true);
            Texture tex2 = assetManager.loadTexture(key2);
            stone_mat.setTexture("ColorMap", tex2);

		initMaterial(assetManager, w);
		ballGeometry = new Geometry("cannon ball", sphere);
		ballGeometry.setMaterial(stone_mat);
		rootNode.attachChild(ballGeometry);
		
		/** Position the cannon ball */
		ballGeometry.setLocalTranslation(startLocation);
		
		/** Add physical ball to physics space. */
		ballPhysicsControl = new RigidBodyControl(1f);
		ballGeometry.addControl(ballPhysicsControl);
		bulletAppState.getPhysicsSpace().add(ballPhysicsControl);
		ballPhysicsControl.setDamping(0.2f, 0.1f);
		ballPhysicsControl.setRestitution(1f);
                bolas.add(ballGeometry);
	}
	
	public Spatial getGeometry() {
		return ballGeometry;
	}

	private void initMaterial(AssetManager assetManager, String w) {
	
            	
	}

}
