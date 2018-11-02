
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Izleme3 implements Runnable {

    Kamera k;
    Graphics g;
    Photographer p = new Photographer();
    int x, y;
    int top, bottom, left, right;
    int wWidth = 50;

    public Izleme3(Kamera k) {
        this.k = k;
        g = k.bufferGraphics;
        x = k.getWidth() / 2;
        y = k.getHeight() / 2;

    }

    public void run() {

        BufferedImage bim, cbim, lbim = null, rbim = null;

        g.drawString("Kamera hazırlanıyor...", k.getWidth() / 2 - 50, k.getWidth() / 2 - 50);
        p.basla();
        g.setColor(Color.black);
        g.fillRect(0, 0, k.getWidth(), k.getHeight());
        g.setColor(Color.white);
        g.drawString("Kamera hazır!", k.getWidth() / 2 - 40, k.getWidth() / 2 - 50);

        while (true) {
            try {
                bim = p.takePhoto();
                k.bufferGraphics.drawImage(bim, 0, 0, null);
                izle();
                cbim = bim.getSubimage(x - 25, y - 25, 50, 50);

                if (x > 75) {
                    lbim = bim.getSubimage(x - 25 - 50, y - 25, 50, 50);

                    k.getGraphics().drawRect(x - 25 - 50, y - 25, 50, 50);
                }
                if (x < k.getWidth() - 25) {
                    rbim = bim.getSubimage(x - 25 + 50, y - 25, 50, 50);
                    k.getGraphics().drawRect(x - 25 + 50, y - 25, 50, 50);
                }
//                if (parmakKontrol(bim)) {
                //altBul(bim);
                if (ucunuBul(cbim) && solunuBul(cbim) && saginiBul(cbim)) {

//                    if (lbim != null && solunuBul(lbim)) {
//                        //System.out.println("LBİM");
//                        x -= left;
//                    }
//
//                    if (rbim != null && saginiBul(rbim)) {
//                        System.out.println("RBİM");
//                        x += left;
//                    }

//                    if (top < 10 && y > 5) {
//                        y -= 10;
//                    }
                    // System.out.println(left);
                    if (left < 10 && x > 5) {
                        x -= 10;
                    }

                    if (right > 20 && x < k.getWidth()) {
                        x += 10;
                    }



                    g.drawLine(x - 25, y - 25 + top, x + 25, y - 25 + top);

                    g.drawLine(x - 25 + left, y - 25, x - 25 + left, y + 25);

                    g.drawLine(x + right, y - 25, x + right, y + 25);

                    k.repaint();
                }
                /* if (ucunuBul(cbim) && solunuBul(cbim) && saginiBul(cbim)) {

                if (top < 10 && y > 5) {
                y -= 10;
                }
                if (left < 10 && x > 5) {
                x -= 10;
                }
                if (right > 20 && x < k.getWidth()) {
                x += 10;
                }
                g.drawLine(x - 25, y - 25 + top, x + 25, y - 25 + top);

                g.drawLine(x - 25 + left, y - 25, x - 25 + left, y + 25);

                g.drawLine(x + right, y - 25, x + right, y + 25);

                k.repaint();
                }*/
//                }
                //Thread.sleep(100);
            } catch (Exception ex) {
                System.out.println("hata" + ex.getMessage());
            }
        }
    }

    public void izle() {
        g.setColor(Color.red);
        g.drawRect(x - 25, y - 25, 50, 50);

        g.drawLine(x - 25, y + 25 + bottom, x + 25, y + 25 + bottom);

    }

    public boolean ucunuBul(BufferedImage bim) {
        //bim = bim.getSubimage(x - 25, y - 25, 50, 50);
        int temp1 = bim.getRGB(20, 25), temp2 = bim.getRGB(15, 25), temp3 = bim.getRGB(30, 25);
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x - 5, y, x + 5, y);
        for (int i = 25; i > 0; i -= 2) {
            rgb1 = bim.getRGB(20, i);
            rgb2 = bim.getRGB(25, i);
            rgb3 = bim.getRGB(30, i);
            // System.out.println("rgb:" + rgb + " temp:" + temp + "fark:" + (rgb - temp));
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 2000000) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 2000000)) {
                //            if (rgb < -9000000 || rgb > -3000000) { // Kenar siyah veya beyaz ise kenar bulundu.
                //if (rgb < -10000000 || rgb > -3000000) { // Kenar siyah veya beyaz ise kenar bulundu.
                top = i;
                //System.out.println("Top " + top);
                return true;
            }
        }
        return false;
    }

    public boolean altBul(BufferedImage bim) {

        //bim = bim.getSubimage(x - 25, y - 25, 50, 50);
        int temp1 = bim.getRGB(20, 50), temp2 = bim.getRGB(15, 50), temp3 = bim.getRGB(30, 50);
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x - 5, y, x + 5, y);
        for (int i = 25; i > 0; i -= 2) {
            rgb1 = bim.getRGB(20, 25 + i);
            rgb2 = bim.getRGB(25, 25 + i);
            rgb3 = bim.getRGB(30, 25 + i);
            // System.out.println("rgb:" + rgb + " temp:" + temp + "fark:" + (rgb - temp));
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 2000000) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 2000000)) {
                //            if (rgb < -9000000 || rgb > -3000000) { // Kenar siyah veya beyaz ise kenar bulundu.
                //if (rgb < -10000000 || rgb > -3000000) { // Kenar siyah veya beyaz ise kenar bulundu.
                bottom = i;
                //System.out.println("Alt " + bottom);
                return true;
            }
        }
        return false;
    }

    public boolean solunuBul(BufferedImage bim) {
        //bim = bim.getSubimage(x - 25, y - 25, 50, 50);
        int temp1 = bim.getRGB(25, 20), temp2 = bim.getRGB(25, 25), temp3 = bim.getRGB(25, 30);
        int rgb1, rgb2, rgb3;
//        int temp = bim.getRGB(25, 25), rgb;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = 25; i > 0; i -= 2) {

            rgb1 = bim.getRGB(i, 20);
            rgb2 = bim.getRGB(i, 25);
            rgb3 = bim.getRGB(i, 30);
//            rgb = bim.getRGB(i, 25);
            //System.out.println("rgb:" + rgb);
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 2000000) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 2000000)) {

//            if (Math.abs(temp - rgb) > 9000000 || Math.abs(temp - rgb) > 2000000) {
//            if (rgb < -9000000 || rgb > -3000000) { // Kenar siyah veya beyaz ise kenar bulundu.
                left = i;
                //System.out.println("Left:" + left);
                return true;
            }
        }
        return false;
    }

    public boolean saginiBul(BufferedImage bim) {
        int temp1 = bim.getRGB(25, 20), temp2 = bim.getRGB(25, 25), temp3 = bim.getRGB(25, 30);
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = 0; i < 25; i += 2) {
            rgb1 = bim.getRGB(25 + i, 20);
            rgb2 = bim.getRGB(25 + i, 25);
            rgb3 = bim.getRGB(25 + i, 30);
            //System.out.println("rgb:" + rgb);
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 2000000) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 2000000)) { // Kenar siyah veya beyaz ise kenar bulundu.
                right = i;
                //System.out.println("Right:" + right);
                return true;
            }
        }
        return false;
    }

    public boolean parmakKontrol(BufferedImage bim) {

        int temp = bim.getRGB(25, 25), rgb;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = 0; i < 25; i += 2) {
            rgb = bim.getRGB(25, 25 + i);
//            k.bufferGraphics.drawLine(x + 25, y + i, x + 25, y + 25 + i);
            System.out.println("parmak:" + Math.abs(temp - rgb));
            if (Math.abs(temp - rgb) > 300000) { // Kenar siyah veya beyaz ise kenar bulundu.
                //right = i;
                return false;
            }
        }
        return true;
    }

    public static void main(String[] args) {
        new Arayuz().setVisible(true);
    }
}
