package engine;

import engine.graph.Mesh;
import engine.graph.Texture;

import java.util.ArrayList;

public class Hud {
    private static Texture fontTextureAtlas;
    private static Mesh thisMesh;
    public static void initializeFontTextureAtlas() throws Exception {
        fontTextureAtlas = new Texture("textures/font.png");
    }

    private final static float scale = 0.00000002f;

    private static float currentScale = 1f;

    public static Mesh getHudMesh(){
        return thisMesh;
    }

    public static void createHudDebug(String text){
        ArrayList positions = new ArrayList();
        ArrayList textureCoord = new ArrayList();
        ArrayList indices = new ArrayList();
        ArrayList light = new ArrayList();


        int indicesCount = 0;

        //front
        positions.add(scale*13.5f * currentScale);
        positions.add(scale * currentScale);
        positions.add(0.0f); //z (how close it is to screen)

        positions.add(-scale*13.5f * currentScale);
        positions.add(scale * currentScale);
        positions.add(0.0f);

        positions.add(-scale*13.5f * currentScale);
        positions.add(-scale * currentScale);
        positions.add(0.0f);

        positions.add(scale*13.5f * currentScale);
        positions.add(-scale * currentScale);
        positions.add(0.0f);
        //front
        float frontLight = 1f;//getLight(x, y, z + 1, chunkX, chunkZ) / maxLight;

        //front
        for (int i = 0; i < 12; i++) {
            light.add(frontLight);
        }
        //front
        indices.add(0 + indicesCount);
        indices.add(1 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(0 + indicesCount);
        indices.add(2 + indicesCount);
        indices.add(3 + indicesCount);

        indicesCount += 4;

        //-x +x   -y +y
        // 0  1    2  3

        //front
        textureCoord.add(1f);//1
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(0f);//2
        textureCoord.add(0f);//0
        textureCoord.add(1f);//3
        textureCoord.add(1f);//1
        textureCoord.add(1f);//3


        //convert the position objects into usable array
        float[] positionsArray = new float[positions.size()];
        for (int i = 0; i < positions.size(); i++) {
            positionsArray[i] = (float) positions.get(i);
        }

        //convert the light objects into usable array
        float[] lightArray = new float[light.size()];
        for (int i = 0; i < light.size(); i++) {
            lightArray[i] = (float) light.get(i);
        }

        //convert the indices objects into usable array
        int[] indicesArray = new int[indices.size()];
        for (int i = 0; i < indices.size(); i++) {
            indicesArray[i] = (int) indices.get(i);
        }

        //convert the textureCoord objects into usable array
        float[] textureCoordArray = new float[textureCoord.size()];
        for (int i = 0; i < textureCoord.size(); i++) {
            textureCoordArray[i] = (float) textureCoord.get(i);
        }

        thisMesh = new Mesh(positionsArray, lightArray, indicesArray, textureCoordArray, fontTextureAtlas);

    }



}