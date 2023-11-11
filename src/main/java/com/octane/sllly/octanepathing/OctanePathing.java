package com.octane.sllly.octanepathing;

import com.octane.sllly.octanepathing.commands.PathingCommandSystem;
import com.octane.sllly.octanepathing.files.BSplineData;
import com.octane.sllly.octanepathing.files.PathConfig;
import com.octane.sllly.octanepathing.files.configlogic.BSplineConfigLogic;
import com.octane.sllly.octanepathing.files.configlogic.BSplineMapConfigLogic;
import com.octane.sllly.octanepathing.files.configlogic.BezierCurveConfigLogic;
import com.octane.sllly.octanepathing.files.configlogic.LocationListLogic;
import com.octane.sllly.octanepathing.objects.Path;
import com.octane.sllly.octanepathing.utils.Util;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public final class OctanePathing extends JavaPlugin {

    public static OctanePathing plugin;
    public BSplineData bSplineData;

    public File pathsDirectory;

    @Override
    public void onEnable() {
        plugin = this;

        //Register ConfigType Logics
        new LocationListLogic().register();
        new BezierCurveConfigLogic().register();
        new BSplineConfigLogic().register();
        new BSplineMapConfigLogic().register();

        //Register Command Branch
        new PathingCommandSystem("paths", "octanepaths", "opaths", "path", "opath").registerCommandBranch(this);

        pathsDirectory = new File(getDataFolder(), "Paths");
        if (!pathsDirectory.exists()) {
            pathsDirectory.mkdirs();
        }

        loadConfigs();
    }

    @Override
    public void onDisable() {
        bSplineData.saveToFile();
        Util.log("&aSaved "+BSplineData.bSplineMap.size()+" B-Splines to file.");
    }

    public void loadConfigs(){
        bSplineData = new BSplineData(getDataFolder(), "b-splines");
        bSplineData.initialize();
        reloadPaths();
    }

    public void reloadConfigs(){
        bSplineData.saveToFile();
        Util.log("&aSaved "+BSplineData.bSplineMap.size()+" B-Splines to file.");
        reloadPaths();
    }
    public void reloadPaths(){
        int count = 0;
        Path.paths.clear();
        File[] pathFiles = pathsDirectory.listFiles();
        if (pathFiles == null || pathFiles.length == 0){
            new PathConfig(new File(pathsDirectory, "example.yml")).initialize();
        }
        pathFiles = pathsDirectory.listFiles();
        for (File pathFile : pathFiles) {
            PathConfig pathConfig = new PathConfig(pathFile);
            pathConfig.initialize();
            Path teleportPad = new Path(pathConfig.identifier, pathConfig.commands);
            Path.paths.put(pathConfig.identifier, teleportPad);
            count++;
        }
        Util.log("&aSuccessfully registered paths: &2&l"+count);
    }
}
