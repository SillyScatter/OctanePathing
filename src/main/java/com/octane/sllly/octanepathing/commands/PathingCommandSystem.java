package com.octane.sllly.octanepathing.commands;

import com.octane.sllly.octanepathing.OctanePathing;
import com.octane.sllly.octanepathing.commands.arguments.BSplineArgument;
import com.octane.sllly.octanepathing.commands.arguments.GameModeArgument;
import com.octane.sllly.octanepathing.commands.arguments.PathArgument;
import com.octane.sllly.octanepathing.files.BSplineData;
import com.octane.sllly.octanepathing.objects.BSpline;
import com.octane.sllly.octanepathing.objects.LocationList;
import com.octane.sllly.octanepathing.objects.Path;
import com.octane.sllly.octanepathing.utils.Util;
import dev.splityosis.commandsystem.SYSCommand;
import dev.splityosis.commandsystem.SYSCommandBranch;
import dev.splityosis.commandsystem.arguments.*;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.util.List;

public class PathingCommandSystem extends SYSCommandBranch {
    public PathingCommandSystem(String... names) {
        super(names);
        setPermission("octanepathing.admin");

        SYSCommandBranch bSplineBranch = new SYSCommandBranch("bspline");
        addBranch(bSplineBranch);

        SYSCommandBranch locationCommandBranch = new SYSCommandBranch("location");
        bSplineBranch.addBranch(locationCommandBranch);

        addCommand(new SYSCommand("reload")
                .setArguments()
                .executes((sender, args) -> {
                    OctanePathing.plugin.reloadConfigs();
                    Util.sendMessage(sender, "&aReloaded");
                }));

        addCommand(new SYSCommand("take")
                .setUsage("/paths take [path] [player]")
                .setArguments(new PathArgument(), new PlayerArgument())
                .executes((sender, args) -> {
                    Path path = Path.paths.get(args[0]);
                    path.executeSchedule(args[1]);
                }));

        bSplineBranch.addCommand(new SYSCommand("create")
                .setArguments(new StringArgument())
                .setUsage("/paths bspline create [name]")
                .executes((sender, args) -> {
                    String bSpineName = args[0];
                    if (BSplineData.bSplineMap.keySet().contains(bSpineName)){
                        Util.sendMessage(sender, "&cThe BSpline "+bSpineName+" already exists.");
                        return;
                    }
                    new BSpline(bSpineName);
                    Util.sendMessage(sender, "&aCreated "+bSpineName+" successfully!");
                }));

        bSplineBranch.addCommand(new SYSCommand("delete")
                .setArguments(new BSplineArgument())
                .setUsage("/paths bspline delete [name]")
                .executes((sender, args) -> {
                    String bSpineName = args[0];
                    BSplineData.bSplineMap.remove(bSpineName);
                    Util.sendMessage(sender, "&aDeleted "+bSpineName+" successfully!");
                }));

        bSplineBranch.addCommand(new SYSCommand("jumps")
                .setArguments(new BSplineArgument())
                .setUsage("/paths bspline jumps [bsplinename]")
                .executes((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    Util.sendMessage(sender, "&aThere are "+bSpline.getAmountOfJumps()+".");
                }));

        bSplineBranch.addCommand(new SYSCommand("setjumps")
                .setArguments(new BSplineArgument(), new IntegerArgument())
                .setUsage("/paths bspline setjumps [bsplinename] [amount]")
                .executes((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    bSpline.setAmountOfJumps(Integer.parseInt(args[1]));
                    Util.sendMessage(sender, "&aYou have set there to be "+bSpline.getAmountOfJumps()+" jumps.");
                }));



        locationCommandBranch.addCommand(new SYSCommand("insert")
                .setArguments(new BSplineArgument(), new IntegerArgument())
                .setUsage("/paths bspline location insert [splinename] [index to insert at]")
                .executesPlayer((sender, args) -> {
                    int number = Integer.parseInt(args[1]);
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    LocationList controlPoints = bSpline.getControlPoints();
                    int size = controlPoints.size();
                    if (number > size){
                        Util.sendMessage(sender, "&cYou can't insert a location here");
                        return;
                    }
                    controlPoints.add(number, sender.getLocation());
                    bSpline.setControlPoints(controlPoints);
                    Util.sendMessage(sender, "&aSuccessfully inserted a location");
                }));

        locationCommandBranch.addCommand(new SYSCommand("add")
                .setArguments(new BSplineArgument())
                .setUsage("/paths bspline location add [splinename]")
                .executesPlayer((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    LocationList controlPoints = bSpline.getControlPoints();
                    controlPoints.add(sender.getLocation());
                    bSpline.setControlPoints(controlPoints);
                    Util.sendMessage(sender, "&aSuccessfully added a location");
                }));

        locationCommandBranch.addCommand(new SYSCommand("set")
                .setArguments(new BSplineArgument(), new IntegerArgument())
                .setUsage("/paths bspline location set [splinename] [index]")
                .executesPlayer((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    bSpline.setLocation(Integer.parseInt(args[1]), sender.getLocation());
                    Util.sendMessage(sender, "&aSuccessfully set a location");
                }));

        bSplineBranch.addCommand(new SYSCommand("show")
                .setArguments(new BSplineArgument())
                .setUsage("/paths bspline show [bsplinename]")
                .executes((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    if (bSpline.isShowing()){
                        bSpline.setShowing(false);
                        Util.sendMessage(sender, "&aToggled showing off");
                        return;
                    }
                    if (bSpline.getControlPoints().size()<4){
                        Util.sendMessage(sender, "&cThere must be at least 4 points before a path can be made");
                        return;
                    }
                    if (bSpline.getAmountOfJumps() == 0){
                        Util.sendMessage(sender, "&cRemember to set the amount of jumps first!");
                        return;
                    }
                    bSpline.showPoints();
                    Util.sendMessage(sender, "&aToggled showing on");
                }));

        bSplineBranch.addCommand(new SYSCommand("period")
                .setArguments(new BSplineArgument(), new IntegerArgument())
                .setUsage("/paths bspline period [bspline name] [period amount in ticks]")
                .executesPlayer((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    bSpline.setPeriod(Integer.parseInt(args[1]));
                    Util.sendMessage(sender, "&aSet period: "+args[1]);
                }));

        bSplineBranch.addCommand(new SYSCommand("ride")
                .setArguments(new BSplineArgument(), new NumberArgument(), new BooleanArgument(), new BooleanArgument(), new GameModeArgument(), new PlayerArgument())
                .setUsage("/paths bspline ride [bsplinename] [yaw-change] [fadein] [fadeout] [gamemode] [player]")
                .executes((sender, args) -> {
                    BSpline bSpline = BSplineData.bSplineMap.get(args[0]);
                    Player player = Bukkit.getPlayer(args[5]);
                    double yawChange = Double.parseDouble(args[1]);
                    boolean fadeIn = Boolean.parseBoolean(args[2]);
                    boolean fadeOut = Boolean.parseBoolean(args[3]);
                    GameMode gameMode = GameMode.valueOf(args[4].toUpperCase());
                    bSpline.cinematicTravel(player, yawChange, fadeIn, fadeOut, gameMode);
                }));


    }
}
