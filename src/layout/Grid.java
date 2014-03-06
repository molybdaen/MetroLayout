package layout;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;

public class Grid {

    private Box minBox;
    int gridWidth, gridHeight;
    private VisualBox[][] grid;
    private ArrayList<Box> boxes;
    private ArrayList<VisualBox> visualBoxes;

    public Grid(Box minBox, int gridWidth, int gridHeight) {
        this.minBox = minBox;
        this.gridWidth = gridWidth;
        this.gridHeight = gridHeight;
        grid = new VisualBox[gridHeight][gridWidth];
        boxes = new ArrayList<Box>();
        visualBoxes = new ArrayList<VisualBox>();
        generateScaledBoxes();
        fillGrid();
    }

    /**
     * Splits the largest box until there are at least 'minNrBoxes' in the visualization area.
     * @param minNrBoxes
     */
    public void splitUpTo(int minNrBoxes) {
        while(visualBoxes.size() < minNrBoxes) {
            VisualBox vbox = visualBoxes.remove(0);
            split(vbox);
            Collections.sort(visualBoxes);
        }
        markFacets(minNrBoxes);
        clean(minNrBoxes);
        updateMinBox();
    }

    private void markFacets(int minNrBoxes) {
        for(VisualBox vb : visualBoxes) {
            if(visualBoxes.indexOf(vb) < minNrBoxes) {
                vb.isFacet(true);
            } else {
                vb.isFacet(false);
            }
        }
    }

    /**
     * Splits a visual box into its fragments and inserts the fragments at the location of their parent.
     * @param vb
     */
    private void split(VisualBox vb) {
        Box[][] fragments = vb.getBox().split();
        for(int fr = 0; fr < fragments.length; fr++) {
            for(int fc = 0; fc < fragments[fr].length; fc++) {
                Box b = fragments[fr][fc];
                int r = vb.getIy() + fr * b.getMinSpanV();
                int c = vb.getIx() + fc * b.getMinSpanH();
                putBoxAt(fragments[fr][fc], r, c);
            }
        }
    }

    private void updateMinBox() {
        boolean lastRowFree = true;
        for(int c = 0; c < grid[grid.length-1].length; c++) {
            lastRowFree &= !grid[grid.length-1][c].isFacet();
        }
        double newH = minBox.getH();
        if(lastRowFree) {
            VisualBox vb = grid[grid.length-1][0];
            newH += (vb.getH() / (grid.length - vb.getBox().getMinSpanV()));
        }

        boolean lastColFree = true;
        for(int r = 0; r < grid.length; r++) {
            lastColFree &= !grid[r][grid[r].length-1].isFacet();
        }
        double newW = minBox.getW();
        if(lastColFree) {
            VisualBox vb = grid[0][grid[0].length-1];
            newW += (vb.getW() / (grid[0].length - vb.getBox().getMinSpanH()));
        }
        minBox.updateDim(newW, newH);
    }

    /**
     *  Forces the largest box to split until the '#facets' largest boxes are no bigger than 'x' times the smallest box.
     *  Ensures that there are not a few very large boxes with very many small boxes surrounding them.
     * @param minNrBoxes
     */
    public void clean(int minNrBoxes) {
        for(int i = visualBoxes.size()-1; i >= 0; i--) {
            VisualBox small_vb = visualBoxes.get(i);
            if(small_vb.isFacet()) {
                VisualBox large_vb = visualBoxes.remove(0);
                if(small_vb.getBox().getType() + 2 < large_vb.getBox().getType()) {
                    split(large_vb);
                    i += 1;
                    Collections.sort(visualBoxes);
                } else {
                    visualBoxes.add(0,large_vb);
                }
                markFacets(minNrBoxes);
            }
        }
    }

    private ArrayList<Box> generateScaledBoxes() {
        Box b1 = minBox;
        while(b1.getMinSpanH() <= gridWidth && b1.getMinSpanV() <= gridHeight) {
            boxes.add(b1);
            b1 = b1.getNextBigger();
        }
//        for(int i = 0; i < 5; i++) {
//            boxes.add(b1);
//            b1 = b1.getNextBigger();
//        }
        Collections.sort(boxes);
        return boxes;
    }

    private void fillGrid() {
        double x = 0.0, y = 0.0;
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[r].length; c++) {
                if(grid[r][c] == null) {
                    for(Box b : boxes) {
                        if(putBoxAt(b, r, c)) {
                            break;
                        }
                    }
                }
            }
        }
        Collections.sort(visualBoxes);
    }

    private boolean putBoxAt(Box b, int r, int c) {
        if(r+b.getMinSpanV() <= gridHeight && c+b.getMinSpanH() <= gridWidth) {
            VisualBox vb = new VisualBox(c, r, b);
            visualBoxes.add(vb);
            for(int mr = r; mr < r + b.getMinSpanV(); mr++) {
                for(int mc = c; mc < c + b.getMinSpanH(); mc++) {
                    grid[mr][mc] = vb;
                }
            }
            return true;
        } else {
            return false;
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        for(int r = 0; r < grid.length; r++) {
            for(int c = 0; c < grid[r].length; c++) {
                sb.append("\t"+grid[r][c].getBox().getType());
            }
            sb.append("\n");
        }
        return sb.toString();
    }

    public ArrayList<Box> getBoxes() {
        return boxes;
    }

    public ArrayList<VisualBox> getVisualBoxes() {
        return visualBoxes;
    }
}
