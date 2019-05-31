package mygame;


import com.jme3.app.SimpleApplication;
import com.jme3.asset.TextureKey;
import com.jme3.audio.AudioData;
import com.jme3.audio.AudioNode;
import com.jme3.bounding.BoundingVolume;
import com.jme3.bullet.BulletAppState;
import com.jme3.bullet.control.RigidBodyControl;
import com.jme3.collision.CollisionResults;
import com.jme3.font.BitmapText;
import com.jme3.input.ChaseCamera;
import com.jme3.input.MouseInput;
import com.jme3.input.controls.ActionListener;
import com.jme3.input.controls.KeyTrigger;
import com.jme3.input.controls.MouseButtonTrigger;
import com.jme3.light.Light;
import com.jme3.light.PointLight;
import com.jme3.material.Material;
import com.jme3.math.ColorRGBA;
import com.jme3.math.Quaternion;
import com.jme3.math.Vector3f;
import com.jme3.renderer.RenderManager;
import com.jme3.scene.Geometry;
import com.jme3.scene.Node;
import com.jme3.scene.Spatial;
import com.jme3.scene.shape.Box;
import com.jme3.scene.shape.Sphere;
import com.jme3.scene.shape.Sphere.TextureMode;
import com.jme3.texture.Texture;
import java.util.ArrayList;

/**
 * This is the Main Class of your Game. You should only do initialization here.
 * Move your Logic into AppStates or Controls
 *
 * @author rodri
 */
public class Main extends SimpleApplication {

