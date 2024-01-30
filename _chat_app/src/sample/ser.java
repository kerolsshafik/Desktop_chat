package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.ServerSocket;
import java.net.Socket;

public class ser extends Application {

    static Socket s1;
    Scene wel;
    AES aes = new AES();
    String text="";
    static DataOutputStream dout = null;
   static  BufferedReader br = null;
    static DataInputStream in = null;
    @Override
    public void start(Stage primaryStage) throws IOException {
        Text lotwel = new Text("Welcome");
        lotwel.setLayoutX(170);
        lotwel.setLayoutY(40);
        lotwel.setScaleX(4);
        lotwel.setScaleY(4);

        Text lolun = new Text(" chating   ");
        lolun.setLayoutX(50);
        lolun.setLayoutY(85);
        lolun.setFont(Font.font(20));

        TextField tf = new TextField();
        tf.setLayoutX(50);
        tf.setLayoutY(380);
        tf.setMinWidth(300);

        TextArea messages = new TextArea();
        //////////
        messages.setLayoutX(150);
        messages.setLayoutY(80);
        messages.setMaxSize(300, 200);

        CheckBox bx = new CheckBox();
        bx.setLayoutX(60);
        bx.setLayoutY(220);

        ToggleGroup tg = new ToggleGroup();
        RadioButton d1 = new RadioButton(" AES ");

        RadioButton d3 = new RadioButton(" NOTHING ");

        d1.setLayoutX(60);
        d1.setLayoutY(270);

        d3.setLayoutX(60);
        d3.setLayoutY(290);

        d1.setToggleGroup(tg);

        d3.setToggleGroup(tg);

        Button bb = new Button("               Enter          ");
        bb.setLayoutX(270);
        bb.setLayoutY(420);

        // setting the connection setting here

        System.out.println("START");







        DataOutputStream finalDout = dout;
        bb.setOnAction((e) -> {
                if (d1.isSelected()){
                    String message =tf.getText().toString();
                    message = AES.encrypt(message);
                    try {
                        finalDout.writeUTF(message);
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    text = text + message+"\n";
                    System.out.println("message sent");
                    messages.setText(text);
                }
                else{
                    String message =tf.getText().toString();
                    try {
                        finalDout.writeUTF(message);
                        System.out.println("message sent");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    text = text + message+"\n";
                    messages.setText(text);
                }





        });
        DataOutputStream finalDout1 = dout;
        DataInputStream finalIn = in;
        BufferedReader finalBr = br;
        DataInputStream finalIn1 = in;
        new Thread(new Runnable() {
            @Override
            public void run() {
                try {
                    String stser = finalBr.readLine();
                    String dataser = "";
                    while (true) {
                        if(finalIn1.available()>0){
                            System.out.println(" i am in the receiving side");
                            finalDout1.flush();
                            dataser = finalIn.readUTF();
                            if(d1.isSelected()){
                                dataser = AES.decrypt(dataser);
                            }
                            text = text + dataser +"\n";
                            messages.setText(text);
                            System.out.println("Server says: ."
                                    + ""
                                    + "" + dataser);
                        }

                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }


//                try {
//                    if (finalBr != null || finalIn != null || finalDout1 != null) {
//                        finalBr.close();
//                        finalIn.close();
//                        finalDout1.close();
//                    }
//                } catch (IOException e) {
//                    e.printStackTrace();
//                }
            }
        }).start();

        Pane root = new Pane();
        root.getChildren().addAll(lotwel, lolun, messages, d1, d3, bb, tf, bx);

        Scene scene = new Scene(root, 500, 550);

        primaryStage.setTitle(" server");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) throws IOException {
        int port = 6666;
        ServerSocket server = new ServerSocket(port);
        System.out.println("Waiting for a client to join");
        s1 = server.accept();
        br = new BufferedReader(new InputStreamReader(System.in));
        in = new DataInputStream(s1.getInputStream());
        OutputStream outputStream = s1.getOutputStream();
        dout = new DataOutputStream(outputStream);
        launch(args);


    }
}
