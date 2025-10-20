package net.minecraft.world.gen;

import com.google.common.base.Objects;
import java.util.Random;
import net.minecraft.block.BlockSand;
import net.minecraft.block.material.Material;
import net.minecraft.block.state.IBlockState;
import net.minecraft.init.Blocks;
import net.minecraft.util.BlockPos;
import net.minecraft.util.MathHelper;
import net.minecraft.world.World;
import net.minecraft.world.chunk.ChunkPrimer;

public class MapGenCaves extends MapGenBase
{
    // Carves a spherical/elliptical "room" at the given world position
    protected void carveRoom(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double roomCenterX, double roomCenterY, double roomCenterZ)
    {
        this.carveTunnel(seed, chunkX, chunkZ, primer, roomCenterX, roomCenterY, roomCenterZ, GenConfig.ROOM_RADIUS_BASE + this.rand.nextFloat() * GenConfig.ROOM_RADIUS_VARIATION, 0.0F, 0.0F, -1, -1, GenConfig.ROOM_VERTICAL_SCALE);
    }

    // Carves a meandering tunnel through the chunk, optionally branching
    protected void carveTunnel(long seed, int chunkX, int chunkZ, ChunkPrimer primer, double currentX, double currentY, double currentZ, float tunnelRadius, float yaw, float pitch, int currentStep, int maxSteps, double verticalScale)
    {
        double chunkCenterX = (double)(chunkX * 16 + 8);
        double chunkCenterZ = (double)(chunkZ * 16 + 8);
        float yawChange = 0.0F;
        float pitchChange = 0.0F;
        Random random = new Random(seed);

        if (maxSteps <= 0)
        {
            int max = this.range * 16 - 16;
            maxSteps = max - random.nextInt(max / GenConfig.TUNNEL_LENGTH_DIVISOR);
        }

        boolean isRootSegment = false;

        if (currentStep == -1)
        {
            currentStep = maxSteps / 2;
            isRootSegment = true;
        }

        int branchStep = random.nextInt(maxSteps / 2) + maxSteps / 4;

        boolean makeWide = random.nextInt(GenConfig.WIDE_TUNNEL_CHANCE_DENOMINATOR) == 0;
        for (; currentStep < maxSteps; ++currentStep)
        {
            double sinValue = MathHelper.sin((float)currentStep * (float)Math.PI / (float)maxSteps);
            double horizontalRadius = GenConfig.TUNNEL_RADIUS_BASE + (double)(sinValue * tunnelRadius * GenConfig.TUNNEL_RADIUS_VARIATION_MULTIPLIER);
            double verticalRadius = horizontalRadius * verticalScale;
            float cosPitch = MathHelper.cos(pitch);
            float sinPitch = MathHelper.sin(pitch);
            currentX += (double)(MathHelper.cos(yaw) * cosPitch);
            currentY += (double)sinPitch;
            currentZ += (double)(MathHelper.sin(yaw) * cosPitch);

            if (makeWide)
            {
                pitch = pitch * GenConfig.WIDE_TUNNEL_PITCH_DECAY;
            }
            else
            {
                pitch = pitch * GenConfig.NORMAL_TUNNEL_PITCH_DECAY;
            }

            pitch = pitch + pitchChange * 0.1F;
            yaw += yawChange * 0.1F;
            pitchChange = pitchChange * 0.9F;
            yawChange = yawChange * 0.75F;
            pitchChange = pitchChange + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 2.0F;
            yawChange = yawChange + (random.nextFloat() - random.nextFloat()) * random.nextFloat() * 4.0F;

            if (!isRootSegment && currentStep == branchStep && tunnelRadius > GenConfig.BRANCH_RADIUS_THRESHOLD && maxSteps > 0)
            {
                this.carveTunnel(random.nextLong(), chunkX, chunkZ, primer, currentX, currentY, currentZ, random.nextFloat() * GenConfig.BRANCH_RADIUS_VARIATION + GenConfig.BRANCH_RADIUS_BASE, yaw - ((float)Math.PI / 2F), pitch / 3.0F, currentStep, maxSteps, 1.0D);
                this.carveTunnel(random.nextLong(), chunkX, chunkZ, primer, currentX, currentY, currentZ, random.nextFloat() * GenConfig.BRANCH_RADIUS_VARIATION + GenConfig.BRANCH_RADIUS_BASE, yaw + ((float)Math.PI / 2F), pitch / 3.0F, currentStep, maxSteps, 1.0D);
                return;
            }

            if (isRootSegment || random.nextInt(GenConfig.TUNNEL_GENERATION_CHANCE_DENOMINATOR) != 0)
            {
                double deltaXFromCenter = currentX - chunkCenterX;
                double deltaZFromCenter = currentZ - chunkCenterZ;
                double remaining = (double)(maxSteps - currentStep);
                double maxDistance = (double)(tunnelRadius + 2.0F + 16.0F);

                if (deltaXFromCenter * deltaXFromCenter + deltaZFromCenter * deltaZFromCenter - remaining * remaining > maxDistance * maxDistance)
                {
                    return;
                }

                if (currentX >= chunkCenterX - GenConfig.CARVE_BOUNDS_OFFSET - horizontalRadius * GenConfig.CARVE_BOUNDS_MULTIPLIER && currentZ >= chunkCenterZ - GenConfig.CARVE_BOUNDS_OFFSET - horizontalRadius * GenConfig.CARVE_BOUNDS_MULTIPLIER && currentX <= chunkCenterX + GenConfig.CARVE_BOUNDS_OFFSET + horizontalRadius * GenConfig.CARVE_BOUNDS_MULTIPLIER && currentZ <= chunkCenterZ + GenConfig.CARVE_BOUNDS_OFFSET + horizontalRadius * GenConfig.CARVE_BOUNDS_MULTIPLIER)
                {
                    int minX = MathHelper.floor_double(currentX - horizontalRadius) - chunkX * 16 - 1;
                    int maxX = MathHelper.floor_double(currentX + horizontalRadius) - chunkX * 16 + 1;
                    int minY = MathHelper.floor_double(currentY - verticalRadius) - 1;
                    int maxY = MathHelper.floor_double(currentY + verticalRadius) + 1;
                    int minZ = MathHelper.floor_double(currentZ - horizontalRadius) - chunkZ * 16 - 1;
                    int maxZ = MathHelper.floor_double(currentZ + horizontalRadius) - chunkZ * 16 + 1;

                    if (minX < 0)
                    {
                        minX = 0;
                    }

                    if (maxX > 16)
                    {
                        maxX = 16;
                    }

                    if (minY < GenConfig.MIN_Y_BOUND)
                    {
                        minY = GenConfig.MIN_Y_BOUND;
                    }

                    if (maxY > GenConfig.MAX_Y_BOUND)
                    {
                        maxY = GenConfig.MAX_Y_BOUND;
                    }

                    if (minZ < 0)
                    {
                        minZ = 0;
                    }

                    if (maxZ > 16)
                    {
                        maxZ = 16;
                    }

                    boolean intersectsWater = false;

                    for (int scanX = minX; !intersectsWater && scanX < maxX; ++scanX)
                    {
                        for (int scanZ = minZ; !intersectsWater && scanZ < maxZ; ++scanZ)
                        {
                            for (int scanY = maxY + 1; !intersectsWater && scanY >= minY - 1; --scanY)
                            {
                                if (scanY >= 0 && scanY < 256)
                                {
                                    IBlockState state = primer.getBlockState(scanX, scanY, scanZ);

                                    if (state.getBlock() == Blocks.flowing_water || state.getBlock() == Blocks.water)
                                    {
                                        intersectsWater = true;
                                    }

                                    if (scanY != minY - 1 && scanX != minX && scanX != maxX - 1 && scanZ != minZ && scanZ != maxZ - 1)
                                    {
                                        scanY = minY;
                                    }
                                }
                            }
                        }
                    }

                    if (!intersectsWater)
                    {
                        BlockPos.MutableBlockPos mutableSurfacePos = new BlockPos.MutableBlockPos();

                        for (int carveX = minX; carveX < maxX; ++carveX)
                        {
                            double normX = ((double)(carveX + chunkX * 16) + 0.5D - currentX) / horizontalRadius;

                            for (int carveZ = minZ; carveZ < maxZ; ++carveZ)
                            {
                                double normZ = ((double)(carveZ + chunkZ * 16) + 0.5D - currentZ) / horizontalRadius;
                                boolean topSurfaceExposed = false;

                                if (normX * normX + normZ * normZ < GenConfig.ELLIPSOID_CARVE_THRESHOLD)
                                {
                                    for (int carveY = maxY; carveY > minY; --carveY)
                                    {
                                        double normY = ((double)(carveY - 1) + 0.5D - currentY) / verticalRadius;

                                        if (normY > GenConfig.ELLIPSOID_CARVE_Y_THRESHOLD && normX * normX + normY * normY + normZ * normZ < GenConfig.ELLIPSOID_CARVE_THRESHOLD)
                                        {
                                            IBlockState currentState = primer.getBlockState(carveX, carveY, carveZ);
                                            IBlockState stateAbove = (IBlockState)Objects.firstNonNull(primer.getBlockState(carveX, carveY + 1, carveZ), Blocks.air.getDefaultState());

                                            if (currentState.getBlock() == Blocks.grass || currentState.getBlock() == Blocks.mycelium)
                                            {
                                                topSurfaceExposed = true;
                                            }

                                            if (this.canReplaceBlockForCave(currentState, stateAbove))
                                            {
                                                if (carveY - 1 < GenConfig.LAVA_DEPTH_THRESHOLD)
                                                {
                                                    primer.setBlockState(carveX, carveY, carveZ, Blocks.lava.getDefaultState());
                                                }
                                                else
                                                {
                                                    primer.setBlockState(carveX, carveY, carveZ, Blocks.air.getDefaultState());

                                                    if (stateAbove.getBlock() == Blocks.sand)
                                                    {
                                                        primer.setBlockState(carveX, carveY + 1, carveZ, stateAbove.getValue(BlockSand.VARIANT) == BlockSand.EnumType.RED_SAND ? Blocks.red_sandstone.getDefaultState() : Blocks.sandstone.getDefaultState());
                                                    }

                                                    if (topSurfaceExposed && primer.getBlockState(carveX, carveY - 1, carveZ).getBlock() == Blocks.dirt)
                                                    {
                                                        mutableSurfacePos.set(carveX + chunkX * 16, 0, carveZ + chunkZ * 16);
                                                        primer.setBlockState(carveX, carveY - 1, carveZ, this.worldObj.getBiomeGenForCoords(mutableSurfacePos).topBlock.getBlock().getDefaultState());
                                                    }
                                                }
                                            }
                                        }
                                    }
                                }
                            }
                        }

                        if (isRootSegment)
                        {
                            break;
                        }
                    }
                }
            }
        }
    }

