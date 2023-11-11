package com.octane.sllly.octanepathing.files.configlogic;

import com.octane.sllly.octanepathing.objects.BezierCurve;
import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import dev.splityosis.configsystem.configsystem.logics.LocationConfigLogic;
import org.bukkit.Location;
import org.bukkit.configuration.ConfigurationSection;

public class BezierCurveConfigLogic extends ConfigTypeLogic<BezierCurve> {
    @Override
    public BezierCurve getFromConfig(ConfigurationSection config, String path) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        Location p0 = locationConfigLogic.getFromConfig(config, path+".p0");
        Location p1 = locationConfigLogic.getFromConfig(config, path+".p1");
        Location p2 = locationConfigLogic.getFromConfig(config, path+".p2");
        Location p3 = locationConfigLogic.getFromConfig(config, path+".p3");
        int duration = config.getInt(path+".duration");

        return new BezierCurve(1.0/duration, p0, p1, p2, p3);
    }

    @Override
    public void setInConfig(BezierCurve instance, ConfigurationSection config, String path) {
        LocationConfigLogic locationConfigLogic = new LocationConfigLogic();
        locationConfigLogic.setInConfig(instance.getP0(), config, path+".p0");
        locationConfigLogic.setInConfig(instance.getP1(), config, path+".p1");
        locationConfigLogic.setInConfig(instance.getP2(), config, path+".p2");
        locationConfigLogic.setInConfig(instance.getP3(), config, path+".p3");
        config.set(path+".duration",  (int) Math.round(1/instance.getAccuracy()));
    }
}
