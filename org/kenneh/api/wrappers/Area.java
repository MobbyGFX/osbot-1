package org.kenneh.api.wrappers;

import org.osbot.script.rs2.map.Position;

import java.awt.Rectangle;
import java.util.Arrays;

/**
 * @author Timer
 */
public class Area {
    public int npoints;
    public int[] xpoints;
    public int[] ypoints;
    private int plane = -1;

    protected Rectangle bounds;
    private static final int MIN_LENGTH = 4;

    public Area() {
        xpoints = new int[MIN_LENGTH];
        ypoints = new int[MIN_LENGTH];
    }

    public Area(final Position t1, final Position t2) {
        this(new Position[]{
                new Position(Math.min(t1.getX(), t2.getX()), Math.min(t1.getY(), t2.getY()), t1.getZ()),
                new Position(Math.max(t1.getX(), t2.getX()), Math.min(t1.getY(), t2.getY()), t1.getZ()),
                new Position(Math.max(t1.getX(), t2.getX()), Math.max(t1.getY(), t2.getY()), t2.getZ()),
                new Position(Math.min(t1.getX(), t2.getX()), Math.max(t1.getY(), t2.getY()), t2.getZ())
        });
    }

    public Area(final Position[] bounds) {
        this();
        for (final Position Position : bounds) {
            if (plane != -1 && Position.getZ() != plane) {
                throw new RuntimeException("area does not support 3d");
            }
            plane = Position.getZ();
            addPosition(Position.getX(), Position.getY());
        }
    }

    public void reset() {
        npoints = 0;
        bounds = null;
    }

    public void invalidate() {
        bounds = null;
    }

    public void translate(final int deltaX, final int deltaY) {
        for (int i = 0; i < npoints; i++) {
            xpoints[i] += deltaX;
            ypoints[i] += deltaY;
        }
        if (bounds != null) {
            bounds.translate(deltaX, deltaY);
        }
    }

    public void addPosition(final int x, final int y) {
        if (npoints >= xpoints.length || npoints >= ypoints.length) {
            int newLength = npoints * 2;
            if (newLength < MIN_LENGTH) {
                newLength = MIN_LENGTH;
            } else if ((newLength & (newLength - 1)) != 0) {
                newLength = Integer.highestOneBit(newLength);
            }

            xpoints = Arrays.copyOf(xpoints, newLength);
            ypoints = Arrays.copyOf(ypoints, newLength);
        }
        xpoints[npoints] = x;
        ypoints[npoints] = y;
        npoints++;
        if (bounds != null) {
            updateBounds(x, y);
        }
    }

    public Rectangle getBounds() {
        if (npoints == 0) {
            return new Rectangle();
        }
        if (bounds == null) {
            calculateBounds(xpoints, ypoints, npoints);
        }
        return bounds.getBounds();
    }

    public int getPlane() {
        return plane;
    }

    public boolean contains(final Position Position) {
        return !(plane != -1 && Position.getZ() != plane) && contains(Position.getX(), Position.getY());
    }

    public boolean contains(final int x, final int y) {
        return contains((double) x, (double) y);
    }

    public boolean contains(double x, double y) {
        if (npoints <= 2 || !getBounds().contains(x, y)) {
            return false;
        }
        int hits = 0;

        int lastx = xpoints[npoints - 1];
        int lasty = ypoints[npoints - 1];
        int curx, cury;

        for (int i = 0; i < npoints; lastx = curx, lasty = cury, i++) {
            curx = xpoints[i];
            cury = ypoints[i];

            if (cury == lasty) {
                continue;
            }

            int leftx;
            if (curx < lastx) {
                if (x >= lastx) {
                    continue;
                }
                leftx = curx;
            } else {
                if (x >= curx) {
                    continue;
                }
                leftx = lastx;
            }

            double test1, test2;
            if (cury < lasty) {
                if (y < cury || y >= lasty) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - curx;
                test2 = y - cury;
            } else {
                if (y < lasty || y >= cury) {
                    continue;
                }
                if (x < leftx) {
                    hits++;
                    continue;
                }
                test1 = x - lastx;
                test2 = y - lasty;
            }

            if (test1 < (test2 / (lasty - cury) * (lastx - curx))) {
                hits++;
            }
        }

        return ((hits & 1) != 0);
    }

    void calculateBounds(final int xpoints[], final int ypoints[], final int npoints) {
        int boundsMinX = Integer.MAX_VALUE;
        int boundsMinY = Integer.MAX_VALUE;
        int boundsMaxX = Integer.MIN_VALUE;
        int boundsMaxY = Integer.MIN_VALUE;

        for (int i = 0; i < npoints; i++) {
            final int x = xpoints[i];
            boundsMinX = Math.min(boundsMinX, x);
            boundsMaxX = Math.max(boundsMaxX, x);

            final int y = ypoints[i];
            boundsMinY = Math.min(boundsMinY, y);
            boundsMaxY = Math.max(boundsMaxY, y);
        }
        bounds = new Rectangle(boundsMinX, boundsMinY, boundsMaxX - boundsMinX, boundsMaxY - boundsMinY);
    }

    void updateBounds(final int x, final int y) {
        if (x < bounds.getX()) {
            bounds.width = bounds.width + (bounds.x - x);
            bounds.x = x;
        } else {
            bounds.width = Math.max(bounds.width, x - bounds.x);
        }

        if (y < bounds.getY()) {
            bounds.height = bounds.height + (bounds.y - y);
            bounds.y = y;
        } else {
            bounds.height = Math.max(bounds.height, y - bounds.y);
        }
    }
}