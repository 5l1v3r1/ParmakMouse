
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
import javax.imageio.ImageIO;

public class Izleme implements Runnable {

    Kamera k;
    Graphics g;
    Photographer p = new Photographer();
    int x, y;
    BufferedImage tempImage;
    int[][] histogram1parmak = new int[2000][128];
    int[][] histogram2parmak = new int[2000][128];
    int[][] histogram3parmak = new int[2000][128];
    int screenWidth, screenHeight;
    // Ok işaretleri
    BufferedImage sol_aktif, sag_aktif, yukari_aktif, asagi_aktif;
    BufferedImage sol_pasif, sag_pasif, yukari_pasif, asagi_pasif;
    BufferedImage sol, sag, yukari, asagi;
    // Parmak Durum
    int parmakStat = 0;

    public Izleme(Kamera k) {
        this.k = k;
        tempImage = new BufferedImage(k.getWidth(), k.getHeight(), BufferedImage.TYPE_INT_RGB);
//        g = k.bufferGraphics;
        g = tempImage.getGraphics();
        x = k.getWidth() / 2;
        y = k.getHeight() / 2;
        try {
            Robot r = new Robot();
            screenWidth = (int) Toolkit.getDefaultToolkit().getScreenSize().getWidth();
            screenHeight = (int) Toolkit.getDefaultToolkit().getScreenSize().getHeight();
            sol_aktif = ImageIO.read(this.getClass().getResource("oklar\\sol_aktif.png"));
            sol_pasif = ImageIO.read(this.getClass().getResource("oklar\\sol_pasif.png"));
            sag_aktif = ImageIO.read(this.getClass().getResource("oklar\\sag_aktif.png"));
            sag_pasif = ImageIO.read(this.getClass().getResource("oklar\\sag_pasif.png"));
            yukari_aktif = ImageIO.read(this.getClass().getResource("oklar\\yukari_aktif.png"));
            yukari_pasif = ImageIO.read(this.getClass().getResource("oklar\\yukari_pasif.png"));
            asagi_aktif = ImageIO.read(this.getClass().getResource("oklar\\asagi_aktif.png"));
            asagi_pasif = ImageIO.read(this.getClass().getResource("oklar\\asagi_pasif.png"));

        } catch (Exception e) {
            System.out.println(e.getMessage());
            e.printStackTrace();
        }
    }

