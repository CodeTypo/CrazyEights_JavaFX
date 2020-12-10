import javafx.embed.swing.SwingFXUtils;
import javafx.scene.image.Image;
import org.apache.batik.svggen.SVGColor;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import org.apache.batik.transcoder.image.ImageTranscoder;

import java.io.IOException;
import java.io.InputStream;

public class SVGUtils {

    // generate back image only once and reuse it every time
    public static Image cardBack = getImageFromSVG("/imagesSVG/1B.svg");

    public static Image getImageFromSVG(String path){
        // SVG image should be set on ImageView in the last step,
        // after all operations like rotation and sizing are performed.
        // The nature of svg format force us to render it every time we need
        // to change its attributes.
        Image image = null;

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        transcoder.addTranscodingHint(ImageTranscoder.KEY_WIDTH, 79F);
        transcoder.addTranscodingHint(ImageTranscoder.KEY_HEIGHT, 111F);
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

//    public static Image getCardFront(Card card){
//        //A method that returns a front image of the Card that is being passed as its argument
//        String path = getSVGCardResourcePath(card);
//        return getImageFromSVG(path);
//    }

    public static String getSVGCardResourcePath(Suit suit, Denomination denomination){
        // change here if file naming convention changes
        // directory in resources folder
        return "/imagesSVG/" +
                // every name consists of:
                denomination.getS() + //denomination shortname
                suit.getS() +         //and suit shortname
                ".svg";
    }

//    public static String getSVGCardResourcePath(Card card){
//        // change here if file naming convention changes
//        // directory in resources folder
//        return "/imagesSVG/" +
//                // every name consists of:
//                card.getDenomination().getS() + //denomination shortname
//                card.getSuit().getS() +         //and suit shortname
//                ".svg";
//    }

    public static Image getCardBack() {
        return cardBack;
    }

}
