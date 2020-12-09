import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
// imports for loading and converting external svg files
import java.io.IOException;
import java.io.InputStream;
import org.apache.batik.transcoder.TranscoderException;
import org.apache.batik.transcoder.TranscoderInput;
import javafx.embed.swing.SwingFXUtils;

public class CrazyEightsController {

    @FXML
    private ResourceBundle resources;

    @FXML
    private URL location;

    @FXML
    private HBox box1;

    @FXML
    private HBox box3;

    @FXML
    private VBox box4;

    @FXML
    private VBox box2;

    @FXML
    private Button startButton;

    @FXML
    private ImageView pileImg;

    @FXML
    private ImageView deckImg;

    GameModel gameModel = new GameModel();
    Player p1 = new Player();
    Player p2 = new Player();
    Player p3 = new Player();
    Player p4 = new Player();

    private Image getCardFront(Card card){
        String path = getSVGCardResourcePath(card);
        Image image = getImageFromSVG(path);
        return image;
    }

    private Image getCardBack(Card card){
        String path = "/imagesSVG/";
        switch (card.getSuit()){
            case CLUBS, SPADES -> path += "1B.svg";
            case HEARTS,DIAMONDS -> path += "2B.svg";
        }
        Image image = getImageFromSVG(path);
        return image;
    }

    public void addImageViewHBox (String id, Card card, Pane hBox, int boxId){
        ImageView imageView = new ImageView();
        imageView.setId(id);

        imageView.setFitWidth(60);
        imageView.setFitHeight(150);
        imageView.setPreserveRatio(true);

        imageView.setSmooth(true);
        imageView.setCache(true);

        switch (boxId){
            case 1 -> imageView.setRotate(imageView.getRotate()+270);
            case 2 -> imageView.setRotate(imageView.getRotate()+180);
            case 3 -> imageView.setRotate(imageView.getRotate()+90);
        }

        hBox.getChildren().add(0, imageView);

        Image image = null;
        if (boxId == 0){
            imageView.setOnMouseClicked(event -> cardClicked(imageView));
            image = getCardFront(card);
        } else {
            image = getCardBack(card);
        }
        imageView.setImage(image);

        System.out.println(imageView.getId());
    }

    private void cardClicked(ImageView imageView) {
        // I am not sure if we should unclick every other card
        // when another is clicked. Game rules allow player
        // to select many cards at once.
        for(Node node : box1.getChildren()){
            node.getStyleClass().remove("clicked");
        }
        imageView.getStyleClass().add("clicked");

        int index=0;
        for(Card card : p1.getCards()){
            if(card.toString().equals(imageView.getId()))
                index = p1.getCards().indexOf(card);
        }
        System.out.println("Selected card: ");
        System.out.println(imageView.getId());
        System.out.println(index);

    }

    public void initGame(){
        gameModel.prepareCardDeck();
        gameModel.invitePlayers(p1,p2,p3,p4);
        gameModel.beginTheDeal();
    }

    public void onClickStart(){

        Pane[] hBoxes = new Pane[4];
        hBoxes [0] = box1;
        hBoxes [1] = box2;
        hBoxes [2] = box3;
        hBoxes [3] = box4;

        int j = 0;
        for (Player player : gameModel.getPlayers()) {
            for (Card card: player.getCards()) {
                addImageViewHBox(card.toString(), card, hBoxes[j], j);
            }
            j++;
        }

        startButton.setVisible(false);

        Image image = getCardBack(gameModel.getTopCardFromStock());
        deckImg.setImage(image);
        deckImg.setOnMouseClicked(event -> {cardDeal(); System.out.println("xd");});

        gameModel.putStarterOnPile();

        Image pileImage = getCardFront(gameModel.getTopCardFromPile());
        pileImg.setImage(pileImage);


    }

    private void cardDeal() {
        gameModel.setTurnPlayer(p1);
        int check = gameModel.dealCard();
        if(check == 0) {
            int pointer = p1.getCards().size() - 1;
            String suit = p1.getCards().get(pointer).getSuit().toString();
            String denomination = p1.getCards().get(pointer).getDenomination().toString();
            addImageViewHBox("p1" + denomination + suit, p1.getCards().get(pointer), box1, 0);
            // change back cover of card from the deck
            deckImg.setImage(getCardBack(gameModel.getTopCardFromStock()));
        } else {
            deckImg.setImage(null);
        }
    }

    public CrazyEightsController() {
    }

    @FXML
    void initialize() {
        initGame();
    }

    // change here if file naming convention changes
    private String getSVGCardResourcePath(Card card){
        StringBuilder stringBuilder = new StringBuilder();
        // directory in resources folder
        stringBuilder.append("/imagesSVG/");

        // every name consists of denomination shortname and suit shortname
        stringBuilder.append(card.getDenomination().getS());
        stringBuilder.append(card.getSuit().getS());
        stringBuilder.append(".svg");

        return stringBuilder.toString();
    }

    // SVG image should be set on ImageView in the last step,
    // after all operations like rotation and sizing are performed.
    // The nature of svg format force us to render it every time we need
    // to change its attributes.
    private Image getImageFromSVG(String path){
        Image image = null;

        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();

        try (InputStream file = getClass().getResourceAsStream(path)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                image = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }

        return image;
    }

}
