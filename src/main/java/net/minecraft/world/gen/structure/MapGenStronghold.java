package net.minecraft.world.gen.structure;

import com.google.common.collect.Lists;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.Map.Entry;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.ChunkCoordIntPair;
import net.minecraft.world.World;
import net.minecraft.world.biome.BiomeGenBase;

public class MapGenStronghold extends MapGenStructure
{
    private List<BiomeGenBase> validBiomes;

    /**
     * is spawned false and set true once the defined BiomeGenBases were compared with the present ones
     */
    private boolean ranBiomeCheck;
    private ChunkCoordIntPair[] structureCoords;
    private double strongholdDistance;
    private int strongholdSpread;

    public MapGenStronghold()
    {
        this.structureCoords = new ChunkCoordIntPair[3];
        this.strongholdDistance = 32.0D;
        this.strongholdSpread = 3;
        this.validBiomes = Lists.<BiomeGenBase>newArrayList();

        for (BiomeGenBase biome : BiomeGenBase.getBiomeGenArray())
        {
            if (biome != null && biome.minHeight > 0.0F)
            {
                this.validBiomes.add(biome);
            }
        }
    }

    public MapGenStronghold(Map<String, String> configMap)
    {
        this();

        for (Entry<String, String> entry : configMap.entrySet())
        {
            if (((String)entry.getKey()).equals("distance"))
            {
                this.strongholdDistance = MathHelper.parseDoubleWithDefaultAndMax((String)entry.getValue(), this.strongholdDistance, 1.0D);
            }
            else if (((String)entry.getKey()).equals("count"))
            {
                this.structureCoords = new ChunkCoordIntPair[MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.structureCoords.length, 1)];
            }
            else if (((String)entry.getKey()).equals("spread"))
            {
                this.strongholdSpread = MathHelper.parseIntWithDefaultAndMax((String)entry.getValue(), this.strongholdSpread, 1);
            }
        }
    }

    public String getStructureName()
    {
        return "Stronghold";
    }

    protected boolean canSpawnStructureAtCoords(int chunkX, int chunkZ)
    {
        if (!this.ranBiomeCheck)
        {
            Random random = new Random();
            random.setSeed(this.worldObj.getSeed());
            double angle = random.nextDouble() * Math.PI * 2.0D;
            int ringNumber = 1;

            for (int strongholdIndex = 0; strongholdIndex < this.structureCoords.length; ++strongholdIndex)
            {
                double distance = (1.25D * (double)ringNumber + random.nextDouble()) * this.strongholdDistance * (double)ringNumber;
                int strongholdChunkX = (int)Math.round(Math.cos(angle) * distance);
                int strongholdChunkZ = (int)Math.round(Math.sin(angle) * distance);
                BlockPos blockpos = this.worldObj.getWorldChunkManager().findBiomePosition((strongholdChunkX << 4) + 8, (strongholdChunkZ << 4) + 8, 112, this.validBiomes, random);

                if (blockpos != null)
                {
                    strongholdChunkX = blockpos.getX() >> 4;
                    strongholdChunkZ = blockpos.getZ() >> 4;
                }

                this.structureCoords[strongholdIndex] = new ChunkCoordIntPair(strongholdChunkX, strongholdChunkZ);
                angle += (Math.PI * 2D) * (double)ringNumber / (double)this.strongholdSpread;

                if (strongholdIndex == this.strongholdSpread)
                {
                    ringNumber += 2 + random.nextInt(5);
                    this.strongholdSpread += 1 + random.nextInt(2);
                }
            }

            this.ranBiomeCheck = true;
        }

        for (ChunkCoordIntPair chunkCoord : this.structureCoords)
        {
            if (chunkX == chunkCoord.chunkXPos && chunkZ == chunkCoord.chunkZPos)
            {
                return true;
            }
        }

        return false;
    }

    protected List<BlockPos> getCoordList()
    {
        List<BlockPos> list = Lists.<BlockPos>newArrayList();

        for (ChunkCoordIntPair chunkCoord : this.structureCoords)
        {
            if (chunkCoord != null)
            {
                list.add(chunkCoord.getCenterBlock(64));
            }
        }

        return list;
    }

    protected StructureStart getStructureStart(int chunkX, int chunkZ)
    {
        MapGenStronghold.Start mapgenstronghold$start;

        for (mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ); mapgenstronghold$start.getComponents().isEmpty() || ((StructureStrongholdPieces.Stairs2)mapgenstronghold$start.getComponents().get(0)).strongholdPortalRoom == null; mapgenstronghold$start = new MapGenStronghold.Start(this.worldObj, this.rand, chunkX, chunkZ))
        {
            ;
        }

        return mapgenstronghold$start;
    }

    public static class Start extends StructureStart
    {
        public Start()
        {
        }

        public Start(World worldIn, Random random, int chunkX, int chunkZ)
        {
            super(chunkX, chunkZ);
            StructureStrongholdPieces.prepareStructurePieces();
            StructureStrongholdPieces.Stairs2 structurestrongholdpieces$stairs2 = new StructureStrongholdPieces.Stairs2(0, random, (chunkX << 4) + 2, (chunkZ << 4) + 2);
            this.components.add(structurestrongholdpieces$stairs2);
            structurestrongholdpieces$stairs2.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            List<StructureComponent> pendingComponents = structurestrongholdpieces$stairs2.field_75026_c;

            while (!pendingComponents.isEmpty())
            {
                int randomIndex = random.nextInt(pendingComponents.size());
                StructureComponent structureComponent = (StructureComponent)pendingComponents.remove(randomIndex);
                structureComponent.buildComponent(structurestrongholdpieces$stairs2, this.components, random);
            }

            this.updateBoundingBox();
            this.markAvailableHeight(worldIn, random, 10);
        }
    }
}