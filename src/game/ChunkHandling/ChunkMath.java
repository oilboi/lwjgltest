package game.ChunkHandling;

import static game.Crafter.chunkRenderDistance;

public class ChunkMath {
    private final static short chunkSizeX = 16;
    private final static short chunkSizeY = 128;
    private final static short chunkSizeZ = 16;

    private final static int maxSize = (chunkRenderDistance*(4*chunkRenderDistance)+(chunkRenderDistance*4) + 1);

    public static int genHash(int x, int y, int z){
        return((x*chunkSizeY) + y + (z*(chunkSizeX * chunkSizeY)));
    }

    public static int[] getHash(int i) {
        int z = (int)(Math.floor(i/(chunkSizeX * chunkSizeY)));
        i %= (chunkSizeX * chunkSizeY);
        int x = (int)(Math.floor(i/chunkSizeY));
        i %= chunkSizeY;
        int y = (int)(Math.floor(i));
        int[] result = {x,y,z};
        return result;
    }

    public static int genChunkHash(int x, int z){
        return ((x+chunkRenderDistance) * ((chunkRenderDistance * 2 ) + 1)) + (z + chunkRenderDistance);
    }

    public static int[] getChunkHash(int i){
        int x = (i / ((chunkRenderDistance*2) + 1)) - chunkRenderDistance;
        i %= ((chunkRenderDistance*2) + 1);
        int z = i - chunkRenderDistance;
        return new int[]{x,z};
    }



    //todo this does not belong in here
    public static float getDistance(float x1, float y1, float z1, float x2, float y2, float z2){
        float x = x1 - x2;
        float y = y1 - y2;
        float z = z1 - z2;
        return (float)Math.hypot(x, Math.hypot(y,z));
    }


}
