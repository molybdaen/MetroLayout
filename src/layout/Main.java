package layout;

import javax.swing.*;
import java.awt.event.HierarchyBoundsListener;
import java.awt.event.HierarchyEvent;

public class Main {

    public static void main(String[] args) {

        JFrame f = new JFrame( "MetroLayout" );
        f.setDefaultCloseOperation( JFrame.EXIT_ON_CLOSE );
        f.setSize(990, 680);
        f.setVisible(true);
        final BoxLayout bl = new BoxLayout(10, 990, 680, 110, 55);
        f.add(bl);
        final int vertInset = f.getInsets().top + f.getInsets().bottom;
        final int horiInset = f.getInsets().left + f.getInsets().right;
        f.getContentPane().addHierarchyBoundsListener(new HierarchyBoundsListener() {

            @Override
            public void ancestorMoved(HierarchyEvent e) {
                System.out.println(e);
            }

            @Override
            public void ancestorResized(HierarchyEvent e) {
                bl.refreshLayout(e.getChanged().getSize().getWidth()-horiInset, e.getChanged().getSize().getHeight() - vertInset);
            }
        });
        for(Box b : bl.getBoxes()) {
            System.out.println(b);
        }
        for(VisualBox vb : bl.getVisualBoxes()) {
            System.out.println(vb);
        }

    }
}
