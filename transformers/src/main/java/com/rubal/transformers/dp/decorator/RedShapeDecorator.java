package com.rubal.transformers.dp.decorator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class RedShapeDecorator extends ShapeDecorator{
    public RedShapeDecorator(Shape shape) {
        super(shape);
    }
    @Override
    public void draw() {
        super.draw();
        System.out.println("Filling with Red Color");
    }
}
