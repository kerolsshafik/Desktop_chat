package sample;

import javafx.application.Application;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.io.*;
import java.net.Socket;

public class Chat extends Application {

    AES aes = new AES();
    String text = "";
   static  DataOutputStream dout = null;
   static  BufferedReader br = null;
   static  Socket s1 = null;
    static DataInputStream in = null;

    @Override
    public void start(Stage primaryStage) throws IOException {
        Text wel = new Text("Welcome");
        wel.setLayoutX(170);
        wel.setLayoutY(40);
        wel.setScaleX(4);
        wel.setScaleY(4);

        Text chat = new Text(" chating   ");
        chat.setLayoutX(50);
        chat.setLayoutY(85);
        chat.setFont(Font.font(20));

        TextField tf = new TextField();
        tf.setLayoutX(50);
        tf.setLayoutY(380);
        tf.setMinWidth(300);

        TextArea messages = new TextArea();
        ///
        messages.setLayoutX(150);
        messages.setLayoutY(80);
        messages.setMaxSize(300, 200);

        CheckBox cb = new CheckBox();
        cb.setLayoutX(60);
        cb.setLayoutY(220);

        ToggleGroup tg = new ToggleGroup();
        RadioButton d1 = new RadioButton(" AES ");

        RadioButton d3 = new RadioButton(" NOTHING ");

        d1.setLayoutX(60);
        d1.setLayoutY(270);

        d3.setLayoutX(60);
        d3.setLayoutY(290);

        d1.setToggleGroup(tg);

        d3.setToggleGroup(tg);

        Button bb = new Button("           Enter          ");
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
                        System.out.println("message sent");
                    } catch (IOException ioException) {
                        ioException.printStackTrace();
                    }
                    text = text + message+"\n";
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
        root.getChildren().addAll(wel, chat, messages, d1, d3, bb, tf, cb);

        Scene scene = new Scene(root, 500, 550);

        primaryStage.setTitle(" client ");
        primaryStage.setScene(scene);
        primaryStage.show();
    }

    public static void main(String[] args) throws IOException {
        s1 = new Socket("localhost", 6666);
        br = new BufferedReader(new InputStreamReader(System.in));
        in = new DataInputStream(s1.getInputStream());
        OutputStream outputStream = s1.getOutputStream();
        dout = new DataOutputStream(outputStream);
        launch(args);
    }

}
