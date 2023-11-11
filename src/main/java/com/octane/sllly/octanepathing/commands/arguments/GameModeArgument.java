package com.octane.sllly.octanepathing.commands.arguments;

import com.octane.sllly.octanepathing.objects.Path;
import dev.splityosis.commandsystem.SYSArgument;
import dev.splityosis.commandsystem.SYSCommand;
import org.bukkit.GameMode;
import org.bukkit.command.CommandSender;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GameModeArgument extends SYSArgument {
    @Override
    public boolean isValid(String input) {
        try{
            GameMode.valueOf(input.toUpperCase());
            return true;
        }catch (Exception e){
            return false;
        }
    }

    @Override
    public List<String> getInvalidInputMessage(String input) {
        return Arrays.asList("&c"+input+" is not a valid gamemode");
    }

    @Override
    public @NonNull List<String> tabComplete(CommandSender sender, SYSCommand command, String input) {
        List<String> values = new ArrayList<>();
        for (GameMode value : GameMode.values()) {
            values.add(value.toString().toLowerCase());
        }
        List<String> complete = new ArrayList<>();
        for (String name : values) {
            if (name.startsWith(input)){
                complete.add(name);
            }
        }
        return complete;
    }
}
