package com.rubal.change.lld.snakeladder;

import java.util.Objects;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class Player {
    int id;
    String name;
    int currPos;

    public Player(int id, String name, int currPos) {
        this.id = id;
        this.name = name;
        this.currPos= currPos;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Player player = (Player) o;
        return id == player.id;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id);
    }
}
