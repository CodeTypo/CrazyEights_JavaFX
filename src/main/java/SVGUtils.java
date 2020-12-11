import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.svggen.SVGColor;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.io.IOException;
import java.io.InputStream;

public class SVGUtils {
    public static Image getImageFromSVG(String path){
        Image image = null;

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, 100F);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, 140F);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_BACKGROUND_COLOR, SVGColor.red);

        try (InputStream file = SVGUtils.class.getResourceAsStream(path)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                image = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            } catch (TranscoderException ex) {ex.printStackTrace();}
        } catch (IOException io) {io.printStackTrace();}
        return image;
    }

    public static String getSVGCardResourcePath(Suit suit, Denomination denomination){
        // change here if file naming convention changes
        // directory in resources folder
        return "/imagesSVG/" +
                // every name consists of:
                denomination.getS() + //denomination shortname
                suit.getS() +         //and suit shortname
                ".svg";
    }
}