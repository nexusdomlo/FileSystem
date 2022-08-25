package com.example.filesystem;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
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
import javafx.stage.WindowEvent;

import java.io.UnsupportedEncodingException;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;

public class HelloController {
/*    @FXML
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
    private ImageView Tips;
    @FXML
    private MenuItem menuItem4;*/

    @FXML
    private AnchorPane mainInterface;
    public TreeItem<Pane> item;
    //帮助按钮点击事件
    @FXML
    void initIllustrate() {
        //滚动面板
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
    void State() {
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
        //自动换行
        textarea.setWrapText(true);
        //初始化设置行数
        textarea.setPrefRowCount(20);
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
    public static  PieChart pieChart;
    public static void addPie(double x,double y,Pane commandPane)
    {
        int count=0;
        for(int i=0;i< FileSub.FatTable.IndexArray.length;i++)
        {
            if(FileSub.FatTable.IndexArray[i]!=0)
                count++;
        }
        DecimalFormat df=new DecimalFormat("0.000");//控制小数点之后的位数
        //饼图
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("已使用"+df.format(count*100.0/128)+"%",count*100.0/128),
                        new PieChart.Data("未使用"+df.format(100.0-count*100.0/128)+"%",100.0-count*100.0/128));
        pieChart = new PieChart(pieChartData);
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
    public void updatePie()
    {
        commandPane.getChildren().remove(pieChart);
        int count=0;
        for(int i=0;i< FileSub.FatTable.IndexArray.length;i++)
        {
            if(FileSub.FatTable.IndexArray[i]!=0)
                count++;
        }
        DecimalFormat df=new DecimalFormat("0.000");//控制小数点之后的位数
        ObservableList<PieChart.Data> pieChartData =
                FXCollections.observableArrayList(
                        new PieChart.Data("已使用"+df.format(count*100.0/128)+"%",count*100.0/128),
                        new PieChart.Data("未使用"+df.format(100.0-count*100.0/128)+"%",100.0-count*100.0/128));
        pieChart = new PieChart(pieChartData);
        commandPane.getChildren().add(pieChart);
        pieChart.setLabelsVisible(false);
        pieChart.setPrefSize(220,220);
        pieChart.setLayoutY(580);
        pieChart.setLayoutX(250);
        //设置图表旋转
        pieChart.setClockwise(false);
        pieChart.setStartAngle(90);
    }

    public GridPane myPane = new GridPane();
    public void addDiskUsing(double x,double y,Pane commandPane)
    {
        //diskUsing区域
        List<StackPane> disk = new ArrayList();
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
            disk.add(i, stackPane);
            myPane.add(stackPane, i % 8, i / 8);
        }
        myPane.setLayoutY(y);
        myPane.setLayoutX(x);
        commandPane.getChildren().add(myPane);
    }
    public void changeDiskusing()
    {
        for(int i = 0; i < 128; ++i) {
            StackPane X=(StackPane) myPane.getChildren().get(i);
            if(FileSub.Disk.blocks[i].object!=null)
                X.setStyle("-fx-background-color: #00ff00");
            else
                X.setStyle("-fx-background-color: #c8c8c8");

        }
    }
    Pane commandPane = new Pane();
    //存储按钮
    @FXML
    void Store(MouseEvent event) {
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
        Writebox.getChildren().clear();
        Writefield(260,0,commandPane);
        addFAT(780,0,commandPane);
        startStage.show();
        FileSub.currentpath="ROOT";
        changeDiskusing();
        changeFAT();
    }
    public VBox numberBox = new VBox();
    public VBox contentBox = new VBox();
    public void addFAT(double x,double y,Pane commandPane)
    {
        //FAT表
        Label[] numberLabel = new Label[128];
        Label[] contentLabel = new Label[128];
        HBox hBox = new HBox(new Node[]{numberBox, contentBox});
        hBox.setSpacing(5.0D);
        ScrollPane scrollPane = new ScrollPane(hBox);
        scrollPane.setMaxHeight(750.0D);
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
    public void changeFAT()
    {
        for(int i = 0; i < 128; ++i) {
            StackPane stackPane=(StackPane) contentBox.getChildren().get(i);
            ((Label)stackPane.getChildren().get(0)).setText(String.valueOf(FileSub.FatTable.IndexArray[i]));
            if(FileSub.FatTable.IndexArray[i]!=0)
                stackPane.setStyle("-fx-background-color: #00ff00");
            else
                stackPane.setStyle("-fx-background-color: #c8c8c8");
        }
    }
    public void errormessage(String message)
    {
        Label exist=new Label("---"+message+"---");
        exist.setPrefSize(400,10);
        Writebox.getChildren().add(exist);
        AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
        setposLabel(FileSub.currentpath);
        setfieldPane();
    }
    public AnchorPane Writebox = new AnchorPane();//用AnchorPane设置输入栏的文字和位置
    public void setfieldPane()
    {
        TextField field = new TextField();
        //设置单行输入框的宽高
        field.setPrefSize(200,10);
        //能否编辑
        field.setEditable(true);
        //单行输入框的提示语
        field.setPromptText("请输入指令");
        //设置单行输入框的对齐方式
        field.setAlignment(Pos.CENTER_LEFT);
        //设置单行输入框的推荐列数
        field.setPrefColumnCount(11);
        Label pa=(Label) Writebox.getChildren().get(Writebox.getChildren().size()-1);
        Writebox.getChildren().add(field);
        AnchorPane.setLeftAnchor(field,pa.getText().length()*7.0);
        AnchorPane.setTopAnchor(field,7.0+(Writebox.getChildren().size()-3)*20);//设置添加的两个元素在Anchorpane中的位置
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
                            File X=FileSub.findfile(FileSub.currentpath+"\\"+orderString[1]);//在输入端只能通过当前位置来查找
                            if(X!=null)
                            {
                                openfile(X);
                                field.setEditable(false);
                            }
                            else {
                                Label exist=new Label("---不存在文件"+orderString[1]+"---");
                                exist.setPrefSize(400,10);
                                field.setEditable(false);
                                Writebox.getChildren().add(exist);
                                AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();
                            }
                        }
                        else
                        {
                            boolean exist=FileSub.findFolder(FileSub.currentpath+"\\"+orderString[1].substring(1));
                            if(!exist)
                            {
                                Label labels=new Label("---不存在文件夹"+orderString[1]+"---");
                                labels.setPrefSize(400,10);
                                Writebox.getChildren().add(labels);
                                AnchorPane.setTopAnchor(labels,15.0+(Writebox.getChildren().size()-3)*20);
                                field.setEditable(false);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();
                            }
                            else
                            {
                                openfolder(orderString[1].substring(1));//执行打开文件的操作
                                field.setEditable(false);
                            }

                        }
                    }
                    else if(orderString[0].equals("create"))
                    {
                        if(orderString[1].equals("file"))//操作的是文件
                        {
                            createFile();//弹出弹框进行操作
                            updatePie();
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                        else if(orderString[1].equals("folder")){
                            createFolder();
                            updatePie();
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                        else{
                            Label exist=new Label("---错误的指令，请输入正确的指令---");
                            exist.setPrefSize(400,10);
                            Writebox.getChildren().add(exist);
                            AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                    }
                    else if(orderString[0].equals("close"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            FileSub.close_file(orderString[1]);
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                        //没有close文件夹的操作
                    }
                    else if(orderString[0].equals("delete"))
                    {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            if(!FileSub.delete_file(orderString[1]))
                            {
                                errormessage("不存在该文件，请检查输入的文件名");
                                return;
                            }
                            String[] pos=(FileSub.currentpath+"\\"+orderString[1]).split("\\\\");
                            TreeItem<Pane> X=root;
                            //如果是root直接用
                            for (String po : pos) {
                                for (int j = 0; j < X.getChildren().size(); j++) {
                                    if (((Label) X.getChildren().get(j).getValue().getChildren().get(0)).getText().equals(po)) {
                                        X = X.getChildren().get(j);//迭代
                                        break;
                                    }

                                }
                            }
                            item=X;
                            delete_file_or_folder();
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                            flashWindows(orderString[1]);
                            updatePie();
                        }
                        else{
/*                            System.out.println(FileSub.currentpath+"\\"+orderString[1].substring(1));*/
                            if(!FileSub.removedir(orderString[1].substring(1)))
                            {
                                errormessage("不存在该文件夹，请检查输入的文件夹名");
                                return;
                            }
                            String[] pos=(FileSub.currentpath+"\\"+orderString[1].substring(1)).split("\\\\");

                            TreeItem<Pane> X=root;
                            //如果是root直接用
                            for (String po : pos) {
                                for (int j = 0; j < X.getChildren().size(); j++) {
                                    if (((Label) X.getChildren().get(j).getValue().getChildren().get(0)).getText().equals(po)) {
                                        X = X.getChildren().get(j);//迭代
                                        break;
                                    }

                                }
                            }
                            item=X;
                            delete_file_or_folder();
                            field.setEditable(false);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                            flashWindows(orderString[1].substring(1));
                            updatePie();
                        }
                    }
                    else if(orderString[0].equals("search")) {
                        if(orderString[1].charAt(0)!='$')//操作的是文件
                        {
                            File X=FileSub.typefile(orderString[1]);
                            Label exist=new Label("---存在文件"+orderString[1]+"---");
                            exist.setPrefSize(400,10);
                            if(X==null)
                            {
                                exist=new Label("---不存在文件"+orderString[1]+"---");
                                Writebox.getChildren().add(exist);
                                AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();
                            }
                            else{
                                Label filetype=new Label("文件类型:"+X.type);
                                filetype.setPrefSize(400,10);
                                Label filesize=new Label("文件大小:"+X.size);
                                filesize.setPrefSize(400,10);
                                Writebox.getChildren().add(exist);
                                AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                                Writebox.getChildren().add(filetype);
                                AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
                                Writebox.getChildren().add(filesize);
                                AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();
                            }

                        }
                        else{
                            Folder X=FileSub.showdir(orderString[1].substring(1));
                            Label exist=new Label("---存在文件夹"+orderString[1].split("\\.")[0]+"---");
                            exist.setPrefSize(400,10);
                            if(X==null)
                            {
                                exist=new Label("---不存在文件夹"+orderString[1].split("\\.")[0]+"---");
                                Writebox.getChildren().add(exist);
                                AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();

                            }
                            else
                            {
                                Label filetype=new Label("文件类型:"+X.type);
                                filetype.setPrefSize(400,10);
                                Label filesize=new Label("文件大小:"+X.size);
                                filesize.setPrefSize(400,10);
                                Writebox.getChildren().add(exist);
                                AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                                Writebox.getChildren().add(filetype);
                                AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
                                Writebox.getChildren().add(filesize);
                                AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
                                setposLabel(FileSub.currentpath);
                                setfieldPane();
                            }
                        }
                        field.setEditable(false);
                        setposLabel(FileSub.currentpath);
                        setfieldPane();
                    }
                    else if(orderString[0].equals("change"))
                    {
                        String[] changeorder=orderString[1].split("\\.");
                        FileSub.change(changeorder[0],changeorder[1]);
                        field.setEditable(false);
                        setposLabel(FileSub.currentpath);
                        setfieldPane();
                        //只能够改变文件
                    }
                    else if(orderString[0].equals("clear"))
                    {
                        Writebox.getChildren().clear();
                        Writefield(260,0,commandPane);
                    }
                    else if(orderString[0].equals("cd")){
                        Label exist=new Label("---路径不存在---");
                        exist.setPrefSize(400,10);
                        if(!FileSub.findFolder(orderString[1]))
                        {
                            Writebox.getChildren().add(exist);
                            AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                        else{
                            FileSub.currentpath=orderString[1];
                            setposLabel(FileSub.currentpath);
                            setfieldPane();
                        }
                        FileSub.currentpath=orderString[1];

                    }
                    else
                    {
                        Label exist=new Label("---错误的指令，请输入正确的指令---");
                        exist.setPrefSize(400,10);
                        Writebox.getChildren().add(exist);
                        AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                        field.setEditable(false);
                        setposLabel(FileSub.currentpath);
                        setfieldPane();
                    }
                    changeDiskusing();
                    changeFAT();
                }
            }
        });
    }
    public void flashWindows(String name)
    {
        for(int i=9;i<mainInterface.getChildren().size();i+=2)
        {
            System.out.println(mainInterface.getChildren().get(i).getClass().toString());
            if(mainInterface.getChildren().get(i).getClass().toString().equals("class javafx.scene.control.Label"))
            {
                if(((Label)mainInterface.getChildren().get(i)).getText().equals(name))
                {
                    System.out.println(1);
                    mainInterface.getChildren().remove(i);
                    mainInterface.getChildren().remove(i-1);
                    i-=2;
                }
                else{
                    AnchorPane.setTopAnchor(mainInterface.getChildren().get(i-1),10.0+((i-3)%28/2)*65);
                    AnchorPane.setLeftAnchor(mainInterface.getChildren().get(i-1),10.0+(i/32)*65);
                    AnchorPane.setTopAnchor(mainInterface.getChildren().get(i),55.0+((i-3)%28/2)*65);
                    AnchorPane.setLeftAnchor(mainInterface.getChildren().get(i),19.0+(i/32)*65);
                }
            }
        }
    }

