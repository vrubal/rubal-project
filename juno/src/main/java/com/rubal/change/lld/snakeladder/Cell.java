package com.rubal.change.lld.snakeladder;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Cell {
    CellType cellType;
    int start;

    int end;
    public Cell(int x, int y, CellType cellType){
        this.start =x;
        this.end=y;
        this.cellType=cellType;
    }
}
