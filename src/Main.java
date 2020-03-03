
import java.awt.*;
import java.awt.geom.*;
import java.awt.image.BufferedImage;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Scanner;
import java.util.Timer;
import java.util.TimerTask;

import Automatic.AutoCursor;
import Automatic.Automatic;
import Automatic.Grandma;
import Automatic.Farm;
import javafx.application.Application;

import static javafx.application.Application.launch;

import javafx.application.Platform;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.image.Image;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.shape.Circle;
import javafx.stage.Stage;
import org.jfree.fx.FXGraphics2D;
import org.jfree.fx.ResizableCanvas;

import javax.imageio.ImageIO;

public class Main extends Application {
    private static int cookieAnoumt = 0;
    private static double perSecond = 0.0;
    private static ResizableCanvas canvas;
    private static ArrayList<Automatic> automatics;
    private ArrayList<Cookie> cookies;
    private Cookie cookie = null;
    private Scanner reader = new Scanner(System.in);

    private Label labelAmount;
    private Label labelPerSecond;

    private int amountOfCursors;
    private int amountOfGrandmas;
    private int amountOfFarms;

    private double multiplicationCursor;

    private Button buttonCursor;
    private Button buttonGrandma;
    private Button buttonFarm;

    private imageState cookieState = Main.imageState.IDLE;

    public enum imageState {IDLE, HOVER, HELD}

    @Override
    public void start(Stage primaryStage) {
        System.out.println("start");
        automatics = new ArrayList<>();
        BorderPane mainPane = new BorderPane();
        canvas = new ResizableCanvas(g -> draw(g), mainPane);
        cookie = new Cookie(Color.BLACK, new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));

        mainPane.setCenter(canvas);
        canvas.setOnMousePressed(e -> mousePressed(e));
        canvas.setOnMouseReleased(event -> mouseReleased(event));
        canvas.setOnMouseMoved(event -> mouseMoved(event));


        labelAmount = new Label("Amount of cookies: " + cookieAnoumt);
        labelPerSecond = new Label("Per second : " + perSecond);
        mainPane.setTop(getVboxAmounts());
        mainPane.setRight(getAutomatics());


        Scene scene = new Scene(mainPane);
        primaryStage.setScene(scene);
        primaryStage.setTitle("Cookie Clicker");

        primaryStage.getIcons().add(new Image("favicon.png"));
        primaryStage.show();
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));


        Timer timerCursor = new Timer();
        timerCursor.schedule(new TimerTask() {
            @Override
            public void run() {
                if (!automatics.isEmpty()) {
                    for (Automatic automatic : automatics) {
                        cookieAnoumt += automatic.update();
                    }
                }
                updateDisplay();
                System.out.println("Amount of cookies: " + cookieAnoumt);
                System.out.println("Per second: " + perSecond);
//                if (reader.hasNextLine()) {
//                    cookieAnoumt = Integer.parseInt(reader.nextLine());
//                }

            }
        }, 1, 1000);
    }

    public void stop() {
        System.exit(0);
    }


    private Node getVboxAmounts() {
        VBox vBox = new VBox();
        vBox.getChildren().addAll(labelAmount, labelPerSecond);

        return vBox;
    }

    private Node getAutomatics() {
        VBox vBox = new VBox();
        AutoCursor autoCursor = new AutoCursor();
        Grandma grandma = new Grandma();
        Farm farm = new Farm();
        buttonCursor = new Button();
        buttonCursor.setText("Cursor +1" + " Cost = " + autoCursor.getCost());
        buttonGrandma = new Button("Grandma +1" + " Cost = " + grandma.getCost());
        buttonFarm = new Button("Farm +1" + " Cost = " + farm.getCost());
        vBox.getChildren().addAll(buttonCursor, buttonGrandma, buttonFarm);

        getButtonLogics();

        return vBox;
    }

    private void getButtonLogics() {
        buttonCursor.setOnAction(event -> {
            amountOfCursors++;
            AutoCursor autoCursor = new AutoCursor();
            if (cookieAnoumt >= autoCursor.getCost()) {
                perSecond += autoCursor.getMultiplication();
                perSecond *= 10;
                perSecond = Math.round(perSecond);
                perSecond /= 10;
                cookieAnoumt -= autoCursor.getCost();
                automatics.add(autoCursor);
                System.out.println("New Cursor added");

                System.out.println("Amount of cursors: " + amountOfCursors);
                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                System.out.println("Not enough cookies. Click more!!");
            }
            updateDisplay();
        });

        buttonGrandma.setOnAction(event -> {
            amountOfGrandmas++;
            Grandma grandma = new Grandma();
            if (cookieAnoumt >= grandma.getCost()) {
                perSecond += grandma.getMultiplication();
                perSecond *= 10;
                perSecond = Math.round(perSecond);
                perSecond /= 10;
                cookieAnoumt -= grandma.getCost();
                automatics.add(grandma);
                System.out.println("New Grandma added");

                System.out.println("Amount of Grandma's: " + amountOfGrandmas);
                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                System.out.println("Not enough cookies. Click more!!");
            }
            updateDisplay();
        });

        buttonFarm.setOnAction(event -> {
            amountOfFarms++;
            Farm farm = new Farm();
            if (cookieAnoumt >= farm.getCost()) {
                perSecond += farm.getMultiplication();
                perSecond *= 10;
                perSecond = Math.round(perSecond);
                perSecond /= 10;
                cookieAnoumt -= farm.getCost();
                automatics.add(farm);
                System.out.println("New Farm added");

                System.out.println("Amount of Farms: " + amountOfFarms);
                System.out.println("Amount of cookies: " + cookieAnoumt);
            } else {
                System.out.println("Not enough cookies. Click more!!");
            }
            updateDisplay();
        });
    }


    public void draw(FXGraphics2D graphics) {
        updateDisplay();

        //BackgroundImage backgroundImage = new BackgroundImage(new Image("bgBlue.png"), BackgroundRepeat.REPEAT, BackgroundRepeat.REPEAT, BackgroundPosition.CENTER, BackgroundSize.DEFAULT);
        graphics.setTransform(new AffineTransform());
        graphics.clearRect(0, 0, (int) canvas.getWidth(), (int) canvas.getHeight());

        BufferedImage total = null;
        try {
            total = ImageIO.read(getClass().getResource("/bgBlue.png"));
            graphics.setPaint(new TexturePaint(total, new Rectangle2D.Double(canvas.getWidth()/2, canvas.getHeight()/2, canvas.getWidth(), canvas.getHeight())));
        } catch (IOException e) {
            e.printStackTrace();
        }

        cookie.setEllipse2D(new Ellipse2D.Double(canvas.getWidth() / 2 - 100, canvas.getHeight() / 2 - 100, 200, 200));
        //graphics.draw(cookie.getEllipse2D());

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


    public static void main(String[] args) {
        launch(Main.class);
    }

    private void mousePressed(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            cookieState = imageState.HELD;
            draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
            System.out.println("Clicked in Circle");
            cookieAnoumt++;
        }
        updateDisplay();
    }

    private void mouseReleased(MouseEvent e) {
        if (cookie.getEllipse2D().contains(e.getX(), e.getY())) {
            cookieState = imageState.HOVER;
        } else {
            cookieState = imageState.IDLE;
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void mouseMoved(MouseEvent event) {
        if (cookie.getEllipse2D().contains(event.getX(), event.getY())) {
            cookieState = imageState.HOVER;
        } else {
            cookieState = imageState.IDLE;
        }
        draw(new FXGraphics2D(canvas.getGraphicsContext2D()));
    }

    private void updateDisplay() {
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


}
