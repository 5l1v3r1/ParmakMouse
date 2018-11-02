
import java.awt.Graphics;
import java.awt.image.BufferedImage;
import javax.swing.JApplet;

public class KameraKesit extends JApplet {

    Graphics bufferGraphics;
    BufferedImage offscreen;

    public void init() {
        setSize(128, 96);
        offscreen = new BufferedImage(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB);
        bufferGraphics = offscreen.getGraphics();
    }

    public void paint(Graphics g) {
        g.drawImage(offscreen.getScaledInstance(getWidth(), getHeight(), BufferedImage.TYPE_INT_RGB), 0, 0, this);
    }

    @Override
    public void update(Graphics g) {
        paint(g);
    }
}