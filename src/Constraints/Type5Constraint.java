package Constraints;

import miniProject.Example;
import miniProject.Model;

/**
 * this Constraint draw a square that its left up corner start at (start.first,start.second) and its
 * down right corner is (end.first,end.second) and calculating if the avg of
 * the pixles whitin the square is >= value
 */
public class Type5Constraint extends Constraint {
    private int x1;
    private int y1;
    private int x2;
    private int y2;
    private int value;

    public Type5Constraint(int x1, int y1, int x2, int y2, int value) {
        this.x1 = x1;
        this.x2 = x2;
        this.y1 = y1;
        this.y2 = y2;
        this.value = value;
    }


    public String toString() {
        return "_5:" + this.x1 + "\t" + this.y1 + "\t" + this.x2 + "\t" + this.y2 + "\t>\t" + this.value;
    }

    @Override
    public boolean checkConstraint(Example example) {
            double avg = 0;
            int num = 0;
            int[][] pic = example.getImage();
            int startX = x1;
            int startY = y1;
            int endX = x2;
            int endY = y2;
            for (int i = startX; i <= endX; i++) {
                for (int j = startY; j >= endY; j--) {
                    num++;
                    avg += pic[i][j];
                }
            }
            return (avg >= value);
    }
}
