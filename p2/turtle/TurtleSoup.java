/* Copyright (c) 2007-2016 MIT 6.005 course staff, all rights reserved.
 * Redistribution of original or derived work requires permission of course staff.
 */
package p2.turtle;

import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.ArrayList;

public class TurtleSoup {

    /**
     * Draw a square.
     * 
     * @param turtle the turtle context
     * @param sideLength length of each side
     */
    public static void drawSquare(Turtle turtle, int sideLength) {
        turtle.draw();
        turtle.forward(sideLength);
        turtle.turn(90.00);
        turtle.forward(sideLength);
        turtle.turn(90.00);
        turtle.forward(sideLength);
        turtle.turn(90.00);
        turtle.forward(sideLength);
        turtle.turn(90.00);
    }

    /**
     * Determine inside angles of a regular polygon.
     * 
     * There is a simple formula for calculating the inside angles of a polygon;
     * you should derive it and use it here.
     * 
     * @param sides number of sides, where sides must be > 2
     * @return angle in degrees, where 0 <= angle < 360
     */
    public static double calculateRegularPolygonAngle(int sides) {
        return 180.00 - 360.00 / sides;
    }

    /**
     * Determine number of sides given the size of interior angles of a regular polygon.
     * 
     * There is a simple formula for this; you should derive it and use it here.
     * Make sure you *properly round* the answer before you return it (see java.lang.Math).
     * HINT: it is easier if you think about the exterior angles.
     * 
     * @param angle size of interior angles in degrees, where 0 < angle < 180
     * @return the integer number of sides
     */
    public static int calculatePolygonSidesFromAngle(double angle) {
        return (int) (360/(180-angle)) + (360%(180-angle) != 0 ? 1 : 0);
    }

    /**
     * Given the number of sides, draw a regular polygon.
     * 
     * (0,0) is the lower-left corner of the polygon; use only right-hand turns to draw.
     * 
     * @param turtle the turtle context
     * @param sides number of sides of the polygon to draw
     * @param sideLength length of each side
     */
    public static void drawRegularPolygon(Turtle turtle, int sides, int sideLength) {
        double degrees = 180 - calculateRegularPolygonAngle(sides);
        for (int i = 0; i < sides; ++i) {
            turtle.forward(sideLength);
            turtle.turn(degrees);
        }
    }

    /**
     * Given the current direction, current location, and a target location, calculate the Bearing
     * towards the target point.
     * 
     * The return value is the angle input to turn() that would point the turtle in the direction of
     * the target point (targetX,targetY), given that the turtle is already at the point
     * (currentX,currentY) and is facing at angle currentBearing. The angle must be expressed in
     * degrees, where 0 <= angle < 360. 
     *
     * HINT: look at http://en.wikipedia.org/wiki/Atan2 and Java's math libraries
     * 
     * @param currentBearing current direction as clockwise from north
     * @param currentX current location x-coordinate
     * @param currentY current location y-coordinate
     * @param targetX target point x-coordinate
     * @param targetY target point y-coordinate
     * @return adjustment to Bearing (right turn amount) to get to target point,
     *         must be 0 <= angle < 360
     */
    public static double calculateBearingToPoint(double currentBearing, int currentX, int currentY,
                                                 int targetX, int targetY) {
        double angle = Math.atan2(targetY - currentY, targetX - currentX) * 180.0 / Math.PI;
        if (angle < 0)
            angle += 360.0;
        double bearing = (360 - angle + 90 >= 360 ? 90 - angle : 360 - angle + 90) - currentBearing;
        return bearing < 0 ? 360.0 + bearing : bearing;
    }

    /**
     * Given a sequence of points, calculate the Bearing adjustments needed to get from each point
     * to the next.
     * 
     * Assumes that the turtle starts at the first point given, facing up (i.e. 0 degrees).
     * For each subsequent point, assumes that the turtle is still facing in the direction it was
     * facing when it moved to the previous point.
     * You should use calculateBearingToPoint() to implement this function.
     * 
     * @param xCoords list of x-coordinates (must be same length as yCoords)
     * @param yCoords list of y-coordinates (must be same length as xCoords)
     * @return list of Bearing adjustments between points, of size 0 if (# of points) == 0,
     *         otherwise of size (# of points) - 1
     */
    public static List<Double> calculateBearings(List<Integer> xCoords, List<Integer> yCoords) {
        double currentBearing = 0.0;
        int currentX = xCoords.get(0), currentY = yCoords.get(0), targetX, targetY;
        int length = xCoords.size();
        List<Double> result = new ArrayList<>();
        for (int i = 1; i < length; i++) {
            targetX = xCoords.get(i);
            targetY = yCoords.get(i);
            result.add(calculateBearingToPoint(currentBearing, currentX, currentY, targetX, targetY));
            currentBearing = result.get(i - 1);
            currentX = targetX;
            currentY = targetY;
        }
        return result;
    }
    
