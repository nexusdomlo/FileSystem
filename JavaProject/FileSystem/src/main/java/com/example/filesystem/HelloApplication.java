package com.example.filesystem;

import javafx.application.Application;
import javafx.event.EventHandler;
import javafx.fxml.FXMLLoader;
import javafx.geometry.Side;
import javafx.scene.Scene;
import javafx.scene.control.Label;
import javafx.scene.control.TreeItem;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.stage.WindowEvent;

import java.io.IOException;
import java.sql.*;

import com.example.filesystem.HelloController;

import static com.example.filesystem.HelloController.*;

public class HelloApplication extends Application {
    public void operDatabase(String parentname, Folder parent, TreeItem<Pane> it) throws ClassNotFoundException, SQLException {
        Class.forName("com.mysql.cj.jdbc.Driver");
        Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/filesystem?characterEncoding=UTF-8","root","22371X");
        Statement state=conn.createStatement();
        String sql_order="select * from Disk where fparent="+"'"+parentname+"'";
        ResultSet rs=state.executeQuery(sql_order);
        while(rs.next())
        {
            if(rs.getInt("ftype")!=8)
            {
                File X=new File(rs.getString("fname"),rs.getString("fpath"),rs.getInt("fbeginnum"),parent,rs.getInt("ftype"),0);
                TreeItem<Pane>i=new TreeItem<>(make_Pane(X.fileName,true));
                FileSub.FatTable.IndexArray[rs.getInt("fnum")]=rs.getInt("findex");
                it.getChildren().add(i);
                FileSub.Disk.blocks[rs.getInt("fnum")].BlockChange(rs.getInt("findex"),X,rs.getBoolean("fbegin"));
                //设置文本的图标位置

            }
            else{
                Folder folder=new Folder(rs.getString("fname"),rs.getString("fpath"),rs.getInt("fbeginnum"),parent);
                FileSub.Disk.blocks[rs.getInt("fnum")].BlockChange(rs.getInt("findex"),folder,rs.getBoolean("fbegin"));
                TreeItem<Pane>i=new TreeItem<>(make_Pane(folder.folderName,true));
                FileSub.FatTable.IndexArray[rs.getInt("fnum")]=rs.getInt("findex");
                if(root==null)
                    root=i;
                if(it!=null)
                    it.getChildren().add(i);
                operDatabase(folder.folderName,folder,i);//递归的寻找并构建
            }
        }
    }
    public void getData()
    {
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
                return;

            operDatabase("null",null,null);
/*            setpicture();*/
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }
    @Override
    public void start(Stage stage) throws IOException {
        FXMLLoader fxmlLoader = new FXMLLoader(HelloApplication.class.getResource("FS.fxml"));
        Scene scene = new Scene(fxmlLoader.load(), 1200, 800);
        stage.setTitle("文件系统");
        stage.setScene(scene);
        stage.show();
        getData();
        stage.setOnCloseRequest(new EventHandler<WindowEvent>() {
            @Override
            public void handle(WindowEvent windowEvent) {
                HelloController.Keepdata();
            }
        });
    }

    public static void main(String[] args) {
        launch();
    }
}