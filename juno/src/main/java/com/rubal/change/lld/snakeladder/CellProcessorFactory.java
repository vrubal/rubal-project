package com.rubal.change.lld.snakeladder;

/**
 * @author
 * @created
 * @project rubal-project
 */
public class CellProcessorFactory {
    static CellProcessor snakeCellProcessor=new SnakeCellProcessor();
    static CellProcessor ladderCellProcessor=new LadderCellProcessor();

    static CellProcessor get(CellType cellType){
        switch (cellType) {
            case SNAKE:
                return snakeCellProcessor;
            case LADDER:
                return ladderCellProcessor;
            default:
                return null;
        }
    }
}