    public void setposLabel(String pathname)
    {
        Label label = new Label(pathname+":\\>");
        label.setPrefSize(400,13);//设置文字的大小
        Writebox.getChildren().add(label);
        AnchorPane.setLeftAnchor(label,0.0);
        AnchorPane.setTopAnchor(label,10.0+(Writebox.getChildren().size()-2)*20);
    }
    public  void Writefield(double x,double y,Pane commandPane)
    {
        //输入指令部分
        //单行输入框
        Label mingningLabel=new Label("命令行");
        mingningLabel.setPrefSize(200,0);
        mingningLabel.setStyle("-fx-font-weight:bold");
        mingningLabel.setStyle("-fx-font-size:20");
        Button bangzhu=new Button("提示");
        bangzhu.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>(){
            @Override
            public void handle(MouseEvent mouseEvent) {
                Pane filePane=new Pane();
                TextArea textArea=new TextArea();
                textArea.setText("提示");
                textArea.setPrefColumnCount(63);
                textArea.setPrefRowCount(38);
                textArea.setWrapText(true);

                textArea.setText("命令行指令:\n" +
                        "create file    功能:创建文件\n" +
                        "create folder    功能:创建文件夹\n" +
                        "open 需要打开的文件名称或文件夹    功能:打开文件或文件夹\n" +
                        "close 需要关闭的文件名称    功能:关闭文件\n" +
                        "delete 需要删除的文件或者文件夹    功能:删除文件或者文件夹\n" +
                        "change 文件名 文件类型    功能:将文件改为其他类型\n" +
                        "search 文件名或者文件夹名    功能:将文件的类型显示出来\n" +
                        "clear    功能:清空之前写过的内容\n"
                );
                textArea.setStyle("-fx-font-weight:bold");
                textArea.setStyle("-fx-font-size:20");
                textArea.setEditable(false);
                filePane.getChildren().add(textArea);
                Scene fileScene = new Scene(filePane,600,600);//设置窗口的大小
                Stage fileStage = new Stage();
                fileStage.setScene(fileScene);
                fileStage.setTitle("提示");//设置窗口的标题
                fileStage.show();
            }
        });
        Writebox.getChildren().add(bangzhu);
        Writebox.getChildren().add(mingningLabel);
        setposLabel(FileSub.currentpath);
        setfieldPane();
        AnchorPane.setLeftAnchor(mingningLabel,200.0);
        AnchorPane.setTopAnchor(mingningLabel,0.0);
        AnchorPane.setLeftAnchor(bangzhu,450.0);
        AnchorPane.setTopAnchor(bangzhu,0.0);

