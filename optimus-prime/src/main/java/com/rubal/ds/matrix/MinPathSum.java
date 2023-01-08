package com.rubal.ds.matrix;

/**
 * @author vrubal
 * @created
 * @project rubal-project
 */
public class MinPathSum {
    public static int minPathSum(int[][] grid) {
        int height = grid.length;
        int width = grid[0].length;
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                if(row == 0 && col == 0) grid[row][col] = grid[row][col];
                else if(row == 0 && col != 0) grid[row][col] =
                        grid[row][col] + grid[row][col - 1];
                else if(col == 0 && row != 0) grid[row][col] =
                        grid[row][col] + grid[row - 1][col];
                else grid[row][col] = grid[row][col] +
                            Math.min(grid[row - 1][col], grid[row][col - 1]);
            }
        }
        return grid[height - 1][width - 1];
    }

    public static void main(String[] args) {
        int[][] arr = new int [][]{
                {1,3,1},
                {1,5,1},
                {4,2,1}};
        System.out.println(minPathSum(arr));
    }
}
