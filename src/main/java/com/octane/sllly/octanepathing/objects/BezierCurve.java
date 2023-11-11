package com.octane.sllly.octanepathing.objects;

import org.bukkit.Location;

import java.util.ArrayList;
import java.util.List;

public class BezierCurve {
    private final Location p0,p1,p2,p3;
    private final List<Location> points;
    private final double accuracy;

    /**
     *
     * @param jumps Incrementing between 0 and 1, what is the accuracy of the curve, lower is more accurate, but more intensive
     * @param p0 Starting point
     * @param p1 Control point 1
     * @param p2 Control point 2
     * @param p3 Ending point
     */
    public BezierCurve(double jumps, Location p0, Location p1, Location p2, Location p3) {
        this.p0 = p0;
        this.p1 = p1;
        this.p2 = p2;
        this.p3 = p3;
        this.accuracy = jumps;
        this.points = createCurve();
    }

    private List<Location> createCurve(){
        List<Location> cPoints = new ArrayList<>();
        for(double t = 0; t < 1; t += accuracy){
            cPoints.add(resolveT(t));
        }
        return cPoints;
    }

    private Location resolveT(double t){
        double a = (1-t)*(1-t)*(1-t); // (1-t)^3
        double b = 3 * ( 1 - t ) * ( 1 - t ) * t; // 3 * (1-t)^2 * t
        double c = 3 * ( 1 - t ) * t * t; // 3 * (1-t) * t^2
        double d = t * t * t; // t^3

        org.bukkit.Location clone = p0.clone();
        clone.multiply(a) // (1-t)^3 * p0
                .add(p1.clone().multiply(b)) // 3 * (1-t)^2 * t * p1
                .add(p2.clone().multiply(c)) // 3 * (1-t) * t^2 * p2
                .add(p3.clone().multiply(d)); // t^3 * p3

        // This is the formula for a cubic bezier curve
        // (1-t)^3 * p0 + 3 * (1-t)^2 * t * p1 + 3 * (1-t) * t^2 * p2 + t^3 * p3

        return clone;
    }

    public Location getP0() {
        return p0;
    }

    public Location getP1() {
        return p1;
    }

    public Location getP2() {
        return p2;
    }

    public Location getP3() {
        return p3;
    }

    public List<Location> getPoints() {
        return points;
    }

    public double getAccuracy() {
        return accuracy;
    }
}