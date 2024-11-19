package seamfinding;

import graphs.Edge;
import seamfinding.energy.EnergyFunction;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Dynamic programming implementation of the {@link SeamFinder} interface.
 *
 * @see SeamFinder
 */
public class DynamicProgrammingSeamFinder implements SeamFinder {

    private double[][] table;
    @Override
    public List<Integer> findHorizontal(Picture picture, EnergyFunction f) {
        table = new double[picture.width()][picture.height()];
        createTable(picture, f);

        List<Integer> path = new ArrayList<>(picture.width());
        // get lowest rightmost value
        double lowest = Double.POSITIVE_INFINITY;
        int start = -1;
        for (int i = 0; i < picture.height(); i++) {
            if (lowest > table[picture.width() - 1][i]) {
                lowest = table[picture.width() - 1][i];
                start = i;
            }
        }

        //System.out.println(start);
        //add starting point
        path.add(start);

        // Iterate through graph
        for (int x = picture.width() - 2; x >= 0; x--) {

            //left-up
            double up = Double.POSITIVE_INFINITY;
            double down = Double.POSITIVE_INFINITY;
            if(!(start == 0)) {
                up = table[x][start - 1];
            }
            if(!(start == picture.height() - 1)) {
                down = table[x][start + 1];
            }

            //left middle
            double mid = table[x][start];
            //left-down



            if (up <= mid && up <= down) {
                path.add(start - 1);
                start -= 1;
            }
            else if (mid <= up && mid <= down) {
                path.add(start);
            }
            else  {
                path.add(start + 1);
                start += 1;
            }
        }

        //reverse path
        Collections.reverse(path);

        //System.out.println("Width " + picture.width() + " ||| Seam Width: " + path.size());

        return path;
    }

    private void createTable(Picture picture, EnergyFunction f) {
        //create leftmost column
        for (int y = 0; y < picture.height(); y++) {
            table[0][y] = f.apply(picture, 0, y);
        }

        //create rest of table
        for (int x = 1; x < picture.width(); x++) {
            for (int y = 0; y < picture.height(); y++) {
                //System.out.println(x + " " + y);
                //left-up
                double up = Double.POSITIVE_INFINITY;
                if(!(y == 0)) {
                    up = table[x - 1][y - 1];
                }
                //left middle
                double mid = table[x - 1][y];
                //left-down

                double down = Double.POSITIVE_INFINITY;
                if(!(y == picture.height() - 1)) {
                    down = table[x - 1][y + 1];
                }


                double lowest = Math.min(Math.min(up,mid), down);

                //fill space
                table[x][y] = f.apply(picture, x, y) + lowest;
            }
        }
        //System.out.println(table);
    }

    public double[][] getTable() {
        return table;
    }
}
