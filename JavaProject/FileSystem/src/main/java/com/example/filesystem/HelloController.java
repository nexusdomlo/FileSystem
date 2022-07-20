package com.example.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;

public class HelloController {


    //帮助按钮
    @FXML
    private ImageView Tips;

    //关于我们按钮
    @FXML
    private ImageView aboutUs;

    //文件系统按钮
    @FXML
    private ImageView fileStorage;

    @FXML
    private AnchorPane mainInterface;

    @FXML
    private ImageView startMenu;

    @FXML
    private ImageView main_BG;

    @FXML
    private Label label1;

    @FXML
    private Label label2;

    @FXML
    private Label label3;

    //文件系统界面
    @FXML
    private Pane StoragePane;

    //饼状图
    @FXML
    private PieChart pieChart;

    //FAT表
    @FXML
    private TableView<?> tableView;

    //树形结构存储图
    @FXML
    private TreeView<?> treeView;


//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }


//    public void init(){
//        ObservableList<PieChart.Data> pieChartData =
//                FXCollections.observableArrayList(
//                        new PieChart.Data("Grapefruit", 13),
//                        new PieChart.Data("Oranges", 25),
//                        new PieChart.Data("Plums", 10),
//                        new PieChart.Data("Pears", 22),
//                        new PieChart.Data("Apples", 30));
//        pieChart.setData(pieChartData);
//    }

//    Tooltip tooltip = new Tooltip("帮助");
//    tooltip.setFont(new Font("YouYuan", 16));
//    Tooltip.install(Tips,tooltip);

    //帮助按钮点击事件
    @FXML
    void Illustrate(MouseEvent event) {
        ScrollPane sp = new ScrollPane();



    }

    @FXML
    void State(MouseEvent event) {

    }

    @FXML
    void Store(MouseEvent event) {

    }


}