/* *****************************************************************************
 *  Name: Koh Jun Jie
 *  Date: 23th September 2021
 *  Description: Implementation of the SeamCarver API (92/100)
 **************************************************************************** */

import edu.princeton.cs.algs4.Picture;

public class SeamCarver {

    private int[][] pictureRGB;             // 2D Array to store RGB values of picture
    private double[][] matrix;              // 2D Array to store energy values
    private double[][] distTo;              // 2D Array to store distances
    private Coord[][] edgeTo;               // 2D Array to store coordinates of the edges to
    private int height;                     // height of the picture
    private int width;                      // width of the picture

    /**
     * Private class to represent values of a pixel coordinate
     */
    private class Coord {
        private int x;
        private int y;

        Coord(int y, int x) {
            this.x = x;
            this.y = y;
        }

        public int getX() {
            return x;
        }

        public int getY() {
            return y;
        }

        public String toString() {
            return "Coord{" +
                    "y=" + y +
                    ", x=" + x +
                    '}';
        }
    }

    /**
     * Public constructor to create a seam carver object based on the given picture
     *
     * @param picture the picture to be used
     */
    public SeamCarver(Picture picture) {
        if (picture == null) {
            throw new IllegalArgumentException("Null arguement detected!");
        }

        // Initialize instance variables
        this.height = picture.height();
        this.width = picture.width();
        this.matrix = new double[height][width];
        this.pictureRGB = new int[height][width];

        // 2D Array creation for RGB values
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pictureRGB[row][col] = picture.getRGB(col, row);
            }
        }

        // 2D Array creation for energy values
        for (int row = 0; row < height(); row++) {
            for (int col = 0; col < width(); col++) {
                matrix[row][col] = energy(col, row);
            }
        }
    }

    /**
     * Private method to transpose the picture
     */
    private void transpose() {
        int temp = width;
        width = height;
        height = temp;
        double[][] transposedMatrix = new double[height][width];
        int[][] transposedRGB = new int[height][width];

        // Transposing of the pixels
        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                transposedMatrix[row][col] = matrix[col][row];
                transposedRGB[row][col] = pictureRGB[col][row];
            }
        }
        matrix = transposedMatrix;
        pictureRGB = transposedRGB;
    }

    /**
     * Method to return the current picture
     *
     * @return the current picture
     */
    public Picture picture() {
        Picture pic = new Picture(width,
                                  height);                   // Creation of a blank Picture canvas

        for (int row = 0; row < height; row++) {
            for (int col = 0; col < width; col++) {
                pic.setRGB(col, row,
                           pictureRGB[row][col]);         // Populating the canvas with each RGB pixel values
            }
        }

        return pic;
    }

    /**
     * Method to return the width of the current picture
     *
     * @return the width of the current picture
     */
    public int width() {
        return width;
    }

    /**
     * Method to return the height of the current picture
     *
     * @return the height of the current picture
     */
    public int height() {
        return height;
    }

    /**
     * Method to compute the energy of a pixel
     *
     * @param x the x coordinate of the pixel
     * @param y the y coordinate of the pixel
     * @return the energy of the pixel, return 1000 if the pixel is a border pixel
     */
    public double energy(int x, int y) {
        if (x < 0 || x > width - 1) {
            throw new IllegalArgumentException("x outside prescribed range!");
        }
        if (y < 0 || y > height - 1) {
            throw new IllegalArgumentException("y outside prescribed range!");
        }

        if (x == 0 || y == 0 || y == height() - 1 || x == width() - 1) {
            return 1000.0;
        }

        return Math.sqrt(xGradient(x, y) + yGradient(x, y));
    }

    /**
     * Private method to calculate the square of the x-gradient
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the x-gradient
     */
    private double xGradient(int x, int y) {
        int leftPixelRGB = pictureRGB[y][x - 1];
        int rightPixelRGB = pictureRGB[y][x + 1];

        int leftPixelR = (leftPixelRGB >> 16) & 0xFF;
        int leftPixelG = (leftPixelRGB >> 8) & 0xFF;
        int leftPixelB = (leftPixelRGB) & 0xFF;

        int rightPixelR = (rightPixelRGB >> 16) & 0xFF;
        int rightPixelG = (rightPixelRGB >> 8) & 0xFF;
        int rightPixelB = (rightPixelRGB) & 0xFF;

        return Math.pow(rightPixelR - leftPixelR, 2) + Math.pow(rightPixelG - leftPixelG, 2) + Math
                .pow(rightPixelB - leftPixelB, 2);
    }

    /**
     * Private method to calculate the square of the y-gradient
     *
     * @param x the x coordinate
     * @param y the y coordinate
     * @return the y-gradient
     */
    private double yGradient(int x, int y) {
        int topPixelRGB = pictureRGB[y - 1][x];
        int bottomPixelRGB = pictureRGB[y + 1][x];
        int topPixelR = (topPixelRGB >> 16) & 0xFF;
        int topPixelG = (topPixelRGB >> 8) & 0xFF;
        int topPixelB = (topPixelRGB) & 0xFF;
        int bottomPixelR = (bottomPixelRGB >> 16) & 0xFF;
        int bottomPixelG = (bottomPixelRGB >> 8) & 0xFF;
        int bottomPixelB = (bottomPixelRGB) & 0xFF;

        return Math.pow(bottomPixelR - topPixelR, 2) + Math.pow(bottomPixelG - topPixelG, 2) + Math
                .pow(bottomPixelB - topPixelB, 2);
    }

    /**
     * Private method to initialize the distance 2D array
     */
    private void initialization() {
        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (i == 0) {
                    distTo[i][j] = 1000.00;
                }
                else {
                    distTo[i][j] = Double.POSITIVE_INFINITY;
                }
            }
        }
    }

    /**
     * Compute the optimal horizontal seam of the picture
     *
     * @return int[] array containing values of the horizontal seam
     */
    public int[] findHorizontalSeam() {
        transpose();
        int[] result = findSeam();
        transpose();
        return result;
    }

    /**
     * Compute the optimal vertical seam of the picture
     *
     * @return int[] array containing values of the vertical seam
     */
    public int[] findVerticalSeam() {
        return findSeam();
    }

    /**
     * Private method to find seams
     *
     * @return int[] array containing values of the seam
     */
    private int[] findSeam() {
        distTo = new double[height][width];
        edgeTo = new Coord[height][width];

        initialization();

        for (int i = 0; i < height; i++) {
            for (int j = 0; j < width; j++) {
                if (testPosition(i, j).equals("LAST_ROW")) {
                }
                else if (width == 1) {
                    relax(i, j, i + 1, j);
                }
                else if (testPosition(i, j).equals("LAST_COL")) {
                    relax(i, j, i + 1, j);
                    relax(i, j, i + 1, j - 1);
                }
                else if (testPosition(i, j).equals("FIRST_COL")) {
                    relax(i, j, i, j + 1);
                    relax(i, j, i + 1, j + 1);
                }
                else {
                    relax(i, j, i + 1, j - 1);
                    relax(i, j, i + 1, j);
                    relax(i, j, i + 1, j + 1);
                }
            }
        }

        /* From the last row of distances, get the index with the smallest distance */
        double min = distTo[height - 1][0];
        int minX = 0;
        for (int k = 1; k < width; k++) {
            if (distTo[height - 1][k] < min) {
                min = distTo[height - 1][k];
                minX = k;
            }
        }

        /* Get the edges by back tracing the path */
        int[] seam = new int[height];
        seam[height - 1] = minX;
        int j = height - 1;
        int i = minX;
        int k = height - 2;
        while (edgeTo[j][i] != null) {
            seam[k] = edgeTo[j][i].getX();
            k--;
            Coord temp = edgeTo[j][i];
            j = temp.getY();
            i = temp.getX();
        }
        return seam;
    }

    /**
     * Private method to relax edges
     *
     * @param y current y coordinate
     * @param x current x coordinate
     * @param u next  y coordinate
     * @param w next x coordinate
     */
    private void relax(int y, int x, int u, int w) {
        if (distTo[u][w] > matrix[u][w] + distTo[y][x]) {
            distTo[u][w] = matrix[u][w] + distTo[y][x];
            edgeTo[u][w] = new Coord(y, x);
        }

    }

    /**
     * Public method to remove the horizontal seam
     *
     * @param seam to be removed
     */
    public void removeHorizontalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Null arguement detected!");
        }

        if (seam.length != width) {
            throw new IllegalArgumentException("Array wrong length!");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > height - 1) {
                throw new IllegalArgumentException("Entry Outside prescribed range!");
            }
            if (i != seam.length - 1) {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                    throw new IllegalArgumentException(
                            "Two adjacent entries differ by more than 1!");
                }
            }
        }

        if (height <= 1) {
            throw new IllegalArgumentException("Height of picture less than or equal to 1!");
        }

        transpose();
        removeVerticalSeam(seam);
        transpose();
    }

    /**
     * Public method to remove the vertical seam
     *
     * @param seam to be removed
     */
    public void removeVerticalSeam(int[] seam) {
        if (seam == null) {
            throw new IllegalArgumentException("Null arguement detected!");
        }

        if (seam.length != height) {
            throw new IllegalArgumentException("Array wrong length!");
        }

        for (int i = 0; i < seam.length; i++) {
            if (seam[i] < 0 || seam[i] > width - 1) {
                throw new IllegalArgumentException("Entry Outside prescribed range!");
            }
            if (i != seam.length - 1) {
                if (Math.abs(seam[i + 1] - seam[i]) > 1) {
                    throw new IllegalArgumentException(
                            "Two adjacent entries differ by more than 1!");
                }
            }
        }

        if (width <= 1) {
            throw new IllegalArgumentException("Width of picture less than or equal to 1!");
        }

        /* Shifting of RGB and energy array values */
        for (int i = 0; i < seam.length; i++) {
            if (seam[i] == width - 1) {
                pictureRGB[i][width - 1] = 0;
                matrix[i][width - 1] = 0.0;
            }
            else {
                System.arraycopy(pictureRGB[i], seam[i] + 1, pictureRGB[i], seam[i],
                                 width - 1 - seam[i]);
                pictureRGB[i][width - 1] = 0;
                System.arraycopy(matrix[i], seam[i] + 1, matrix[i], seam[i],
                                 width - 1 - seam[i]);
                matrix[i][width - 1] = 0;
            }
        }
        width--;

        /* Recalculation of energy values */
        for (int i = 0; i < seam.length; i++) {
            if (i == 0 || i == seam.length - 1) {
                continue;
            }
            for (int j = 0; j < width; j++) {
                matrix[i][j] = energy(j, i);
            }
        }
    }

    /**
     * Private method to test if a pixel belongs to any special region
     *
     * @param y coordinate
     * @param x coordinate
     * @return String value which states if the pixel belongs to any special region
     */
    private String testPosition(int y, int x) {
        if (y == height - 1) {
            return "LAST_ROW";
        }
        else if (x == width - 1) {
            return "LAST_COL";
        }
        else if (x == 0) {
            return "FIRST_COL";
        }
        else {
            return "NORM";
        }
    }

    public static void main(String[] args) {
        Picture picture = new Picture(args[0]);

        SeamCarver sc = new SeamCarver(picture);
    }
}
