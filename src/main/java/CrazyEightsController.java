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


    public void addImageViewHBox (String id, String suit, String denomination,Pane hBox,int boxId){
        ImageView imageView = new ImageView();
        imageView.setId(id);

        Image image = new Image("imagesPNG/"+denomination+suit+".png");
        imageView.setImage(image);
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
        System.out.println(imageView.getId());

    }

    public void start1(){
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
        int playerNumber  = 1;
        for (Player player : gameModel.getPlayers()) {
            for (int i = 0; i < player.getCards().size(); i++) {
                //System.out.println(player.getCards().get(i).getDenomination().toString() + "" + player.getCards().get(i).getSuit().toString() + ".jpg");
                addImageViewHBox("p" + playerNumber + "card" + (i + 1),
                            player.getCards().get(i).getSuit().toString(),
                            player.getCards().get(i).getDenomination().toString(),
                            hBoxes[j],
                            j);
            }
            playerNumber++;
            j++;

        }
        startButton.setVisible(false);
        Image image = new Image("imagesPNG/green_back.png");
        deckImg.setImage(image);
        gameModel.putStarterOnPile();
        String denomination = gameModel.getPile().get(0).getDenomination().toString();
        String suit =  gameModel.getPile().get(0).getSuit().toString();
        Image pileImage = new Image("imagesPNG/"+denomination+suit+".png");
        pileImg.setImage(pileImage);
    }

    @FXML
    void initialize() {
        start1();
    }
}
