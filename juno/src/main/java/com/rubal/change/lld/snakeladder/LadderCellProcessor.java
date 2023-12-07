package com.rubal.change.lld.snakeladder;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class LadderCellProcessor implements CellProcessor{
    @Override
    public int process(Cell cell, int pos) {
        System.out.println("Got ladder");
        return cell.end;
    }
}
