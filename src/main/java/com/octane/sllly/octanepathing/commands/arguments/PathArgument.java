package com.octane.sllly.octanepathing.commands.arguments;

import com.octane.sllly.octanepathing.files.BSplineData;
import com.octane.sllly.octanepathing.objects.Path;
import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class PathArgument extends SYSArgument {
    @Override
    public boolean isValid(String input) {
        return (Path.paths.keySet().contains(input));
    }

    @Override
    public List<String> getInvalidInputMessage(String input) {
        return Arrays.asList("&cThe BSpline "+input+" does not exist.");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> complete = new ArrayList<>();
        for (String name : Path.paths.keySet()) {
            if (name.startsWith(input)){
                complete.add(name);
            }
        }
        return complete;
    }
}
