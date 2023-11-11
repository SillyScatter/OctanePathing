package com.octane.sllly.octanepathing.utils;

import org.bukkit.Location;
import org.bukkit.util.Vector;
import java.util.ArrayList;
import java.util.List;

public class LocationUtils {

    public static List<Location> getLocationsWithDirection(List<Location> originalLocations) {
        List<Location> directedLocations = new ArrayList<>();

        for (int i = 0; i < originalLocations.size() - 1; i++) {
            Location current = originalLocations.get(i);
            Location next = originalLocations.get(i + 1);

            Vector direction = next.toVector().subtract(current.toVector());
            Location directedLocation = current.clone();
            directedLocation.setDirection(direction);

            directedLocations.add(directedLocation);
        }

        return directedLocations;
    }

    // Function to convert a vector to a yaw and pitch which will be used to set the direction.
    private static float[] vectorToYawPitch(Vector vector) {
        Vector normalizedVector = vector.normalize();
        double x = normalizedVector.getX();
        double y = normalizedVector.getY();
        double z = normalizedVector.getZ();

        double pitch = Math.asin(-y);
        double yaw = Math.atan2(-x, z);

        return new float[]{(float) Math.toDegrees(yaw), (float) Math.toDegrees(pitch)};
    }
}