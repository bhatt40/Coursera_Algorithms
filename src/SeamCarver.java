import java.awt.Color;

/**
 * Created by rishee on 11/15/14.
 */
public class SeamCarver {

    private byte[][][] pictureArray;
    private double[][] energyArray;

    private int width, height;
    private int arrayWidth, arrayHeight;
    private int N;

    // create a seam carver object based on the given picture
    public SeamCarver(Picture picture) {

        this.N = Math.max(picture.width(), picture.height());

        pictureArray = new byte[N][N][3];
        Color pixelColor;

        this.width = picture.width();
        this.height = picture.height();
        this.arrayWidth = this.width;
        this.arrayHeight = this.height;

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                pixelColor = picture.get(i, j);
                pictureArray[j][i][0] = (byte) pixelColor.getRed();
                pictureArray[j][i][1] = (byte) pixelColor.getGreen();
                pictureArray[j][i][2] = (byte) pixelColor.getBlue();
            }
        }

        energyArray = new double[N][N];

        for (int i = 0; i < this.width; i++) {
            for (int j = 0; j < this.height; j++) {
                energyArray[j][i] = energy(i, j);
            }
        }
    }

    // current picture
    public Picture picture() {

        Picture picture = new Picture(this.width(), this.height());
        Color pixelColor;

        for (int i = 0; i < picture.width(); i++) {
            for (int j = 0; j < picture.height(); j++) {
                pixelColor = new Color(pictureArray[j][i][0] & 0xFF, pictureArray[j][i][1] & 0xFF, pictureArray[j][i][2] & 0xFF);
                picture.set(i, j, pixelColor);
            }
        }

        return picture;
    }

    // width of current picture
    public int width() {

        return this.width;
    }

    // height of current picture
    public int height() {

        return this.height;
    }

    // energy of pixel at column x and row y
    public double energy(int x, int y) {

        double xEnergySquared, yEnergySquared;

        if (x > 0 & x < this.arrayWidth - 1) {
            xEnergySquared = this.getEnergySquared(pictureArray[y][x - 1], pictureArray[y][x + 1]);
        } else if (x == 0 || x == this.arrayWidth - 1) {
            return 255 * 255 + 255 * 255 + 255 * 255;
        } else {
            throw new IndexOutOfBoundsException();
        }

        if (y > 0 & y < this.arrayHeight - 1) {
            yEnergySquared = this.getEnergySquared(pictureArray[y - 1][x], pictureArray[y + 1][x]);
        } else if (y == 0 || y == this.arrayHeight - 1) {
            return 255 * 255 + 255 * 255 + 255 * 255;
        } else {
            throw new IndexOutOfBoundsException();
        }

        return (double) (xEnergySquared + yEnergySquared);
    }

    // sequence of indices for horizontal seam
    public int[] findHorizontalSeam() {

        this.transpose();

        int[] seam = this.findSeam();

        this.transpose();

        return seam;
    }

    // sequence of indices for vertical seam
    public int[] findVerticalSeam() {

        return this.findSeam();
    }

    // remove horizontal seam from current picture
    public void removeHorizontalSeam(int[] seam) {

        if (seam.length != this.width)
            throw new RuntimeException();

        this.transpose();

        this.removeSeam(seam);

        this.transpose();

        this.height--;
        this.arrayHeight--;
    }

    // remove vertical seam from current picture
    public void removeVerticalSeam(int[] seam) {

        if (seam.length != this.height)
            throw new RuntimeException();

        this.removeSeam(seam);

        this.width--;
        this.arrayWidth--;
    }

    private int getEnergySquared(byte[] a, byte[] b) {

        int rDelta = Math.abs((a[0] & 0xFF) - (b[0] & 0xFF));
        int gDelta = Math.abs((a[1] & 0xFF) - (b[1] & 0xFF));
        int bDelta = Math.abs((a[2] & 0xFF) - (b[2] & 0xFF));

        return rDelta * rDelta + gDelta * gDelta + bDelta * bDelta;
    }

    private void transpose() {

        for (int i = 0; i < N; i++) {
            for (int j = i + 1; j < N; j++) {
                byte[] tempBytes = pictureArray[i][j];
                pictureArray[i][j] = pictureArray[j][i];
                pictureArray[j][i] = tempBytes;

                double tempDouble = energyArray[i][j];
                energyArray[i][j] = energyArray[j][i];
                energyArray[j][i] = tempDouble;
            }
        }

        int temp = this.arrayHeight;
        this.arrayHeight = this.arrayWidth;
        this.arrayWidth = temp;

    }

    private int[] findSeam() {

        double[][] shortestDistances = new double[arrayHeight][arrayWidth];
        int[][] shortestPaths = new int[arrayHeight][arrayWidth];

        // Traverse from top to bottom of image
        for (int j = 0; j < arrayHeight; j++) {
            // Traverse from left to right of row
            for (int i = 0; i < arrayWidth; i++) {
                // Special case for first row
                if (j == 0) {
                    shortestDistances[j][i] = energy(i, j);
                } else {
                    // Update shortest path to this point
                    double shortestDistance = Double.MAX_VALUE;
                    int shortestDistanceIndex = -1;

                    for (int a = i-1; a <= i+1; a++) {
                        if (a >= 0 & a < arrayWidth) {
                            if (shortestDistances[j-1][a] < shortestDistance) {
                                shortestDistance = shortestDistances[j-1][a];
                                shortestDistanceIndex = a;
                            }
                        }
                    }

                    shortestDistances[j][i] = shortestDistances[j - 1][shortestDistanceIndex] + energy(i, j);
                    shortestPaths[j][i] = shortestDistanceIndex;
                }
            }
        }

        int[] seam = new int[this.arrayHeight];
        int counter = this.arrayHeight - 1;

        double shortestDistance = Double.MAX_VALUE;
        int nextIndex = -1;

        for (int i = 0; i < arrayWidth; i++) {
            if (shortestDistances[arrayHeight - 1][i] < shortestDistance) {
                shortestDistance = shortestDistances[arrayHeight - 1][i];
                nextIndex = i;
            }
        }

        do {
            seam[counter] = nextIndex;
            nextIndex = shortestPaths[counter][nextIndex];
            counter--;
        } while (counter >= 0);

        return seam;
    }

    private void removeSeam(int[] seam) {
        for (int j = 0; j < this.arrayHeight; j++) {

            // Remove seam element
            System.arraycopy(pictureArray[j], seam[j] + 1, pictureArray[j], seam[j], arrayWidth - seam[j] - 1);
            System.arraycopy(energyArray[j], seam[j] + 1, energyArray[j], seam[j], arrayWidth - seam[j] - 1);
        }

        // Update energy values around seam
        for (int j = 0; j < this.arrayHeight; j++) {
            for (int i = seam[j] - 1; i <= seam[j]; i++) {
                if (i >= 0)
                    energyArray[j][i] = energy(i, j);
            }
        }
    }
}


