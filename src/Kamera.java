
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;

public class Kamera extends JApplet {

    Graphics bufferGraphics;
    BufferedImage offscreen;
    KameraKesit kameraKesit = new KameraKesit();

    public void init() {
        setSize(320, 240);
        offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferGraphics = offscreen.getGraphics();
        kameraKesit.init();
        new Thread(new Izleme(this)).start();
    }

    public void paint(Graphics g) {
        g.drawImage(offscreen.getScaledInstance(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB), 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }

//    @Override
//    public void start() {
//        repaint();
//    }
    public boolean capturer(BufferedImage bim) {
        bufferGraphics.drawImage(bim, 0, 0, this);
        repaint();
        return true;
    }
}
