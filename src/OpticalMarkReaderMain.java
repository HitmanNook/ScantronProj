import FileIO.PDFHelper;
import core.DImage;
import processing.core.PImage;

import javax.swing.*;
import java.io.File;
import java.io.FileWriter;
import java.util.ArrayList;
import java.util.HashMap;

import static java.lang.Integer.MAX_VALUE;

public class OpticalMarkReaderMain {
    public static void main(String[] args) {
        String pathToPdf = fileChooser();
        System.out.println("Loading pdf at " + pathToPdf);
        ArrayList<PImage> imgs = PDFHelper.getPImagesFromPdf(pathToPdf);
        System.out.println("Loaded " + imgs.size() + " pages");
        DImage ansImg = new DImage(imgs.get(0));
        short[][] grid = ansImg.getBWPixelGrid();
        grid = crop(grid, 0, 0, 800, 400);
        ArrayList<String> answers = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            answers.add(findMarked(grid, 109 + (i * 48)));
        }
        //System.out.println(answers);
        String output = "";
        for (int i = 1; i < imgs.size(); i++) {
            DImage img = new DImage(imgs.get(i));
            short[][] newGrid = img.getBWPixelGrid();
            newGrid = crop(newGrid, 0, 0, 800, 400);
            String currentStudent = i + ", ";
            String checkedAns = checkAns(answers, newGrid);
            currentStudent += countRight(checkedAns);
            output += currentStudent + checkedAns + "\n";
        }
        writeToFile(output, "storedResults/output.csv");
    }

    private static String countRight(String checkedAns) {
        int right = 0;
        String[] splitAns = checkedAns.split(", ");
        for (String s : splitAns) {
            if (s.equals("right")) {
                right++;
            }
        }
        return right + ", ";
    }

    private static String checkAns(ArrayList<String> answers, short[][] grid) {
        ArrayList<String> studentAnswers = new ArrayList<>();
        for (int i = 0; i < 12; i++) {
            studentAnswers.add(findMarked(grid, 109 + (i * 48)));
        }
        //System.out.println(studentAnswers);
        String output = "";
        for (int i = 0; i < 12; i++) {
            if (answers.get(i).equals(studentAnswers.get(i))) {
                output += "right, ";
            } else {
                output += "wrong, ";
            }
        }
        return output;
    }

    public static String findMarked(short[][] grid, int v) {
        //grid = crop(grid, 0, 0, 500, 500);

        HashMap<String, Integer> options = new HashMap<>();

        options.put("a", 106);
        options.put("b", 131);
        options.put("c", 156);
        options.put("d", 178);
        options.put("e", 204);
        String lowestAvgOption = "";
        int lowestAvg = MAX_VALUE;
        for (String s : options.keySet()) {
            int avg = 0;
            for (int r = v; r < v + 20; r++) {
                for (int c = options.get(s); c < options.get(s) + 20; c++) {
                    avg += grid[r][c];
                }
            }
            avg = avg / 400;
            if (avg < lowestAvg) {
                lowestAvg = avg;
                lowestAvgOption = s;
            }
        }
        //System.out.println("The most marked option is " + lowestAvgOption);
        return lowestAvgOption;
    }

    private static String fileChooser() {
        String userDirLocation = System.getProperty("user.dir");
        File userDir = new File(userDirLocation);
        JFileChooser fc = new JFileChooser(userDir);
        int returnVal = fc.showOpenDialog(null);
        File file = fc.getSelectedFile();
        return file.getAbsolutePath();
    }

    private static short[][] crop(short[][] grid, int sr, int sc, int r1, int c1) {
        short[][] newGrid = new short[r1 - sr][c1 - sc];
        for (int r = sr; r < r1; r++) {
            for (int c = sc; c < c1; c++) {
                newGrid[r - sr][c - sc] = grid[r][c];
            }
        }
        return newGrid;
    }

    private static void writeToFile(String output, String path) {
        try (FileWriter writer = new FileWriter(path)) {
            writer.write(output);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }
}
