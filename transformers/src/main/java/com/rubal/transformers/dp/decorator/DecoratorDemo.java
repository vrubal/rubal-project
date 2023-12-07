package com.rubal.transformers.dp.decorator;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class DecoratorDemo {
    public static void main(String[] args) {
        Shape rectangle = new Rectangle();
        Shape green = new Rectangle();
        RedShapeDecorator redRectangle = new RedShapeDecorator(rectangle);
        GreenShapeDecorator greenRectangle = new GreenShapeDecorator(green);
        redRectangle.draw();
        greenRectangle.draw();

    }
}
