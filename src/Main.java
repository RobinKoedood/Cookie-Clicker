import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Timer;
import java.util.TimerTask;

import Automatic.*;

import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.Menu;
import javafx.scene.control.MenuBar;
import javafx.scene.control.MenuItem;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;
import javax.sound.sampled.*;

public class Main extends Application {
    private static long cookieAnoumt = 0;
    private static double perSecond = 0.0;
    private static ResizableCanvas canvas;
    private static ArrayList<Automatic> automatics;
    private Cookie cookie = null;

    private Label labelAmount;
    private Label labelPerSecond;
    private Label labelInformation;

    private Button buttonCursor;
    private Button buttonGrandma;
    private Button buttonFarm;
    private Button buttonMine;
    private Button buttonFactory;
    private Button buttonBank;
    private Button buttonLab;

    private imageState cookieState = Main.imageState.IDLE;

    public enum imageState {IDLE, HOVER, HELD}
    private BufferedImage total = null;
    private BufferedImage imageCursorButton = null;
    private FXGraphics2D fxGraphics2D;

    private AutoCursor autoCursor = new AutoCursor();
    private Grandma grandma = new Grandma();
    private Farm farm = new Farm();
    private Mine mine = new Mine();
    private Factory factory = new Factory();
    private Bank bank = new Bank();
    private Lab lab = new Lab();

