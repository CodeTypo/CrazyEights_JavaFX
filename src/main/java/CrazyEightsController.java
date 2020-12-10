import java.net.URL;
import java.util.ResourceBundle;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.*;
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
    //Only p1 Player is interactive and controlled by user
    Player p1 = new Player();

    //These players can be created automatically because in init method
    // because we dont need reference to them
    Player p2 = new BotPlayer();
    Player p3 = new BotPlayer();
    Player p4 = new BotPlayer();

    private Image getCardFront(Card card){
        String path = getSVGCardResourcePath(card);
        return getImageFromSVG(path);
    }

    private Image getCardBack(Card card){
        String path = "/imagesSVG2/";
        switch (card.getSuit()){
            case CLUBS, SPADES -> path += "1B.svg";
            case HEARTS,DIAMONDS -> path += "2B.svg";
        }
        return getImageFromSVG(path);
    }

    public void addImageViewToBox (String id, Card card, Pane hBox, int boxId){
        ImageView imageView = new ImageView();
        imageView.setId(id);

        imageView.setFitWidth(80);
        imageView.setFitHeight(100);
        imageView.setPreserveRatio(true);

        imageView.setSmooth(true);
        imageView.setCache(true);

        switch (boxId){
            case 1 -> imageView.setRotate(imageView.getRotate()+270);
            case 2 -> imageView.setRotate(imageView.getRotate()+180);
            case 3 -> imageView.setRotate(imageView.getRotate()+90);
        }

        hBox.getChildren().add(0, imageView);

        Image image;
        if (boxId == 0){
            imageView.setOnMouseClicked(event -> cardClicked(card, imageView));
            image = getCardFront(card);
        } else {
            image = getCardBack(card);
        }
        imageView.setImage(image);

        System.out.println(imageView.getId());
    }


    private void cardClicked(Card card, ImageView imageView) {
        if (card.isSelected()){
            //unselect card
            imageView.getStyleClass().remove("clicked");
            card.setSelected(false);
            p1.unselectCard(card);
        } else {
            //select card if it agree with rules
            if (p1.selectCard(card)){
                card.setSelected(true);
                imageView.getStyleClass().add("clicked");
            }

        }

        System.out.println(card.toString() + " selected: " + card.isSelected());
    }

    public void initGame(){
        gameModel.prepareCardDeck();
        gameModel.invitePlayers(p1,p2,p3,p4);
        gameModel.beginTheDeal();
    }

    public void onClickStart(){

        Pane[] Boxes = new Pane[4];
        Boxes [0] = box1;
        Boxes [1] = box2;
        Boxes [2] = box3;
        Boxes [3] = box4;

        box1.setSpacing(4);
        box3.setSpacing(4);
        box2.setSpacing(-24);
        box4.setSpacing(-24);

        int j = 0;
        for (Player player : gameModel.getPlayers()) {
            for (Card card: player.getCards()) {
                addImageViewToBox(card.toString(), card, Boxes[j], j);
            }
            j++;
        }

        startButton.setVisible(false);

        Image image = getCardBack(gameModel.getTopCardFromStock());
        deckImg.setFitWidth(100);
        deckImg.setFitHeight(120);
        deckImg.setImage(image);
        deckImg.setOnMouseClicked(event -> {cardDeal(); System.out.println("deck clicked");});

        gameModel.putStarterOnPile();

        Image pileImage = getCardFront(gameModel.getTopCardFromPile());
        pileImg.setFitWidth(100);
        pileImg.setFitHeight(120);
        pileImg.setImage(pileImage);
    }

    private void cardDeal() {
        gameModel.setTurnPlayer(p1);
        int check = gameModel.dealCard();
        if(check == 0) {
            int pointer = p1.getCards().size() - 1;
            System.out.println(pointer);
            String suit = p1.getCards().get(pointer).getSuit().toString();
            String denomination = p1.getCards().get(pointer).getDenomination().toString();
            addImageViewToBox( denomination + suit, p1.getCards().get(pointer), box1, 0);
            // change back cover of card from the deck
            deckImg.setImage(getCardBack(gameModel.getTopCardFromStock()));
        } else {
            deckImg.setImage(null);
        }
    }


    @FXML
    void onConfirmedClicked(ActionEvent event) {
        gameModel.setTurnPlayer(p1);
        gameModel.playCards();
        pileImg.setImage(getCardFront(gameModel.getTopCardFromPile()));
    }

    @FXML
    void onResetClicked(ActionEvent event) {
        for(Node node : box1.getChildren()){
            node.getStyleClass().remove("clicked");
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
        // directory in resources folder

        return "/imagesSVG2/" +

                // every name consists of denomination shortname and suit shortname
                card.getDenomination().getS() +
                card.getSuit().getS() +
                ".svg";
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
