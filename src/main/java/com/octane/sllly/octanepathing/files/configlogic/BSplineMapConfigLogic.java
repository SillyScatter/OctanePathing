package com.octane.sllly.octanepathing.files.configlogic;

import com.octane.sllly.octanepathing.files.BSplineData;
import com.octane.sllly.octanepathing.objects.BSpline;
import com.octane.sllly.octanepathing.objects.BSplineMap;
import dev.splityosis.configsystem.configsystem.ConfigTypeLogic;
import org.bukkit.configuration.ConfigurationSection;

public class BSplineMapConfigLogic extends ConfigTypeLogic<BSplineMap> {
    @Override
    public BSplineMap getFromConfig(ConfigurationSection config, String path) {
        BSplineConfigLogic bSplineConfigLogic = new BSplineConfigLogic();
        ConfigurationSection configurationSection = config.getConfigurationSection(path);
        if (configurationSection == null) {
            return new BSplineMap();
        }
        BSplineMap bSplineMap = new BSplineMap();
        for (String identifier : configurationSection.getKeys(false)) {
            BSpline bSpline = bSplineConfigLogic.getFromConfig(configurationSection, identifier+".bspline");
            bSplineMap.put(bSpline.getIdentifier(), bSpline);
        }
        return bSplineMap;
    }

    @Override
    public void setInConfig(BSplineMap instance, ConfigurationSection config, String path) {
        BSplineConfigLogic bSplineConfigLogic = new BSplineConfigLogic();
        for (String identifier : instance.keySet()) {
            BSpline bSpline = instance.get(identifier);
            bSplineConfigLogic.setInConfig(bSpline, config, path+"."+identifier+".bspline");
        }
    }
}
