
import java.awt.Color;
import java.awt.Point;
import java.awt.image.RGBImageFilter;
import java.util.ArrayList;

class SkinFilter extends RGBImageFilter {

    ArrayList<Point> pixelList = new ArrayList<Point>();
    int div = 10;
    int[] arr = {900, 0, 900, 0};
//    public int left = 900, right = 0, top = 900, down = 0;

    public SkinFilter() {
        canFilterIndexColorModel = true;
    }

    public ArrayList<Point> getPixelList() {
        return pixelList;
    }

    public void clearList() {
        pixelList.clear();
        resetArea();
    }

    // This method is called for every pixel in the image
    public int filterRGB(int x, int y, int rgb) {
        if (x == -1) {
            // The pixel value is from the image's color table rather than the image itself
        }

        Color c = new Color(rgb);
        Color s = new Color(20, 20, 20);

        if (c.getRed() > s.getRed() && c.getGreen() > s.getGreen() && c.getBlue() > s.getBlue()) {
            if (x < arr[0]) {
                arr[0] = x;
            } else if (x > arr[1]) {
                arr[1] = x;
            }
            if (y < arr[2]) {
                arr[2] = y;
            } else if (y > arr[3]) {
                arr[3] = y;
            }

            Point p = new Point((x / div), (y / div));
            if (!pixelList.contains(p)) {
                pixelList.add(p);
            }
            return Color.WHITE.getRGB();
        }


        return Color.BLACK.getRGB();
    }

    public int getDiv() {
        return div;
    }

    public void setDiv(int div) {
        this.div = div;
    }

    public void resetArea() {
        int[] arr = {900, 0, 900, 0};
        this.arr = arr;
    }
}
