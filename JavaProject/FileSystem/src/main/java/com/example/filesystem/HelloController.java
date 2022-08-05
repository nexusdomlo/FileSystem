package com.example.filesystem;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Orientation;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.PathMatcher;

//import static com.example.filesystem.HelloController.GlobalMenu.INSTANCE;

public class HelloController {


    private static final GlobalMenu INSTANCE = null;
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


    //帮助
    @FXML
    private MenuItem menuItem1;

    //关于我们
    @FXML
    private MenuItem menuItem2;

    //文件系统
    @FXML
    private MenuItem menuItem3;

    //关机
    @FXML
    private MenuItem menuItem4;


    //文件系统界面
    @FXML
    private Pane StoragePane;

    //饼状图
    @FXML
    private PieChart pieChart;

    //FAT表
    @FXML
    private TableView<?> tableView;



//    @FXML
//    private Label welcomeText;
//
//    @FXML
//    protected void onHelloButtonClick() {
//        welcomeText.setText("Welcome to JavaFX Application!");
//    }




//    Tooltip tooltip = new Tooltip("帮助");
//    tooltip.setFont(new Font("YouYuan", 16));
//    Tooltip.install(Tips,tooltip);

    //帮助按钮点击事件
    @FXML
    void initIllustrate(MouseEvent event) {
        //滚动面板
        VBox vb = new VBox();
        ScrollBar sc = new ScrollBar();
        Group gp = new Group();
        Scene scene = new Scene(gp,550,450);
        Stage helpStage = new Stage();
        helpStage.setScene(scene);
        helpStage.setTitle("帮助");
        //窗体大小不可拉伸
        helpStage.setResizable(false);
        gp.getChildren().addAll(sc,vb);

        //设置VBox布局器的水平方向左侧布局位置，5个像素
        vb.setLayoutX(5);
        vb.setSpacing(10);

        sc.setLayoutX(scene.getWidth() - sc.getWidth());
        sc.setMin(0);
        sc.setOrientation(Orientation.VERTICAL);
        //ScrollBar对象的初始化高度
        sc.setPrefHeight(450);
        //ScrollBar对象的活动位置最大值
        sc.setMax(360);

        sc.setUnitIncrement(10.0);
        sc.setBlockIncrement(5.0);

        TextArea text = new TextArea("命令类型：操作类型 目录名\\文件名\n          默认最初的目录为根目录\n          命令中，最后的指令名字前带$指类型是目录 例如 create a\\$b 在a目录下创建b目录\n                                                  create a\\b 在目录下创建b文件 \n          改变文件属性中OR为只读 RAW为读与写 例如 change a.OR 改为只读 \n                                                  change a.RAW 改为读与写 \n                                 创建出来的文件默认是读与写 不支持文件夹属性改变 \n创建命令：create \n          例子 创建文件：create a        在b文件夹中创建文件 create b\\a \n            创建文件夹：create $c       在文件夹中创建文件夹 create b\\$c \n打开命令：open\n          例子 打开文件：open a        打开文件夹下的文件 open b\\a\n            打开文件夹：open $c       打开文件夹下的文件夹 open b\\$c\n\n关闭命令：close\n          例子 关闭文件：close a       关闭文件夹下的文件 close b\\a\n            关闭文件夹：close $c      关闭文件夹下的文件夹 close b\\$c\n\n以下命令列子相似\n\n删除命令：delete\n\n查询命令：search\n\n改变文件属性命令（只支持文件类型）：change\n                                    例如 change a.OR 改为只读\n                                        change a.RAW 改为读与写 \n\t\t\t\t创建出来的文件默认是读与写 不支持文件夹属性改变\n");
        gp.getChildren().add(text);

//         //ScrollBar对象进行改变值事件监听处理机制
//        sc.valueProperty().addListener((ObservableValue<? extends Number> ov,
//                                        Number old_val, Number new_val) -> {
//            vb.setLayoutY(-new_val.doubleValue());
//        });

         //为垂直VBOX实现滚动
//        sc.valueProperty().addListener((ObservableValue<? extends Number> ov,
//                                        Number old_val, Number new_val) -> {
//            vb.setLayoutY(-new_val.doubleValue());
//        });
          sc.valueProperty().addListener((ov, old_val, new_val) -> {
              vb.setLayoutY(-new_val.doubleValue());
          });



        helpStage.show();
    }

