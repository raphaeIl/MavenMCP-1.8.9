package net.minecraft.world.gen;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import java.io.File;
import java.io.FileReader;
import java.io.IOException;

public class GenConfig
{
    // Configuration loaded from JSON file
    private static JsonObject config = null;
    private static boolean initialized = false;
    
    // Cave generation frequency and count - Loaded from JSON
    public static int MAX_CAVE_COUNT_BASE = 200;
    public static int CAVE_SPAWN_CHANCE_DENOMINATOR = 3;
    
    // Room generation - Loaded from JSON
    public static int ROOM_SPAWN_CHANCE_DENOMINATOR = 4;
    public static int MAX_EXTRA_TUNNELS_FROM_ROOM = 4;
    
    // Room size parameters - Loaded from JSON
    public static float ROOM_RADIUS_BASE = 1.0F;
    public static float ROOM_RADIUS_VARIATION = 6.0F;
    public static double ROOM_VERTICAL_SCALE = 0.5D;
    
    // Tunnel size and shape - Loaded from JSON
    public static double TUNNEL_RADIUS_BASE = 1.5D;
    public static float TUNNEL_RADIUS_VARIATION_MULTIPLIER = 1.0F;
    
    // Tunnel branching - Loaded from JSON
    public static float BRANCH_RADIUS_THRESHOLD = 1.0F;
    public static float BRANCH_RADIUS_BASE = 0.5F;
    public static float BRANCH_RADIUS_VARIATION = 0.5F;
    
    // Tunnel length - Loaded from JSON
    public static int TUNNEL_LENGTH_DIVISOR = 4;
    
    // Lava generation - Loaded from JSON
    public static int LAVA_DEPTH_THRESHOLD = 10;
    
    // Tunnel curvature and behavior - Loaded from JSON
    public static int WIDE_TUNNEL_CHANCE_DENOMINATOR = 6;
    public static float WIDE_TUNNEL_PITCH_DECAY = 0.92F;
    public static float NORMAL_TUNNEL_PITCH_DECAY = 0.7F;
    
    // Large tunnel chance - Loaded from JSON
    public static int LARGE_TUNNEL_CHANCE_DENOMINATOR = 10;
    public static float LARGE_TUNNEL_SIZE_MULTIPLIER = 3.0F;
    
    // Tunnel generation chance - Loaded from JSON
    public static int TUNNEL_GENERATION_CHANCE_DENOMINATOR = 4;
    
    // Y coordinate bounds - Loaded from JSON
    public static int MIN_Y_BOUND = 1;
    public static int MAX_Y_BOUND = 248;
    
    // Carving bounds - Loaded from JSON
    public static double CARVE_BOUNDS_OFFSET = 16.0D;
    public static double CARVE_BOUNDS_MULTIPLIER = 2.0D;
    
    // Ellipsoid carving parameters - Loaded from JSON
    public static double ELLIPSOID_CARVE_THRESHOLD = 1.0D;
    public static double ELLIPSOID_CARVE_Y_THRESHOLD = -0.7D;
    
    // Structure spawn rates - Loaded from JSON
    public static int VILLAGE_DISTANCE = 32;        // Vanilla village distance
    public static int VILLAGE_MIN_DISTANCE = 8;     // Vanilla village minimum distance
    public static double MINESHAFT_CHANCE = 0.004D; // Vanilla mineshaft chance
    public static int DUNGEON_CHANCE = 8;           // Vanilla dungeon chance
    public static int STRONGHOLD_DISTANCE = 32;     // Vanilla stronghold distance
    
    // Structure distance limitations - Loaded from JSON
    public static int SCATTERED_FEATURE_MAX_DISTANCE = 32;  // Vanilla temple max distance
    public static int SCATTERED_FEATURE_MIN_DISTANCE = 4;   // Vanilla temple min distance
    public static int OCEAN_MONUMENT_SPACING = 32;          // Vanilla ocean monument spacing
    public static int NETHER_FORTRESS_DISTANCE = 16;        // Vanilla nether fortress distance
    
    // Structure biome restrictions - Loaded from JSON
    // Biome restriction toggles - Set to false for vanilla biome restrictions
    public static boolean OCEAN_MONUMENT_ANY_BIOME = false;  // Ocean monuments only in deep ocean
    public static boolean SCATTERED_FEATURE_ANY_BIOME = false;  // Temples in specific biomes only
    public static boolean VILLAGE_ANY_BIOME = false;  // Villages in specific biomes only
    public static boolean STRONGHOLD_ANY_BIOME = false;  // Strongholds follow vanilla rules
    
