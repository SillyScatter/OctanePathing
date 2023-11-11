package com.octane.sllly.octanepathing.files.configlogic;

import com.octane.sllly.octanepathing.objects.BSpline;
import com.octane.sllly.octanepathing.objects.LocationList;
import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import dev.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import org.bukkit.configuration.ConfigurationSection;

public class BSplineConfigLogic extends ConfigTypeLogic<BSpline> {
    @Override
    public BSpline getFromConfig(ConfigurationSection config, String path) {
        LocationListLogic locationListLogic = new LocationListLogic();
        String identifier = config.getString(path+".identifier");
        int jumps = config.getInt(path+".jumps");
        LocationList locations = locationListLogic.getFromConfig(config, path+".control-points");
        return new BSpline(identifier, jumps, locations);
    }

    @Override
    public void setInConfig(BSpline instance, ConfigurationSection config, String path) {
        LocationListLogic locationListLogic = new LocationListLogic();
        config.set(path+".identifier", instance.getIdentifier());
        config.set(path+".jumps", instance.getAmountOfJumps());
        locationListLogic.setInConfig(instance.getControlPoints(), config, path+".control-points");
    }
}