    //关于我们按钮点击事件
    @FXML
    void State(MouseEvent event) {
        Pane commandPane = new Pane();
        Stage aboutStage = new Stage();
        Scene scene = new Scene(commandPane,400,300);
        aboutStage.setScene(scene);
        //文本框
        TextArea textarea = new TextArea("\n\n\t\t\t\t\t模拟文件操作系统     \t \n   \t\t\t\t\t 前端：沈晓敏      \t \n   \t\t\t\t\t 后端：黄顺豪        \t \n");
        //大小
        textarea.setFont(Font.font(5));
        //加粗
        textarea.setStyle("-fx-font-weight:bold");
        //textarea.setStyle("-fx-font-size:10");
        //自动换行
        textarea.setWrapText(true);
        //初始化设置行数
        textarea.setPrefRowCount(20);
        //textarea.setText("setText");
        commandPane.getChildren().add(textarea);
        //窗口不可拉伸
        aboutStage.setResizable(false);
        aboutStage.setTitle("关于我们");
        aboutStage.show();
    }

    //存储按钮
    @FXML
    void Store(MouseEvent event) {
        String filename = new String();
        Pane commandPane = new Pane();
        TreeItem<String>  root = new TreeItem("ROOT");
        root.setExpanded(true);
        TreeView tree = new TreeView(root);
        tree.setMaxWidth(100.0D);
        tree.setMaxHeight(700.0D);
        tree.setEditable(true);
        //addTree(filename);
        commandPane.getChildren().add(tree);

        //单行输入框
        Pane hbPane = new Pane();
        HBox hbox = new HBox();
        Label label = new Label("ROOT:>");
        //label.setPadding(15,15,20,20);
        hbox.setLayoutX(110);
        hbox.setLayoutY(20);
        TextField field = new TextField();
         //设置单行输入框的宽高
        field.setPrefSize(150,20);
         //能否编辑
        field.setEditable(true);
         //单行输入框的提示语
        field.setPromptText("请输入指令");
         //设置单行输入框的对齐方式
        field.setAlignment(Pos.CENTER_LEFT);
         //设置单行输入框的推荐列数
        field.setPrefColumnCount(11);
        hbox.getChildren().addAll(label,field);
        hbPane.getChildren().add(hbox);
        commandPane.getChildren().add(hbPane);

        //饼图
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("沈阳",50),
                        new PieChart.Data("鞍山",50));
        PieChart pieChart = new PieChart(pieChartData);
        pieChart.setTitle("磁盘资源使用情况");
        //设置标签可见
        pieChart.setLabelsVisible(true);
        Pane p =new Pane();
        p.getChildren().add(pieChart);
        p.setLayoutX(100);
        p.setLayoutY(100);
        commandPane.getChildren().add(p);
        Scene scene = new Scene(commandPane,500,500);

        addRightMenu(tree);
        Stage startStage = new Stage();
        startStage.setScene(scene);
        startStage.setResizable(false);
        startStage.setTitle("存储");
        startStage.show();

    }

    public static void addTree(String fileName){
        String str = fileName.substring(0,1);
        if(str.equals('$')){
           TreeItem newTreeItem = new TreeItem<String>(fileName);
        }
        else{
            TreeItem newTreeItem = new TreeItem(fileName);
        }
    }

    //添加右键菜单
    public static class GlobalMenu extends ContextMenu {
        //单例
        private static GlobalMenu INSTANCE = null;

        //私有构造函数
        private GlobalMenu() {
            MenuItem fileMenuItem = new MenuItem("新建文件");
            fileMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFile();
                }
            });
            
            MenuItem folderMenuItem = new MenuItem("新建文件夹");
            folderMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFolder();
                }
            });

            getItems().add(fileMenuItem);
            getItems().add(folderMenuItem);
        }

        //获取实例
        public static GlobalMenu getInstance() {
            if (INSTANCE == null) {
                INSTANCE = new GlobalMenu();
            }
            return INSTANCE;
        }
    }

    public void addRightMenu(TreeView treeView){
        treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                Node node = event.getPickResult().getIntersectedNode();
                //给node对象添加下拉菜单
                GlobalMenu.getInstance().show(node, Side.BOTTOM,0,0);
                String name = (String) ((TreeItem) treeView.getSelectionModel().getSelectedItem()).getValue();
                System.out.println("Node click:" + name);
            }
        });
    }

    //新建文件
    public static void createFile(){
        Pane filePane = new Pane();
        ImageView view = new ImageView();
        Image icon = new Image("Txt.png");
        view.setImage(icon);
        filePane.getChildren().add(view);
        Scene fileScene = new Scene(filePane,300,300);
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle("新建文件");
        fileStage.show();
    }

    //新建文件夹
    public static void createFolder(){
        Pane folderPane = new Pane();
        Image icon1 = new Image("File.png");
        ImageView view1 = new ImageView();
        view1.setImage(icon1);
        folderPane.getChildren().add(view1);
        Scene folderScene = new Scene(folderPane,300,300);
        Stage folderStage = new Stage();
        folderStage.setScene(folderScene);
        folderStage.show();
    }



}