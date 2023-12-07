package com.rubal.transformers.dp.decorator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public abstract class ShapeDecorator implements Shape{
    private Shape decoratedShape;

    @Override
    public void draw() {
        decoratedShape.draw();
    }

    public ShapeDecorator(Shape shape){
        this.decoratedShape = shape;
    }
}
