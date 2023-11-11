package com.octane.sllly.octanepathing.objects;

import com.octane.sllly.octanepathing.OctanePathing;
import com.octane.sllly.octanepathing.files.BSplineData;
import com.octane.sllly.octanepathing.utils.LocationUtils;
import com.octane.sllly.octanepathing.utils.Util;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Particle;
import org.bukkit.entity.*;
import org.bukkit.potion.PotionEffect;
import org.bukkit.potion.PotionEffectType;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class BSpline {

    private String identifier;
    private int amountOfJumps;
    private LocationList controlPoints;

    private List<Location> finalPositions;
    private List<Location> finalLocations;

    private boolean showing = false;
    private int period = 3;



    public BSpline(String identifier, int amountOfJumps, LocationList controlPoints) {
        this.identifier = identifier;
        this.amountOfJumps = amountOfJumps;
        this.controlPoints = controlPoints;

        calculateFinalPositions();
        finalLocations = LocationUtils.getLocationsWithDirection(finalPositions);

        BSplineData.bSplineMap.put(identifier, this);
    }

    public BSpline(String identifier) {
        this.identifier = identifier;
        this.controlPoints = new LocationList();
        this.amountOfJumps = 0;

        BSplineData.bSplineMap.put(identifier, this);
    }

    public boolean cinematicTravel(Player player, double yawChange, boolean fadeIn, boolean fadeOut, GameMode gameMode){
        GameMode oldGameMode = player.getGameMode();
        player.setGameMode(GameMode.SPECTATOR);
        if (finalPositions == null || finalPositions.size()<2){
            return false;
        }
        finalLocations = LocationUtils.getLocationsWithDirection(finalPositions);
        finalLocations = updateYaw(finalLocations, yawChange);
        setShowing(false);



//        ArmorStand armorStand = (ArmorStand) finalLocations.get(0).getWorld().spawnEntity(finalLocations.get(0), EntityType.ARMOR_STAND);
//        armorStand.setVisible(false);
//        armorStand.setInvulnerable(true);
        if (fadeIn){
            if (amountOfJumps>32){
                player.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30,0,false, false));
//                armorStand.addPotionEffect(new PotionEffect(PotionEffectType.BLINDNESS, 30,0,false, false));
            }
        }
//        player.teleport(armorStand);
//        player.setSpectatorTarget(armorStand);

        new BukkitRunnable() {
            int count = 0;
            @Override
            public void run() {
                count++;
                if (count > finalLocations.size()-2){
                    //player.setSpectatorTarget(null);
                    player.setGameMode(gameMode);
                    this.cancel();
                    return;
                }
                if (count == finalLocations.size()-30){
                    if (fadeOut){
                        player.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60,0,false, false));
//                        armorStand.addPotionEffect(new PotionEffect(PotionEffectType.DARKNESS, 60,0,false, false));
                    }
                }
                player.teleport(finalLocations.get(count));
            }
        }.runTaskTimer(OctanePathing.plugin, 1, 1);

        return true;
    }

    public List<Location> plot(int jumps, double startingTValue, double endingTValue){
        double difference = endingTValue-startingTValue;
        double step = difference/jumps;
        List<Location> points = new ArrayList<>();
        for(double t = startingTValue; t < endingTValue; t += step){
            points.add(getBSplinePoint(controlPoints, t));
        }
        return points;
    }

    public Location getBSplinePoint(List<Location> controlPoints, double t) {
        int n = controlPoints.size() - 1;
        double x = 0.0, y = 0.0, z = 0.0;

        for (int i = 0; i <= n; i++) {
            double basis = N(i, 3, t); // cubic B-spline
            x += basis * controlPoints.get(i).getX();
            y += basis * controlPoints.get(i).getY();
            z += basis * controlPoints.get(i).getZ();
        }

        return new Location(controlPoints.get(0).getWorld(), x, y, z);
    }

    public double N(int i, int degree, double t) {
        if (degree == 0) {
            if ((t >= i) && (t < i + 1)) {
                return 1.0;
            } else {
                return 0.0;
            }
        } else {
            double a = ((t - i) / degree) * N(i, degree - 1, t);
            double b = ((i + degree + 1 - t) / degree) * N(i + 1, degree - 1, t);
            return a + b;
        }
    }

    public void showPoints(){
        setShowing(true);
        showControlPoints();
        showPathPoints();
    }

    public void showControlPoints(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!showing){
                    this.cancel();
                    return;
                }
                if (amountOfJumps == 0){
                    this.cancel();
                    return;
                }
                for (Location controlPoint : controlPoints) {
                    showPoint(controlPoint, true);
                }
            }
        }.runTaskTimer(OctanePathing.plugin, 0,period);
    }

    public void showPathPoints(){
        new BukkitRunnable() {
            @Override
            public void run() {
                if (!showing){
                    this.cancel();
                    return;
                }
                if (amountOfJumps == 0){
                    this.cancel();
                    return;
                }
                calculateFinalPositions();
                for (Location controlPoint : finalPositions) {
                    showPoint(controlPoint, false);
                }
            }
        }.runTaskTimer(OctanePathing.plugin, 0,period);
    }

    public void calculateFinalPositions(){
        if (controlPoints.size()<4){
            finalPositions = new ArrayList<>();
            return;
        }
        setFinalPositions(plot(amountOfJumps, 3, controlPoints.size()));
    }

    public void showPoint(Location location, boolean glowing){
        ShulkerBullet shulkerBullet = Util.spawnStationaryShulkerBullet(location);
        shulkerBullet.setInvulnerable(true);
        if (glowing){
            shulkerBullet.setGlowing(true);
        }
        new BukkitRunnable() {
            @Override
            public void run() {
                shulkerBullet.remove();
            }
        }.runTaskLater(OctanePathing.plugin, period);
    }

    public void setLocation(int index, Location location){
        Location controlPoint = controlPoints.get(index);
        controlPoint.setX(location.getX());
        controlPoint.setY(location.getY());
        controlPoint.setZ(location.getZ());
    }

    public List<Location> updateYaw(List<Location> locations, double yawChange) {
        List<Location> updatedLocations = new ArrayList<>();
        for (Location loc : locations) {
            Location newLoc = loc.clone(); // Clone to avoid mutating the original Location objects
            newLoc.setYaw((float)(newLoc.getYaw() + yawChange)); // Update the yaw
            updatedLocations.add(newLoc);
        }
        return updatedLocations;
    }

    public String getIdentifier() {
        return identifier;
    }

    public void setIdentifier(String identifier) {
        this.identifier = identifier;
    }

    public int getAmountOfJumps() {
        return amountOfJumps;
    }

    public void setAmountOfJumps(int amountOfJumps) {
        this.amountOfJumps = amountOfJumps;
    }

    public LocationList getControlPoints() {
        return controlPoints;
    }

    public void setControlPoints(LocationList controlPoints) {
        this.controlPoints = controlPoints;
    }

    public List<Location> getFinalPositions() {
        return finalPositions;
    }

    public void setFinalPositions(List<Location> finalPositions) {
        this.finalPositions = finalPositions;
    }

    public List<Location> getFinalLocations() {
        return finalLocations;
    }

    public void setFinalLocations(List<Location> finalLocations) {
        this.finalLocations = finalLocations;
    }

    public boolean isShowing() {
        return showing;
    }

    public void setShowing(boolean showing) {
        this.showing = showing;
    }

    public int getPeriod() {
        return period;
    }

    public void setPeriod(int period) {
        this.period = period;
    }
}