    // Universal structure limit overrides - Loaded from JSON
    public static boolean REMOVE_ALL_STRUCTURE_LIMITS = false;  // Use vanilla structure limits
    
    // Individual structure limit overrides - Loaded from JSON
    public static boolean OCEAN_MONUMENT_NO_LIMITS = false;  // Use vanilla ocean monument limits
    public static boolean SCATTERED_FEATURE_NO_LIMITS = false;  // Use vanilla temple limits
    public static boolean VILLAGE_NO_LIMITS = false;  // Use vanilla village limits
    public static boolean STRONGHOLD_NO_LIMITS = false;  // Use vanilla stronghold limits
    public static boolean NETHER_FORTRESS_NO_LIMITS = false;  // Use vanilla nether fortress limits
    public static boolean MINESHAFT_NO_LIMITS = false;  // Use vanilla mineshaft limits
    
    // Y coordinate overrides - Loaded from JSON
    public static boolean REMOVE_Y_LIMITS = false;  // Use vanilla Y limits
    public static boolean OCEAN_MONUMENT_NO_Y_LIMITS = false;  // Use vanilla ocean monument Y limits
    public static boolean SCATTERED_FEATURE_NO_Y_LIMITS = false;  // Use vanilla temple Y limits
    public static boolean VILLAGE_NO_Y_LIMITS = false;  // Use vanilla village Y limits
    public static boolean STRONGHOLD_NO_Y_LIMITS = false;  // Use vanilla stronghold Y limits
    public static boolean NETHER_FORTRESS_NO_Y_LIMITS = false;  // Use vanilla nether fortress Y limits
    public static boolean MINESHAFT_NO_Y_LIMITS = false;  // Use vanilla mineshaft Y limits
    
    // Custom Y ranges when limits are removed (0-255 range) - Loaded from JSON
    public static int CUSTOM_MIN_Y = 1;  // Custom minimum Y when limits removed
    public static int CUSTOM_MAX_Y = 255;  // Custom maximum Y when limits removed
    
    // Ocean Monument biomes - Must be in deep ocean with ocean-viable surroundings
    public static final java.util.List<net.minecraft.world.biome.BiomeGenBase> OCEAN_MONUMENT_BIOMES = 
        java.util.Arrays.asList(
            net.minecraft.world.biome.BiomeGenBase.ocean,
            net.minecraft.world.biome.BiomeGenBase.deepOcean,
            net.minecraft.world.biome.BiomeGenBase.river,
            net.minecraft.world.biome.BiomeGenBase.frozenOcean,
            net.minecraft.world.biome.BiomeGenBase.frozenRiver
        );
    
    // Scattered Feature (Temples) biomes - Desert, Jungle, and Swamp temples
    public static final java.util.List<net.minecraft.world.biome.BiomeGenBase> SCATTERED_FEATURE_BIOMES = 
        java.util.Arrays.asList(
            net.minecraft.world.biome.BiomeGenBase.desert,
            net.minecraft.world.biome.BiomeGenBase.desertHills,
            net.minecraft.world.biome.BiomeGenBase.jungle,
            net.minecraft.world.biome.BiomeGenBase.jungleHills,
            net.minecraft.world.biome.BiomeGenBase.swampland
        );
    
    // Village biomes - Plains, Desert, and Savanna villages
    public static final java.util.List<net.minecraft.world.biome.BiomeGenBase> VILLAGE_BIOMES = 
        java.util.Arrays.asList(
            net.minecraft.world.biome.BiomeGenBase.plains,
            net.minecraft.world.biome.BiomeGenBase.desert,
            net.minecraft.world.biome.BiomeGenBase.savanna
        );
    
    // Stronghold biome filtering - Any biome with minHeight > 0.0F
    public static float STRONGHOLD_MIN_HEIGHT_THRESHOLD = 0.0F;
    
    /**
     * Initialize the configuration by loading from JSON file
     * This should be called at the start of world generation
     * Reloads configuration every time to pick up changes
     */
    public static void initialize() {
        // Always reload configuration to pick up changes
        reloadConfiguration();
    }
    