        ScrollPane scrollPane=new ScrollPane();
        scrollPane.setContent(Writebox);
        scrollPane.setLayoutX(x);
        scrollPane.setLayoutY(y);//设置整一个输入栏（包括文字和输入文字框）的位置
        scrollPane.setHbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setVbarPolicy(ScrollPane.ScrollBarPolicy.AS_NEEDED);
        scrollPane.setPrefSize(510,450);
        commandPane.getChildren().add(scrollPane);


    }
    public void delete_file_or_folder()
    {
        TreeItem<Pane> Par=item.getParent();
        Par.getChildren().remove(item);
        changeFAT();
        changeDiskusing();
    }

    public void openfile(File X) {
        if(X==null) {
            Label exist;
            exist=new Label("---不存在该文件---");
            exist.setPrefSize(400,10);
            Writebox.getChildren().add(exist);
            AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
            setposLabel(FileSub.currentpath);
            setfieldPane();
        }
        else {
            Pane filePane=new Pane();
            TextArea textArea=new TextArea();
            textArea.setText(X.content);
            textArea.setPrefColumnCount(63);
            textArea.setPrefRowCount(38);
            textArea.setWrapText(true);
            filePane.getChildren().add(textArea);
            Scene fileScene = new Scene(filePane,800,600);//设置窗口的大小
            Stage fileStage = new Stage();
            fileStage.setScene(fileScene);
            fileStage.setTitle(X.fileName);//设置窗口的标题
            fileStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {

                    try {
                        X.changeFileContent(textArea.getText());
                        updatePie();
                        changeFAT();
                        changeDiskusing();
                    } catch (UnsupportedEncodingException e) {
                        throw new RuntimeException(e);
                    }
                }
            });
            fileStage.show();
        }

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
                FIV.setFitHeight(40);
                FIV.setFitWidth(40);
                Label FLname=new Label(((File)FileSub.F.children.get(i)).fileName);
                FIV.setLayoutX(15.0+i*30);
                FIV.setLayoutY(10);
                FLname.setLayoutX(27.0+i*30);
                FLname.setLayoutY(50);
                filePane.getChildren().add(FIV);
                filePane.getChildren().add(FLname);
                int finalI = i;
                FIV.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        if(mouseEvent.getClickCount()==2)
                            openfile((File)FileSub.F.children.get(finalI));
                    }
                });
            }
            else if(FileSub.F.children.get(i) instanceof Folder){
                Image fl=new Image("File.png");
                ImageView FIV=new ImageView();
                FIV.setImage(fl);
                FIV.setFitHeight(40);
                FIV.setFitWidth(40);
                Label FLname=new Label(((Folder)FileSub.F.children.get(i)).folderName);
                FIV.setLayoutX(15.0+i*30);
                FIV.setLayoutY(10);
                FLname.setLayoutX(27.0+i*30);
                FLname.setLayoutY(50);
                filePane.getChildren().add(FIV);
                filePane.getChildren().add(FLname);
                FIV.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        openfolder(FLname.getText());
                    }
                });
            }
            //设置文件图片和标签的位置
        }
        Scene fileScene = new Scene(filePane,600,400);//设置窗口的大小
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle(foldername);//设置窗口的标题
        fileStage.show();

    }


    //添加右键菜单
    public class FileMenu extends ContextMenu {
        public  String name;
        public File X;
        public FileMenu(){
            MenuItem fileopenItem=new MenuItem("打开");
            fileopenItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    openfile(X);
                }
            });
            MenuItem filedeleteItem=new MenuItem("删除");
            filedeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.delete_pathfile(printcurrent());
                    delete_file_or_folder();
                    flashWindows(name);
                    updatePie();
                }
            });
            MenuItem filetypeItem=new MenuItem("属性");
            filetypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    File X=FileSub.typefile(name);
                    Label exist=new Label("---存在文件"+name+"---");
                    exist.setPrefSize(400,10);
                    Label filetype=new Label("文件类型:"+X.type);
                    filetype.setPrefSize(400,10);
                    Label filesize=new Label("文件大小:"+X.size);
                    filesize.setPrefSize(400,10);
                    Writebox.getChildren().add(exist);
                    AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                    Writebox.getChildren().add(filetype);
                    AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
                    Writebox.getChildren().add(filesize);
                    AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
                    setposLabel(FileSub.currentpath);
                    setfieldPane();
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
                    openfolder(name);
                }
            });
            MenuItem folderdeleteItem = new MenuItem("删除");
            folderdeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.removepathdir(printcurrent());
                    //点击删除按钮后执行remove和delete函数
                    delete_file_or_folder();
                    flashWindows(name);
                    updatePie();
                }
            });
            MenuItem foldertypeItem = new MenuItem("属性");
            foldertypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //展示文件夹属性
                    Folder X=FileSub.showdir(name);
                    Label exist=new Label("---存在文件夹"+name+"---");
                    exist.setPrefSize(400,10);
                    Label filetype=new Label("文件类型:"+X.type);
                    filetype.setPrefSize(400,10);
                    Label filesize=new Label("文件大小:"+X.size);
                    filesize.setPrefSize(400,10);
                    Writebox.getChildren().add(exist);
                    AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
                    Writebox.getChildren().add(filetype);
                    AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
                    Writebox.getChildren().add(filesize);
                    AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
                    setposLabel(FileSub.currentpath);
                    setfieldPane();
                }
            });
            MenuItem folderMenuItem1 = new MenuItem("新建文件");
            folderMenuItem1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFile();
                    updatePie();
                }
            });

            MenuItem folderMenuItem2 = new MenuItem("新建文件夹");
            folderMenuItem2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFolder();
                    updatePie();
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
                    updatePie();
                }
            });
            
            MenuItem folderMenuItem = new MenuItem("新建文件夹");
            folderMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFolder();
                    updatePie();//点击新建文件夹之后新启动一个画面，来输入对应的信息进行新建
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
                if(!((TreeItem<?>) treeView.getSelectionModel().getSelectedItem()).expandedProperty().get())//如果点击的是文件
                {
                    if(event.getClickCount()==2)//设置双击的事件
                    {
                        item=(TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem();

                        File X=FileSub.findfile(printcurrent());
                        openfile(X);//双击的话就点击打开窗口
                    }
                    if(event.getButton()==MouseButton.SECONDARY)//设置右键点击的事件  开启一个新的弹窗
                    {
                        item=(TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem();
                        FileMenu L=new FileMenu();//特地用给file的弹窗类
                        /*System.out.println(printcurrent());*/
                        L.name=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();//将文件名字给到类中
                        L.X=FileSub.findfile(printcurrent());
                        L.show(node,Side.BOTTOM,0,0);
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
                        X.name=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();
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
                if(filename.contains("/")||filename.contains(".")||filename.contains("$")||filename.length()>3||filename.length()<=0)
                {
                    ErrorWindows("新建文件错误","文件名中字符错误");
                    return;
                }
                TreeItem<Pane> newItem=new TreeItem<>(make_Pane(filename,true));
                newItem.setExpanded(false);
                SetItem();//获取当前应当选中的item
                item.getChildren().add(newItem);
                int index=FileSub.findFAT(3);//找到对应的空闲的盘块
                FileSub.findFolder(printcurrent());//修改当前路径下的所在的文件夹
                File X=new File(filename,printcurrent()+"\\"+filename,index,FileSub.F,0,1);
                boolean can=FileSub.F.addChildrenNode(X);//当前的文件夹数据添加文件节点，并更新数据
                if(!can)
                {
                    Label label=new Label("文件夹内容不能超过8个");
                    Writebox.getChildren().add(label);
                    AnchorPane.setTopAnchor(label,15.0+((Writebox.getChildren().size()-3))*20);
                    fileStage.close();//点击确定后关闭界面
                    return;
                }
                FileSub.Disk.blocks[index].BlockChange(-1,X,true);//更改对应的磁盘块的内容
                FileSub.FatTable.IndexArray[index]=-1;//修改FAT表
                //对后端数据的修改
                //在桌面设置文件的图片
                Image FilePicture=new Image("Txt.png");
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
                changeDiskusing();
                changeFAT();//关闭页面后FAT和磁盘使用进行更新
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
                SetItem();//获取当前应当选中的item
                item.getChildren().add(newItem);
                FileSub.mkpathdir(filename,printcurrent());
                //对后端数据的修改
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
                            FolderMenu X=new FolderMenu();
                            X.show(FP,Side.BOTTOM,0,0);
                        }
                    }
                });
                folderStage.close();//关闭窗口
                changeDiskusing();
                changeFAT();//关闭窗口后更新
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
    public void SetItem()
    {
        String[] pos=FileSub.currentpath.split("\\\\");
        if(item==null)
        {
            TreeItem<Pane> X=root;
            //如果是root直接用
            for (String po : pos) {
                for (int j = 0; j < X.getChildren().size(); j++) {
                    if (((Label) X.getChildren().get(j).getValue().getChildren().get(0)).getText().equals(po)) {
                        X = X.getChildren().get(j);//迭代
                        break;
                    }

                }
            }
            item=X;
        }
    }
    public String  printcurrent()//获取当前路径
    {
        String road = "";
        for(TreeItem<Pane> it=item;!((Label)it.getValue().getChildren().get(0)).getText().equals("ROOT");it=it.getParent())
        {
            road="\\"+((Label)it.getValue().getChildren().get(0)).getText()+road;
        }
        return  "ROOT"+road;
    }

}