    public void run() {
        Scanner s;
        /////////////////////////////////////////////////////
        // HİSTOGRAMLARI YÜKLE
        try {
            s = new Scanner(new File("D:\\ParmakResim\\Histogram1parmak.txt"));
            int i = 0, j = 0;
            for (int l = 0; l < histogram1parmak.length; l++) {
                for (int m = 0; m < histogram1parmak[0].length; m++) {
                    histogram1parmak[l][m] = s.nextInt();
                }
            }
            s = new Scanner(new File("D:\\ParmakResim\\Histogram2parmak.txt"));
            i = 0;
            j = 0;
            for (int l = 0; l < histogram2parmak.length; l++) {
                for (int m = 0; m < histogram2parmak[0].length; m++) {
                    histogram2parmak[l][m] = s.nextInt();
                }
            }
            s = new Scanner(new File("D:\\ParmakResim\\Histogram3parmak.txt"));
            i = 0;
            j = 0;
            for (int l = 0; l < histogram3parmak.length; l++) {
                for (int m = 0; m < histogram3parmak[0].length; m++) {
                    histogram3parmak[l][m] = s.nextInt();
                }
            }
        } catch (Exception e) {
            System.out.println("Histogramlar yüklenemedi:" + e.getMessage());
        }
        //
        /////////////////////////////////////////////////////

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
        boolean clickRelease = false;
        long clickTime = System.currentTimeMillis();

        while (true) {
            frame = (int) (1000 / (System.currentTimeMillis() - time + 1));
            time = System.currentTimeMillis();
            sol = sol_pasif;
            sag = sag_pasif;
            yukari = yukari_pasif;
            asagi = asagi_pasif;
            parmakStat = 0;
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

                g.setColor(Color.BLACK);
                g.fillRect(0, 0, k.getWidth(), k.getHeight());
                g.drawImage(cbim, 0, 0, null);
                g.drawImage(bim, 50, 50, null);

                /////////////////////////////////////////////
                // EKRANDAKİ BEYAZ ALANI BULUP İŞLEME
                try {
                    if (skinFilter.getPixelList().size() > 1) {
                        parmakStat = 0;


                        if ((skinFilter.arr[1] - skinFilter.arr[0]) > 0 && (skinFilter.arr[3] - skinFilter.arr[2]) > 0) {
                            /////////////////////////////////////
                            // KESİT ALMA 128x96
                            g.setColor(Color.YELLOW);
                            g.drawRect(50 + skinFilter.arr[0], 50 + skinFilter.arr[2], (skinFilter.arr[1] - skinFilter.arr[0]), (skinFilter.arr[3] - skinFilter.arr[2]));
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
                            int benzer = 0;
                            /////////////////////////////////
                            // 1 parmak
                            int maxBenzer1parmak = 0;
                            for (int q = 0; q < histogram1parmak.length; q++) {
                                benzer = 0;
                                for (int j = 0; j < ax.length; j++) {
                                    if ((ax[j] / 10) == (histogram1parmak[q][j] / 10)) {
                                        benzer++;
                                    }
                                }
                                if (benzer > maxBenzer1parmak) {
                                    maxBenzer1parmak = benzer;
                                }

                            }
                            if (maxBenzer1parmak > 70) {
                                g.drawString("1p:" + (int) (((float) maxBenzer1parmak / (float) 128) * 100) + "%", 10, 30);
                            }
                            //
                            /////////////////////////////////
                            // 2 parmak
                            int maxBenzer2parmak = 0;
                            for (int q = 0; q < histogram2parmak.length; q++) {
                                benzer = 0;
                                for (int j = 0; j < ax.length; j++) {
                                    if ((ax[j] / 10) == (histogram2parmak[q][j] / 10)) {
                                        benzer++;
                                    }
                                }
                                if (benzer > maxBenzer2parmak) {
                                    maxBenzer2parmak = benzer;
                                }

                            }
                            if (maxBenzer2parmak > 70) {
                                g.drawString("2p :" + (int) (((float) maxBenzer2parmak / (float) 128) * 100) + "%", 10, 50);
                            }
                            //
                            /////////////////////////////////
                            // 3 parmak
                            int maxBenzer3parmak = 0;
                            for (int q = 0; q < histogram3parmak.length; q++) {
                                benzer = 0;
                                for (int j = 0; j < ax.length; j++) {
                                    if ((ax[j] / 10) == (histogram3parmak[q][j] / 10)) {
                                        benzer++;
                                    }
                                }
                                if (benzer > maxBenzer3parmak) {
                                    maxBenzer3parmak = benzer;
                                }

                            }
                            if (maxBenzer3parmak > 70) {
                                g.drawString("3p :" + (int) (((float) maxBenzer3parmak / (float) 128) * 100) + "%", 10, 70);
                            }
                            //
                            /////////////////////////////////////

                            /////////////////////////////////////
                            // HİSTOGRAM EĞİTİM VERİLERİ ALMA
//                            for (int j = 0; j < ax.length; j++) {
//                                System.out.print((ax[j]) + " ");
//                            }
//                            System.out.println("");
                            //
                            /////////////////////////////////////


                            /////////////////////////////////////
                            // FARE HAREKETLERİ
                            // BİR PARMAK İÇİN HAREKETLER
                            if (maxBenzer1parmak > 70 && maxBenzer2parmak < maxBenzer1parmak) {

                                int mx = MouseInfo.getPointerInfo().getLocation().x;
                                int my = MouseInfo.getPointerInfo().getLocation().y;
                                if (skinFilter.arr[0] < 25 && skinFilter.arr[1] < bim.getWidth() - 25) {
                                    mx -= (50 - skinFilter.arr[0]) / 5;
                                    sol = sol_aktif;
                                }
                                if (skinFilter.arr[0] > 25 && skinFilter.arr[1] > bim.getWidth() - 25) {
                                    mx += (50 - (bim.getWidth() - skinFilter.arr[1])) / 5;
                                    sag = sag_aktif;
                                }
                                if (skinFilter.arr[2] < 25) {
                                    my -= (50 - skinFilter.arr[2]) / 5;
                                    yukari = yukari_aktif;
                                }
                                if (skinFilter.arr[2] > bim.getHeight() - 25) {
                                    my += (50 - (bim.getHeight() - skinFilter.arr[2])) / 5;
                                    asagi = asagi_aktif;
                                }
                                new Robot().mouseMove(mx, my);
                                parmakStat = 1;
                            } // İKİ PARMAK İÇİN HAREKETLER
                            else if (maxBenzer2parmak > 70 && maxBenzer2parmak > maxBenzer1parmak) {
                                parmakStat = 2;
                            }// ÜÇ PARMAK İÇİN HAREKETLER
                            else if (maxBenzer3parmak > 60 && maxBenzer3parmak > maxBenzer1parmak && maxBenzer3parmak > maxBenzer2parmak) {
                                parmakStat = 3;
                                if (skinFilter.arr[2] < 30) {
                                    (new Robot()).mouseWheel(-2);
                                    yukari = yukari_aktif;
                                }
                                if (skinFilter.arr[2] > bim.getHeight() - 30) {
                                    (new Robot()).mouseWheel(2);
                                    asagi = asagi_aktif;
                                }
                            }
                            //
                            /////////////////////////////////

                            /////////////////////////////////
                            // TIKLAMA
                            if (time - yuksTik > 100 && (parmakStat == 1 || parmakStat == 2) && (skinFilter.arr[3] - skinFilter.arr[2]) > 10) {
                                double tikOrani = 0.8;
                                if ((skinFilter.arr[3] - skinFilter.arr[2]) < (yuksTop * tikOrani) // Yükseklik önceki yüksekliğin 0.60'ından küçükse
                                        && (Math.abs((yuksRight - yuksLeft) - (skinFilter.arr[1] - skinFilter.arr[0])) < 10)
                                        && !click1 && !click2) {
                                    click1 = true;
                                    yuksTemp = (skinFilter.arr[3] - skinFilter.arr[2]); // küçük olan yükseklik
                                } else if (click1 && !click2 && yuksTemp < ((skinFilter.arr[3] - skinFilter.arr[2]) * tikOrani) && parmakStat != 0) {
//                                    System.out.print("TIK :");
                                    Robot r = new Robot();
                                    if (parmakStat == 1) {
//                                        System.out.print(" Sol");
                                        if (time - clickTime < 2000) {
                                            r.mousePress(InputEvent.BUTTON1_MASK);
                                            r.mouseRelease(InputEvent.BUTTON1_MASK);
//                                            System.out.print(" Çift");
                                        }
                                        r.mousePress(InputEvent.BUTTON1_MASK);
                                        r.mouseRelease(InputEvent.BUTTON1_MASK);
                                        clickTime = time;
                                    } else if (parmakStat == 2) {
//                                        System.out.print(" Sağ");
                                        r.mousePress(InputEvent.BUTTON3_MASK);
                                        r.mouseRelease(InputEvent.BUTTON3_MASK);
                                    }
                                    Toolkit.getDefaultToolkit().beep();
                                    clickRelease = !clickRelease;
                                    click1 = false;
                                    click2 = false;
                                    System.out.println("");
                                } else {
                                    click1 = false;
                                    click2 = false;
                                }
                                yuksLeft = (skinFilter.arr[0]);
                                yuksRight = (skinFilter.arr[1]);
                                yuksTop = (skinFilter.arr[3] - skinFilter.arr[2]);
                                yuksTik = System.currentTimeMillis();
                            }
                            //
                            /////////////////////////////
                        }
                    }
                } catch (Exception e) {
                    System.out.println("Hata: " + e.getLocalizedMessage());
                    e.printStackTrace();
                }
                //
                /////////////////////////////////////////////

                alanCiz();

                /////////////////////////////////////////////
                // FRAME PER SECOND 
                g.setColor(Color.WHITE);
                g.drawString(frame + "fps", 10, 10);
                //
                /////////////////////////////////////////////
            } catch (Exception ex) {
                System.out.println("hata" + ex.getMessage());
                ex.printStackTrace();
            }
            k.bufferGraphics.drawImage(tempImage, 0, 0, k);
        }
    }

    public void alanCiz() {

        /////////////////////////////////////////////////////
        // 1 PARMAK İÇİN
        if (parmakStat == 1) {
            g.setColor(Color.gray);
            g.drawRect(75, 75, k.getWidth() - 150, k.getHeight() - 150);

            g.drawImage(sag, k.getWidth() - 100 + 12, (k.getHeight() / 2) - (sag.getHeight() / 2), k);
            g.drawImage(sol, 50 - 12, (k.getHeight() / 2) - (sol.getHeight() / 2), k);
            g.drawImage(yukari, (k.getWidth() / 2) - (yukari.getWidth() / 2), 50 - 10, k);
            g.drawImage(asagi, (k.getWidth() / 2) - (yukari.getWidth() / 2), 150 + 4, k);
        }
        //
        //////////////////////////////////////////////////////

        //////////////////////////////////////////////////////
        // 2 PARMAK İÇİN
        if (parmakStat == 2) {
            g.setColor(Color.gray);
        }
        //
        /////////////////////////////////////////////////////

        /////////////////////////////////////////////////////
        // 3 PARMAK İÇİN
        if (parmakStat == 3) {
            g.setColor(Color.gray);
            g.drawRect(50, 50 + 30, k.getWidth() - 100, k.getHeight() - 100 - 30 - 30);

            g.drawImage(yukari, (k.getWidth() / 2) - (yukari.getWidth() / 2), 50 - 8, k);
            g.drawImage(asagi, (k.getWidth() / 2) - (yukari.getWidth() / 2), k.getHeight() - 100 + 8, k);
        }
        //
        /////////////////////////////////////////////////////

        /////////////////////////////////////////////////////
        // İŞLEM BÖLGESİNİ ÇİZ
        g.setColor(Color.red);
        g.drawRect(50, 50, k.getWidth() - 100, k.getHeight() - 100);
        //
        /////////////////////////////////////////////////////
    }

    public static void main(String[] args) {
        new Arayuz().setVisible(true);
    }
}
