package layout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Collections;

public class BoxLayout extends JPanel {

    private int facets = 1;
    private int boxScalings = 8;
    private double w = 900.0;
    private double h = 600.0;
    private double minBoxW = 40.0;
    private double minBoxH = 20.0;
    private int nrBoxesHor;
    private int nrBoxesVer;

    private Box minBox;

    private Grid iGrid;

    public BoxLayout(int facets, double vizW, double vizH, double minBoxW, double minBoxH) {
        this.facets = facets;
        this.w = vizW;
        this.h = vizH;
        this.minBoxW = minBoxW;
        this.minBoxH = minBoxH;

        refreshLayout(vizW, vizH);
    }

    public void refreshLayout(double vizW, double vizH) {
        this.w = vizW;
        this.h = vizH;
        if(findFillingMinBox()) {
            iGrid = new Grid(minBox, nrBoxesHor, nrBoxesVer);
            iGrid.splitUpTo(facets);

        } else {
            // TODO: throw exception
        }
    }

    private boolean findFillingMinBox() {
        nrBoxesHor = (int)(w / minBoxW);
        nrBoxesVer = (int)(h / minBoxH);
        if(nrBoxesHor * nrBoxesVer >= facets) {
            double fillingMinW = minBoxW + (w % minBoxW) / (double)nrBoxesHor;
            double fillingMinH = minBoxH + (h % minBoxH) / (double)nrBoxesVer;
            minBox = Box.getMinInstance(fillingMinW, fillingMinH);
            return true;
        } else {
            System.out.println("Can't fit " + facets + " Facets of minSize ( " + minBoxW + ", " + minBoxH + " ) into Area of size ( " + w + ", " + h + " )");
            return false;
        }
    }

    public ArrayList<Box> getBoxes() {
        return iGrid.getBoxes();
    }

    public ArrayList<VisualBox> getVisualBoxes() {
        return iGrid.getVisualBoxes();
    }

    @Override
    protected void paintComponent(Graphics g) {
        for(VisualBox vb : iGrid.getVisualBoxes()) {
            vb.male(g, iGrid.getBoxes(), iGrid.getVisualBoxes(), facets);
        }
    }
}