    /**
     * Force reload the configuration from JSON file
     * This allows picking up changes without restarting the server
     */
    public static void reloadConfiguration() {
        try {
            File configFile = new File("E:\\documents\\Decompiling\\Extracted\\Minecraft\\MavenMCP-1.8.9\\test_run\\genconfig.json");
            if (configFile.exists()) {
                JsonParser parser = new JsonParser();
                config = parser.parse(new FileReader(configFile)).getAsJsonObject();
                loadFromJson();
                System.out.println("[GenConfig] Configuration RELOADED from E:\\documents\\Decompiling\\Extracted\\Minecraft\\MavenMCP-1.8.9\\test_run\\genconfig.json");
                System.err.println("[GenConfig] Configuration RELOADED from E:\\documents\\Decompiling\\Extracted\\Minecraft\\MavenMCP-1.8.9\\test_run\\genconfig.json");
            } else {
                System.out.println("[GenConfig] Configuration file not found at E:\\documents\\Decompiling\\Extracted\\Minecraft\\MavenMCP-1.8.9\\test_run\\genconfig.json, using default values");
                System.err.println("[GenConfig] Configuration file not found, using default values");
            }
        } catch (IOException e) {
            System.err.println("[GenConfig] Failed to load configuration: " + e.getMessage());
            System.out.println("[GenConfig] Using default values");
        } catch (Exception e) {
            System.err.println("[GenConfig] Error parsing configuration: " + e.getMessage());
            System.out.println("[GenConfig] Using default values");
        }
        
        // Mark as initialized but allow reloading on next call
        initialized = true;
    }
    