    public static void main(String[] args) {
        launch(Main.class);
    }

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");
        automatics = new ArrayList<>();
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> onResize(g), mainPane);
        cookie = new Cookie(Color.BLACK, new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));
        fxGraphics2D = new FXGraphics2D(canvas.getGraphicsContext2D());

        mainPane.setCenter(canvas);
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(event -> mouseReleased(event));
        canvas.setOnMouseMoved(event -> mouseMoved(event));


        labelAmount = new Label("Amount of cookies: " + cookieAnoumt);
        labelPerSecond = new Label("Per second : " + perSecond);
        labelInformation = new Label ();
        mainPane.setTop(getVboxAmounts());
        mainPane.setRight(getAutomatics());

        Menu menu = new Menu("Game");
        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuQuit = new MenuItem("Quit");

        menuQuit.setOnAction(event -> stop());

        menuSave.setOnAction(event -> save("savefile.txt"));

        menu.getItems().addAll(menuSave, menuQuit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, mainPane);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cookie Clicker");

        primaryStage.getIcons().add(new Image("favicon.png"));
        primaryStage.show();
        draw(fxGraphics2D);


        Timer timerCursor = new Timer();
        timerCursor.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!automatics.isEmpty()) {
                    for (Automatic automatic : automatics) {
                        cookieAnoumt += automatic.update();
                    }
                }
                try {
                    Thread.sleep(1);
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }
                update();
            }
        }, 1, 10);
    }

    public void draw(FXGraphics2D graphics) {
        update();

        graphics.setTransform(new AffineTransform());
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());


        switch (cookieState) {
            case IDLE:
                graphics.drawImage(cookie.getImageIdle(), (int) canvas.getWidth() / 2 - 100, (int) canvas.getHeight() / 2 - 100, 200, 200, null);
                break;
            case HOVER:
                graphics.drawImage(cookie.getImageHover(), (int) canvas.getWidth() / 2 - 100, (int) canvas.getHeight() / 2 - 100, 200, 200, null);
                break;
            case HELD:
                graphics.drawImage(cookie.getImageHeld(), (int) canvas.getWidth() / 2 - 100, (int) canvas.getHeight() / 2 - 100, 200, 200, null);
                break;
        }
    }
    private void update() {
        try {
            Platform.runLater(() -> {
                        labelAmount.setText("Amount of cookies: " + cookieAnoumt);
                        labelPerSecond.setText("Per second: " + perSecond);

                    }
            );

        } catch (Exception e) {
            System.out.println(e);
        }
    }

    public void init() {
        try {
            AudioInputStream audioIn =
                    AudioSystem.getAudioInputStream (getClass().getResource("/sound/music.wav")); //
            // inputting sound
            Clip clip = AudioSystem.getClip(); // inputting sound
            clip.open (audioIn); // inputting sound
            FloatControl gainControl =
                    (FloatControl) clip.getControl(FloatControl.Type.MASTER_GAIN);
            gainControl.setValue(-20.0f); // Reduce volume by 20 decibels.
            clip.start();
            clip.loop(100);

            readSaveFile("savefile");

            total = ImageIO.read(getClass().getResource("/bgBlue.png"));
            imageCursorButton = ImageIO.read(getClass().getResource("/cursorButton.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }


    }

    private void onResize(FXGraphics2D g) {
        g.setPaint(new TexturePaint(total, new Rectangle2D.Double(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight())));
        cookie.setEllipse2D(new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));
        draw(g);
    }

    public void stop() {
        System.exit(0);
    }


    private Node getVboxAmounts() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelAmount, labelPerSecond, labelInformation);

        return vBox;
    }

    private Node getAutomatics() {
        VBox vBox = new VBox();

        buttonCursor = new Button(autoCursor.getName() + " +1" + " Cost = " + autoCursor.getCost());
        buttonGrandma = new Button( grandma.getName() +  " +1" + " Cost = " + grandma.getCost());
        buttonFarm = new Button( farm.getName() + " +1" + " Cost = " + farm.getCost());
        buttonMine = new Button( mine.getName() +  " +1" + " Cost = " + mine.getCost());
        buttonFactory = new Button( factory.getName() +  " +1" + " Cost = " + factory.getCost());
        buttonBank = new Button( bank.getName() +  " +1" + " Cost = " + bank.getCost());
        buttonLab = new Button( lab.getName() +  " +1" + " Cost = " + lab.getCost());

        vBox.getChildren().addAll(buttonCursor, buttonGrandma, buttonFarm, buttonMine, buttonFactory, buttonBank, buttonLab);

        getButtonLogics();

        return vBox;
    }

    private void getButtonLogics() {
        buttonCursor.setOnAction(event -> {
            if (cookieAnoumt >= 25) {
                perSecond += autoCursor.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= autoCursor.getCost();
                autoCursor.addCursor();
                buttonCursor.setText( autoCursor.getName() + " +1 " + "Cost = " + autoCursor.getCost());
                automatics.add(autoCursor);
                labelInformation.setText("New Cursor added!" + " Amount of Cursors: " + autoCursor.getAmountOfAutoCursors());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonGrandma.setOnAction(event -> {
            if (cookieAnoumt >= 100) {
                perSecond += grandma.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= grandma.getCost();
                grandma.addGrandma();
                buttonGrandma.setText( grandma.getName() + " +1 " + "Cost = " + grandma.getCost());
                automatics.add(grandma);
                labelInformation.setText("New Grandma added!" + " Amount of Grandma's: " + grandma.getAmountOfGrandmas());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonFarm.setOnAction(event -> {
            if (cookieAnoumt >= 250) {
                perSecond += farm.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= farm.getCost();
                farm.addFarm();
                buttonFarm.setText( farm.getName() + " +1 " + "Cost = " + farm.getCost());
                automatics.add(farm);
                labelInformation.setText("New farm added!" + " Amount of farms: " + farm.getAmountOfFarms());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonMine.setOnAction(event -> {
            if (cookieAnoumt >= 1000) {
                perSecond += mine.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= mine.getCost();
                mine.addMine();
                buttonMine.setText( mine.getName() + " +1 " + "Cost = " + mine.getCost());
                automatics.add(mine);
                labelInformation.setText("New Mine added!" + " Amount of Mines: " + mine.getAmountOfMines());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonFactory.setOnAction(event -> {
            if (cookieAnoumt >= 2500) {
                perSecond += factory.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= factory.getCost();
                factory.addFactory();
                buttonFactory.setText( factory.getName() + " +1 " + "Cost = " + factory.getCost());
                automatics.add(factory);
                labelInformation.setText("New Factory added!" + " Amount of Factories: " + factory.getAmountOfFactories());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonBank.setOnAction(event -> {
            if (cookieAnoumt >= 5000) {
                perSecond += bank.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= bank.getCost();
                bank.addBank();
                buttonBank.setText( bank.getName() + " +1 " + "Cost = " + bank.getCost());
                automatics.add(bank);
                labelInformation.setText("New Bank added!" + " Amount of Banks: " + bank.getAmountOfBanks());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });

        buttonLab.setOnAction(event -> {
            if (cookieAnoumt >= 10000) {
                perSecond += lab.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAnoumt -= lab.getCost();
                lab.addLab();
                buttonLab.setText( lab.getName() + " +1 " + "Cost = " + lab.getCost());
                automatics.add(lab);
                labelInformation.setText("New Lab added!" + " Amount of Labs: " + lab.getAmountOfLabs());

                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
            update();
        });
    }

    public void save(String itemfile){

    }

    public static Automatic readSaveFile(String infile) {

        return null;
    }


    private void mousePressed(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            cookieState = imageState.HELD;
            draw(fxGraphics2D);
            cookieAnoumt++;
        }
        update();
    }

    private void mouseReleased(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            cookieState = imageState.HOVER;
        } else {
            cookieState = imageState.IDLE;
        }
        draw(fxGraphics2D);
    }

    private void mouseMoved(MouseEvent event) {
        if (cookie.getEllipse2D().contains(event.getX(), event.getY())) {
            cookieState = imageState.HOVER;
        } else {
            cookieState = imageState.IDLE;
        }
        draw(fxGraphics2D);
    }

    private double roundOf(double perSecond) {
        perSecond *= 10;
        perSecond = Math.round(perSecond);
        perSecond /= 10;

        return perSecond;
    }


}
