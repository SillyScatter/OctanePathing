package com.octane.sllly.octanepathing.files.configlogic;

import com.octane.sllly.octanepathing.objects.LocationList;
import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

public class LocationListLogic extends ConfigTypeLogic<LocationList> {
    @Override
    public LocationList getFromConfig(ConfigurationSection config, String path) {
        LocationList locationList = new LocationList();
        ConfigurationSection configurationSection = config.getConfigurationSection(path);
        if (configurationSection == null) {
            return new LocationList();
        }
        for (String count : configurationSection.getKeys(false)) {
            ConfigurationSection countSection = configurationSection.getConfigurationSection(count);
            String worldName = countSection.getString("world");
            World world = Bukkit.getWorld(worldName);
            if (world == null) {
                continue;
            }
            double x = countSection.getDouble("x");
            double y = countSection.getDouble("y");
            double z = countSection.getDouble("z");
            double yaw = countSection.getDouble("yaw");
            double pitch = countSection.getDouble("pitch");

            locationList.add(new Location(world, x,y,z, (float) (yaw), (float) pitch));
        }
        return locationList;
    }

    @Override
    public void setInConfig(LocationList instance, ConfigurationSection config, String path) {
        int count = 0;
        for (Location location : instance) {
            count++;
            String world = location.getWorld().getName();
            double x = location.getX();
            double y = location.getY();
            double z = location.getZ();
            double yaw = location.getYaw();
            double pitch = location.getPitch();
            config.set(path+"."+count+".world", world);
            config.set(path+"."+count+".x", x);
            config.set(path+"."+count+".y", y);
            config.set(path+"."+count+".z", z);
            config.set(path+"."+count+".yaw", yaw);
            config.set(path+"."+count+".pitch", pitch);
        }
    }
}
