package com.octane.sllly.octanepathing.files;

import dev.splityosis.configsystem.configsystem.AnnotatedConfig;
import dev.splityosis.configsystem.configsystem.ConfigField;
import dev.splityosis.configsystem.configsystem.InvalidConfigFileException;

import java.io.File;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathConfig extends AnnotatedConfig {
    public PathConfig(File file) throws InvalidConfigFileException {
        super(file);
        this.identifier = file.getName().substring(0,file.getName().length()-4);
    }

    public String identifier;

    @ConfigField(path = "commands", comment = "placeholders: %player%")
    public List<String> commands = Arrays.asList("1:paths bspline blah blah blah");

}
