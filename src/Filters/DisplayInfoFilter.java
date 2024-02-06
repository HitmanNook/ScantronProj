package Filters;

import Interfaces.PixelFilter;
import core.DImage;

import java.sql.SQLOutput;

public class DisplayInfoFilter implements PixelFilter {
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }

    @Override
    public DImage processImage(DImage img) {
        short[][] grid = img.getBWPixelGrid();

        System.out.println("Image is " + grid.length + " by "+ grid[0].length);
        grid = crop(grid, 0, 0, 500, 500);
        int blackCount = 0;
        int whiteCount = 0;
        for (int r = 0; r < grid.length; r++) {
            for (int c = 0; c < grid[0].length; c++) {
                if (grid[r][c] < 10) blackCount++;
                if (grid[r][c] > 240) whiteCount++;
            }
        }

        System.out.println(blackCount + " nearly black pixels and " + whiteCount + " nearly white pixels");
        System.out.println("----------------------------------------");
        System.out.println("If you want, you could output information to a file instead of printing it.");
        img.setPixels(grid);
        return img;
    }

    private short[][] crop(short[][] grid, int sr, int sc, int r1, int c1) {
short[][] newGrid = new short[r1-sr][c1-sc];
        for (int r = sr; r < r1; r++) {
            for (int c = sc; c < c1; c++) {
                newGrid[r-sr][c-sc] = grid[r][c];
            }
        }
        return newGrid;
    }
}

