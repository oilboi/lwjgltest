package game.chunk;

import engine.FastNoise;
import engine.graph.Mesh;

import java.util.Collection;
import java.util.HashMap;
import java.util.Map;

import static game.Crafter.getChunkRenderDistance;
import static game.chunk.ChunkUpdateHandler.chunkUpdate;

public class Chunk {

    private static final Map<String, ChunkObject> map = new HashMap<>();

    public static Collection<ChunkObject> getMap(){
        return map.values();
    }

    public static ChunkObject getChunk(int x, int z){
        return map.get(x + " " + z);
    }

    public static void setChunkMesh(int chunkX, int chunkZ, int yHeight, Mesh newMesh){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.mesh == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.mesh[yHeight] != null){
            thisChunk.mesh[yHeight].cleanUp(false);
        }
        thisChunk.mesh[yHeight] = newMesh;
    }

    public static void setChunkLiquidMesh(int chunkX, int chunkZ, int yHeight, Mesh newMesh){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.liquidMesh == null){
            newMesh.cleanUp(false);
            return;
        }
        if (thisChunk.liquidMesh[yHeight] != null){
            thisChunk.liquidMesh[yHeight].cleanUp(false);
        }
        thisChunk.liquidMesh[yHeight] = newMesh;
    }

    public static Mesh getChunkMesh(int chunkX, int chunkZ, int yHeight){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            return null;
        }
        if (thisChunk.mesh == null){
            return null;
        }
        if (thisChunk.mesh[yHeight] != null){
            return thisChunk.mesh[yHeight];
        }
        return null;
    }

    public static Mesh getChunkLiquidMesh(int chunkX, int chunkZ, int yHeight){
        ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
        if (thisChunk == null){
            return null;
        }
        if (thisChunk.liquidMesh == null){
            return null;
        }
        if (thisChunk.liquidMesh[yHeight] != null){
            return thisChunk.liquidMesh[yHeight];
        }
        return null;
    }

    public static int getBlock(int x,int y,int z){
        if (y > 127 || y < 0){
            return -1;
        }
        int chunkX = (int)Math.floor(x/16f);
        int chunkZ = (int)Math.floor(z/16f);
        int blockX = (int)(x - (16f*chunkX));
        int blockZ = (int)(z - (16f*chunkZ));
        String key = chunkX + " " + chunkZ;
        ChunkObject thisChunk = map.get(key);
        if (thisChunk == null){
            return -1;
        }
        if (thisChunk.block == null){
            return -1;
        }
        return thisChunk.block[y][blockX][blockZ];
    }

    public static void setBlock(int x,int y,int z, int newBlock){
        if (y > 127 || y < 0){
            return;
        }
        int yPillar = (int)Math.floor(y/16f);
        int chunkX = (int)Math.floor(x/16f);
        int chunkZ = (int)Math.floor(z/16f);
        int blockX = (int)(x - (16f*chunkX));
        int blockZ = (int)(z - (16f*chunkZ));
        String key = chunkX + " " + chunkZ;
        ChunkObject thisChunk = map.get(key);

        if (thisChunk == null){
            return;
        }
        if (thisChunk.block == null){
            return;
        }
        thisChunk.block[y][blockX][blockZ] = newBlock;
        chunkUpdate(chunkX,chunkZ,yPillar);
        updateNeighbor(chunkX, chunkZ,blockX,y,blockZ);
    }

    public static byte getLight(int x,int y,int z){
        if (y > 127 || y < 0){
            return 0;
        }
        int chunkX = (int)Math.floor(x/16f);
        int chunkZ = (int)Math.floor(z/16f);
        int blockX = (int)(x - (16f*chunkX));
        int blockZ = (int)(z - (16f*chunkZ));
        String key = chunkX + " " + chunkZ;
        ChunkObject thisChunk = map.get(key);
        if (thisChunk == null){
            return 0;
        }
        if (thisChunk.light == null){
            return 0;
        }
        return thisChunk.light[y][blockX][blockZ];
    }

    private static void updateNeighbor(int chunkX, int chunkZ, int x, int y, int z){
        if (y > 127 || y < 0){
            return;
        }
        int yPillar = (int)Math.floor(y/16f);
        switch (y){
            case 112:
            case 96:
            case 80:
            case 64:
            case 48:
            case 32:
            case 16:
                chunkUpdate(chunkX, chunkZ, yPillar-1);
                break;
            case 111:
            case 95:
            case 79:
            case 63:
            case 47:
            case 31:
            case 15:
                chunkUpdate(chunkX, chunkZ, yPillar+1);
                break;
        }
        if (x == 15){ //update neighbor
            chunkUpdate(chunkX+1, chunkZ, yPillar);
        }
        if (x == 0){
            chunkUpdate(chunkX-1, chunkZ, yPillar);
        }
        if (z == 15){
            chunkUpdate(chunkX, chunkZ+1, yPillar);
        }
        if (z == 0){
            chunkUpdate(chunkX, chunkZ-1, yPillar);
        }
    }

    private static void fullNeighborUpdate(int chunkX, int chunkZ){
        if (map.get(chunkX+1 + " " + chunkZ) != null){
            for (int y = 0; y < 8; y++){
                chunkUpdate(chunkX+1, chunkZ, y);
            }
        }
        if (map.get(chunkX-1 + " " + chunkZ) != null){
            for (int y = 0; y < 8; y++){
                chunkUpdate(chunkX-1, chunkZ, y);
            }
        }
        if (map.get(chunkX + " " + chunkZ+1) != null){
            for (int y = 0; y < 8; y++){
                chunkUpdate(chunkX, chunkZ+1, y);
            }
        }
        if (map.get(chunkX + " " + (chunkZ-1)) != null){
            for (int y = 0; y < 8; y++){
                chunkUpdate(chunkX, chunkZ-1, y);
            }
        }
    }


    public static void generateNewChunks(int currentChunkX, int currentChunkZ, int dirX, int dirZ){
        if (dirX != 0){
            for (int z = -getChunkRenderDistance() + currentChunkZ; z < getChunkRenderDistance() + currentChunkZ; z++){
                if (map.get((currentChunkX + (getChunkRenderDistance() * dirX)) + " " + z) == null) {
                    genBiome(currentChunkX + (getChunkRenderDistance() * dirX), z);
                    for (int y = 0; y < 8; y++) {
                        chunkUpdate(currentChunkX + (getChunkRenderDistance() * dirX), z, y);
                    }
                    fullNeighborUpdate(currentChunkX + (getChunkRenderDistance() * dirX), z);
                }
            }
        } else if (dirZ != 0){
            for (int x = -getChunkRenderDistance() + currentChunkX; x < getChunkRenderDistance() + currentChunkX; x++){
                if (map.get( x + " " + (currentChunkZ + (getChunkRenderDistance() * dirZ))) == null) {
                    genBiome(x, currentChunkZ + (getChunkRenderDistance() * dirZ));
                    for (int y = 0; y < 8; y++) {
                        chunkUpdate(x, currentChunkZ + (getChunkRenderDistance() * dirZ), y);
                    }
                    fullNeighborUpdate(x, currentChunkZ + (getChunkRenderDistance() * dirZ));
                }
            }
        }

        deleteOldChunks(currentChunkX+dirX, currentChunkZ+dirZ);
    }

    private static void deleteOldChunks(int chunkX, int chunkZ){
        HashMap<Integer, String> deletionQueue = new HashMap<>();
        int queueCounter = 0;
        for (ChunkObject thisChunk : map.values()){
            if (Math.abs(thisChunk.z - chunkZ) > getChunkRenderDistance() || Math.abs(thisChunk.x - chunkX) > getChunkRenderDistance()){
                if (thisChunk.mesh != null){
                    for (int y = 0; y < 8; y++) {
                        if (thisChunk.mesh[y] != null){
                            thisChunk.mesh[y].cleanUp(false);
                        }
                    }
                }
                deletionQueue.put(queueCounter, thisChunk.x + " " + thisChunk.z);
                queueCounter++;
            }
        }
        for (String thisString : deletionQueue.values()){
            map.remove(thisString);
        }
    }

    private static final FastNoise noise = new FastNoise();
    private static final int heightAdder = 40;
    private static final byte dirtHeight = 4;
    private static final byte waterHeight = 50;

    public static void genBiome(int chunkX, int chunkZ){
            short currBlock;
            byte height;
            ChunkObject thisChunk = map.get(chunkX + " " + chunkZ);
            if (thisChunk == null){
                map.put(chunkX + " " + chunkZ, new ChunkObject(chunkX, chunkZ));
            }
            thisChunk = map.get(chunkX + " " + chunkZ);
            for (int x = 0; x < 16; x++) {
                for (int z = 0; z < 16; z++) {
                    float dirtHeightRandom = (float)Math.floor(Math.random() * 2f);
                    height = (byte)(Math.abs(noise.GetCubicFractal((chunkX*16)+x,(chunkZ*16)+z))*127+heightAdder);
                    for (int y = 127; y >= 0; y--) {
                    if (y <= 0 + dirtHeightRandom) {
                        currBlock = 5;
                    } else if (y == height) {
                        currBlock = 2;
                    } else if (y < height && y >= height - dirtHeight - dirtHeightRandom) {
                        currBlock = 1;
                    } else if (y < height - dirtHeight) { //TODO: stone level
                        if (y <= 30 && y > 0) {
                            if (Math.random() > 0.95) {
                                currBlock = (short) Math.floor(8 + (Math.random() * 8));
                            } else {
                                currBlock = 3;
                            }
                        } else {
                            currBlock = 3;
                        }
                    } else {
                        if (y <= waterHeight) {
                            currBlock = 7;
                        } else {
                            currBlock = 0;
                        }
                    }

                    thisChunk.block[y][x][z] = currBlock;

    //            if (currBlock == 0) {
                    thisChunk.light[y][x][z] = 15;//0;
    //            }else{
    //                light[chunkX][chunkZ][y][x][z] = 0;
                }
            }
        }
    }
    public static void cleanUp(){
        for (ChunkObject thisChunk : map.values()){
            if (thisChunk == null){
                continue;
            }
            if (thisChunk.mesh != null){
                for (Mesh thisMesh : thisChunk.mesh){
                    if (thisMesh != null){
                        thisMesh.cleanUp(true);
                    }
                }
            }
        }
    }
}
