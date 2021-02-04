import javafx.animation.TranslateTransition;
import javafx.collections.ListChangeListener;
import javafx.collections.transformation.FilteredList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.fxml.FXML;
import javafx.geometry.Bounds;
import javafx.scene.Node;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.DialogPane;
import javafx.scene.image.ImageView;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.layout.VBox;
import javafx.scene.shape.Rectangle;
import javafx.stage.Stage;
import javafx.util.Duration;

import java.net.URL;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.ResourceBundle;

public class CrazyEightsReactiveController {

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
    private Button confirmButton;

    @FXML
    private ImageView pileImg;

    @FXML
    private ImageView deckImg;

    @FXML
    private ImageView suitSymbol;

    @FXML
    private HBox hBoxsOfSuits;

    @FXML
    private ImageView spadesIV;

    @FXML
    private ImageView clubsIV;

    @FXML
    private Button passButton;

    @FXML
    private ImageView heartsIV;

    @FXML
    private ImageView diamondsIV;

    @FXML
    private AnchorPane table;

    @FXML
    private Button exitButton;

    /**
     * The whole game flow is being controlled by this class.
     */
    GameModelReactive gameModel = new GameModelReactive();

    /**
     * Only p1 Player is interactive and controlled by user.
     */
    InteractivePlayerReactive player;

    /**
     * Map player hands to panes
     */
    Map<PlayerReactive, Pane> hands = new HashMap<>();

    public CrazyEightsReactiveController() {
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Controller class fields ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * Prepares a suit selector
     */
    public void initSuitSymbolSelector() {
        suitSymbol.setFitWidth(75);
        suitSymbol.setFitHeight(75);
        suitSymbol.setImage(Suit.SPADES.getSymbol());

        clubsIV.setImage(Suit.CLUBS.getSymbol());
        diamondsIV.setImage(Suit.DIAMONDS.getSymbol());
        heartsIV.setImage(Suit.HEARTS.getSymbol());
        spadesIV.setImage(Suit.SPADES.getSymbol());
    }

    /**
     * adds a card to hand
     * @param card represents the card the player wants to add
     * @param playerReactive represents the player who want to take the card
     */
    public void addCardToHand(CardReactive card, PlayerReactive playerReactive) {
        Pane pane = hands.get(playerReactive);
        ImageView imageView = createCardView(card, pane);
        setupCardView(card, imageView, playerReactive);
        pane.getChildren().add(imageView);
        animateCardDeal(deckImg, imageView);
    }


    /**
     * removes the card from hand
     * @param card represents the card the player wants to remove
     * @param playerReactive represents the player who want to remove the card
     */
    public void removeCardFromHand(CardReactive card, PlayerReactive playerReactive) {
        Pane pane = hands.get(playerReactive);
        FilteredList<Node> toRemove = pane.getChildren().filtered(node -> node.getId().equals(card.getId()));
        Node node = toRemove.get(0);

        animateCardRemove(node, pileImg);

        pane.getChildren().remove(toRemove.get(0));
    }

    /**
     * Called to animate when a card is added to hand
     * @param from the Node FROM which the card will be animated
     * @param to the Node where the card will be
     */
    private void animateCardDeal(Node from, Node to) {
        to.setVisible(false);
        ImageView animationIV = new ImageView();
        animationIV.setImage(CardReactive.getCardBack());
        table.getChildren().add(animationIV);

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), animationIV);
        transition.setFromX(deckImg.getLayoutX());
        transition.setFromY(deckImg.getLayoutY());

        Bounds boundsInScene = to.localToScene(to.getBoundsInLocal());

        transition.setToX(boundsInScene.getMinX() - boundsInScene.getWidth());
        transition.setToY(boundsInScene.getMinY() - boundsInScene.getHeight());

