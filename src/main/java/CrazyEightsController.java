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

    public CrazyEightsController() {
    }

    // SVG image should be set on ImageView in the last step,
    // after all operations like rotation and sizing are performed.
    // The nature of svg format force us to render it every time we need
    // to change its attributes.
    private void setCardOnImageView(String path, ImageView imageView){
        BufferedImageTranscoder transcoder = new BufferedImageTranscoder();
        try (InputStream file = getClass().getResourceAsStream(path)) {
            TranscoderInput transIn = new TranscoderInput(file);
            try {
                transcoder.transcode(transIn, null);
                Image img = SwingFXUtils.toFXImage(transcoder.getBufferedImage(), null);
                imageView.setImage(img);
            } catch (TranscoderException ex) {
                ex.printStackTrace();
            }
        }
        catch (IOException io) {
            io.printStackTrace();
        }
    }

    public void addImageViewHBox (String id, String suit, String denomination,Pane hBox,int boxId){
        ImageView imageView = new ImageView();
        imageView.setId(id);

        String imagePath = "";
        if(boxId == 0) {
             imagePath = "/imagesSVG/2H.svg";
        } else {
            imagePath = "imagesSVG/1B.svg";
        }

        imageView.setFitWidth(60);
        imageView.setFitHeight(150);

        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        if(boxId ==0)
            imageView.setOnMouseClicked(event -> cardClicked(imageView));

        switch (boxId){
            case 1 -> imageView.setRotate(imageView.getRotate()+270);
            case 2 -> imageView.setRotate(imageView.getRotate()+180);
            case 3 -> imageView.setRotate(imageView.getRotate()+90);
        }

        hBox.getChildren().add(0, imageView);

        setCardOnImageView(imagePath, imageView);
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

//        for (Player player : gameModel.getPlayers()) {
//            for (Card card: player.getAllCards()){
//
//            }
//        }


        int j = 0;
        int playerNumber  = 1;
        for (Player player : gameModel.getPlayers()) {
            for (int i = 0; i < player.getCards().size(); i++) {
                addImageViewHBox( player.getCards().get(i).getDenomination().toString() + "" + player.getCards().get(i).getSuit().toString(),
                            player.getCards().get(i).getSuit().toString(),
                            player.getCards().get(i).getDenomination().toString(),
                            hBoxes[j],
                            j);
            }
            playerNumber++;
            j++;
        }


        startButton.setVisible(false);
        Image image = new Image("imagesPNG/purple_back.png");
        deckImg.setImage(image);
        gameModel.putStarterOnPile();
        String denomination = gameModel.getPile().get(0).getDenomination().toString();
        String suit =  gameModel.getPile().get(0).getSuit().toString();
        Image pileImage = new Image("imagesPNG/"+denomination+suit+".png");
        pileImg.setImage(pileImage);
        deckImg.setOnMouseClicked(event -> {cardDeal(); System.out.println("xd");});

    }

    private void cardDeal() {
        gameModel.setTurnPlayer(p1);
        int check = gameModel.dealCard();
        if(check == 0) {
            int pointer = p1.getCards().size() - 1;
            String suit = p1.getCards().get(pointer).getSuit().toString();
            String denomination = p1.getCards().get(pointer).getDenomination().toString();
            addImageViewHBox("p1" + denomination + suit, suit, denomination, box1, 0);
        } else {
            deckImg.setImage(null);
        }
    }

    @FXML
    void initialize() {
        initGame();
    }
}
