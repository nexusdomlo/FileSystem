package com.example.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Side;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.ImageView;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.stage.Stage;

import java.nio.file.PathMatcher;
import javafx.scene.control.ContextMenu;
import javafx.scene.control.MenuItem;

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
    void initIllustrate(MouseEvent event) {
        //滚动面板
       ScrollPane sp = new ScrollPane();
        //纵向滚动条
        sp.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        TextArea textarea = new TextArea("命令类型：操作类型 目录名\\文件名\n          默认最初的目录为根目录\n          命令中，最后的指令名字前带$指类型是目录 例如 create a\\$b 在a目录下创建b目录\n                                                  create a\\b 在目录下创建b文件 \n          改变文件属性中OR为只读 RAW为读与写 例如 change a.OR 改为只读 \n                                                  change a.RAW 改为读与写 \n                                 创建出来的文件默认是读与写 不支持文件夹属性改变 \n创建命令：create \n          例子 创建文件：create a        在b文件夹中创建文件 create b\\a \n            创建文件夹：create $c       在文件夹中创建文件夹 create b\\$c \n打开命令：open\n          例子 打开文件：open a        打开文件夹下的文件 open b\\a\n            打开文件夹：open $c       打开文件夹下的文件夹 open b\\$c\n\n关闭命令：close\n          例子 关闭文件：close a       关闭文件夹下的文件 close b\\a\n            关闭文件夹：close $c      关闭文件夹下的文件夹 close b\\$c\n\n以下命令列子相似\n\n删除命令：delete\n\n查询命令：search\n\n改变文件属性命令（只支持文件类型）：change\n                                    例如 change a.OR 改为只读\n                                        change a.RAW 改为读与写 \n\t\t\t\t创建出来的文件默认是读与写 不支持文件夹属性改变\n");
        //自动换行
        textarea.setWrapText(true);

        textarea. setEditable(false);
        //sp.getChildren().add(textarea);
        Scene scene = new Scene(sp);

        Stage helpStage = new Stage();
        helpStage.setScene(scene);

        helpStage.setTitle("帮助");
        helpStage.setHeight(450);
        helpStage.setWidth(550);
        //窗体大小不可拉伸
        helpStage.setResizable(false);
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

    @FXML
    void Store(MouseEvent event) {
        String fileName;
        Pane commandPane = new Pane();
        TreeItem<String> root = new TreeItem("ROOT");
        root.setExpanded(true);

        TreeView tree = new TreeView(root);
        tree.setMaxWidth(100.0D);
        tree.setMaxHeight(500.0D);
        tree.setEditable(true);
        //addTree(fileName);

//        //创建右键菜单
//        ContextMenu contextMenu = new ContextMenu();
//        //菜单项
//        MenuItem item1 = new MenuItem("新建文件");
//        MenuItem item2 = new MenuItem("新建文件夹");
//        contextMenu.getItems().addAll(item1,item2);
//        //添加右键菜单到label
//        label.setContextMenu(contextMenu);
        commandPane.getChildren().add(tree);
        Scene scene = new Scene(commandPane,500,500);
//        scene.addEventFilter(MouseEvent.MOUSE_CLICKED,new EventHandler<MouseEvent>(){
//            @Override
//            public void handle(MouseEvent event){
//                Label label = new Label();
//                //右键菜单
//                //创建右键菜单
//                ContextMenu contextMenu = new ContextMenu();
//                //菜单项
//                MenuItem item1 = new MenuItem("新建文件");
//                MenuItem item2 = new MenuItem("新建文件夹");
//                contextMenu.getItems().addAll(item1,item2);
//                //添加右键菜单到label
//                label.setContextMenu(contextMenu);
//                Scene rs = new Scene(label,100,100);
//                Stage rstage = new Stage();
//                rstage.setScene(rs);
//                rstage.show();
//            }
//        });
        Stage startStage = new Stage();
        startStage.setScene(scene);
        startStage.setResizable(false);
        startStage.setTitle("文件系统");
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
@SuppressWarnings("restriction")
    public static class GlobalMenu extends ContextMenu {
        //单例
        private static GlobalMenu INSTANCE = null;

        //私有构造函数
        private GlobalMenu() {
            MenuItem fileMenuItem = new MenuItem("新建文件");
            MenuItem folderMenuItem = new MenuItem("新建文件夹");
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


}