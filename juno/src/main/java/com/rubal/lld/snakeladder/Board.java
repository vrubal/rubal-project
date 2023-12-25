package com.rubal.lld.snakeladder;

import java.util.List;
import java.util.Map;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Board {
    int size;
    Map<CellType, List<Cell>> cells;

    public Board(int size, Map<CellType, List<Cell>> cells) {
        this.size = size;
        this.cells = cells;
    }
}
