package layout;

public class Box implements Comparable<Box> {

    private static int nextId = 1;

    private int id = 0;
    private int type = 0;
    private double w = 40.0;
    private double h = 20.0;
    private int minSpanH = 1;
    private int minSpanV = 1;

    private static Box minBox;

    protected Box() {

    }

    protected Box(int type, double width, double height) {
        this.id = nextId++;
        this.type = type;
        this.w = width;
        this.h = height;
        this.minSpanH = getComprisedMinorsHorizontal(1);
        this.minSpanV = getComprisedMinorsVertical(1);
    }

    public static Box getMinInstance(double minW, double minH) {
        minBox = new Box(1, minW, minH);
        return minBox;
    }

    public Box getNextBigger() {
        if(type % 2 == 0) {
            return new Box(type + 1, w * 2, h);
        } else {
            return new Box(type + 1, w, h * 2);
        }
    }

    public Box getNextSmaller() {
        if(type > 1) {
            if(type % 2 == 0) {
                return new Box(type - 1, w, h / 2);
            } else {
                return new Box(type - 1, w / 2, h);
            }
        } else {
            return new Box(type, w, h);
        }
    }

    public Box[][] split() {
        int nrMinorsH = getComprisedMinorsHorizontal(type-1);
        int nrMinorsV = getComprisedMinorsVertical(type-1);
        Box[][] boxFragments = new Box[nrMinorsV][nrMinorsH];
        for(int r = 0; r < nrMinorsV; r++) {
            for(int c = 0; c < nrMinorsH; c++) {
                boxFragments[r][c] = this.getNextSmaller();
            }
        }
        return boxFragments;
    }

    public int getComprisedMinorsHorizontal(int type) {
        if(this.type == 1) {
            return 1;
        }
        if(type <= this.type) {
            int fold = this.type - type;
            if(this.type % 2 == 0) {
                return (int)Math.pow(2, (fold + 0) / 2);
            } else {
                return (int)Math.pow(2, (fold + 1) / 2);
            }
        } else {
            return -1;
        }
    }

    public int getComprisedMinorsVertical(int type) {
        if(this.type == 1) {
            return 1;
        }
        if(type <= this.type) {
            int fold = this.type - type;
            if(this.type % 2 == 0) {
                return (int)Math.pow(2, (fold + 1) / 2);
            } else {
                return (int)Math.pow(2, (fold + 0) / 2);
            }
        } else {
            return -1;
        }
    }

    public Box getCopy() {
        return new Box(type, w, h);
    }

    public double getW() {
        return w;
    }

    public void updateDim(double w, double h) {
        this.w = w;
        this.h = h;
    }

    public double getH() {
        return h;
    }

    public int getMinSpanH() {
        return minSpanH;
    }

    public int getMinSpanV() {
        return minSpanV;
    }

    public int getType() {
        return type;
    }

    public int getId() {
        return id;
    }

    public static Box getMinBox() {
        return minBox;
    }

    public String toString() {
        return "B" + type + " ( " + (int)w + ", " + (int)h + " )";
    }

    @Override
    public int compareTo(Box box) {
        if(this.type > box.getType()) {
            return -1;
        } else {
            if(this.type == box.getType()) {
                return 0;
            } else {
                return 1;
            }
        }
    }
}
