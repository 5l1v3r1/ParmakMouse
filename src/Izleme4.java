
import java.awt.Color;
import java.awt.Graphics;
import java.awt.MouseInfo;
import java.awt.Robot;
import java.awt.Toolkit;
import java.awt.event.InputEvent;
import java.awt.image.BufferedImage;
import java.awt.image.FilteredImageSource;
import java.io.File;
import java.util.Scanner;

public class Izleme4 implements Runnable {

    Kamera k;
    Graphics g;
    Photographer p = new Photographer();
    int x, y;
    int top, bottom, left, right;
    int wWidth = 50;
    boolean egitim = false;
    int[][] histogramX = new int[2180][128];
    int screenWidth, screenHeight;

    public Izleme4(Kamera k) {
        this.k = k;
        g = k.bufferGraphics;
        x = k.getWidth() / 2;
        y = k.getHeight() / 2;
        try {
            Robot r = new Robot();
            screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            System.out.println("Screen Width: " + screenWidth);
        } catch (Exception e) {
        }
    }

    public void run() {

        try {
            Scanner s = new Scanner(new File("D:\\ParmakResim\\HistogramX.txt"));
            int i = 0, j = 0;
            for (int l = 0; l < histogramX.length; l++) {
                for (int m = 0; m < histogramX[0].length; m++) {
                    histogramX[l][m] = s.nextInt();
                }
            }
        } catch (Exception e) {
        }
//        for (int i = 0; i < histogramX[440].length; i++) {
//            System.out.print(histogramX[440][i] + ", ");
//
//        }
//        System.out.println("");

        BufferedImage bim, cbim,
                kesit = new BufferedImage(128, 96, BufferedImage.TYPE_BYTE_BINARY),
                kesit2 = new BufferedImage(128, 96, BufferedImage.TYPE_BYTE_BINARY);

        g.drawString("Kamera hazırlanıyor...", k.getWidth() / 2 - 50, k.getWidth() / 2 - 50);
        p.basla();
        g.setColor(Color.black);
        g.fillRect(0, 0, k.getWidth(), k.getHeight());
        g.setColor(Color.white);
        g.drawString("Kamera hazır!", k.getWidth() / 2 - 40, k.getWidth() / 2 - 50);
        FilteredImageSource filteredSrc, histoFiltre;
        SkinFilter skinFilter = new SkinFilter();

        int i = 0;
        long time = System.currentTimeMillis();
        int frame = 0;

        int yuksTemp = 0,
                yuksTop = 0,
                yuksLeft = 0,
                yuksRight = 0;
        long yuksTik = System.currentTimeMillis();
        boolean click1 = false, click2 = false;
        int clickX = 0, clickY = 0;
        boolean clickRelease = false;

        while (true) {
            frame = (int) (1000 / (System.currentTimeMillis() - time + 1));
            time = System.currentTimeMillis();
            try {
                cbim = p.takePhoto();
                if (cbim == null) {
                    continue;
                }
                bim = cbim.getSubimage(50, 50, k.getWidth() - 100, k.getHeight() - 100);

                Graphics bimG = bim.getGraphics();


                skinFilter.clearList();
                filteredSrc = new FilteredImageSource(bim.getSource(), skinFilter);
                bimG.drawImage(Toolkit.getDefaultToolkit().createImage(filteredSrc), 0, 0, null);

                k.bufferGraphics.setColor(Color.BLACK);
                k.bufferGraphics.fillRect(0, 0, k.getWidth(), k.getHeight());
                k.bufferGraphics.drawImage(cbim, 0, 0, null);
                k.bufferGraphics.drawImage(bim, 50, 50, null);
                alanCiz();

                /////////////////////////////////////////////
                // EKRANDAKİ BEYAZ ALANI BULUP İŞLEME
                try {
                    if (skinFilter.getPixelList().size() > 1) {

                        /////////////////////////////////////
                        // KESİT ALMA 128x96
                        k.bufferGraphics.setColor(Color.YELLOW);
                        k.bufferGraphics.drawRect(50 + skinFilter.arr[0], 50 + skinFilter.arr[2], (skinFilter.arr[1] - skinFilter.arr[0]), (skinFilter.arr[3] - skinFilter.arr[2]));

                        if ((skinFilter.arr[1] - skinFilter.arr[0]) > 0 && (skinFilter.arr[3] - skinFilter.arr[2]) > 0) {
                            kesit.getGraphics().drawImage(
                                    bim.getSubimage(skinFilter.arr[0], skinFilter.arr[2], (skinFilter.arr[1] - skinFilter.arr[0]), (skinFilter.arr[3] - skinFilter.arr[2]) + 1).getScaledInstance(
                                    128, 96, 0), 0, 0, null);
                            int[] ax = new int[128];
                            int[] ay = new int[96];
                            Histogram histogram = new Histogram(ax, ay);
                            histoFiltre = new FilteredImageSource(bim.getSubimage(skinFilter.arr[0], skinFilter.arr[2], (skinFilter.arr[1] - skinFilter.arr[0]), (skinFilter.arr[3] - skinFilter.arr[2])).getScaledInstance(
                                    128, 96, 0).getSource(), histogram);
                            kesit.getGraphics().drawImage(Toolkit.getDefaultToolkit().createImage(histoFiltre), 0, 0, null);

                            k.kameraKesit.bufferGraphics.drawImage(kesit, 0, 0, null);
                            Thread.sleep(10);

                            //
                            /////////////////////////////////////

                            /////////////////////////////////////
                            // HİSTOGRAMDAN BENZERLİK ÖLÇME
                            int maxBenzer = 0, benzer;
                            for (int q = 0; q < histogramX.length; q++) {
                                benzer = 0;
                                for (int j = 0; j < ax.length; j++) {
                                    if ((ax[j] / 10) == (histogramX[q][j] / 10)) {
                                        benzer++;
                                    }
                                }
                                if (benzer > maxBenzer) {
                                    maxBenzer = benzer;
                                }

                            }
                            k.bufferGraphics.drawString("MaxBenzer:" + maxBenzer, 10, 30);
                            //
                            /////////////////////////////////////

                            /////////////////////////////////////
                            // HİSTOGRAM SONUÇ ALMA
                            //for (int j = 0; j < ax.length; j++) {
                            //    System.out.print((ax[j]) + " ");
                            //}
                            //System.out.println("");
                            //
                            /////////////////////////////////////


                            /////////////////////////////////////
                            // FARE HAREKETLERİ
                            if (maxBenzer > 0) {
                                if (!click1 && !click2) {
                                    int xCenter = skinFilter.arr[0] + ((skinFilter.arr[1] - skinFilter.arr[0]) / 2);
                                    xCenter += ((skinFilter.arr[1] - skinFilter.arr[0]) / 2) / xCenter;
                                    float xPos = (float) xCenter / (float) bim.getWidth();
                                    float yPos = (float) (skinFilter.arr[2]) / (float) bim.getHeight();
                                    new Robot().mouseMove((int) (screenWidth * xPos), (int) (screenHeight * yPos));
                                }

                                if (time - yuksTik > 200) {
                                    if ((yuksTop <= (skinFilter.arr[2]) * 0.7)
                                            && (Math.abs(yuksLeft - skinFilter.arr[0]) < 5
                                            || Math.abs(yuksRight - skinFilter.arr[1]) < 5) && !click1 && !click2) {
                                        click1 = true;
                                        yuksTemp = yuksTop;
                                        new Robot().mouseMove(clickX, clickY);

                                    } else if (click1 && !click2) {

                                        if (Math.abs(yuksTop - yuksTemp) < 10) {
                                            System.out.println("Tık..");
                                            new Robot().mouseMove(clickX, clickY);
                                            new Robot().mousePress(InputEvent.BUTTON1_MASK);
                                            new Robot().mouseRelease(InputEvent.BUTTON1_MASK);
                                            clickRelease = !clickRelease;
                                            click2 = true;
                                        }
                                    } else {
                                        click1 = false;
                                        click2 = false;
                                        clickX = MouseInfo.getPointerInfo().getLocation().x;
                                        clickY = MouseInfo.getPointerInfo().getLocation().y;
                                    }
                                    yuksLeft = (skinFilter.arr[0]);
                                    yuksRight = (skinFilter.arr[1]);
                                    yuksTop = (skinFilter.arr[2]);
                                    yuksTik = System.currentTimeMillis();
                                }
                            }
                            //
                            /////////////////////////////////////
                        }
                    }
                } catch (Exception e) {
                    System.out.println("hata");
                    e.printStackTrace();
                }
                //
                /////////////////////////////////////////////

                /////////////////////////////////////////////
                // FRAME PER SECOND 
                k.bufferGraphics.setColor(Color.WHITE);
                k.bufferGraphics.drawString(frame + "fps", 10, 10);
                //
                /////////////////////////////////////////////
            } catch (Exception ex) {
                System.out.println("hata" + ex.getMessage());
                ex.printStackTrace();
            }
        }
    }

    public void alanCiz() {
        g.setColor(Color.red);
        g.drawRect(50, 50, k.getWidth() - 100, k.getHeight() - 100);

//        g.drawLine(x - 25, y + 25 + bottom, x + 25, y + 25 + bottom);

    }

    public static void main(String[] args) {
        new Arayuz().setVisible(true);
    }
}