        transition.play();
        transition.setOnFinished(event -> {
            table.getChildren().remove(animationIV);
            to.setVisible(true);
        });
    }

    /**
     * Called to animate when a card is removed from hand
     * @param from the Node FROM which the card will be animated
     * @param to the Node where the card will be
     */
    private void animateCardRemove(Node from, Node to) {
        ImageView animationIV = new ImageView();

        animationIV.setImage(gameModel.getTopCardFromPile().getCardFront());
        table.getChildren().add(animationIV);

        Bounds fromBoundsInScene = from.localToScene(from.getBoundsInLocal());

        TranslateTransition transition = new TranslateTransition(Duration.seconds(0.5), animationIV);

        transition.setFromX(fromBoundsInScene.getMinX() - 2 * fromBoundsInScene.getWidth());
        transition.setFromY(fromBoundsInScene.getMinY() - fromBoundsInScene.getHeight());


        transition.setToX(to.getLayoutX());
        transition.setToY(to.getLayoutY());

        transition.play();
        transition.setOnFinished(event -> table.getChildren().remove(animationIV));

    }

    /**
     * Creates a ImageView for every card we want to show
     * @param card represents the card to show
     * @param pane represents the pane where the card should be
     * @return the ImageView of the card
     */
    public ImageView createCardView(CardReactive card, Pane pane) {
        ImageView imageView = new ImageView();

        imageView.setId(card.getId());

        imageView.setFitWidth(100);
        imageView.setFitHeight(140);
        imageView.setPreserveRatio(true);
        imageView.setSmooth(true);
        imageView.setCache(true);

        if (pane instanceof VBox) {
            imageView.setRotate(imageView.getRotate() + 90);
        }

        return imageView;
    }

    /**
     * We want to see the front of our cards and the back of bot cards
     * @param card represents card we want to show / don't want to show
     * @param imageView represents the place where the card will be showed
     * @param playerReactive represents the playerReactive
     *
     */
    public void setupCardView(CardReactive card, ImageView imageView, PlayerReactive playerReactive) {
        if (playerReactive == this.player) {
            imageView.setOnMouseClicked(event -> onCardClicked(card, imageView));
            imageView.getStyleClass().add("playerCard");
            imageView.setImage(card.getCardFront());
        } else {
            imageView.setImage(CardReactive.getCardBack());
        }
    }

    /**
     * Called only once at the beginning when we need to update whole cards at once.
     * After game is started, cards are updated individually
     */
    public void addCardsToHands() {

        hands.forEach((playerReactive, pane) -> {

            if (pane instanceof HBox) {
                ((HBox) pane).setSpacing(-60);
            } else if (pane instanceof VBox) {
                ((VBox) pane).setSpacing(-100);
            }

            for (CardReactive card : playerReactive.getCards()) {
                ImageView imageView = createCardView(card, pane);
                setupCardView(card, imageView, playerReactive);
                pane.getChildren().add(imageView);
            }
        });
    }

    /**
     * Prepares the deck
     */
    public void initDeckImage() {
        deckImg.setFitWidth(100);
        deckImg.setFitHeight(140);
        deckImg.setImage(CardReactive.getCardBack());

        deckImg.setOnMouseClicked(event -> {
            if (gameModel.getTurnPlayer() == this.player) {
                onCardDealt();
            }
        });
    }

    /**
     * after pressed start, cards are added to the hand of each player
     */
    public void onStartClick() {
        addCardsToHands();
        gameModel.putStarterOnPile();
        startButton.setVisible(false);

        initDeckImage();
        suitSymbol.setVisible(true);
        suitSymbol.setRotate(suitSymbol.getRotate() + 180);
        passButton.setVisible(true);
        confirmButton.setVisible(true);
        exitButton.setVisible(true);
    }

    /**
     * When player puts EIGHT on the table he must to select a suit
     * @param event used for get source of which suit was selected
     */
    @FXML
    void onSelectSuitClick(Event event) {
        ImageView object = (ImageView) event.getSource();
        switch (object.getId()) {
            case "spadesIV" -> gameModel.setSuit(Suit.SPADES);
            case "diamondsIV" -> gameModel.setSuit(Suit.DIAMONDS);
            case "heartsIV" -> gameModel.setSuit(Suit.HEARTS);
            case "clubsIV" -> gameModel.setSuit(Suit.CLUBS);
        }

        hBoxsOfSuits.setVisible(false);
        gameModel.nextPlayerTurn();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Preparing the game ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * if a player clicks on a card, it is selected and added to his selected cards
     * @param card represents the clicked card
     * @param imageView represents the imageView of this card
     */
    private void onCardClicked(CardReactive card, ImageView imageView) {
        if (card.isSelected()) {
            imageView.getStyleClass().remove("clicked");
            imageView.getStyleClass().add("playerCard");
            player.unselectCard(card);
        } else {
            player.selectCard(card);
            if (card.isSelected()) {
                imageView.getStyleClass().remove("playerCard");
                imageView.getStyleClass().add("clicked");
            }

        }
        System.out.println(card.toString() + " selected: " + card.isSelected());
    }


    /**
     * is used to get the top card from the stock
     */
    private void onCardDealt() {
        player.dealCard(gameModel.takeTopCardFromStock());
    }

    /**
     * when a player clicked the confirm button the selected cards are added to the stock
     * if the list of selected cards is empty, an alert is shown
     */
    @FXML
    void onConfirmedClicked(ActionEvent event) {
        if (gameModel.getInteractivePlayer().getSelectedCards().size() == 0) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setContentText("Please select a card!");
            alert.show();
        } else if (gameModel.playCards()) {
            hBoxsOfSuits.setVisible(true);
        }
    }

    /**
     * when a player clicked the pass button the turn is passed on to the next player
     */
    @FXML
    void onPassClicked(ActionEvent event) {
        gameModel.nextPlayerTurn();
    }

    //~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~ /Handling interactions ~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~~

    /**
     * the whole game is prepared here
     * the listeners are added
     */
    @FXML
    void initialize() {

        pileImg.setFitWidth(100);
        pileImg.setFitHeight(140);
        suitSymbol.setVisible(false);


        Rectangle clip = new Rectangle(
                suitSymbol.getFitWidth(), suitSymbol.getFitHeight()
        );

        clip.setArcWidth(45);
        clip.setArcHeight(45);
        suitSymbol.setClip(clip);

        passButton.setVisible(false);
        confirmButton.setVisible(false);
        exitButton.setVisible(false);
        hBoxsOfSuits.setRotate(hBoxsOfSuits.getRotate() + 180);


        gameModel.getPile().addListener((ListChangeListener<? super CardReactive>) c -> {
            System.out.println("Pile changed");
            while (c.next()) {
                if (c.wasAdded()) {
                    pileImg.setImage(gameModel.getTopCardFromPile().getCardFront());
                    gameModel.setSuit(gameModel.getTopCardFromPile().getSuit());
                }
            }
        });

        gameModel.getStock().addListener((ListChangeListener<? super CardReactive>) c -> {
            while (c.next()) {
                if (c.wasRemoved()) {
                    if (c.getList().size() == 0) {
                        deckImg.setImage(null);
                        deckImg.setDisable(true);
                    }
                }
            }
        });

        gameModel.turnPlayerProperty().addListener((observable, oldTurnPlayer, turnPlayer) -> {
            System.out.println("TurnPlayer changed from: " + oldTurnPlayer + " to: " + turnPlayer);
            confirmButton.setDisable(turnPlayer != this.player);
        });

        gameModel.suitProperty().addListener((observable, oldSuit, newSuit) -> {
            System.out.println("Suit changed from: " + oldSuit + " to: " + newSuit);
            suitSymbol.setImage(gameModel.getSuit().getSymbol());
        });

        gameModel.init();
        this.player = gameModel.getInteractivePlayer();

        List<BotPlayerReactive> bots = gameModel.getBotPlayers();

        hands.put(this.player, box1);
        hands.put(bots.get(0), box2);
        hands.put(bots.get(1), box3);
        hands.put(bots.get(2), box4);

        gameModel.beginTheDeal();

        bots.forEach(bot -> {
            bot.getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
                System.out.println(bot + " cards changed");
                while (c.next()) {
                    if (c.wasAdded()) {
                        List<CardReactive> addedCards = (List<CardReactive>) c.getAddedSubList();
                        addedCards.forEach(cardReactive -> {
                            addCardToHand(cardReactive, bot);
                            System.out.println(bot + ": card was added:" + cardReactive);
                        });
                    }

                    if (c.wasRemoved()) {
                        System.out.println(bot + " card was removed");
                        List<CardReactive> removedCards = (List<CardReactive>) c.getRemoved();
                        removedCards.forEach(cardReactive -> {
                            removeCardFromHand(cardReactive, bot);
                            System.out.println(bot + ": card was removed:" + cardReactive);
                        });

                        if (c.getList().isEmpty()) {
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            DialogPane dialogPane = alert.getDialogPane();
                            dialogPane.setHeader(null);
                            dialogPane.setHeaderText(null);
                            dialogPane.setGraphic(null);
                            dialogPane.getStylesheets().add(
                                    getClass().getResource("myDialog.css").toExternalForm());
                            dialogPane.getStyleClass().add("myDialog");
                            alert.setContentText("Johnny is the winner!");

                            alert.show();

                            Stage stage = (Stage) exitButton.getScene().getWindow();
                            stage.close();
                        }

                    }

                }
            });
        });

        player.getCards().addListener((ListChangeListener<? super CardReactive>) c -> {
            System.out.println(player + " cards changed");
            while (c.next()) {
                if (c.wasAdded()) {
                    List<CardReactive> addedCards = (List<CardReactive>) c.getAddedSubList();
                    addedCards.forEach(cardReactive -> {
                        addCardToHand(cardReactive, player);
                        System.out.println(player + ": card was added:" + cardReactive);
                    });

                }

                if (c.wasRemoved()) {
                    List<CardReactive> removedCards = (List<CardReactive>) c.getRemoved();
                    removedCards.forEach(cardReactive -> {
                        removeCardFromHand(cardReactive, player);
                        System.out.println(player + ": card was removed:" + cardReactive);
                    });

                    if (c.getList().isEmpty()) {
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        DialogPane dialogPane = alert.getDialogPane();
                        dialogPane.setHeader(null);
                        dialogPane.setHeaderText(null);
                        dialogPane.setGraphic(null);
                        dialogPane.getStylesheets().add(
                                getClass().getResource("myDialog.css").toExternalForm());
                        dialogPane.getStyleClass().add("myDialog");
                        alert.setContentText("You are the winner!");

                        alert.show();

                        Stage stage = (Stage) exitButton.getScene().getWindow();
                        stage.close();
                    }
                }

            }
        });


        System.out.println(gameModel.getTurnPlayer());

        initSuitSymbolSelector();

        gameModel.setupBots();

        gameModel.setTurnPlayer(this.player);
    }

    /**
     * when the player clicked exit button the the game window is closed
     */
    public void onExitClicked(ActionEvent actionEvent) {
        Stage stage = (Stage) exitButton.getScene().getWindow();
        stage.close();
    }

}