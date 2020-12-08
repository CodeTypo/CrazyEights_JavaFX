import java.net.URL;
import java.util.ResourceBundle;
import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.layout.HBox;
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
    private HBox box4;

    @FXML
    private HBox box2;

    @FXML
    private Button startButton;



    GameModel gameModel = new GameModel();
    Player p1 = new Player();
    Player p2 = new Player();
    Player p3 = new Player();
    Player p4 = new Player();


    public void addImageViewHBox (String id, String suit, String denomination,HBox hBox){
        ImageView imageView = new ImageView();
        imageView.setId(id);

        Image image = new Image("images/"+denomination+suit+".jpg");
        imageView.setImage(image);
        imageView.setFitWidth(60);
        imageView.setFitHeight(80);
        hBox.getChildren().add(0, imageView);
        System.out.println(imageView.getId());

    }

    public void start1(){
        gameModel.prepareCardDeck();
        gameModel.invitePlayers(p1,p2,p3,p4);
        gameModel.beginTheDeal();

    }

    public void onClickStart(){
        HBox [] hBoxes = new HBox[4];
        hBoxes [0] = box1;
        hBoxes [1] = box2;
        hBoxes [2] = box3;
        hBoxes [3] = box4;
        int j = 0;
        for (Player player : gameModel.getPlayers()) {
            for (int i = 0; i < player.getCards().size(); i++) {
                System.out.println(player.getCards().get(i).getDenomination().toString() + "" + player.getCards().get(i).getSuit().toString() + ".jpg");
                addImageViewHBox("p1" + i, player.getCards().get(i).getSuit().toString(), player.getCards().get(i).getDenomination().toString(), hBoxes[j]);
            }
            j++;
        }

        startButton.setVisible(false);
    }

    @FXML
    void initialize() {
        start1();
    }
}
