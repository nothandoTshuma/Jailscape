package com.group18.core;

import java.util.HashMap;
import java.util.Map;

/**
 * Stores all asset resource links in a HashMap and allows users to get specific
 * resources they require
 *
 * @author danielturato
 */
public class ResourceRepository {

    /**
     * Map's class names to asset resources
     */
    private static final Map<String, String> ASSET_RESOURCES = new HashMap<>();

    /**
     * The directory in which all assets are held
     */
    private static final String ASSET_DIRECTORY = "resources/assets";

    /**
     * Injects all the required resource links into the HashMap
     */
    public static void createResourceMap() {
        ASSET_RESOURCES.put("User-Idle", ASSET_DIRECTORY + "/Player/Idle/PlayerIdle.gif");
        ASSET_RESOURCES.put("User-Walk", ASSET_DIRECTORY + "/Player/Walk/PlayerWalk.gif");
        ASSET_RESOURCES.put("SmartTargetingEnemy", ASSET_DIRECTORY + "/Enemy/SmartEnemyIdle.png");
        ASSET_RESOURCES.put("DumbTargetingEnemy", ASSET_DIRECTORY + "/Enemy/DumbEnemyIdle.png");
        ASSET_RESOURCES.put("StraightLineEnemy", ASSET_DIRECTORY + "/Enemy/StraightEnemyIdle.png");
        ASSET_RESOURCES.put("WallFollowingEnemy", ASSET_DIRECTORY + "/Enemy/WallEnemyIdle.png");

        ASSET_RESOURCES.put("Black-Door", ASSET_DIRECTORY + "/Cell/DoorBlack.png");
        ASSET_RESOURCES.put("Blue-Door", ASSET_DIRECTORY + "/Cell/DoorBlue.png");
        ASSET_RESOURCES.put("Green-Door", ASSET_DIRECTORY + "/Cell/DoorGreen.png");
        ASSET_RESOURCES.put("Red-Door", ASSET_DIRECTORY + "/Cell/DoorRed.png");
        ASSET_RESOURCES.put("Yellow-Door", ASSET_DIRECTORY + "/Cell/DoorYellow.png");
        ASSET_RESOURCES.put("Element-Fire", ASSET_DIRECTORY + "/Cell/FireCell.png");
        ASSET_RESOURCES.put("Element-Water", ASSET_DIRECTORY + "/Cell/WaterCell.png");
        ASSET_RESOURCES.put("Goal",  ASSET_DIRECTORY + "/Cell/GoalCell.png");
        ASSET_RESOURCES.put("Ground", ASSET_DIRECTORY + "/Cell/GroundCell.png");
        ASSET_RESOURCES.put("Teleporter", ASSET_DIRECTORY + "/Cell/TeleporterCell.png");
        ASSET_RESOURCES.put("Token-Door", ASSET_DIRECTORY + "/Cell/TokenDoor.png");
        ASSET_RESOURCES.put("Wall", ASSET_DIRECTORY + "/Cell/WallCell.png");
        ASSET_RESOURCES.put("Element-Ice", ASSET_DIRECTORY + "/Cell/IceCell.png");

        ASSET_RESOURCES.put("FireBoots", ASSET_DIRECTORY + "/Item/FireBoots.png");
        ASSET_RESOURCES.put("Flippers", ASSET_DIRECTORY + "/Item/Flippers.png");
        ASSET_RESOURCES.put("Key-Blue", ASSET_DIRECTORY + "/Item/KeyBlue.png");
        ASSET_RESOURCES.put("Key-Green", ASSET_DIRECTORY + "/Item/KeyGreen.png");
        ASSET_RESOURCES.put("Key-Red", ASSET_DIRECTORY + "/Item/KeyRed.png");
        ASSET_RESOURCES.put("Key-Yellow", ASSET_DIRECTORY + "/Item/KeyYellow.png");
        ASSET_RESOURCES.put("Token", ASSET_DIRECTORY + "/Item/Token.gif");
        ASSET_RESOURCES.put("IceSkates", ASSET_DIRECTORY + "/Item/IceSkates.png");

    }

    /**
     * Get's a specific resource by a class name
     * @param className The class name of the resource
     * @return The resource link
     */
    public static String getResource(String className) {
        return ASSET_RESOURCES.get(className);
    }

}
