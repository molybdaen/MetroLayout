package layout;

import javax.swing.*;
import java.awt.*;
import java.util.ArrayList;
import java.util.Random;

public class VisualBox  implements Comparable<VisualBox> {

    private static int nextId = 1;

    private int id = 0;
    private int ix = 0;
    private int iy = 0;
    private boolean isFacet = false;
    private double x, y, w, h;

    private Box box;

    public VisualBox(double x, double y, Box box) {
        this.id = nextId++;
        this.x = x;
        this.y = y;
        this.box = box;
        update();
    }

    public VisualBox(int x, int y, Box box) {
        this.id = nextId++;
        ix = x;
        iy = y;
        this.box = box;
        update();
    }

    public void isFacet(boolean isFacet) {
        this.isFacet = isFacet;
    }

    public boolean isFacet() {
        return isFacet;
    }

    public Box getBox() {
        return box;
    }

    public int getIx() {
        return ix;
    }

    public int getIy() {
        return iy;
    }

    public double getW() {
        return w;
    }

    public double getH() {
        return h;
    }

    public void update() {
        x = ix * Box.getMinBox().getW();
        y = iy * Box.getMinBox().getH();
        w = box.getMinSpanH() * Box.getMinBox().getW();
        h = box.getMinSpanV() * Box.getMinBox().getH();
    }

    public String toString() {
        update();
        return "VB" + id + "T" + box.getType() + "P(" + x + "," + y +")D(" + w + "," + h + ")";
    }

    public void male( Graphics g, ArrayList<Box> boxes, ArrayList<VisualBox> visualBoxes, int facets)
    {
        update();
        int nr = 0;
        int myIdx = 0;
        for(VisualBox v : visualBoxes) {
            if(v.getBox().getType() == box.getType()) {
                nr++;
                if(v.getBox() == box) {
                    myIdx = visualBoxes.indexOf(v);
                }
            }
        }
        float hue = (float)box.getType() / (float)boxes.size();
        float saturation = (float)myIdx / (float)nr;
        float luminance = 0.9f;
        Color color = Color.getHSBColor(hue, saturation, luminance);
        g.setColor(Color.black);
        g.drawRect((int) x, (int) y, (int) w, (int) h);
        g.fillRect((int) x, (int) y, (int) w, (int) h);
        g.setColor(color);
        g.fillRect((int) x+3, (int) y+3, (int) w-6, (int) h-6);

        g.setColor(Color.black);
        if(isFacet) {
            g.drawString(box.toString(), (int)(x + w/2 - 40), (int)(y + h/2));
            g.drawString("I'm a facet!", (int)(x+w/2 - 30), (int)(y+h/2+12));
        } else {
            g.drawString("merge me", (int)(x + w/2 - 40), (int)(y + h/2));
        }
    }

    @Override
    public int compareTo(VisualBox visualBox) {
        return this.box.compareTo(visualBox.getBox());
    }
}
