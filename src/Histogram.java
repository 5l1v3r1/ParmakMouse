
import java.awt.Color;
import java.awt.image.RGBImageFilter;

class Histogram extends RGBImageFilter {

    int[] histogramX;
    int[] histogramY;
    int white = Color.WHITE.getRGB();

    public Histogram(int[] ax, int[] ay) {
        for (int i = 0; i < ax.length; i++) {
            ax[i] = 0;
        }
        for (int i = 0; i < ay.length; i++) {
            ay[i] = 0;
        }
        histogramX = ax;
        histogramY = ay;
        canFilterIndexColorModel = true;
    }

    // This method is called for every pixel in the image
    public int filterRGB(int x, int y, int rgb) {
        if (x == -1 || y == -1) {
            return rgb;
            // The pixel value is from the image's color table rather than the image itself
        }
//        System.out.println("x:" + x);
//        System.out.println("y:" + y);
        if (rgb == Color.WHITE.getRGB()) {
            histogramX[x]++;
            histogramY[y]++;
            return Color.RED.getRGB();
        }

        return rgb;
    }
}
