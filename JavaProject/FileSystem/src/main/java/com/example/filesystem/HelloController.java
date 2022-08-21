package com.example.filesystem;

import javafx.beans.value.ObservableValue;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
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
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.nio.file.PathMatcher;
import java.util.ArrayList;
import java.util.List;

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
        Group gp = new Group();
        Scene scene = new Scene(gp,520,200);
        Stage helpStage = new Stage();
        helpStage.setScene(scene);
        helpStage.setTitle("帮助");
        //窗体大小不可拉伸
        helpStage.setResizable(false);

        TextArea text = new TextArea("命令类型：操作类型 目录名\\文件名\n          默认最初的目录为根目录\n          命令中，最后的指令名字前带$指类型是目录 例如 create a\\$b 在a目录下创建b目录\n                                                  create a\\b 在目录下创建b文件 \n          改变文件属性中OR为只读 RAW为读与写 例如 change a.OR 改为只读 \n                                                  change a.RAW 改为读与写 \n                                 创建出来的文件默认是读与写 不支持文件夹属性改变 \n创建命令：create \n          例子 创建文件：create a        在b文件夹中创建文件 create b\\a \n            创建文件夹：create $c       在文件夹中创建文件夹 create b\\$c \n打开命令：open\n          例子 打开文件：open a        打开文件夹下的文件 open b\\a\n            打开文件夹：open $c       打开文件夹下的文件夹 open b\\$c\n\n关闭命令：close\n          例子 关闭文件：close a       关闭文件夹下的文件 close b\\a\n            关闭文件夹：close $c      关闭文件夹下的文件夹 close b\\$c\n\n以下命令列子相似\n\n删除命令：delete\n\n查询命令：search\n\n改变文件属性命令（只支持文件类型）：change\n                                    例如 change a.OR 改为只读\n                                        change a.RAW 改为读与写 \n\t\t\t\t创建出来的文件默认是读与写 不支持文件夹属性改变\n");
        gp.getChildren().add(text);


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
        Scene scene = new Scene(commandPane,700,550);
        Stage startStage = new Stage();
        startStage.setScene(scene);
        startStage.setResizable(false);
        startStage.setTitle("存储");

        //左侧树形区域
        TreeItem<String>  root = new TreeItem("ROOT");
        VBox vb = new VBox();
        vb.setPrefHeight(600);
        root.setExpanded(true);
        TreeView treeView = new TreeView(root);
        treeView.setMaxWidth(100.0D);
        treeView.setMaxHeight(550.0D);
        treeView.setLayoutX(0);
        treeView.setLayoutY(0);
        treeView.setEditable(true);
         //将TreeView对象的选项节点元素设置为自定义的treecell实现
        treeView.setCellFactory((TreeView<String> p) ->
                new TextFieldTreeCellImpl());

        //addTree(filename);
        vb.getChildren().add(treeView);
        ScrollPane sp = new ScrollPane(vb);
        sp.setMaxHeight(550);
        commandPane.getChildren().add(vb);
        addRightMenu(treeView);

        //Disk区域
        HBox hb = new HBox();
        Label l = new Label("磁盘资源使用情况");
        l.setFont(Font.font("Time New Roman",15));
        hb.getChildren().add(l);
        hb.setLayoutY(2);
        hb.setLayoutX(130);
        Pane dp =new Pane();
        dp.getChildren().add(hb);
        dp.setLayoutX(80);
        dp.setLayoutY(6);
        dp.setMaxHeight(300);
        dp.setMaxHeight(300);
        commandPane.getChildren().add(dp);

        //饼图
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("已使用",20),
                        new PieChart.Data("未使用",80));
        PieChart pieChart = new PieChart(pieChartData);
         //设置标签不可见
        pieChart.setLabelsVisible(false);
        pieChart.setPrefSize(220,220);
        pieChart.setLayoutY(9);
        pieChart.setLayoutX(5);
         //设置图表旋转
        pieChart.setClockwise(false);
        pieChart.setStartAngle(90);
        dp.getChildren().add(pieChart);

        //diskUsing区域
        List<StackPane> disk = new ArrayList();
        GridPane myPane = new GridPane();
        myPane.setStyle("-fx-background-color: #fff");
        myPane.setVgap(3.0D); //两个格子之间的垂直距离
        myPane.setHgap(3.0D); //两个格子之间的水平距离
        myPane.setLayoutX(200);
        myPane.setLayoutY(25);
        myPane.setPadding(new Insets(3.0D, 3.0D, 3.0D, 3.0D));
        for(int i = 0; i < 128; ++i) {
            Text numberLabel = new Text(String.valueOf(i));
            StackPane stackPane = new StackPane();
            stackPane.getChildren().add(numberLabel);
            stackPane.setStyle("-fx-background-color: #c8c8c8");
            final Text text = (Text)stackPane.getChildren().get(0);
            stackPane.setOnMouseClicked(new EventHandler<Event>() {
                public void handle(Event arg0) {
                    System.out.println("点击" + text.getText() + "号块");
                }
            });
            disk.add(i, stackPane);
            myPane.add(stackPane, i % 8, i / 8);
        }
        dp.getChildren().add(myPane);

        //输入指令部分
        //单行输入框

        HBox hbox = new HBox();
        Label label = new Label("ROOT:>");
        //label.setPadding(15,15,20,20);
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
        ScrollPane hbPane = new ScrollPane(hbox);
        hbPane.setLayoutX(180);
        hbPane.setLayoutY(340);
        hbPane.setMaxHeight(120.0D);
        hbPane.setMinWidth(100.0D);
        commandPane.getChildren().add(hbPane);


        //FAT表
        VBox numberBox = new VBox();
        VBox backgroundBox = new VBox();
        VBox contentBox = new VBox();
        Label[] numberLabel = new Label[128];
        Label[] contentLabel = new Label[128];
        HBox hBox = new HBox(new Node[]{numberBox, contentBox});
        hBox.setSpacing(5.0D);
        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setMaxHeight(520.0D);
        scrollPane.setMinWidth(100.0D);
        Text fatTitle = new Text("FAT表");
        fatTitle.setFont(Font.font(15.0D));
        fatTitle.setWrappingWidth(220.0D);
        fatTitle.setTextAlignment(TextAlignment.CENTER);
        VBox rootVBox = new VBox(new Node[]{fatTitle, scrollPane});
        rootVBox.setPadding(new Insets(5.0D, 0.0D, 0.0D, 0.0D));
        rootVBox.setLayoutY(5);
        rootVBox.setLayoutX(480);
        commandPane.getChildren().add(rootVBox);

        contentBox.setSpacing(5.0D);
        contentBox.setMinWidth(100.0D);

        //FAT表左侧部分(contentBox)
        for(int i = 0; i < 128; ++i) {
            contentLabel[i] = new Label(String.valueOf(i));
            StackPane stackPane = new StackPane(new Node[]{contentLabel[i]});
            stackPane.setStyle("-fx-background-color: #c8c8c8");
            contentBox.getChildren().add(stackPane);
        }

        //FAT表右侧numberBox区域
        numberBox.setSpacing(5.0D);
        numberBox.setMinWidth(100.0D);

        for(int i = 0; i < 128; ++i) {
            numberLabel[i] = new Label(String.valueOf(i));
            StackPane stackPane = new StackPane(new Node[]{numberLabel[i]});
            stackPane.setStyle("-fx-background-color: #c8c8c8");
            numberBox.getChildren().add(stackPane);
        }
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

    //新建文件窗口
    public static void createFile(){
        Pane filePane = new Pane();
        ImageView view = new ImageView();
        Image icon = new Image("Txt.png");
        view.setImage(icon);
        view.setFitWidth(80);
        view.setFitHeight(80);
        view.setLayoutX(100);
        view.setLayoutY(20);
        HBox hb1 = new HBox();
        Label l1 = new Label("请输入文件名");
        hb1.getChildren().add(l1);
        hb1.setLayoutX(100);
        hb1.setLayoutY(110);
        HBox hb2 = new HBox();
        TextField f1 = new TextField();
        hb2.getChildren().add(f1);
        hb2.setLayoutX(60);
        hb2.setLayoutY(130);
        Button bt1 = new Button("确定");
        bt1.setLayoutX(75);
        bt1.setLayoutY(170);
        Button bt2 = new Button("取消");
        bt2.setLayoutX(160);
        bt2.setLayoutY(170);
        filePane.getChildren().addAll(view,hb1,hb2,bt1,bt2);
        Scene fileScene = new Scene(filePane,300,300);
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle("新建文件");
        fileStage.show();
    }

    //新建文件夹窗口
    public static void createFolder(){
        Pane folderPane = new Pane();
        Image icon1 = new Image("File.png");
        ImageView view1 = new ImageView();
        view1.setImage(icon1);
        view1.setFitWidth(80);
        view1.setFitHeight(80);
        view1.setLayoutX(100);
        view1.setLayoutY(20);
        HBox hb1 = new HBox();
        Label l1 = new Label("请输入文件夹名");
        hb1.getChildren().add(l1);
        hb1.setLayoutX(100);
        hb1.setLayoutY(100);
        HBox hb2 = new HBox();
        TextField f1 = new TextField();
        hb2.getChildren().add(f1);
        hb2.setLayoutX(70);
        hb2.setLayoutY(125);
        Button bt1 = new Button("确定");
        bt1.setLayoutX(85);
        bt1.setLayoutY(170);
        Button bt2 = new Button("取消");
        bt2.setLayoutX(160);
        bt2.setLayoutY(170);
        folderPane.getChildren().addAll(view1,hb1,hb2,bt1,bt2);
        Scene folderScene = new Scene(folderPane,300,300);
        Stage folderStage = new Stage();
        folderStage.setScene(folderScene);
        folderStage.setTitle("新建文件夹");
        folderStage.show();
    }

    public final class TextFieldTreeCellImpl extends TreeCell<String>{
        private final ContextMenu folderMenu = new ContextMenu();
        private final ContextMenu fileMenu = new ContextMenu();
        private final ContextMenu rootMenu = new ContextMenu();

        public TextFieldTreeCellImpl(){

        }
    }


}