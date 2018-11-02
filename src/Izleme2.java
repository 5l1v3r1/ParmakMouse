
import java.awt.Color;
import java.awt.Graphics;
import java.awt.image.BufferedImage;

public class Izleme2 implements Runnable {

    Kamera k;
    Graphics g;
    Photographer p = new Photographer();
    int x, y;
    int top, bottom, left, right;
    int wWidth = 50;

    public Izleme2(Kamera k) {
        this.k = k;
        g = k.bufferGraphics;
        x = k.getWidth() / 2;
        y = k.getHeight() / 2;

    }

    public void run() {

        BufferedImage bim, cbim, lbim = null, rbim = null;

        g.drawString("Kamera hazırlanıyor...", k.getWidth() / 2 - wWidth, k.getWidth() / 2 - wWidth);
        p.basla();
        g.setColor(Color.black);
        g.fillRect(0, 0, k.getWidth(), k.getHeight());
        g.setColor(Color.white);
        g.drawString("Kamera hazır!", k.getWidth() / 2 - 40, k.getWidth() / 2 - wWidth);

        while (true) {
            try {
                bim = p.takePhoto();
                k.bufferGraphics.drawImage(bim, 0, 0, null);
                izle();
                cbim = bim.getSubimage(x - (wWidth / 2), y - (wWidth / 2), wWidth, wWidth);

                if (x > 75) {
                    lbim = bim.getSubimage(x - (wWidth / 2) - wWidth, y - (wWidth / 2), wWidth, wWidth);

                    k.getGraphics().drawRect(x - (wWidth / 2) - wWidth, y - (wWidth / 2), wWidth, wWidth);
                }
                if (x < k.getWidth() - (wWidth / 2)) {
                    rbim = bim.getSubimage(x - (wWidth / 2) + wWidth, y - (wWidth / 2), wWidth, wWidth);
                    k.getGraphics().drawRect(x - (wWidth / 2) + wWidth, y - (wWidth / 2), wWidth, wWidth);
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

                    if (right > 15 && x < k.getWidth()) {
                        x += 10;
                    }



                    g.drawLine(x - (wWidth / 2), y - (wWidth / 2) + top, x + (wWidth / 2), y - (wWidth / 2) + top);

                    g.drawLine(x - (wWidth / 2) + left, y - (wWidth / 2), x - (wWidth / 2) + left, y + (wWidth / 2));

                    g.drawLine(x + right, y - (wWidth / 2), x + right, y + (wWidth / 2));

                    k.repaint();
                }
                /* if (ucunuBul(cbim) && solunuBul(cbim) && saginiBul(cbim)) {

                if (top < 10 && y > 5) {
                y -= 10;
                }
                if (left < 10 && x > 5) {
                x -= 10;
                }
                if (right > (wWidth/2-5) && x < k.getWidth()) {
                x += 10;
                }
                g.drawLine(x - (wWidth/2), y - (wWidth/2) + top, x + (wWidth/2), y - (wWidth/2) + top);

                g.drawLine(x - (wWidth/2) + left, y - (wWidth/2), x - (wWidth/2) + left, y + (wWidth/2));

                g.drawLine(x + right, y - (wWidth/2), x + right, y + (wWidth/2));

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
        g.drawRect(x - (wWidth / 2), y - (wWidth / 2), wWidth, wWidth);

        g.drawLine(x - (wWidth / 2), y + (wWidth / 2) + bottom, x + (wWidth / 2), y + (wWidth / 2) + bottom);

    }

    public boolean ucunuBul(BufferedImage bim) {
        //bim = bim.getSubimage(x - (wWidth/2), y - (wWidth/2), wWidth, wWidth);
        int temp1 = bim.getRGB((wWidth/2-5), (wWidth / 2)), temp2 = bim.getRGB(15, (wWidth / 2)), temp3 = bim.getRGB((wWidth/2+5), (wWidth / 2));
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x - 5, y, x + 5, y);
        for (int i = (wWidth / 2); i > 0; i -= 2) {
            rgb1 = bim.getRGB((wWidth/2-5), i);
            rgb2 = bim.getRGB((wWidth / 2), i);
            rgb3 = bim.getRGB((wWidth/2+5), i);
            // System.out.println("rgb:" + rgb + " temp:" + temp + "fark:" + (rgb - temp));
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 20) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 20) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 20)) {
                //            if (rgb < -9000000 || rgb > -(wWidth/2+5)00000) { // Kenar siyah veya beyaz ise kenar bulundu.
                //if (rgb < -10000000 || rgb > -(wWidth/2+5)00000) { // Kenar siyah veya beyaz ise kenar bulundu.
                top = i;
                //System.out.println("Top " + top);
                return true;
            }
        }
        return false;
    }

    public boolean altBul(BufferedImage bim) {

        //bim = bim.getSubimage(x - (wWidth/2), y - (wWidth/2), wWidth, wWidth);
        int temp1 = bim.getRGB((wWidth/2-5), wWidth), temp2 = bim.getRGB(15, wWidth), temp3 = bim.getRGB((wWidth/2+5), wWidth);
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x - 5, y, x + 5, y);
        for (int i = (wWidth / 2); i > 0; i -= 2) {
            rgb1 = bim.getRGB((wWidth/2-5), (wWidth / 2) + i);
            rgb2 = bim.getRGB((wWidth / 2), (wWidth / 2) + i);
            rgb3 = bim.getRGB((wWidth/2+5), (wWidth / 2) + i);
            // System.out.println("rgb:" + rgb + " temp:" + temp + "fark:" + (rgb - temp));
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 20) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 20) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 20)) {
                //            if (rgb < -9000000 || rgb > -(wWidth/2+5)00000) { // Kenar siyah veya beyaz ise kenar bulundu.
                //if (rgb < -10000000 || rgb > -(wWidth/2+5)00000) { // Kenar siyah veya beyaz ise kenar bulundu.
                bottom = i;
                //System.out.println("Alt " + bottom);
                return true;
            }
        }
        return false;
    }

    public boolean solunuBul(BufferedImage bim) {
        //bim = bim.getSubimage(x - (wWidth/2), y - (wWidth/2), wWidth, wWidth);
        int temp1 = bim.getRGB((wWidth / 2), (wWidth/2-5)), temp2 = bim.getRGB((wWidth / 2), (wWidth / 2)), temp3 = bim.getRGB((wWidth / 2), (wWidth/2+5));
        int rgb1, rgb2, rgb3;
//        int temp = bim.getRGB((wWidth/2), (wWidth/2)), rgb;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = (wWidth / 2); i > 0; i -= 2) {

            rgb1 = bim.getRGB(i, (wWidth/2-5));
            rgb2 = bim.getRGB(i, (wWidth / 2));
            rgb3 = bim.getRGB(i, (wWidth/2+5));
//            rgb = bim.getRGB(i, (wWidth/2));
            //System.out.println("rgb:" + rgb);
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 20) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 20)) {

//            if (Math.abs(temp - rgb) > 9000000 || Math.abs(temp - rgb) > 20) {
//            if (rgb < -9000000 || rgb > -(wWidth/2+5)00000) { // Kenar siyah veya beyaz ise kenar bulundu.
                left = i;
                //System.out.println("Left:" + left);
//                wWidth-=2;
                return true;
            }
        }
        wWidth+=2;
        return false;
    }

    public boolean saginiBul(BufferedImage bim) {
        int temp1 = bim.getRGB((wWidth / 2), (wWidth/2-5)), temp2 = bim.getRGB((wWidth / 2), (wWidth / 2)), temp3 = bim.getRGB((wWidth / 2), (wWidth/2+5));
        int rgb1, rgb2, rgb3;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = 0; i < (wWidth / 2); i += 2) {
            rgb1 = bim.getRGB((wWidth / 2) + i, (wWidth/2-5));
            rgb2 = bim.getRGB((wWidth / 2) + i, (wWidth / 2));
            rgb3 = bim.getRGB((wWidth / 2) + i, (wWidth/2+5));
            //System.out.println("rgb:" + rgb);
            if ((Math.abs(temp1 - rgb1) > 9000000 || Math.abs(temp1 - rgb1) > 2000000) || (Math.abs(temp2 - rgb2) > 9000000 || Math.abs(temp2 - rgb2) > 20) || (Math.abs(temp3 - rgb3) > 9000000 || Math.abs(temp3 - rgb3) > 20)) { // Kenar siyah veya beyaz ise kenar bulundu.
                right = i;
                //System.out.println("Right:" + right);
//                wWidth-=2;
                return true;
            }
        }

        wWidth+=2;
        return false;
    }

    public boolean parmakKontrol(BufferedImage bim) {

        int temp = bim.getRGB((wWidth / 2), (wWidth / 2)), rgb;
        //k.bufferGraphics.drawLine(x, y, x, y);
        for (int i = 0; i < (wWidth / 2); i += 2) {
            rgb = bim.getRGB((wWidth / 2), (wWidth / 2) + i);
//            k.bufferGraphics.drawLine(x + (wWidth/2), y + i, x + (wWidth/2), y + (wWidth/2) + i);
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
