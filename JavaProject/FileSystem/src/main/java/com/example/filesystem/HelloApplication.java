package com.example.filesystem;
import javafx.application.Application;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;
import java.io.IOException;
import java.sql.*;
import static com.example.filesystem.HelloController.*;

public class HelloApplication extends Application {
    public void Windows()
    {
        Dialog<ButtonType>dialog=new Dialog<>();
        DialogPane dialogPane=dialog.getDialogPane();
        dialog.setContentText("是否加载已经保存的数据");
        dialogPane.setPrefSize(300,100);
        ObservableList<ButtonType>buttonTypes=dialogPane.getButtonTypes();
        buttonTypes.addAll(ButtonType.OK,ButtonType.CANCEL);
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn= DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306?characterEncoding=UTF-8","root","22371X");
            Statement state=conn.createStatement();
            String sql_order="select * from information_schema.SCHEMATA where SCHEMA_NAME = 'filesystem';";
            ResultSet rs=state.executeQuery(sql_order);
            if(!rs.next())
            {
                return;//如果为空就直接不读
            }
            DatabaseMetaData metaData= conn.getMetaData();
            rs=metaData.getTables(null,null,"Disk",null);
            if(!rs.next())
            {
                rs.close();
                return;
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
        Button apply=(Button) dialogPane.lookupButton(ButtonType.OK);
        apply.setOnAction(actionEvent -> {
            initlazie=true;
        });
        Button cancel=(Button) dialogPane.lookupButton(ButtonType.CANCEL);
        cancel.setOnAction(actionEvent -> {
        });
        dialog.showAndWait();
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FS.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("文件系统");
        stage.setScene(scene);
        stage.show();
        Windows();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                Dialog<ButtonType>dialog=new Dialog<>();
                DialogPane dialogPane=dialog.getDialogPane();
                dialogPane.setPrefSize(300,100);
                dialog.setContentText("是否保存数据到数据库中");
                ObservableList<ButtonType>buttonTypes=dialogPane.getButtonTypes();
                buttonTypes.addAll(ButtonType.OK,ButtonType.CANCEL);
                Button apply=(Button) dialogPane.lookupButton(ButtonType.OK);
                apply.setOnAction(actionEvent -> {
                    Keepdata();
                });
                Button cancel=(Button) dialogPane.lookupButton(ButtonType.CANCEL);
                cancel.setOnAction(actionEvent -> {
                });
                dialog.showAndWait();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}