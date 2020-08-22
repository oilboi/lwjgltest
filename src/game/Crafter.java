package game;

import engine.GameItem;
import engine.IGameLogic;
import engine.MouseInput;
import engine.Window;
import engine.graph.Camera;
import game.ChunkHandling.Chunk;
import game.ChunkHandling.ChunkData;
import game.player.Player;
import org.joml.Vector2f;
import org.joml.Vector3f;

import java.util.ArrayList;

import static game.ChunkHandling.ChunkMesh.generateChunkMesh;
import static org.lwjgl.glfw.GLFW.*;

public class Crafter implements IGameLogic {

    public final static int chunkRenderDistance = 2;

    private static final float MOUSE_SENSITIVITY = 0.01f;

    private final Vector3f cameraInc;

    private final Renderer renderer;

    private final Camera camera;

    private GameItem[] gameItems;

    private static final float CAMERA_POS_STEP = 0.5f;

    private boolean buttonPushed = false;

    private String[] chunkNames;

    private Player player;

    public static int getChunkRenderDistance(){
        return chunkRenderDistance;
    }

    public Crafter(){
        renderer = new Renderer();
        camera = new Camera();
        cameraInc = new Vector3f();
    }

    @Override
    public void init(Window window) throws Exception{
        renderer.init(window);


        ArrayList items = new ArrayList();
        ArrayList names = new ArrayList();
        for (int x = -chunkRenderDistance; x <= chunkRenderDistance; x++){
            for (int z = -chunkRenderDistance; z <= chunkRenderDistance; z++) {
                Chunk chunk = new Chunk(x, z);
                ChunkData.storeChunk(x, z, chunk);
                generateChunkMesh(chunk, x, z, items, names, false);
                names.add(x + " " + z);
                System.out.println(x + " " + z);
            }
        }

        //convert the position objects into usable array
        GameItem[] itemsArray = new GameItem[items.size()];
        chunkNames = new String[names.size()];
        for (int i = 0; i < items.size(); i++) {
            itemsArray[i] = (GameItem)items.get(i);
            chunkNames[i] = (String)names.get(i);
        }

        gameItems = itemsArray;

        player = new Player();
    }

    @Override
    public void input(Window window, MouseInput input){
        boolean keyIsPressed = false;
        if (window.isKeyPressed(GLFW_KEY_W)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)Math.PI;
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }
        if (window.isKeyPressed(GLFW_KEY_S)){
            //no mod needed
            float yaw = (float)Math.toRadians(camera.getRotation().y);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }

        if (window.isKeyPressed(GLFW_KEY_A)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) + (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }
        if (window.isKeyPressed(GLFW_KEY_D)){
            float yaw = (float)Math.toRadians(camera.getRotation().y) - (float)(Math.PI /2);
            float x = (float)Math.sin(-yaw);
            float z = (float)Math.cos(yaw);
            player.addInertia(x,0,z);
        }



        if (window.isKeyPressed(GLFW_KEY_LEFT_SHIFT)){
            //cameraInc.y = -1;
        } else if (window.isKeyPressed(GLFW_KEY_SPACE)){
            //cameraInc.y = 1;
        }

        //prototype toggle locking mouse - F KEY
        if (window.isKeyPressed(GLFW_KEY_F)) {
            if (!buttonPushed) {
                input.setMouseLocked(!input.isMouseLocked());
                buttonPushed = true;
                if(!input.isMouseLocked()) {
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_NORMAL);
                } else{
                    glfwSetInputMode(window.getWindowHandle(), GLFW_CURSOR, GLFW_CURSOR_HIDDEN);
                }
            }
        } else if (!window.isKeyPressed(GLFW_KEY_F)){
            buttonPushed = false;
        }
    }

    @Override
    public void update(float interval, MouseInput mouseInput){

        //update camera position
//        camera.movePosition(cameraInc.x * CAMERA_POS_STEP,
//                cameraInc.y * CAMERA_POS_STEP,
//                cameraInc.z * CAMERA_POS_STEP);

        camera.setPosition(player.getPosWithEyeHeight().x, player.getPosWithEyeHeight().y, player.getPosWithEyeHeight().z);

        //update camera based on mouse
        Vector2f rotVec = mouseInput.getDisplVec();
        camera.moveRotation(rotVec.x * MOUSE_SENSITIVITY, rotVec.y * MOUSE_SENSITIVITY, 0);
        //limit camera pitch
        if (camera.getRotation().x < -90f) {
            camera.moveRotation((90f + camera.getRotation().x) * -1f, 0, 0);
        }
        if (camera.getRotation().x > 90f){
            camera.moveRotation((camera.getRotation().x - 90f) * -1f , 0, 0);
        }

        //loop camera yaw
        if (camera.getRotation().y < -180f){
            camera.moveRotation(0,360, 0);
        }
        if (camera.getRotation().y > 180f){
            camera.moveRotation(0,-360, 0);
        }

        player.onTick();

//        System.out.println(Player.getPos().y);

//        for (int i = 0; i < chunkNames.length; i++){
//            if (chunkNames[i].equals("5 1")){
//                System.out.println(gameItems[i]);
//                gameItems[i].setPosition(0,gameItems[i].getPosition().y + 0.001f, 0);
//                break;
//            }
//        }

        //this is the game item loop
        for (GameItem gameItem : gameItems){
            //update position
//            Vector3f itemPos = gameItem.getPosition();
//            float posx = itemPos.x + displxInc * 0.01f;
//            float posy = itemPos.y + displyInc * 0.01f;
//            float posz = itemPos.z + displzInc * 0.01f;

//            gameItem.setPosition(posx, posy, posz);

            //Update scale
//            float scale = gameItem.getScale();
//            scale += scaleInc * 0.05f;
//            if (scale < 0) {
//                scale = 0;
//            }
//            gameItem.setScale(scale);

            //gameItem.setPosition((float)Math.random()-0.5f,(float)Math.random()-0.5f,-2f);

            //update rotation angle
//            float rotation = gameItem.getRotation().y - 1.5f;
//
//
//            if (rotation > 360) {
//                rotation -= 360;
//            }
//
//            gameItem.setRotation(rotation, rotation, rotation);
        }
    }

    @Override
    public void render(Window window){
        renderer.render(window, camera, gameItems);
    }

    @Override
    public void cleanup(){
        renderer.cleanup();
        for (GameItem gameItem : gameItems){
            gameItem.getMesh().cleanUp();
        }
    }
}