    // Determines if a block can be replaced by cave air/lava, considering the block above
    protected boolean canReplaceBlockForCave(IBlockState current, IBlockState blockAbove)
    {
        return current.getBlock() == Blocks.stone ? true : (current.getBlock() == Blocks.dirt ? true : (current.getBlock() == Blocks.grass ? true : (current.getBlock() == Blocks.hardened_clay ? true : (current.getBlock() == Blocks.stained_hardened_clay ? true : (current.getBlock() == Blocks.sandstone ? true : (current.getBlock() == Blocks.red_sandstone ? true : (current.getBlock() == Blocks.mycelium ? true : (current.getBlock() == Blocks.snow_layer ? true : (current.getBlock() == Blocks.sand || current.getBlock() == Blocks.gravel) && blockAbove.getBlock().getMaterial() != Material.water))))))));
    }

    /**
     * Recursively called by generate()
     */
    protected void recursiveGenerate(World worldIn, int chunkX, int chunkZ, int baseChunkX, int baseChunkZ, ChunkPrimer chunkPrimer)
    {
        int caveStartCount = this.rand.nextInt(this.rand.nextInt(this.rand.nextInt(GenConfig.MAX_CAVE_COUNT_BASE) + 1) + 1);

        if (this.rand.nextInt(GenConfig.CAVE_SPAWN_CHANCE_DENOMINATOR) != 0)
        {
            caveStartCount = 0;
        }

        for (int startIndex = 0; startIndex < caveStartCount; ++startIndex)
        {
            double startX = (double)(chunkX * 16 + this.rand.nextInt(16));
            double startY = (double)this.rand.nextInt(this.rand.nextInt(120) + 8);
            double startZ = (double)(chunkZ * 16 + this.rand.nextInt(16));
            int tunnelCount = 1;

            if (this.rand.nextInt(GenConfig.ROOM_SPAWN_CHANCE_DENOMINATOR) == 0)
            {
                this.carveRoom(this.rand.nextLong(), baseChunkX, baseChunkZ, chunkPrimer, startX, startY, startZ);
                tunnelCount += this.rand.nextInt(GenConfig.MAX_EXTRA_TUNNELS_FROM_ROOM);
            }

            for (int tunnelIndex = 0; tunnelIndex < tunnelCount; ++tunnelIndex)
            {
                float yaw = this.rand.nextFloat() * (float)Math.PI * 2.0F;
                float pitch = (this.rand.nextFloat() - 0.5F) * 2.0F / 8.0F;
                float radius = this.rand.nextFloat() * 2.0F + this.rand.nextFloat();

                if (this.rand.nextInt(GenConfig.LARGE_TUNNEL_CHANCE_DENOMINATOR) == 0)
                {
                    radius *= this.rand.nextFloat() * this.rand.nextFloat() * GenConfig.LARGE_TUNNEL_SIZE_MULTIPLIER + 1.0F;
                }

                this.carveTunnel(this.rand.nextLong(), baseChunkX, baseChunkZ, chunkPrimer, startX, startY, startZ, radius, yaw, pitch, 0, 0, 1.0D);
            }
        }
    }
}
