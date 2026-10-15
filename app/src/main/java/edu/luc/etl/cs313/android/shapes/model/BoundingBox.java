package edu.luc.etl.cs313.android.shapes.model;

/**
 * A shape visitor for calculating the bounding box, that is, the smallest
 * rectangle containing the shape. The resulting bounding box is returned as a
 * rectangle at a specific location.
 */
public class BoundingBox implements Visitor<Location> {

    @Override
    public Location onCircle(final Circle c) {
        final int radius = c.getRadius();
        return new Location(-radius, -radius, new Rectangle(2 * radius, 2 * radius));
    }

    @Override
    public Location onFill(final Fill f) {
        return f.getShape().accept(this);
    }

    @Override
    public Location onGroup(final Group g) {
        return boundingBox(g.getShapes());
    }

    @Override
    public Location onLocation(final Location l) {
        final Location bounds = l.getShape().accept(this);
        return new Location(
            l.getX() + bounds.getX(),
            l.getY() + bounds.getY(),
            bounds.getShape()
        );
    }

    @Override
    public Location onRectangle(final Rectangle r) {
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
        return new Location(0, 0, new Rectangle(r.getWidth(), r.getHeight()));
    }

    @Override
    public Location onStrokeColor(final StrokeColor c) {
        return c.getShape().accept(this);
        return c.getShape().accept(this);
    }

    @Override
    public Location onOutline(final Outline o) {
        return o.getShape().accept(this);
        return o.getShape().accept(this);
    }

    @Override
    public Location onPolygon(final Polygon p) {
        return boundingBox(p.getPoints());
    }

    private Location boundingBox(final Iterable<? extends Shape> shapes) {
        boolean empty = true;
        int left = 0;
        int top = 0;
        int right = 0;
        int bottom = 0;

        for (final Shape shape : shapes) {
            final Location bounds = shape.accept(this);
            final Rectangle rectangle = (Rectangle) bounds.getShape();
            final int shapeLeft = bounds.getX();
            final int shapeTop = bounds.getY();
            final int shapeRight = shapeLeft + rectangle.getWidth();
            final int shapeBottom = shapeTop + rectangle.getHeight();

            if (empty) {
                left = shapeLeft;
                top = shapeTop;
                right = shapeRight;
                bottom = shapeBottom;
                empty = false;
            } else {
                left = Math.min(left, shapeLeft);
                top = Math.min(top, shapeTop);
                right = Math.max(right, shapeRight);
                bottom = Math.max(bottom, shapeBottom);
            }
        }

        return new Location(left, top, new Rectangle(right - left, bottom - top));
    }
}
