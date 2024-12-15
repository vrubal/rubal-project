package com.rubal.transformers.dp.decorator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class GreenShapeDecorator extends ShapeDecorator{
    private Shape shape;
    public GreenShapeDecorator(Shape shape) {
        super(shape);
    }

    @Override
    public void draw() {
        super.draw();
        System.out.println("Filling with Green Color");

    }
}
