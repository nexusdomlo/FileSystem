package com.example.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.event.Event;
import javafx.event.EventHandler;
import javafx.fxml.FXML;
import javafx.geometry.Insets;
import javafx.geometry.Pos;
import javafx.geometry.Side;
import javafx.scene.Group;
import javafx.scene.Node;
import javafx.scene.Scene;
import javafx.scene.chart.PieChart;
import javafx.scene.control.*;
import javafx.scene.image.Image;
import javafx.scene.image.ImageView;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;
import javafx.scene.input.MouseButton;
import javafx.scene.input.MouseEvent;
import javafx.scene.layout.*;
import javafx.scene.text.Text;
import javafx.scene.text.Font;
import javafx.scene.text.TextAlignment;
import javafx.stage.Stage;

import java.util.ArrayList;
import java.util.List;

public class HelloController {
    @FXML
    private ImageView aboutUs;

    @FXML
    private ImageView fileStorage;

    @FXML
    private ImageView main_BG;

    @FXML
    private MenuBar startMenuBar;

    @FXML
    private ImageView startMenu;

    @FXML
    private MenuItem menuItem1;

    @FXML
    private MenuItem menuItem3;

    @FXML
    private MenuItem menuItem2;

    @FXML
    private MenuItem menuItem4;

    @FXML
    private AnchorPane mainInterface;
    public TreeItem<Pane> item;
    @FXML
    private ImageView Tips;
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
    public TreeItem<Pane>  root = new TreeItem<>(make_Pane("ROOT",true));//在treeitem中初始化一个root节点
    public TreeView treeView = new TreeView(root);
    public void addTreeitem(Pane commandPane)
    {
        //左侧树形区域
        AnchorPane vb=new AnchorPane();
        root.setExpanded(true);
        treeView.setPrefSize(250,800);//设置树形区的大小
        treeView.setLayoutX(0);
        treeView.setLayoutY(0);
        treeView.setEditable(true);
        vb.getChildren().add(treeView);
        AnchorPane.setLeftAnchor(treeView,0.0);
        AnchorPane.setTopAnchor(treeView,0.0);
        commandPane.getChildren().add(vb);
        addRightMenu(treeView);
    }
    public static void addDisk(double x,double y,Pane commandPane)
    {
        AnchorPane hb=new AnchorPane();
        Label l = new Label("磁盘资源使用情况");
        l.setFont(Font.font("Time New Roman",15));//设置字体
        l.setPrefSize(200,0);
        hb.getChildren().add(l);
        hb.setLayoutY(y);
        hb.setLayoutX(x);
        commandPane.getChildren().add(hb);
    }
    public static void addPie(double x,double y,Pane commandPane)
    {
        //饼图
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("已使用",20),
                        new PieChart.Data("未使用",80));
        PieChart pieChart = new PieChart(pieChartData);
        //设置标签不可见
        pieChart.setLabelsVisible(false);
        pieChart.setPrefSize(220,220);
        pieChart.setLayoutY(y);
        pieChart.setLayoutX(x);
        //设置图表旋转
        pieChart.setClockwise(false);
        pieChart.setStartAngle(90);
        commandPane.getChildren().add(pieChart);
    }
    public static void addDiskUsing(double x,double y,Pane commandPane)
    {
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
        myPane.setLayoutY(y);
        myPane.setLayoutX(x);
        commandPane.getChildren().add(myPane);
    }

    //存储按钮
    @FXML
    void Store(MouseEvent event) {
        Pane commandPane = new Pane();
        Scene scene = new Scene(commandPane,1000,800);//750的宽度，550的高度
        Stage startStage = new Stage();
        startStage.setScene(scene);
        startStage.setResizable(false);
        startStage.setTitle("存储");
        //左侧树形区域
        addTreeitem(commandPane);
        //Disk
        addDisk(400,450,commandPane);
        addPie(250,580,commandPane);
//饼图部分
        addDiskUsing(450,480,commandPane);
        //输入指令部分
        Writefield(260,0,commandPane);
        addFAT(780,0,commandPane);
        startStage.show();
    }
    public static void addFAT(double x,double y,Pane commandPane)
    {
        //FAT表
        VBox numberBox = new VBox();
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
        rootVBox.setLayoutY(y);
        rootVBox.setLayoutX(x);
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
    }
    public  void Writefield(double x,double y,Pane commandPane)
    {
        //输入指令部分
        //单行输入框
        AnchorPane Writebox = new AnchorPane();//用AnchorPane设置输入栏的文字和位置
        Label label = new Label("ROOT:\\>");
        Label mingningLabel=new Label("命令行");
        mingningLabel.setPrefSize(50,0);

        label.setPrefSize(50,20);//设置文字的大小
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
        Button bangzhu=new Button("提示");

        Writebox.getChildren().add(label);
        Writebox.getChildren().add(field);
        Writebox.getChildren().add(bangzhu);
        Writebox.getChildren().add(mingningLabel);

        AnchorPane.setLeftAnchor(mingningLabel,200.0);
        AnchorPane.setTopAnchor(mingningLabel,0.0);
        AnchorPane.setLeftAnchor(bangzhu,450.0);
        AnchorPane.setTopAnchor(bangzhu,0.0);

        AnchorPane.setLeftAnchor(label,0.0);
        AnchorPane.setTopAnchor(label,30.0);
        AnchorPane.setLeftAnchor(field,50.0);
        AnchorPane.setTopAnchor(field,30.0);//设置添加的两个元素在Anchorpane中的位置
        Writebox.setLayoutX(x);
        Writebox.setLayoutY(y);//设置整一个输入栏（包括文字和输入文字框）的位置
        commandPane.getChildren().add(Writebox);
        field.setOnKeyPressed(new EventHandler<KeyEvent>() {//在文本输入栏中回车接受信息
            @Override
            public void handle(KeyEvent keyEvent) {
                if(keyEvent.getCode()== KeyCode.ENTER){
                    String order=field.getText();
                    String[] orderString=order.split(" ");
                    if(orderString[0].equals("open"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            openfile(orderString[1]);
                            boolean is=FileSub.open_file(orderString[1],0);//以读写的操作来打开文件，后端数据层面
                            System.out.println(is);
                        }
                        else
                        {
                            openfolder(orderString[1].substring(1));//执行打开文件的操作
                        }
                    }
                    else if(orderString[0].equals("create"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            createFile();
                        }
                        else{
                            createFolder();
                        }
                    }
                    else if(orderString[0].equals("close"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            FileSub.close_file(orderString[1]);
                        }
                    }
                    else if(orderString[0].equals("delete"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            FileSub.delete_file(orderString[1]);
                        }
                        else{
                            FileSub.removedir(orderString[1]);
                        }
                    }
                    else if(orderString[0].equals("search")) {

                    }
                    else if(orderString[0].equals("change"))
                    {
                        String[] changeorder=orderString[1].split("\\.");
                        FileSub.change(changeorder[0],changeorder[1]);
                    }
                    else
                    {
                        System.out.println("ERRoR");
                    }
                }
            }
        });
    }
    public void openfile(String filename)
    {
        Pane filePane=new Pane();
        TextArea textArea=new TextArea();
        textArea.setPrefColumnCount(60);
        textArea.setPrefRowCount(30);
        textArea.setWrapText(true);
        filePane.getChildren().add(textArea);
        Scene fileScene = new Scene(filePane,800,600);//设置窗口的大小
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle(filename);//设置窗口的标题
        fileStage.show();
    }
    public void openfolder(String foldername)
    {
        Pane filePane=new Pane();
        for(int i=0;i<FileSub.F.children.size();i++)
        {
            if(FileSub.F.children.get(i) instanceof File)//如果他属于文件类
            {
                Image fl=new Image("Txt.png");
                ImageView FIV=new ImageView();
                FIV.setImage(fl);
                Label FLname=new Label(((File)FileSub.F.children.get(i)).fileName);
                FIV.setLayoutX(15.0+i*30);
                FIV.setLayoutY(10);
                filePane.getChildren().add(FIV);
                filePane.getChildren().add(FLname);
            }
            else if(FileSub.F.children.get(i) instanceof Folder){
                Image fl=new Image("File.png");
                ImageView FIV=new ImageView();
                FIV.setImage(fl);
                Label FLname=new Label(((Folder)FileSub.F.children.get(i)).folderName);
                FIV.setLayoutX(15.0+i*30);
                FIV.setLayoutY(10);
                filePane.getChildren().add(FIV);
                filePane.getChildren().add(FLname);
            }
            //设置文件图片和标签的位置
        }
        Scene fileScene = new Scene(filePane,800,600);//设置窗口的大小
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle(foldername);//设置窗口的标题
        fileStage.show();
    }


    //添加右键菜单
    public class FileMenu extends ContextMenu {
        public  String name;
        public FileMenu(){
            MenuItem fileopenItem=new MenuItem("打开");
            fileopenItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    openfile(name);
                }
            });
            MenuItem filedeleteItem=new MenuItem("删除");
            filedeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.delete_file(name);
                }
            });
            MenuItem filetypeItem=new MenuItem("属性");
            filetypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.typefile(name);
                }
            });
            getItems().addAll(fileopenItem,filedeleteItem,filetypeItem);
        }
    }
    public class FolderMenu extends ContextMenu {
        public String name;

        public FolderMenu() {
            MenuItem folderopenItem = new MenuItem("打开");
            folderopenItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    openfile(name);
                }
            });
            MenuItem folderdeleteItem = new MenuItem("删除");
            folderdeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("shanchu");
                }
            });
            MenuItem foldertypeItem = new MenuItem("属性");
            foldertypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    System.out.println("属性");
                }
            });
            MenuItem folderMenuItem1 = new MenuItem("新建文件");
            folderMenuItem1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFile();
                }
            });

            MenuItem folderMenuItem2 = new MenuItem("新建文件夹");
            folderMenuItem2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFolder();//点击新建文件夹之后新启动一个画面，来输入对应的信息进行新建
                }
            });
            getItems().addAll(folderopenItem,folderdeleteItem,foldertypeItem,folderMenuItem1,folderMenuItem2);
        }
    }
    public class GlobalMenu extends ContextMenu {
        //私有构造函数
        public GlobalMenu() {
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
                    createFolder();//点击新建文件夹之后新启动一个画面，来输入对应的信息进行新建
                }
            });
            getItems().add(fileMenuItem);
            getItems().add(folderMenuItem);
        }
    }

    public void addRightMenu(TreeView treeView){
        treeView.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent event) {
                if(treeView.getSelectionModel().getSelectedItem()==null)//设置右键点击的事件
                    return;
                Node node = event.getPickResult().getIntersectedNode();
                //如果不能够展开，就是文件
                if(!((TreeItem<?>) treeView.getSelectionModel().getSelectedItem()).expandedProperty().get())
                {
                    if(event.getClickCount()==2)//设置双击的事件
                    {
                        openfile(((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText());//双击的话就点击打开窗口
                    }
                    if(event.getButton()==MouseButton.SECONDARY)//设置右键点击的事件  开启一个新的弹窗
                    {
                        FileMenu X=new FileMenu();//特地用给file的弹窗类
                        X.name=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();//将文件名字给到类中
                        X.show(node,Side.BOTTOM,0,0);
                    }

                }
                else{
                    String R=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();//右键点击的时候就能够执行下列语句
                    if(R.equals("ROOT"))
                    {
                        if(event.getButton()!=MouseButton.SECONDARY)
                            return;
                        GlobalMenu X=new GlobalMenu();
                        X.show(node,Side.BOTTOM,0,0);
                        item=(TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem();
                    }
                    else{
                        if(event.getButton()!=MouseButton.SECONDARY)
                            return;
                        FolderMenu X=new FolderMenu();
                        X.show(node,Side.BOTTOM,0,0);
                        item=(TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem();
                    }

                }
            }
        });
    }
    public Pane make_Pane(String name,boolean is)
    {
        Pane X=new Pane();
        Label Xname=new Label(name);
        Xname.setLayoutX(20);
        Xname.setLayoutY(0);
        Image R = null;
        if(name.equals("ROOT"))
        {
            R=new Image("computer.png");//用ROOT的图片
        }
        else if(is){
            R=new Image("Txt.png");//用文件的图片
        }
        else{
            R=new Image("File.png");//用文件夹的图片
        }
        ImageView P=new ImageView();
        P.setImage(R);
        P.setFitWidth(20);
        P.setFitHeight(20);
        P.setLayoutX(0);
        P.setLayoutY(0);
        X.getChildren().addAll(Xname,P);
        return X;
    }

    //新建文件窗口
    public  void createFile(){
        Pane filePane = new Pane();
        ImageView view = new ImageView();
        Image icon = new Image("Txt.png");
        view.setImage(icon);
        view.setFitWidth(80);
        view.setFitHeight(80);
        view.setLayoutX(100);
        view.setLayoutY(20);
        //添加图片，设置图片位置

        HBox hb1 = new HBox();
        Label l1 = new Label("请输入文件名");
        hb1.getChildren().add(l1);
        hb1.setLayoutX(100);
        hb1.setLayoutY(110);
        //请输入文件名的位置
        HBox hb2 = new HBox();
        TextField f1 = new TextField();
        hb2.getChildren().add(f1);
        hb2.setLayoutX(60);
        hb2.setLayoutY(130);
        //输入文本框的位置
        Button bt1 = new Button("确定");
        bt1.setLayoutX(75);
        bt1.setLayoutY(170);

        //确定按钮的位置
        Button bt2 = new Button("取消");
        bt2.setLayoutX(160);
        bt2.setLayoutY(170);

        //取消按钮的位置
        filePane.getChildren().addAll(view,hb1,hb2,bt1,bt2);
        Scene fileScene = new Scene(filePane,270,200);//设置窗口的大小
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle("新建文件");//设置窗口里面的人的
        fileStage.show();
        bt1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()!=MouseButton.PRIMARY)
                    return;
                String filename=f1.getText();
                if(filename.contains("/")||filename.contains(".")||filename.contains("$")||filename.length()>3)
                {
                    ErrorWindows("新建文件错误","文件名中字符错误");
                }
                TreeItem<Pane> newItem=new TreeItem<>(make_Pane(filename,true));
                newItem.setExpanded(false);
                item.getChildren().add(newItem);
                int index=FileSub.findFAT(3);//找到对应的空闲的盘块
                FileSub.currentpath=printcurrent();//获取对应的当前路径
                FileSub.findFolder(FileSub.currentpath);//修改当前路径下的所在的文件夹
                File X=new File(filename,FileSub.currentpath+"\\"+filename,index,FileSub.F,0,1);
                FileSub.F.addChildrenNode(X);//当前的文件夹数据添加文件节点
                FileSub.Disk.blocks[index].BlockChange(-1,X,true);//更改对应的磁盘块的内容
                FileSub.FatTable.IndexArray[index]=-1;//修改FAT表
                //对后端数据的修改

                //在桌面设置文件的图片
                Image FilePicture=new Image("File.png");
                ImageView FP=new ImageView();
                FP.setImage(FilePicture);
                FP.setFitWidth(40);
                FP.setFitHeight(40);
                Label label=new Label(filename);
                mainInterface.getChildren().addAll(FP,label);
                AnchorPane.setTopAnchor(FP,10.0+((mainInterface.getChildren().size()-3)%28/2)*65);
                AnchorPane.setLeftAnchor(FP,10.0+(mainInterface.getChildren().size()/32)*65);
                AnchorPane.setTopAnchor(label,55.0+((mainInterface.getChildren().size()-3)%28/2)*65);
                AnchorPane.setLeftAnchor(label,19.0+(mainInterface.getChildren().size()/32)*65);
                FP.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getButton()==MouseButton.SECONDARY)
                        {
                            FileMenu X=new FileMenu();
                            X.show(FP,Side.BOTTOM,0,0);
                        }
                    }
                });
                fileStage.close();//点击确定后关闭界面
            }
        });
        bt2.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()!=MouseButton.PRIMARY)
                    return;
                fileStage.close();//关闭窗口
            }
        });
    }
    public  void ErrorWindows(String ErrorType,String ErrorString)
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(ErrorType);
        alert.setContentText(ErrorString);
        alert.showAndWait();
    }

    //新建文件夹窗口
    public  void createFolder(){

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
        //设置文本输入框位置
        Button bt1 = new Button("确定");
        bt1.setLayoutX(85);
        bt1.setLayoutY(170);

        Button bt2 = new Button("取消");
        bt2.setLayoutX(160);
        bt2.setLayoutY(170);
        folderPane.getChildren().addAll(view1,hb1,hb2,bt1,bt2);
        Scene folderScene = new Scene(folderPane,270,200);
        Stage folderStage = new Stage();
        folderStage.setScene(folderScene);
        folderStage.setTitle("新建文件夹");
        folderStage.show();
        bt1.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()!=MouseButton.PRIMARY)
                    return;
                String filename=f1.getText();
                if(filename.contains("/")||filename.contains(".")||filename.contains("$")||filename.length()>3)
                {
                    ErrorWindows("新建文件夹错误","文件夹名中字符错误");
                }
                TreeItem<Pane> newItem=new TreeItem<>(make_Pane(filename,false));
                newItem.setExpanded(true);
                item.getChildren().add(newItem);

                FileSub.mkdir(filename);
                //对后端数据的修改

                FileSub.currentpath=printcurrent();
/*                System.out.println(FileSub.currentpath);*/
                folderStage.close();//关闭窗口
            }
        });
        bt2.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
            @Override
            public void handle(MouseEvent mouseEvent) {
                if(mouseEvent.getButton()!=MouseButton.PRIMARY)
                    return;
                folderStage.close();
            }
        });
    }
    public String  printcurrent()//获取当前路径
    {
        String road = "ROOT";
        for(TreeItem<Pane> it=item;!((Label)it.getValue().getChildren().get(0)).getText().equals("ROOT");it=it.getParent())
        {
            road=road+'\\'+((Label)item.getValue().getChildren().get(0)).getText();
        }
        return  road;
    }

}