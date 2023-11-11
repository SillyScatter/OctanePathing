package com.octane.sllly.octanepathing.files;

import com.octane.sllly.octanepathing.objects.BSplineMap;
import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;

import java.io.File;

public class BSplineData extends AnnotatedConfig {
    public BSplineData(File parentDirectory, String name) {
        super(parentDirectory, name);
    }

    @ConfigField(path = "b-splines")
    public static BSplineMap bSplineMap = new BSplineMap();
}
