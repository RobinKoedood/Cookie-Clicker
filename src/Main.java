import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.*;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import Automatic.*;

import javafx.animation.AnimationTimer;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.scene.Node;
import javafx.scene.Scene;
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
    private static long cookieAmount = 0;
    private static double perSecond = 0.0;
    private static ResizableCanvas canvas;
    private static ArrayList<Automatic> automatics;
    private ArrayList<Kruimel> kruimels = new ArrayList<>(10);
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
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        cookie = new Cookie(Color.BLACK, new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));
        fxGraphics2D = new FXGraphics2D(canvas.getGraphicsContext2D());

        mainPane.setCenter(canvas);
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(event -> mouseReleased(event));
        canvas.setOnMouseMoved(event -> mouseMoved(event));


        labelAmount = new Label("Amount of cookies: " + cookieAmount);
        labelPerSecond = new Label("Per second : " + perSecond);
        labelInformation = new Label ();
        mainPane.setTop(getVboxAmounts());
        mainPane.setRight(getAutomatics());

        for (int i = 0; i < 10; i++) {
            kruimels.add(i, null);
        }

        Menu menu = new Menu("Game");
        MenuItem menuSave = new MenuItem("Save");
        MenuItem menuQuit = new MenuItem("Quit");

        menuQuit.setOnAction(event -> stop());

        menuSave.setOnAction(event -> save("savefile.txt") );

        menu.getItems().addAll(menuSave, menuQuit);

        MenuBar menuBar = new MenuBar();
        menuBar.getMenus().addAll(menu);

        VBox vBox = new VBox();
        vBox.getChildren().addAll(menuBar, mainPane);

        Scene scene = new Scene(vBox);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cookie Clicker");

        primaryStage.getIcons().add(new Image("favicon.png"));
        primaryStage.setResizable(false);
        primaryStage.show();
        draw(fxGraphics2D);


        new AnimationTimer() {
            long last = -1;

            @Override
            public void handle(long now) {
                if (last == -1) {
                    last = now;
                }
                update((now - last) / 1000000000.0);
                last = now;
                draw(fxGraphics2D);
            }
        }.start();
    }

    private void save( String filename ) {
        try (PrintWriter writer = new PrintWriter(new File(filename))){
             writer.println(cookieAmount);
             System.out.println(cookieAmount);

             writer.println(perSecond);
            System.out.println(perSecond);

            writer.println(autoCursor.getAmountOfAutoCursors());
            writer.println(grandma.getAmountOfGrandmas());
            writer.println(farm.getAmountOfFarms());
            writer.println(mine.getAmountOfMines());
            writer.println(factory.getAmountOfFactories());
            writer.println(bank.getAmountOfBanks());
            writer.println(lab.getAmountOfLabs());

             for (Automatic a : automatics) {
                 writer.println(a.getName());
                 System.out.println(a.getName());
             }


        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
    }

    public void draw(FXGraphics2D graphics) {

        graphics.setTransform(new AffineTransform());
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        graphics.drawImage(total, 0,0,(int) canvas.getWidth(), (int) canvas.getHeight(), null);
        cookie.setEllipse2D(new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));

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


        for (Kruimel k : kruimels) {
            if (k != null)
                k.draw(graphics);
        }
    }

    private void update(double deltaTime) {
        for (Automatic a : automatics) {
            cookieAmount += a.update();
        }


        try {
            Platform.runLater(() -> {
                        labelAmount.setText("Amount of cookies: " + cookieAmount);
                        labelPerSecond.setText("Per second: " + perSecond);

                    }
            );

        } catch (Exception e) {
            System.out.println(e);
        }

        for (Kruimel k : kruimels) {
            if (k != null)
            k.update(deltaTime);
        }
    }

    public void init() {
        automatics = new ArrayList<>();
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

            readSaveFile("savefile.txt");

            total = ImageIO.read(getClass().getResource("/bgBlue.png"));
        } catch (IOException e) {
            e.printStackTrace();
        } catch (UnsupportedAudioFileException e) {
            e.printStackTrace();
        } catch (LineUnavailableException e) {
            e.printStackTrace();
        }


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
            if (cookieAmount >= 25) {
                perSecond += autoCursor.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= autoCursor.getCost();
                autoCursor.addCursor();
                buttonCursor.setText( autoCursor.getName() + " +1 " + "Cost = " + autoCursor.getCost());
                automatics.add(autoCursor);
                labelInformation.setText("New Cursor added!" + " Amount of Cursors: " + autoCursor.getAmountOfAutoCursors());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });

        buttonGrandma.setOnAction(event -> {
            if (cookieAmount >= 100) {
                perSecond += grandma.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= grandma.getCost();
                grandma.addGrandma();
                buttonGrandma.setText( grandma.getName() + " +1 " + "Cost = " + grandma.getCost());
                automatics.add(grandma);
                labelInformation.setText("New Grandma added!" + " Amount of Grandma's: " + grandma.getAmountOfGrandmas());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }

        });

        buttonFarm.setOnAction(event -> {
            if (cookieAmount >= 250) {
                perSecond += farm.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= farm.getCost();
                farm.addFarm();
                buttonFarm.setText( farm.getName() + " +1 " + "Cost = " + farm.getCost());
                automatics.add(farm);
                labelInformation.setText("New farm added!" + " Amount of farms: " + farm.getAmountOfFarms());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });

        buttonMine.setOnAction(event -> {
            if (cookieAmount >= 1000) {
                perSecond += mine.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= mine.getCost();
                mine.addMine();
                buttonMine.setText( mine.getName() + " +1 " + "Cost = " + mine.getCost());
                automatics.add(mine);
                labelInformation.setText("New Mine added!" + " Amount of Mines: " + mine.getAmountOfMines());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });

        buttonFactory.setOnAction(event -> {
            if (cookieAmount >= 2500) {
                perSecond += factory.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= factory.getCost();
                factory.addFactory();
                buttonFactory.setText( factory.getName() + " +1 " + "Cost = " + factory.getCost());
                automatics.add(factory);
                labelInformation.setText("New Factory added!" + " Amount of Factories: " + factory.getAmountOfFactories());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });

        buttonBank.setOnAction(event -> {
            if (cookieAmount >= 5000) {
                perSecond += bank.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= bank.getCost();
                bank.addBank();
                buttonBank.setText( bank.getName() + " +1 " + "Cost = " + bank.getCost());
                automatics.add(bank);
                labelInformation.setText("New Bank added!" + " Amount of Banks: " + bank.getAmountOfBanks());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });

        buttonLab.setOnAction(event -> {
            if (cookieAmount >= 10000) {
                perSecond += lab.getMultiplication();
                perSecond = roundOf(perSecond);
                cookieAmount -= lab.getCost();
                lab.addLab();
                buttonLab.setText( lab.getName() + " +1 " + "Cost = " + lab.getCost());
                automatics.add(lab);
                labelInformation.setText("New Lab added!" + " Amount of Labs: " + lab.getAmountOfLabs());

                System.out.println("Amount of cookies: " + cookieAmount);
            } else {
                labelInformation.setText("Not enough cookies. Click more!!");
            }
        });
    }



    public boolean readSaveFile(String saveFile) {
        File file = new File(saveFile);
        if (file.exists()){
            try (Scanner reader = new Scanner(file)){
                if (reader.hasNextLine()){
                    cookieAmount = Long.parseLong(reader.nextLine());
                    perSecond = Double.parseDouble(reader.nextLine());
                    autoCursor.setAmountOfAutoCursors(Integer.parseInt(reader.nextLine()));
                    grandma.setAmountOfGrandmas(Integer.parseInt(reader.nextLine()));
                    farm.setAmountOfFarms(Integer.parseInt(reader.nextLine()));
                    mine.setAmountOfMines(Integer.parseInt(reader.nextLine()));
                    factory.setAmountOfFactories(Integer.parseInt(reader.nextLine()));
                    bank.setAmountOfBanks(Integer.parseInt(reader.nextLine()));
                    lab.setAmountOfLabs(Integer.parseInt(reader.nextLine()));

                    for (int i = 0; i < autoCursor.getAmountOfAutoCursors(); i++) {
                        automatics.add(new AutoCursor());
                    }
                    for (int i = 0; i < grandma.getAmountOfGrandmas(); i++) {
                        automatics.add(new Grandma());
                    }
                    for (int i = 0; i < farm.getAmountOfFarms(); i++) {
                        automatics.add(new Farm());
                    }
                    for (int i = 0; i < mine.getAmountOfMines(); i++) {
                        automatics.add(new Mine());
                    }
                    for (int i = 0; i < factory.getAmountOfFactories(); i++) {
                        automatics.add(new Factory());
                    }
                    for (int i = 0; i < bank.getAmountOfBanks(); i++) {
                        automatics.add(new Bank());
                    }
                    for (int i = 0; i < lab.getAmountOfLabs(); i++) {
                        automatics.add(new Lab());
                    }

                    /*
                    writer.println(autoCursor.getAmountOfAutoCursors());
                     writer.println(grandma.getAmountOfGrandmas());
                    writer.println(farm.getAmountOfFarms());
                    writer.println(mine.getAmountOfMines());
                    writer.println(factory.getAmountOfFactories());
                    writer.println(bank.getAmountOfBanks());
                   riter.println(lab.getAmountOfLabs());
                     */

                    while (reader.hasNextLine()){
                        String nextAutomatic = reader.nextLine();
                        if (nextAutomatic.equals("AutoCursor")){
                            AutoCursor autoCursor = new AutoCursor();
                            automatics.add(autoCursor);
                        }
                        else if (nextAutomatic.equals("Grandma")){
                            Grandma grandma = new Grandma();
                            automatics.add(grandma);
                        }
                        else if (nextAutomatic.equals("Farm")){
                            Farm farm = new Farm();
                            automatics.add(farm);
                        }
                        else if (nextAutomatic.equals("Mine")){
                            Mine mine = new Mine();
                            automatics.add(mine);
                        }
                        else if (nextAutomatic.equals("Bank")){
                            Bank bank = new Bank();
                            automatics.add(bank);
                        }
                        else if (nextAutomatic.equals("Lab")){
                            Lab lab = new Lab();
                            automatics.add(lab);
                        }
                        else if (nextAutomatic.equals("Factory")){
                            Factory factory = new Factory();
                            automatics.add(factory);
                        }

                    }
                }

            } catch (FileNotFoundException e) {
                e.printStackTrace();
            }
        } else {
            try {
                file.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

        return true;
    }


    private void mousePressed(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            cookieState = imageState.HELD;
            draw(fxGraphics2D);
            cookieAmount++;
        }
    }

    private void mouseReleased(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            for (int i = 0; i < 10; i++) {
                kruimels.set(i, new Kruimel(e.getX(), e.getY()));
            }

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
