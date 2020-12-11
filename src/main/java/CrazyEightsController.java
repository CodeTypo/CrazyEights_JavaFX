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


public class CrazyEightsController {

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Controller class fields ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~
    //A bunch of FXML objects initialization statements
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

    @FXML
    private ImageView suitSymbol;

    //The whole game flow is being controlled by this class
    GameModel gameModel = new GameModel();

    //Only p1 Player is interactive and controlled by user
    Player p1 = new Player();

    //These players can be created automatically because in init method
    // because we dont need reference to them
    Player p2 = new BotPlayer();
    Player p3 = new BotPlayer();
    Player p4 = new BotPlayer();

    public CrazyEightsController() {}

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Controller class fields ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~


    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    @FXML
    void initialize() {
        initGame();
    }

    public void initGame(){
        gameModel.prepareCardDeck();            //Prepares a fresh, brand new deck of cards
        gameModel.invitePlayers(p1,p2,p3,p4);   //Adds four players to the game
        gameModel.beginTheDeal();               //Starts dealing 8 cards to each player

        suitSymbol.setFitWidth(100);
        suitSymbol.setFitHeight(100);
        suitSymbol.setImage(Suit.CLUBS.getSymbol());

    }

    public void onClickStart(){
        //A method that executes when a big start button is being clicked by the user

        Pane[] Boxes = new Pane[]{box1,box2,box3,box4};   //A set of four boxes representing players hands is being created
        //Correct spacings between each card are being set
        box1.setSpacing(-60);
        box3.setSpacing(-60);
        box2.setSpacing(-100);
        box4.setSpacing(-100);

        int j = 0;
        for (Player player : gameModel.getPlayers()) {              //Each player that was invited to the gamemodel
            for (Card card: player.getCards()) {
                addImageViewToBox(card.toString(), card, Boxes[j]); //Gets a container for each card added to his box
            }
            j++;
        }

        startButton.setVisible(false);                              //A start button is being hidden

        Image image = Card.getCardBack(); //An image of deck laying on the table is being set
        deckImg.setFitWidth(100);                                   //Setting its max width
        deckImg.setFitHeight(120);                                  //Setting its max height
        deckImg.setImage(image);                                    //Filling the imageView with a card back image
        //Adding a onclick listener to the card laying on the top of the deck. While clicked, it executes cardDeal()
        deckImg.setOnMouseClicked(event -> {cardDeal(); System.out.println("deck clicked");});

        //Calling a GameModel putStarterOnPile() method, it puts the first card from the stock and puts it on the pile
        gameModel.putStarterOnPile();

        pileImg.setFitWidth(100);                                      //Setting its max width
        pileImg.setFitHeight(140);                                     //Setting its max height
        pileImg.setImage(gameModel.getTopCardFromPile().getCardFront());                                   //Filling the imageView with a card front image
    }

    public void addImageViewToBox (String id, Card card, Pane box){
        //A method being executed whenever a card needs to be added to a player's hand
        ImageView imageView = new ImageView();  //Creating a new ImageView
        imageView.setId(id);                    //Settings its Id to represent the card it depictures

        imageView.setFitWidth(100);              //Setting its max width
        imageView.setFitHeight(140);            //Setting its max height
        imageView.setPreserveRatio(true);       //Keeps the original value of the image
        imageView.setSmooth(true);              //Smoothens the image a litte bit
        imageView.setCache(true);               //Sets image caching boolean value to true

        String boxId = box.getId();             //Gets the id of the box the card should be put into
        switch (boxId){                         //Sets the appropriate imageview rotation depending on the box
            case "box2" -> imageView.setRotate(imageView.getRotate()+270);
            case "box3" -> imageView.setRotate(imageView.getRotate()+180);
            case "box4" -> imageView.setRotate(imageView.getRotate()+90);
        }

        box.getChildren().add(0, imageView);// Adds the image view to the box

        Image image;                              //A new Image object is being created
        if (boxId.equals("box1")){                //If the card is going to be added to a player box
            //Then the card front image is being set and an onClick listener method is being added to it making it interactive
            imageView.setOnMouseClicked(event -> cardClicked(card, imageView));
            image = card.getCardFront();
        } else {                                  //If the box belongs to one of the 3 bot players left
            image = Card.getCardBack();            //Then the card back image is being shown
        }

        imageView.setImage(image);                //Populates the ImageView with selected image
        //System.out.println(imageView.getId());    //outputs the image's id to the console, used to verify if everything is correct
    }

    public void render(){
        Pane[] Boxes = new Pane[]{box1,box2,box3,box4};   //A set of four boxes representing players hands is being created
        for (Pane pane : Boxes)
            pane.getChildren().clear();

        int j = 0;
        for (Player player : gameModel.getPlayers()) {              //Each player that was invited to the gamemodel
            for (Card card: player.getCards()) {
                addImageViewToBox(card.toString(), card, Boxes[j]); //Gets a container for each card added to his box
            }
            j++;
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    private void cardClicked(Card card, ImageView imageView) {
        //A method that is being added to all of the cardImages belonging to player, making them interactive
        //After the card is clicked, it is being checked if it was already selected or rather not
        if (card.isSelected()){
            //unselect card
            imageView.getStyleClass().remove("clicked"); //removes card css class that styles it as selected
            card.setSelected(false);                        //sets the card boolean "selected" value to false
            p1.unselectCard(card);                          //removes the card from player selected cards list
        } else {
            //select card if it agree with rules
            if (p1.selectCard(card)){                       //Checks if the game rules allow player to select this particular card
                card.setSelected(true);                     //sets the card boolean "selected" value to true
                imageView.getStyleClass().add("clicked");   //adds card css class that styles it as selected
            }
        }
        System.out.println(card.toString() + " selected: " + card.isSelected()); //An output statement to check if everything worked well
    }

    private void cardDeal() {
        gameModel.setTurnPlayer(p1);        //Usunąć w późniejszych wersjach !!!
        int check = gameModel.dealCard();                   //calls the gameModel dealcard() method, the method returns 0 if succedeed, 1 when failed
        if(check == 0) {                                    //if the card was drawn successfully
            int pointer = p1.getCards().size() - 1;         //a pointer to the top card of the pile is being set
            System.out.println(pointer);                    //a sout to check if everything is correct
            String suit = p1.getCards().get(pointer).getSuit().toString();                  //A cards suit
            String denomination = p1.getCards().get(pointer).getDenomination().toString();  //and denomination are saved
            addImageViewToBox( denomination + suit, p1.getCards().get(pointer), box1);   //Adds a card to a players box
            // change back cover of card from the deck
            deckImg.setImage(Card.getCardBack());//sets the stock image to the nex card's back   (Czy to jest potrzebne? Każda karta ma taki sam rewers)
        } else {
            deckImg.setImage(null);                         //If the method fails, an image is being removed, we are out of cards
        }
    }


    @FXML
    void onConfirmedClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "confirm" button
        gameModel.setTurnPlayer(p1);    // Usunąć w późniejszej wersji!!!!!!!!
        if (gameModel.playCards()) {    // GameModel playCards() method is being called
            render();
        }
        pileImg.setImage(gameModel.getTopCardFromPile().getCardFront()); //A card from the users box is being put on the pile
    }

    @FXML
    void onResetClicked(ActionEvent event) {
        // A method executing when the user clicks a GUI "reset" button
        for(Node node : box1.getChildren()){
            node.getStyleClass().remove("clicked"); //removes "clicked" styling from all of the players cards
            //Dodać tutaj usunięcie karty z listy Selected!!!!
        }
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

}//End of controller class file