    private Spatial PoolCue;
    private Spatial Teste;
    private Spatial Table;
    private Spatial Barreira;
    private Spatial Ball;
    private Spatial Ball2;
    private Spatial Ball3;
    private Spatial Ball4;
    private Spatial VenetianBlind;
    private BulletAppState bulletAppState;
    private AudioNode music;
    private AudioNode batidataco;
    private AudioNode batidabola;
    private RigidBodyControl ball_phy;
    private RigidBodyControl taco;
    private RigidBodyControl teste;
    private static final Sphere sphere;
    Material stone_mat;
    Material arcade;
    Material mat;
    Material tecido;
    Material madeira;
    Material piso;
    private ArrayList<Geometry> bolas = new ArrayList<Geometry>();
    private Ball whiteBall;
    static {
        /**
         * Initialize the cannon ball geometry
         */
        sphere = new Sphere(50, 50, 0.39f, true, false);
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
    private Node wall1;
    private Node wall2;
 
    private Node wall3;
    @Override
    public void simpleInitApp() {

        initPhysics();
        initLights();
        initWhiteBall();
  
        initFlyCamera(whiteBall.getGeometry());
        initKeys();
        initBalls();
        
      
        initAudio();
        initTexture();
        //makeTable();
        initTable();
        flyCam.setMoveSpeed(60);
        cam.setLocation(new Vector3f(-3.204769E-4f, 19.85392f, -18.305336f));

      //  cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
   
        shootables = new Node("Shootables");
        shootables.attachChild(makeFloor(-5, -4, -12.5f));
        bulletAppState.getPhysicsSpace().add(shootables);
        rootNode.attachChild(shootables);

        wall = new Node("wall");
        wall.attachChild(makeWall(30, 4, -10.5f, 0.2f, 8, 35));
        bulletAppState.getPhysicsSpace().add(wall);
        rootNode.attachChild(wall);
        //MakeBox(11, 1, 5.5f, "Textures/Mahogany.jpg", 0, -3.2f, -15.5f);
  
        wall3 = new Node("wall3");
        wall3.attachChild(makeWall(-35, 4, -10.5f, 0.2f, 8, 35));
        bulletAppState.getPhysicsSpace().add(wall3);
        rootNode.attachChild(wall3);

        wall1 = new Node("wall");
        wall1.attachChild(makeWall(-22, 4, 5, 0, 8, 40));
        wall1.rotate(0, 1.5708f, 0); //90 graus 1.5708 rad
        bulletAppState.getPhysicsSpace().add(wall1);
        rootNode.attachChild(wall1);        //////
        
        wall2 = new Node("wall");
        wall2.attachChild(makeWall(30, 4, 0, 0, 8, 35));
        wall2.rotate(0, 1.5708f, 0); //90 graus 1.5708 rad
        bulletAppState.getPhysicsSpace().add(wall2);
        rootNode.attachChild(wall2); 


      
        Spatial Wardrobe = assetManager.loadModel("/Models/Others/Sideboard.obj");
        Wardrobe.scale(20);
        rootNode.attachChild(Wardrobe);


        VenetianBlind = assetManager.loadModel("/Models/Others/Arcade machine.j3o");
        VenetianBlind.scale(0.06f);
        VenetianBlind.setMaterial(arcade);

        VenetianBlind.rotate(0, 0, 0);
        VenetianBlind.setLocalTranslation(0, -4, -27.6f);
        bulletAppState.getPhysicsSpace().add(VenetianBlind);
        rootNode.attachChild(VenetianBlind);

        //tamanho (tamanho x,tamanho y,tamanho z,textura,posicao x,posicao y,posicao z)
        MakeQuadro(2, 3, 0.1f, "Textures/monalisa.jpg", 5, 5, -29.5f);
        MakeQuadro(2, 3, 0.1f, "Textures/palmeiras.jpg", -5, 5, -29.5f);
        MakeQuadro(2, 2, 0.1f, "Textures/glauco.jpg", -10, 5, -29.5f);

    }
    private CollisionResults results = new CollisionResults();

    @Override
    public void simpleUpdate(float tpf) {
      //  System.out.println(cam.getLocation());
       
      //  System.out.println(cam.getRotation());
//        colisaoBolaChao(); 
     //   colisaoBolaTaco();
        guiFont = assetManager.loadFont("Interface/Fonts/Default.fnt");
         BitmapText text1 = new BitmapText(guiFont, false);
        text1.setSize(guiFont.getCharSet().getRenderedSize());
        text1.setText("Controle com o botao esquerdo do mause,segure mire e solte");
        text1.setLocalTranslation(1500, 50, 0);
        guiNode.attachChild(text1);
        //setPoolCue();
        // taco.setPhysicsLocation(PoolCue.getLocalTranslation());
        // System.out.println(PoolCue.getLocalTranslation().y);
    }

    @Override
    public void simpleRender(RenderManager rm) {
        //TODO: add render code
    }

    protected Geometry makeFloor(float x, float y, float z) {

 
        Box a = new Box(35, 0.1f, 35);
        Geometry geom = new Geometry("Box", a);
        geom.setMaterial(piso);

        geom.setLocalTranslation(x, y, z);

        return geom;

    }

    protected void makeTable() {

        //tamanho (tamanho x,tamanho y,tamanho z,textura,posicao x,posicao y,posicao z)
        MakeBox(9.88f, 0.2f, 4.6f, "Textures/Felt.jpg", 0, -2, -15.5f);

        //   MakeBox(11, 1, 5.5f, "Textures/Mahogany.jpg", 0, -3.2f, -15.5f);
        //    MakeRemove(2, -14.2f, -18f);
        //   MakeRemove(-12, -14.2f, -22f);
        MakeBox(4.5f, -0.35f, 0.5f, "Textures/Felt.jpg", -4.5f, -1.39f, -10.5f);

        MakeBox(4, -0.35f, 0.5f, "Textures/Felt.jpg", 5, -1.39f, -10.5f);

        MakeBox(4.5f, -0.35f, 0.5f, "Textures/Felt.jpg", -4.5f, -1.39f, -20.5f);

        MakeBox(4, -0.35f, 0.5f, "Textures/Felt.jpg", 5, -1.39f, -20.5f);

        MakeBox(0.5f, -0.35f, 4, "Textures/Felt.jpg", 10.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 4, "Textures/Felt.jpg", -10.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 6, "Textures/Mahogany.jpg", 11.2f, -1.39f, -15.5f);

        MakeBox(0.5f, -0.35f, 6, "Textures/Mahogany.jpg", -11.2f, -1.39f, -15.5f);

        MakeBox(11.7f, -0.35f, 0.5f, "Textures/Mahogany.jpg", 0f, -1.39f, -21.5f);

        MakeBox(11.7f, -0.35f, 0.5f, "Textures/Mahogany.jpg", 0f, -1.39f, -9.5f);

    }

//(10, 1, -10.5f)
    protected Geometry makeWall(float xpos, float ypos, float zpos, float xtam, float ytam, float ztam) {

        Box a = new Box(xtam, ytam, ztam);
        Geometry geom = new Geometry("Box", a);
        geom.setMaterial(mat);

        geom.setLocalTranslation(xpos, ypos, zpos);

        return geom;

    }
    
    private void initBalls() {
        for (int i = 0; i <= 3; ++i) {
            for (int j = 0; j <= i; ++j) {
                new Ball(assetManager, rootNode, bulletAppState,
                        new Vector3f(10f + i * .87f, 1f, -i * .5f + j), "Textures/3.jpg");
            }
        }

        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(14f, 0f, 0f), "Textures/2.jpg");

        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(9f, 0f, 0f), "Textures/3.jpg");
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(0f, 0f, 0f), "Textures/4.jpg");
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, 0f), "Textures/5.jpg");
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, -3f), "Textures/6.jpg");
        new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-10f, 0f, 3f), "Textures/7.jpg");
    }


    
         private void initTable() {
              
        Spatial table = assetManager.loadModel("/Models/table2.j3o");
        table.setLocalTranslation(0, -2, 0);
        RigidBodyControl tableBodyControl = new RigidBodyControl(0.0f);
        table.addControl(tableBodyControl);
        bulletAppState.getPhysicsSpace().add(tableBodyControl);
        tableBodyControl.setRestitution(0f);
        rootNode.attachChild(table);
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
        bolas.add(ball_geo);
    }

    private void initTexture() {

        stone_mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        TextureKey key2 = new TextureKey("Textures/5.jpg");
        key2.setGenerateMips(true);
        Texture tex2 = assetManager.loadTexture(key2);
        stone_mat.setTexture("ColorMap", tex2);

        mat = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex = assetManager.loadTexture("Textures/paredee.jpg");
        mat.setTexture("ColorMap", monkeyTex);

        arcade = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex2 = assetManager.loadTexture("Models/Others/AM_Screen.jpg");
        arcade.setTexture("ColorMap", monkeyTex2);
        
        piso = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex5 = assetManager.loadTexture("Textures/piso2.jpg");
        piso.setTexture("ColorMap", monkeyTex5);

        madeira = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex3 = assetManager.loadTexture("Textures/Mahogany.jpg");
        madeira.setTexture("ColorMap", monkeyTex3);

        tecido = new Material(assetManager, "Common/MatDefs/Misc/Unshaded.j3md");
        Texture monkeyTex4 = assetManager.loadTexture("Textures/Felt.jpg");
        tecido.setTexture("ColorMap", monkeyTex4);

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

        batidataco = new AudioNode(assetManager, "Sounds/batidataco.wav", AudioData.DataType.Buffer);
        batidataco.setPositional(false);
        //   batidataco.setLooping(true);
        batidataco.setVolume(3);
        rootNode.attachChild(batidataco);

        batidabola = new AudioNode(assetManager, "Sounds/bolasbatendo.wav", AudioData.DataType.Buffer);
        batidabola.setPositional(false);
        //   batidabola.setLooping(true);
        batidabola.setVolume(3);
        rootNode.attachChild(batidabola);

    }

    private void initKeys() {
        inputManager.addMapping("shoot", new MouseButtonTrigger(MouseInput.BUTTON_LEFT));
      //  inputManager.addMapping("play2", new MouseButtonTrigger(mouseInput.BUTTON_RIGHT));
        
       
        inputManager.addListener(actionListener, "shoot");
       
              
    }

    private ActionListener actionListener = new ActionListener() {
        int i = -6;

        @Override
        public void onAction(String name, boolean keyPressed, float tpf) {

           if (name.equals("shoot") && !keyPressed) {
                    hitWhiteBall();
                    batidataco.playInstance();
                }
           /* if (name.equals("play2") && !keyPressed) {
                i--;
                hitPoolCue(1.2f, -5.9f, i);

            }*/
            /*
            if (name.equals("1") && !keyPressed) {

                cam.setLocation(new Vector3f(-3.204769E-4f, 19.85392f, -18.305336f));

                cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
            }
            if (name.equals("2") && !keyPressed) {

                cam.setLocation(new Vector3f(0.09246415f, 5.9476595f, 3.7059166f));

                cam.lookAt(new Vector3f(0,5,0), Vector3f.UNIT_Y);
            }
            if (name.equals("3") && !keyPressed) {

                cam.setLocation(new Vector3f(13.952617f, 8.124902f, -22.604095f));

                cam.lookAt(new Vector3f(0, 0, -17), Vector3f.UNIT_Y);
            }
             if (name.equals("4") && !keyPressed) {

                cam.setLocation(new Vector3f(0, 30, -20));

                cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
            }
              if (name.equals("5") && !keyPressed) {

                cam.setLocation(new Vector3f(0, 30, -20));

                cam.lookAt(new Vector3f(0, -90, 0), Vector3f.UNIT_Y);
            }*/

        }
    };
