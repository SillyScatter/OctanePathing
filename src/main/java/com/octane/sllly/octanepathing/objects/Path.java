package com.octane.sllly.octanepathing.objects;

import com.octane.sllly.octanepathing.OctanePathing;
import com.octane.sllly.octanepathing.utils.Util;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class Path {

    public static Map<String, Path> paths = new HashMap<>();

    private String identifier;

    private List<String> input;

    private Map<Integer, List<String>> schedule;

    public Path(String identifier, List<String> input) {
        this.identifier = identifier;
        this.input = input;
        schedule = new HashMap<>();

        calculateSchedule();

        paths.put(identifier, this);
    }

    public void calculateSchedule(){
        for (String value : input) {
            String[] splitValue = value.split(":");
            Integer tick = Integer.parseInt(splitValue[0]);
            String command = splitValue[1];

            List<String> existingCommandsForTick = schedule.get(tick);
            if (existingCommandsForTick == null) {
                existingCommandsForTick = new ArrayList<>();
            }
            existingCommandsForTick.add(command);

            schedule.put(tick, existingCommandsForTick);
        }
    }

    public void executeSchedule(String playerName){
        for (Integer tick : schedule.keySet()) {
            List<String> commands = schedule.get(tick);
            if (commands == null) {
                continue;
            }
            commands =  Util.replaceList(commands, "%player%", playerName);
            if (tick == 0){
                for (String command : commands) {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                }
                continue;
            }
            List<String> finalCommands = commands;
            new BukkitRunnable() {
                @Override
                public void run() {
                    for (String command : finalCommands) {
                        Bukkit.dispatchCommand(Bukkit.getConsoleSender(), command);
                    }
                }
            }.runTaskLater(OctanePathing.plugin, tick);
        }
    }

    public String getIdentifier() {
        return identifier;
    }

    public List<String> getInput() {
        return input;
    }

    public Map<Integer, List<String>> getSchedule() {
        return schedule;
    }
}