    /**
     * Given a set of points, compute the convex hull, the smallest convex set that contains all the points 
     * in a set of input points. The gift-wrapping algorithm is one simple approach to this problem, and 
     * there are other algorithms too.
     * 
     * @param points a set of points with xCoords and yCoords. It might be empty, contain only 1 point, two points or more.
     * @return minimal subset of the input points that form the vertices of the perimeter of the convex hull
     */
    public static Set<Point> convexHull(Set<Point> points) {
        if (points.size() <= 3) return points;
        Set<Point> convexHullPoints = new HashSet<Point>();
        Point a = new Point(99999, 99999);
        // 找出点集中x,y最小的点作为参照点
        for (Point i : points) {
            if (i.x() < a.x() || (i.x() == a.x() && i.y() < a.y()))
                a = i;
        }
        Point curPoint = a, minPoint = null, lastPoint = a;
        double x1 = 0.0, y1 = -1.0;
        do {
            convexHullPoints.add(curPoint);
            double minTheta = 99999, x2 = 0.0, y2 = 0.0;
            for (Point i : points) {
                if ((!convexHullPoints.contains(i) || i == a) && (i != lastPoint)) {
                    double x3 = i.x() - curPoint.x(), y3 = i.y() - curPoint.y();
                    double Theta = Math
                            .acos((x1 * x3 + y1 * y3) / Math.sqrt(x1 * x1 + y1 * y1) / Math.sqrt(x3 * x3 + y3 * y3));
                     System.out.println(i.x() + " " + i.y() + " " + Theta);
                    if (Theta < minTheta || (Theta == minTheta && x3 * x3 + y3 * y3 > Math.pow(i.x() - minPoint.x(), 2)
                            + Math.pow(i.y() - minPoint.y(), 2))) {
                        minPoint = i;
                        minTheta = Theta;
                        x2 = x3;
                        y2 = y3;
                    }
                }
            }
            x1 = x2;
            y1 = y2;
            lastPoint = curPoint;
            curPoint = minPoint;
             System.out.println(curPoint.x() + " " + curPoint.y());
        } while (curPoint != a);
        return convexHullPoints;
    }
    
    /**
     * Draw your personal, custom art.
     * 
     * Many interesting images can be drawn using the simple implementation of a turtle.  For this
     * function, draw something interesting; the complexity can be as little or as much as you want.
     * 
     * @param turtle the turtle context
     */
    public static void drawPersonalArt(Turtle turtle) {
        turtle.draw();
        int i=0;
        while(i<90)
        {
            turtle.color(PenColor.RED);
            turtle.forward(10);
            turtle.turn(4);
            i++;

        }

        turtle.color(PenColor.GREEN);
        turtle.turn(90);
        turtle.forward(140);
        for(i=0;i<9;i++)
        {
            turtle.color(PenColor.BLACK);
            turtle.turn(120);
            turtle.forward(140);
            turtle.color(PenColor.GREEN);
            turtle.turn(180);
            turtle.forward(140);
            i++;
        }
        turtle.color(PenColor.CYAN);
        turtle.turn(180);
        turtle.forward(70);
        turtle.turn(90);
        i=0;
        while(i<90)
        {
            turtle.color(PenColor.RED);
            turtle.forward(10);
            turtle.turn(8);
            i++;

        }



        turtle.turn(90);

    }

    /**
     * Main method.
     * 
     * This is the method that runs when you run "java TurtleSoup".
     * 
     * @param args unused
     */
    public static void main(String args[]) {
        DrawableTurtle turtle = new DrawableTurtle();

        drawSquare(turtle, 40);

        // draw the window
        turtle.draw();
    }

}
