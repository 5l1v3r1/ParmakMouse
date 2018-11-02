
//import java.awt.*;
import java.awt.image.BufferedImage;
import javax.media.*;
import javax.media.control.FormatControl;
import javax.media.control.FrameGrabbingControl;
import javax.media.format.VideoFormat;
import javax.media.protocol.CaptureDevice;
import javax.media.protocol.DataSource;
import javax.media.util.BufferToImage;

//import javax.media.control.FormatControl;
public class Photographer {

    static MediaLocator ml;
    static Player player;
//    static Component videoScreen;
    static Buffer buf = null;
//    static String str = null;
    static BufferToImage btoi = null;
//    //static BufferedImage bi = null;
//    static Image frameImage = null;
//    static String filename = null;
//    static FormatControl formatControl = null;
//    static Format currFormat = null;
//    static VideoFormat currentFormat = null;

    public void basla() {
        try {

            Manager.setHint(Manager.PLUGIN_PLAYER, true);

            Player player;
//            CaptureDeviceInfo captureDeviceInfo = CaptureDeviceManager.getDevice("vfw:A4 TECH PC Camera V:0");
            CaptureDeviceInfo captureDeviceInfo = CaptureDeviceManager.getDevice("vfw:Microsoft WDM Image Capture (Win32):0");
//            CaptureDeviceInfo captureDeviceInfo = CaptureDeviceManager.getDevice("vfw:0");
//            Format[] formats = captureDeviceInfo.getFormats();
//            Format selectedFormat = null;
//            for (Format f : formats) {
////                System.out.println(f.toString());
//
//                if (f.toString().contains("width=640,height=480")) {
//                    selectedFormat = f;
////                    break;
//                }
//            }
////            System.out.println(selectedFormat.toString());
//            
            MediaLocator mediaLocator = captureDeviceInfo.getLocator();
            DataSource videoDataSource = Manager.createDataSource(mediaLocator);
//            player.addControllerListener(this);
//            player.start();
            FormatControl[] fcs = ((CaptureDevice) videoDataSource).getFormatControls();
            for (FormatControl fc : fcs) {
                Format[] formatsa = ((FormatControl) fc).getSupportedFormats();
                for (Format format : formatsa) {
                    System.out.println(format.toString());
                    if (format.toString().contains("width=640,height=480")) {
                        ((FormatControl) fc).setFormat(format);
                        System.out.println("Format ayarlandı..");
//                        return true;
                        break;
                    }
                }
            }

            player = Manager.createPlayer(videoDataSource);
            player.start();

            this.player = player;

            Thread.sleep(1000);
            System.out.println("Kamera hazır!");
        } catch (Exception ex) {
            System.out.println("basla hata var");
            ex.printStackTrace();
        }
    }

    public void bitir() {
        player.close();
        player.deallocate();
        System.out.println("Kamera kapatıldı..");

    }

    public BufferedImage takePhoto() {
        try {


//            videoScreen = player.getVisualComponent();
//            Frame frame = new Frame();
//            frame.setBounds(10, 10, 300, 300);
//            frame.add(videoScreen);
            //frame.setVisible(true);
            //System.out.println(player.getState());
            //Thread.sleep(1000);

            //Control[] controls = player.getControls();

            FrameGrabbingControl fgc = (FrameGrabbingControl) player.getControl("javax.media.control.FrameGrabbingControl");
            //    fgc.grabFrame().set
            if (fgc == null) {
                System.out.println("fgc == null");
            }

            buf = fgc.grabFrame();
            btoi = new BufferToImage((VideoFormat) buf.getFormat());
            //if (btoi == null) {
            //   System.out.println("btoi == null");
            //}

            //System.out.println(player.getState());
//            java.awt.image.BufferedImage img = (java.awt.image.BufferedImage) btoi.createImage(buf);
//Thread.sleep(100);
//            try {
//                filename = "c:\\myImage.jpg";
//                File file = new File(filename);
//                FileOutputStream out = new FileOutputStream(file);
//                JPEGImageEncoder encoder = JPEGCodec.createJPEGEncoder(out);
//                //JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(bi);
//                JPEGEncodeParam param = encoder.getDefaultJPEGEncodeParam(img);
//                param.setQuality(1.0f, false);   // 100% high quality setting, no compression
//                encoder.setJPEGEncodeParam(param);
//                //encoder.encode ( bi );
//                encoder.encode(img);
//                out.close();
//            } catch (Exception e) {
//                System.out.println("Error saving JPEG: " + e.getMessage());
//            }
//            frame.setVisible(false);
            //player.close();
            //player.deallocate();
        } catch (Exception e) {
            System.err.println("Error..: " + e.getMessage());
        }

        return (BufferedImage) btoi.createImage(buf);

        /*return bi;*/
    }

    public static void main(String[] args) {
        Photographer p = new Photographer();
        p.basla();
//        p.takePhoto();
    }
}
