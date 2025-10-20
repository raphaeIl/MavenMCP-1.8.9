package net.minecraft.world.gen;

public class GenConfig
{
    // Cave generation frequency and count - MASSIVE caves everywhere!
    public static final int MAX_CAVE_COUNT_BASE = 500;
    public static final int CAVE_SPAWN_CHANCE_DENOMINATOR = 2;
    
    // Room generation - Almost every cave gets a room
    public static final int ROOM_SPAWN_CHANCE_DENOMINATOR = 2;
    public static final int MAX_EXTRA_TUNNELS_FROM_ROOM = 8;
    
    // Room size parameters - HUGE rooms
    public static final float ROOM_RADIUS_BASE = 8.0F;
    public static final float ROOM_RADIUS_VARIATION = 15.0F;
    public static final double ROOM_VERTICAL_SCALE = 2.5D;
    
    // Tunnel size and shape - MASSIVE tunnels
    public static final double TUNNEL_RADIUS_BASE = 8.0D;
    public static final float TUNNEL_RADIUS_VARIATION_MULTIPLIER = 4.0F;
    
    // Tunnel branching - More branching, bigger branches
    public static final float BRANCH_RADIUS_THRESHOLD = 3.0F;
    public static final float BRANCH_RADIUS_BASE = 2.0F;
    public static final float BRANCH_RADIUS_VARIATION = 3.0F;
    
    // Tunnel length - Much longer tunnels
    public static final int TUNNEL_LENGTH_DIVISOR = 2;
    
    // Lava generation - Lava everywhere below surface
    public static final int LAVA_DEPTH_THRESHOLD = 50;
    
    // Tunnel curvature and behavior - More erratic movement
    public static final int WIDE_TUNNEL_CHANCE_DENOMINATOR = 2;
    public static final float WIDE_TUNNEL_PITCH_DECAY = 0.5F;
    public static final float NORMAL_TUNNEL_PITCH_DECAY = 0.2F;
    
    // Large tunnel chance - Most tunnels are huge
    public static final int LARGE_TUNNEL_CHANCE_DENOMINATOR = 3;
    public static final float LARGE_TUNNEL_SIZE_MULTIPLIER = 8.0F;
    
    // Tunnel generation chance - Always generate tunnels
    public static final int TUNNEL_GENERATION_CHANCE_DENOMINATOR = 1;
    
    // Y coordinate bounds - Full world height
    public static final int MIN_Y_BOUND = 0;
    public static final int MAX_Y_BOUND = 255;
    
    // Carving bounds - Much larger carving area
    public static final double CARVE_BOUNDS_OFFSET = 32.0D;
    public static final double CARVE_BOUNDS_MULTIPLIER = 4.0D;
    
    // Ellipsoid carving parameters - More aggressive carving
    public static final double ELLIPSOID_CARVE_THRESHOLD = 2.5D;
    public static final double ELLIPSOID_CARVE_Y_THRESHOLD = -1.5D;
}