/*
    private void colisaoBolaChao() {
        for (int i = 0; i < bolas.size(); i++) {

            CollisionResults results = new CollisionResults();
            BoundingVolume bv = whiteBall.getWorldBound();
            shootables.collideWith(bv, results);

            if (results.size() > 0) {
                whiteBall.removeFromParent();
              
             //
                //batidataco.playInstance();
            }
        }
    }
    */
    

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

    private void MakeQuadro(float tamx, float tamy, float tamz, String texture, float posx, float posy, float posz) {

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

    private void MakeRemove(float posx, float posy, float posz) {

        Teste = assetManager.loadModel("/Models/Others/colisao.j3o");

        Teste.scale(15);
        Teste.setMaterial(madeira);
        //PoolCue.rotate(0,180,0);
        Teste.setLocalTranslation(posx, posy, posz);
        //setPoolCue();
        teste = new RigidBodyControl(0.0f);

        Teste.addControl(teste);
        teste.setPhysicsLocation(Teste.getLocalTranslation());

        rootNode.attachChild(Teste);
        bulletAppState.getPhysicsSpace().add(teste);
    }

    private void hitPoolCue(float x, float y, int z) {

        PoolCue.move(x, y, z);
        PoolCue.getControl(RigidBodyControl.class).setPhysicsLocation(new Vector3f(x, y, z));

    }

    private void initPhysics() {

        bulletAppState = new BulletAppState();
      //  bulletAppState.setDebugEnabled(true);
        stateManager.attach(bulletAppState);

    }
    private void initWhiteBall() {
        
        whiteBall = new Ball(assetManager, rootNode, bulletAppState,
                new Vector3f(-14f, 0f, 0f), "Textures/branca.jpg");
    }
    private void initFlyCamera(Spatial target) {
        
        cam.setLocation(new Vector3f());
        flyCam.setEnabled(false);
        ChaseCamera chaseCam = new ChaseCamera(cam, target, inputManager);
        chaseCam.setSmoothMotion(true);
    }
    
 
      private void initLights() {
          
        initLight(-15, 10, -5);
        initLight(-15, 10, 5);
        initLight(15, 10, 5);
        initLight(15, 10, -5);
    }
      
        private Light initLight(int posX, int posY, int posZ) {
        PointLight light = new PointLight();
        light.setPosition(new Vector3f(posX, posY, posZ));
        light.setColor(ColorRGBA.LightGray);
        rootNode.addLight(light);
        return light;
    }
        
        private void hitWhiteBall() {
        whiteBall.getGeometry().getControl(RigidBodyControl.class).applyCentralForce(cam.getDirection().setY(0).normalize().mult(1000));
    }
     
}
