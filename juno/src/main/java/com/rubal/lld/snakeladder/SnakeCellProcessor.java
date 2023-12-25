package com.rubal.lld.snakeladder;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class SnakeCellProcessor implements CellProcessor{
    @Override
    public int process(Cell cell, int  currPos) {
        System.out.println("Bitten by snake");
        return cell.end;
    }
}
