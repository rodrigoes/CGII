package mygame;

import com.jme3.animation.LoopMode;
import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.collision.shapes.CapsuleCollisionShape;
import com.jme3.bullet.control.CharacterControl;
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
        /**
         * Initialize the cannon ball geometry
         */
        sphere = new Sphere(32, 32, 0.39f, true, false);
        sphere.setTextureMode(TextureMode.Projected);
        /**
         * Initialize the brick geometry
         */

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
        cam.setLocation(new Vector3f(0, 30, -20));

        cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
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
        wall.attachChild(makeWall(15, 4, -10.5f, 0.2f, 8, 20));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(-15, 4, -10.5f, 0.2f, 8, 20));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);

        wall = new Node("wall");
        wall.attachChild(makeWall(30, 4, 0, 0.f, 8, 15));
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
        /////
        Spatial Wardrobe = assetManager.loadModel("/Models/Others/Sideboard.obj");
        Wardrobe.scale(20);
        rootNode.attachChild(Wardrobe);

        
        PoolCue = assetManager.loadModel("/Models/Others/Poolcue.j3o");

        PoolCue.scale(5);
        PoolCue.setLocalTranslation(1.2f, -5.9f, -7.1f);

        taco = new RigidBodyControl(0.0f);

        PoolCue.addControl(taco);
        taco.setPhysicsLocation(PoolCue.getLocalTranslation());

        rootNode.attachChild(PoolCue);
        bulletAppState.getPhysicsSpace().add(taco);
        
        float a = 0.195f;
        float b = -2f;
        
        makeCannonBall(a * 5, 10, -14.5f, "Textures/branca.jpg");
        
        makeCannonBall(a * 1, 10, -14.5f, "Textures/branca.jpg");
        makeCannonBall(a * 3.5f, 10, -14.5f, "Textures/branca.jpg");
        makeCannonBall(0, b, -14.5f, "Textures/1.jpg");
        makeCannonBall(a, b, -14.5f, "Textures/2.jpg");
        makeCannonBall(a * 2, b, -14.5f, "Textures/3.jpg");
        makeCannonBall(a * 3, b, -14.5f, "Textures/4.jpg");
        makeCannonBall(a * 4, b, -14.5f, "Textures/5.jpg");
        makeCannonBall(0, b, -13.5f, "Textures/6.jpg");
        makeCannonBall(a, b, -13.5f, "Textures/7.jpg");
        makeCannonBall(a * 2, b, -13.5f, "Textures/8.jpg");
        makeCannonBall(a * 3, b, -13.5f, "Textures/9.jpg");
        makeCannonBall(0, b, -12.5f, "Textures/10.jpg");
        makeCannonBall(a, b, -12.5f, "Textures/11.jpg");
        makeCannonBall(a * 2, b, -12.5f, "Textures/12.jpg");
        makeCannonBall(0, b, -11.5f, "Textures/13.jpg");
        makeCannonBall(a, b, -11.5f, "Textures/14.jpg");
        makeCannonBall(a * 3, b, -10.5f, "Textures/15.jpg");
        // Ball.move(0, -5, -10.1f);

        VenetianBlind = assetManager.loadModel("/Models/Others/VenetianBlind.j3o");
        VenetianBlind.scale(0.3f);
        VenetianBlind.rotate(0, 1.5708f, 0);
        VenetianBlind.setLocalTranslation(0, 2, -29.6f);
        bulletAppState.getPhysicsSpace().add(VenetianBlind);
        rootNode.attachChild(VenetianBlind);

        
        //tamanho (tamanho x,tamanho y,tamanho z,textura,posicao x,posicao y,posicao z)
        MakeQuadro(2, 3, 0.1f,"Textures/monalisa.jpg",5, 5, -29.5f);
        MakeQuadro(2, 3, 0.1f,"Textures/palmeiras.jpg",-5, 5, -29.5f);
        MakeQuadro(1, 1, 0.1f,"Textures/glauco.jpg",-10, 5, -29.5f);
        

         //Wardrobe.setLocalTranslation(0, -4, -10.5f);
         //VenetianBlind.setLocalTranslation(0, 0, -29.6f);
         //Table.setLocalTranslation(0, -4, -10.5f);
         //PoolCue.setLocalTranslation(0, -3.9f, -10.5f);
         //Ball.setLocalTranslation(0, -4, -10.5f);
    }

    @Override
    public void simpleUpdate(float tpf) {
        //     setPoolCue();
       // System.out.println(PoolCue.getLocalTranslation().y);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    protected void makeFloor(float x, float y, float z) {
        
        MakeBox(15, 0.1f, 20,"Textures/piso2.jpg",x,y,z);

    }

    protected void makeTable() {

        //tamanho (tamanho x,tamanho y,tamanho z,textura,posicao x,posicao y,posicao z)
        MakeBox(9.88f, 0.2f, 4.6f, "Textures/Felt.jpg", 0, -2, -15.5f);

        MakeBox(11, 1, 5.5f, "Textures/Mahogany.jpg", 0, -3.2f, -15.5f);

        MakeBox(4.5f, -0.35f, 0.5f, "Textures/Mahogany.jpg", -4.5f, -1.39f, -10.5f);

        MakeBox(4, -0.35f, 0.5f, "Textures/Mahogany.jpg", 5, -1.39f, -10.5f);

        MakeBox(4.5f, -0.35f, 0.5f, "Textures/Mahogany.jpg", -4.5f, -1.39f, -20.5f);

        MakeBox(4, -0.35f, 0.5f, "Textures/Mahogany.jpg", 5, -1.39f, -20.5f);

        MakeBox(0.5f, -0.35f, 4, "Textures/Mahogany.jpg", 10.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 4, "Textures/Mahogany.jpg", -10.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 6, "Textures/Mahogany.jpg", 11.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 6, "Textures/Mahogany.jpg", -11.2f, -1.39f, -15.5f);

        MakeBox(11.7f, -0.35f, 0.5f, "Textures/Mahogany.jpg", 0f, -1.39f, -21.5f);

        MakeBox(11.7f, -0.35f, 0.5f, "Textures/Mahogany.jpg", 0f, -1.39f, -9.5f);

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
        spot.setSpotInnerAngle(15f * FastMath.DEG_TO_RAD);  // inner light cone (central beam)
        spot.setSpotOuterAngle(35f * FastMath.DEG_TO_RAD);  // outer light cone (edge of the light)
        spot.setColor(ColorRGBA.White.mult(5));             // light color
        spot.setPosition(new Vector3f(-1, 5, 5));           // shine from camera loc
        spot.setDirection(cam.getDirection());              // shine forward from camera loc
        rootNode.addLight(spot);

    }

    public void makeCannonBall(float x, float y, float z, String w) {

        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey(w);
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        /**
         * Create a cannon ball geometry and attach to scene graph.
         */
        Geometry ball_geo = new Geometry("cannon ball", sphere);
        ball_geo.setMaterial(stone_mat);
        rootNode.attachChild(ball_geo);
        /**
         * Position the cannon ball
         */
        ball_geo.setLocalTranslation(x, y, z);
        /**
         * Make the ball physcial with a mass > 0.0f
         */
        ball_phy = new RigidBodyControl(1f);
        /**
         * Add physical ball to physics space.
         */
        ball_geo.addControl(ball_phy);
        bulletAppState.getPhysicsSpace().add(ball_phy);
        /**
         * Accelerate the physcial ball to shoot it.
         */
        ball_phy.setLinearVelocity(cam.getDirection().mult(2.6f));

    }

    private void initTexture() {
        
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
        inputManager.addMapping("x", new KeyTrigger(keyInput.KEY_X));
        inputManager.addMapping("y", new KeyTrigger(keyInput.KEY_C));
        inputManager.addListener(actionListener, "play");
        inputManager.addListener(actionListener, "play2");
        inputManager.addListener(actionListener, "x");
        inputManager.addListener(actionListener, "y");
    }

    private ActionListener actionListener = new ActionListener() {
        int i = -6;
       

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

            if (name.equals("play") && !keyPressed) {
                i++;
                hitPoolCue(1.2f, -5.9f, i);
                
            }
            if (name.equals("play2") && !keyPressed) {
                i--;
                hitPoolCue(1.2f, -5.9f, i);
                
            }
            if (name.equals("x") && !keyPressed) {

                cam.setLocation(new Vector3f(0, 5, 20));

                cam.lookAt(new Vector3f(0, -0, 0), Vector3f.UNIT_Y);
            }
            if (name.equals("y") && !keyPressed) {

                cam.setLocation(new Vector3f(0, 30, -20));

                cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
            }

        }
    };

    private void MakeBox(float tamx, float tamy, float tamz, String texture, float posx, float posy, float posz) {

        Box a = new Box(tamx, tamy, tamz);
        Geometry geom = new Geometry("Box", a);

        Material mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex4 = assetManager.loadTexture(texture);
        mat.setTexture("ColorMap", monkeyTex4);
        geom.setMaterial(mat);

        geom.setLocalTranslation(posx, posy, posz);
        rootNode.attachChild(geom);

        RigidBodyControl r = new RigidBodyControl(0.0f);

        geom.addControl(r);

        r.setPhysicsLocation(geom.getLocalTranslation());

        bulletAppState.getPhysicsSpace().add(r);
    }
    
    private void MakeQuadro(float tamx,float tamy,float tamz,String texture,float posx,float posy,float posz){
        
      //Nao chamo o MakeBox por causa dos quadros nao terem fisica.
        Box quad = new Box(tamx, tamy, tamz);
        Geometry geoma = new Geometry("Box", quad);

        Material mats = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex6 = assetManager.loadTexture(texture);
        mats.setTexture("ColorMap", monkeyTex6);
        geoma.setMaterial(mats);
        geoma.setLocalTranslation(posx, posy, posz);

        rootNode.attachChild(geoma);

  
    }
    

    private void hitPoolCue(float x, float y, int z) {

        PoolCue.move(x, y, z);
        PoolCue.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(x, y, z));

    }

    private void initPhysics() {

        bulletAppState = new BulletAppState();
   //     bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);

    }

}