    /**
     * Load configuration values from the parsed JSON object
     */
    private static void loadFromJson() {
        if (config == null) return;
        
        try {
            // Cave generation
            if (config.has("caveGeneration")) {
                JsonObject caveGen = config.getAsJsonObject("caveGeneration");
                MAX_CAVE_COUNT_BASE = getInt(caveGen, "maxCaveCountBase", MAX_CAVE_COUNT_BASE);
                CAVE_SPAWN_CHANCE_DENOMINATOR = getInt(caveGen, "caveSpawnChanceDenominator", CAVE_SPAWN_CHANCE_DENOMINATOR);
                ROOM_SPAWN_CHANCE_DENOMINATOR = getInt(caveGen, "roomSpawnChanceDenominator", ROOM_SPAWN_CHANCE_DENOMINATOR);
                MAX_EXTRA_TUNNELS_FROM_ROOM = getInt(caveGen, "maxExtraTunnelsFromRoom", MAX_EXTRA_TUNNELS_FROM_ROOM);
                ROOM_RADIUS_BASE = getFloat(caveGen, "roomRadiusBase", ROOM_RADIUS_BASE);
                ROOM_RADIUS_VARIATION = getFloat(caveGen, "roomRadiusVariation", ROOM_RADIUS_VARIATION);
                ROOM_VERTICAL_SCALE = getDouble(caveGen, "roomVerticalScale", ROOM_VERTICAL_SCALE);
                TUNNEL_RADIUS_BASE = getDouble(caveGen, "tunnelRadiusBase", TUNNEL_RADIUS_BASE);
                TUNNEL_RADIUS_VARIATION_MULTIPLIER = getFloat(caveGen, "tunnelRadiusVariationMultiplier", TUNNEL_RADIUS_VARIATION_MULTIPLIER);
                BRANCH_RADIUS_THRESHOLD = getFloat(caveGen, "branchRadiusThreshold", BRANCH_RADIUS_THRESHOLD);
                BRANCH_RADIUS_BASE = getFloat(caveGen, "branchRadiusBase", BRANCH_RADIUS_BASE);
                BRANCH_RADIUS_VARIATION = getFloat(caveGen, "branchRadiusVariation", BRANCH_RADIUS_VARIATION);
                TUNNEL_LENGTH_DIVISOR = getInt(caveGen, "tunnelLengthDivisor", TUNNEL_LENGTH_DIVISOR);
                LAVA_DEPTH_THRESHOLD = getInt(caveGen, "lavaDepthThreshold", LAVA_DEPTH_THRESHOLD);
                WIDE_TUNNEL_CHANCE_DENOMINATOR = getInt(caveGen, "wideTunnelChanceDenominator", WIDE_TUNNEL_CHANCE_DENOMINATOR);
                WIDE_TUNNEL_PITCH_DECAY = getFloat(caveGen, "wideTunnelPitchDecay", WIDE_TUNNEL_PITCH_DECAY);
                NORMAL_TUNNEL_PITCH_DECAY = getFloat(caveGen, "normalTunnelPitchDecay", NORMAL_TUNNEL_PITCH_DECAY);
                LARGE_TUNNEL_CHANCE_DENOMINATOR = getInt(caveGen, "largeTunnelChanceDenominator", LARGE_TUNNEL_CHANCE_DENOMINATOR);
                LARGE_TUNNEL_SIZE_MULTIPLIER = getFloat(caveGen, "largeTunnelSizeMultiplier", LARGE_TUNNEL_SIZE_MULTIPLIER);
                TUNNEL_GENERATION_CHANCE_DENOMINATOR = getInt(caveGen, "tunnelGenerationChanceDenominator", TUNNEL_GENERATION_CHANCE_DENOMINATOR);
            }
            
            // Y coordinate bounds
            if (config.has("yCoordinateBounds")) {
                JsonObject yBounds = config.getAsJsonObject("yCoordinateBounds");
                MIN_Y_BOUND = getInt(yBounds, "minYBound", MIN_Y_BOUND);
                MAX_Y_BOUND = getInt(yBounds, "maxYBound", MAX_Y_BOUND);
            }
            
            // Carving bounds
            if (config.has("carvingBounds")) {
                JsonObject carvingBounds = config.getAsJsonObject("carvingBounds");
                CARVE_BOUNDS_OFFSET = getDouble(carvingBounds, "carveBoundsOffset", CARVE_BOUNDS_OFFSET);
                CARVE_BOUNDS_MULTIPLIER = getDouble(carvingBounds, "carveBoundsMultiplier", CARVE_BOUNDS_MULTIPLIER);
                ELLIPSOID_CARVE_THRESHOLD = getDouble(carvingBounds, "ellipsoidCarveThreshold", ELLIPSOID_CARVE_THRESHOLD);
                ELLIPSOID_CARVE_Y_THRESHOLD = getDouble(carvingBounds, "ellipsoidCarveYThreshold", ELLIPSOID_CARVE_Y_THRESHOLD);
            }
            
            // Structure spawn rates
            if (config.has("structureSpawnRates")) {
                JsonObject spawnRates = config.getAsJsonObject("structureSpawnRates");
                VILLAGE_DISTANCE = getInt(spawnRates, "villageDistance", VILLAGE_DISTANCE);
                VILLAGE_MIN_DISTANCE = getInt(spawnRates, "villageMinDistance", VILLAGE_MIN_DISTANCE);
                MINESHAFT_CHANCE = getDouble(spawnRates, "mineshaftChance", MINESHAFT_CHANCE);
                DUNGEON_CHANCE = getInt(spawnRates, "dungeonChance", DUNGEON_CHANCE);
                STRONGHOLD_DISTANCE = getInt(spawnRates, "strongholdDistance", STRONGHOLD_DISTANCE);
            }
            
            // Structure distance limitations
            if (config.has("structureDistanceLimitations")) {
                JsonObject distanceLimits = config.getAsJsonObject("structureDistanceLimitations");
                SCATTERED_FEATURE_MAX_DISTANCE = getInt(distanceLimits, "scatteredFeatureMaxDistance", SCATTERED_FEATURE_MAX_DISTANCE);
                SCATTERED_FEATURE_MIN_DISTANCE = getInt(distanceLimits, "scatteredFeatureMinDistance", SCATTERED_FEATURE_MIN_DISTANCE);
                OCEAN_MONUMENT_SPACING = getInt(distanceLimits, "oceanMonumentSpacing", OCEAN_MONUMENT_SPACING);
                NETHER_FORTRESS_DISTANCE = getInt(distanceLimits, "netherFortressDistance", NETHER_FORTRESS_DISTANCE);
            }
            
            // Biome restrictions
            if (config.has("biomeRestrictions")) {
                JsonObject biomeRestrictions = config.getAsJsonObject("biomeRestrictions");
                OCEAN_MONUMENT_ANY_BIOME = getBoolean(biomeRestrictions, "oceanMonumentAnyBiome", OCEAN_MONUMENT_ANY_BIOME);
                SCATTERED_FEATURE_ANY_BIOME = getBoolean(biomeRestrictions, "scatteredFeatureAnyBiome", SCATTERED_FEATURE_ANY_BIOME);
                VILLAGE_ANY_BIOME = getBoolean(biomeRestrictions, "villageAnyBiome", VILLAGE_ANY_BIOME);
                STRONGHOLD_ANY_BIOME = getBoolean(biomeRestrictions, "strongholdAnyBiome", STRONGHOLD_ANY_BIOME);
            }
            
            // Structure limit overrides
            if (config.has("structureLimitOverrides")) {
                JsonObject limitOverrides = config.getAsJsonObject("structureLimitOverrides");
                REMOVE_ALL_STRUCTURE_LIMITS = getBoolean(limitOverrides, "removeAllStructureLimits", REMOVE_ALL_STRUCTURE_LIMITS);
                OCEAN_MONUMENT_NO_LIMITS = getBoolean(limitOverrides, "oceanMonumentNoLimits", OCEAN_MONUMENT_NO_LIMITS);
                SCATTERED_FEATURE_NO_LIMITS = getBoolean(limitOverrides, "scatteredFeatureNoLimits", SCATTERED_FEATURE_NO_LIMITS);
                VILLAGE_NO_LIMITS = getBoolean(limitOverrides, "villageNoLimits", VILLAGE_NO_LIMITS);
                STRONGHOLD_NO_LIMITS = getBoolean(limitOverrides, "strongholdNoLimits", STRONGHOLD_NO_LIMITS);
                NETHER_FORTRESS_NO_LIMITS = getBoolean(limitOverrides, "netherFortressNoLimits", NETHER_FORTRESS_NO_LIMITS);
                MINESHAFT_NO_LIMITS = getBoolean(limitOverrides, "mineshaftNoLimits", MINESHAFT_NO_LIMITS);
            }
            
            // Y coordinate overrides
            if (config.has("yCoordinateOverrides")) {
                JsonObject yOverrides = config.getAsJsonObject("yCoordinateOverrides");
                REMOVE_Y_LIMITS = getBoolean(yOverrides, "removeYLimits", REMOVE_Y_LIMITS);
                OCEAN_MONUMENT_NO_Y_LIMITS = getBoolean(yOverrides, "oceanMonumentNoYLimits", OCEAN_MONUMENT_NO_Y_LIMITS);
                SCATTERED_FEATURE_NO_Y_LIMITS = getBoolean(yOverrides, "scatteredFeatureNoYLimits", SCATTERED_FEATURE_NO_Y_LIMITS);
                VILLAGE_NO_Y_LIMITS = getBoolean(yOverrides, "villageNoYLimits", VILLAGE_NO_Y_LIMITS);
                STRONGHOLD_NO_Y_LIMITS = getBoolean(yOverrides, "strongholdNoYLimits", STRONGHOLD_NO_Y_LIMITS);
                NETHER_FORTRESS_NO_Y_LIMITS = getBoolean(yOverrides, "netherFortressNoYLimits", NETHER_FORTRESS_NO_Y_LIMITS);
                MINESHAFT_NO_Y_LIMITS = getBoolean(yOverrides, "mineshaftNoYLimits", MINESHAFT_NO_Y_LIMITS);
            }
            
            // Custom Y range
            if (config.has("customYRange")) {
                JsonObject customYRange = config.getAsJsonObject("customYRange");
                CUSTOM_MIN_Y = getInt(customYRange, "customMinY", CUSTOM_MIN_Y);
                CUSTOM_MAX_Y = getInt(customYRange, "customMaxY", CUSTOM_MAX_Y);
            }
            
            // Stronghold biome filtering
            if (config.has("strongholdBiomeFiltering")) {
                JsonObject strongholdFiltering = config.getAsJsonObject("strongholdBiomeFiltering");
                STRONGHOLD_MIN_HEIGHT_THRESHOLD = getFloat(strongholdFiltering, "strongholdMinHeightThreshold", STRONGHOLD_MIN_HEIGHT_THRESHOLD);
            }
            
        } catch (Exception e) {
            System.err.println("[GenConfig] Error loading configuration values: " + e.getMessage());
        }
    }
    
    // Helper methods for safe JSON value extraction
    private static int getInt(JsonObject obj, String key, int defaultValue) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsInt() : defaultValue;
    }
    
    private static float getFloat(JsonObject obj, String key, float defaultValue) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsFloat() : defaultValue;
    }
    
    private static double getDouble(JsonObject obj, String key, double defaultValue) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsDouble() : defaultValue;
    }
    
    private static boolean getBoolean(JsonObject obj, String key, boolean defaultValue) {
        return obj.has(key) && obj.get(key).isJsonPrimitive() ? obj.get(key).getAsBoolean() : defaultValue;
    }
}
