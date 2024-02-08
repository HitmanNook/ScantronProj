package Filters;

import Interfaces.Interactive;
import Interfaces.PixelFilter;
import core.DImage;

import java.sql.SQLOutput;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.MAX_VALUE;

public class DisplayInfoFilter implements PixelFilter, Interactive{
    public DisplayInfoFilter() {
        System.out.println("Filter running...");
    }

    public void findMarked(short[][] grid) {
        //grid = crop(grid, 0, 0, 500, 500);

        HashMap<String,Integer> options = new HashMap<>();

        options.put("a", 106);
        options.put("b", 131);
        options.put("c", 156);
        options.put("d", 178);
        options.put("e", 204);
        String lowestAvgOption = "";
        int lowestAvg = MAX_VALUE;
        for(String s : options.keySet()){
            System.out.println(s);
            int avg = 0;
            for(int r = options.get(s); r < options.get(s) + 20; r++){
                for(int c = 109; c < 129; c++){
                    avg += grid[r][c];
                }
            }
            avg = avg / 400;
            System.out.println(s + " has " + avg + " marked");
            if(avg < lowestAvg){
                lowestAvg = avg;
                System.out.println(s);
                lowestAvgOption = s;
            }
        }
        System.out.println("The most marked option is " + lowestAvgOption);
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
                if (grid[r][c] < 100) blackCount++;
                if (grid[r][c] > 240) whiteCount++;
            }
        }

        System.out.println(blackCount + " nearly black pixels and " + whiteCount + " nearly white pixels");
        System.out.println("----------------------------------------");
        System.out.println("If you want, you could output information to a file instead of printing it.");
        findMarked(grid);
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


    @Override
    public void mouseClicked(int mouseX, int mouseY, DImage img) {
        short [][] grid = img.getBWPixelGrid();
        grid = crop(grid, 0, 0, 500, 500);
        System.out.println("Mouse clicked at " + mouseX + ", " + mouseY + " with pixel value " + grid[mouseY][mouseX]);
    }

    @Override
    public void keyPressed(char key) {

    }
}

