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
import java.sql.*;
import java.io.*;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
public class HelloController {
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
                X.content=rs.getString("fcontent");
                int index=rs.getInt("findex");
                FileSub.FatTable.IndexArray[rs.getInt("fnum")]=index;
                if(rs.getBoolean("fbegin"))
                {
                    parent.addChildrenNode(X);
                    TreeItem<Pane>i=new TreeItem<>(make_Pane(X.fileName,true));
                    it.getChildren().add(i);
                }
                FileSub.Disk.blocks[rs.getInt("fnum")].BlockChange(index,X,rs.getBoolean("fbegin"));
                //设置文本的图标位置
            }
            else{
                Folder folder=new Folder(rs.getString("fname"),rs.getString("fpath"),rs.getInt("fbeginnum"),parent);
                FileSub.Disk.blocks[rs.getInt("fnum")].BlockChange(rs.getInt("findex"),folder,rs.getBoolean("fbegin"));
                TreeItem<Pane>i=new TreeItem<>(make_Pane(folder.folderName,true));
                FileSub.FatTable.IndexArray[rs.getInt("fnum")]=rs.getInt("findex");
                if(root==null)
                {
                    root=i;
                    operDatabase(folder.folderName,FileSub.Disk.root,i);
                }
                else if(it!=null)
                {
                    it.getChildren().add(i);
                    parent.addChildrenNode(folder);
                    operDatabase(folder.folderName,folder,i);
                }
                //递归的寻找并构建
            }
        }

    }
    public void setDiskblocksindex()
    {
        for(int i=0;i<FileSub.Disk.blocks.length;i++)
        {
            if(FileSub.Disk.blocks[i].object==null)
                continue;
            FileSub.Disk.blocks[i].index=FileSub.FatTable.IndexArray[i];
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
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private AnchorPane mainInterface;
    public static TreeItem<Pane> item;
    public void setpicture()
    {
        for(int i=3;i<FileSub.Disk.blocks.length;i++)
        {
            if(FileSub.Disk.blocks[i].begin)
            {
                if(FileSub.Disk.blocks[i].object==null)
                    continue;
                if(FileSub.Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.Folder"))
                {
                    Folder folder=(Folder)FileSub.Disk.blocks[i].object;
                    Image FilePicture=new Image("File.png");
                    ImageView FP=new ImageView();
                    FP.setImage(FilePicture);
                    FP.setFitWidth(40);
                    FP.setFitHeight(40);
                    Label label=new Label(folder.folderName);
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
                                X.folder=folder;
                                X.name=folder.folderName;
                                X.show(FP,Side.BOTTOM,0,0);
                            }
                        }
                    });
                }
                else if(FileSub.Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.File")){
                    File X=(File)FileSub.Disk.blocks[i].object;
                    Image FilePicture=new Image("Txt.png");
                    ImageView FP=new ImageView();
                    FP.setImage(FilePicture);
                    FP.setFitWidth(40);
                    FP.setFitHeight(40);
                    Label label=new Label(X.fileName);
                    mainInterface.getChildren().addAll(FP,label);
                    AnchorPane.setTopAnchor(FP,10.0+((mainInterface.getChildren().size()-3)%28/2)*65);
                    AnchorPane.setLeftAnchor(FP,10.0+(mainInterface.getChildren().size()/32)*65);
                    AnchorPane.setTopAnchor(label,55.0+((mainInterface.getChildren().size()-3)%28/2)*65);
                    AnchorPane.setLeftAnchor(label,19.0+(mainInterface.getChildren().size()/32)*65);
                    FP.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                        @Override
                        public void handle(MouseEvent mouseEvent) {
                            if(mouseEvent.getButton()== MouseButton.SECONDARY)
                            {
                                FileMenu G=new FileMenu();
                                G.file =X;
                                G.name=X.fileName;
                                G.show(FP, Side.BOTTOM,0,0);
                            }
                        }
                    });
                }
            }
        }
    }
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


    public static  String sqlpath(String path)
    {
        String[] sqlp=path.split("\\\\");
        String answer="ROOT";
        for(int i=1;i<sqlp.length;i++){
            answer=answer+"\\\\"+sqlp[i];
        }
        return answer;
    }

    public static void Keepdata(){
        try{
            Class.forName("com.mysql.cj.jdbc.Driver");
            Connection conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306?characterEncoding=UTF-8","root","22371X");
            Statement state=conn.createStatement();
            String sql_order="select * from information_schema.SCHEMATA where SCHEMA_NAME = 'filesystem';";
            ResultSet rs=state.executeQuery(sql_order);
            if(!rs.next())
            {
                sql_order="create database FileSystem;";
                state.execute(sql_order);//没有数据库就创建一个新的数据库
            }
            conn=DriverManager.getConnection("jdbc:mysql://127.0.0.1:3306/filesystem?characterEncoding=UTF-8","root","22371X");
            state=conn.createStatement();
            DatabaseMetaData metaData= conn.getMetaData();
            rs=metaData.getTables(null,null,"Disk",null);
            if(rs.next())
                state.execute("drop table Disk");
            sql_order="create table Disk(fnum INT,fbeginnum INT,findex INT,fname varchar(10),ftype INT,fbegin BIT,fpath varchar(100),fcontent varchar(400),fparent varchar(4),fsize INT);";
            state.execute(sql_order);//创建一个table 名字叫disk，数据类型分别是num，初始盘块号，name 文件或者是文件夹的名字，type 数据类型（判断他是文件还是文件夹),begin是否为初始盘块，path为路径，content 是内容，parent是父目录项，size是大小，item是子节点
            int num=0;
            String name="";
            int type=0;
            int index=0;
            int beginnum=0;
            boolean begin=true;
            String path="";
            String content="";
            String parent="";
            int size=0;
            for(int i=0;i<FileSub.Disk.blocks.length;i++)
            {
                num=i;//记录这是第几块
                if(FileSub.Disk.blocks[i].object==null)
                    continue;
                else if(FileSub.Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.Folder"))
                {
                    Folder X=(Folder)FileSub.Disk.blocks[i].object;
                    beginnum=X.num;//记录起始盘块号
                    index=FileSub.Disk.blocks[i].index;
                    name=X.folderName;
                    type=X.type;
                    begin=FileSub.Disk.blocks[i].begin;//判断这是不是第一块
                    path=sqlpath(X.path);
                    content="";
                    if(X.parent==null)
                        parent="null";
                    else
                        parent=X.parent.folderName;
                    size=64;
                }
                else if(FileSub.Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.File")){
                    File X=(File)FileSub.Disk.blocks[i].object;
                    beginnum=X.num;//记录起始盘块号
                    index=FileSub.Disk.blocks[i].index;
                    name=X.fileName;
                    type=X.type;
                    begin=FileSub.Disk.blocks[i].begin;
                    path=sqlpath(X.path);
                    content=X.content;
                    if(X.parent==null)
                        parent="null";
                    else
                        parent=X.parent.folderName;
                    size=X.size;
                }
                sql_order="insert into Disk(fnum,fbeginnum,findex,fname,ftype,fbegin,fpath,fcontent,fparent,fsize) values("+num+','+beginnum+','+index+",'"+name+"'"+','+type+','+begin+",'"+path+"'"+",'"+content+"'"+",'"+parent+"'"+','+size+");";
                state.execute(sql_order);
            }
        } catch (ClassNotFoundException | SQLException e) {
            e.printStackTrace();
        }
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
    public static TreeItem<Pane> root=null;//在treeitem中初始化一个root节点
    public TreeView treeView;
    public void addTreeitem(Pane commandPane)
    {
        //左侧树形区域
        if(root==null)
            root=new TreeItem<>(make_Pane("ROOT",true));
        treeView=new TreeView<>(root);
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
    public static void updatePie()
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

    public static GridPane myPane = new GridPane();
    public void addDiskUsing(double x,double y,Pane commandPane)
    {
        //diskUsing区域
        List<StackPane> disk = new ArrayList();
        myPane=new GridPane();
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
    public static void changeDiskusing()
    {
        for(int i = 0; i < 128; ++i) {
            StackPane X=(StackPane) myPane.getChildren().get(i);
            if(FileSub.Disk.blocks[i].object!=null) {
                X.setStyle("-fx-background-color: red");
            }            else
                X.setStyle("-fx-background-color: #c8c8c8");

        }
    }
    static Pane commandPane = new Pane();
    public static boolean initlazie=false;
    //存储按钮
    @FXML
    void Store(MouseEvent event) {
        if(initlazie) {//如果需要初始化就执行
            getData();
            setDiskblocksindex();
            setpicture();
            initlazie=false;
        }
        commandPane=new Pane();
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
        changeFAT();
        changeDiskusing();
/*        for(int i=0;i<FileSub.Disk.root.children.size();i++)
            System.out.println(1);*/
    }
    public VBox numberBox = new VBox();
    public static VBox contentBox = new VBox();
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
    public static void changeFAT()
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
    public void setmessage(String message)
    {
        Label exist=new Label("---"+message+"---");
        exist.setPrefSize(400,10);
        Writebox.getChildren().add(exist);
        AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
        setposLabel(FileSub.currentpath);
        setfieldPane();
    }
    public void setinputmessage(String path)
    {
        setposLabel(path);
        setfieldPane();
    }
    public static String GetFileType(int type)
    {
        String X="";
        while(type>0)
        {
            if(type%2==0)
            {
                X="0"+X;
            }
            else{
                X="1"+X;

            }
            type=type/2;
        }
        if(X.length()==4)
            return "目录项";
        else{
            if(X.length()==3)
            {
                if(X.charAt(2)=='0')
                    return "可读写的普通文件";
                else
                    return "只读的普通文件";
            }
            else{
                if(X.charAt(1)=='0')
                    return "可读写的系统文件";
                else
                    return "只读的系统文件";
            }
        }

    }
    public static AnchorPane Writebox = new AnchorPane();//用AnchorPane设置输入栏的文字和位置
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
                    switch(orderString[0]){
                        case "read":
                            if(orderString[1].charAt(0)!='$')//操作的是文件
                            {
                                File X = FileSub.findfile(FileSub.currentpath + "\\" + orderString[1]);//在输入端只能通过当前位置来查找
                                if (X != null) {
                                    openfile(X);
                                    FileSub.open_file(X.fileName,1);
                                    setinputmessage(FileSub.currentpath);
                                    field.setEditable(false);

                                } else {
                                    setmessage("不存在文件" + orderString[1]);
                                    field.setEditable(false);
                                }
                            }
                            else{
                                setmessage("错误的指令");
                                field.setEditable(false);
                            }
                            break;
                        case "open":
                            if(orderString[1].charAt(0)!='$')//操作的是文件
                            {
                                File X = FileSub.findfile(FileSub.currentpath + "\\" + orderString[1]);//在输入端只能通过当前位置来查找
                                if (X != null) {
                                    openfile(X);
                                    if (X.flag == 0) {
                                        FileSub.open_file(X.fileName, 1);//以读方式打开文件
                                        setinputmessage(FileSub.currentpath);
                                        field.setEditable(false);
                                    } else {
                                        FileSub.open_file(X.fileName, 0);
                                        setinputmessage(FileSub.currentpath);
                                        field.setEditable(false);
                                    }

                                } else {
                                    setmessage("不存在文件" + orderString[1]);
                                    field.setEditable(false);
                                }
                            }
                            else
                            {
                                boolean exist=FileSub.findFolder(FileSub.currentpath+"\\"+orderString[1].substring(1));//通过exist来查看文件夹是否存在
                                if(!exist)
                                {
                                    setmessage("不存在文件夹"+orderString[1]);
                                    field.setEditable(false);
                                }
                                else
                                {
                                    openfolder(FileSub.F);//执行打开文件的操作
                                    setinputmessage(FileSub.currentpath);
                                    field.setEditable(false);//setEditable可以让这个输入栏无法写入任何东西
                                }

                            }
                            break;
                        case "create":
                            if(orderString[1].equals("file"))//操作的是文件
                            {
                                createFile(1);//弹出弹框进行操作
                                updatePie();
                                field.setEditable(false);
                                setinputmessage(FileSub.currentpath);
                            }
                            else if(orderString[1].equals("folder")){
                                createFolder(1);
                                updatePie();
                                field.setEditable(false);
                                setinputmessage(FileSub.currentpath);
                            }
                            else{
                                setmessage("错误的指令，请输入正确的指令");
                                field.setEditable(false);
                            }
                            break;
                        case "close":
                            if(orderString[1].charAt(0)!='$')//操作的是文件
                            {
                                FileSub.close_file(orderString[1]);
                                field.setEditable(false);
                                setinputmessage(FileSub.currentpath);
                            }
                            else{
                                setmessage("错误的指令，请输入正确的指令");
                                field.setEditable(false);
                            }
                            break;
                            //没有close文件夹的操作
                        case "delete":
                            if(orderString[1].charAt(0)!='$')//操作的是文件
                            {
                                if(!FileSub.delete_file(orderString[1]))//如果FileSub中的delete函数返回false，那么说明不存在文件
                                {
                                    field.setEditable(false);
                                    setmessage("不存在该文件，请检查输入的文件名");
                                    return;
                                }
                                delete_file_or_folder(orderString[1]);
                                field.setEditable(false);
                                setinputmessage(FileSub.currentpath);
                                flashWindows(orderString[1]);
                                updatePie();
                            }
                            else{
                                if(!FileSub.removedir(orderString[1].substring(1)))
                                {
                                    setmessage("不存在该文件夹，请检查输入的文件夹名");
                                    return;
                                }
                                delete_file_or_folder(orderString[1].substring('$'));
                                field.setEditable(false);
                                setinputmessage(FileSub.currentpath);
                                flashWindows(orderString[1].substring(1));
                                updatePie();
                            }
                            break;
                        case "search":
                            if(orderString[1].charAt(0)!='$')//操作的是文件
                            {
                                File X=FileSub.typefile(orderString[1]);

                                if(X==null)
                                {
                                    setmessage("不存在文件"+orderString[1]);
                                    field.setEditable(false);
                                }
                                else{
                                    field.setEditable(false);
                                    searchfile(X);//运用同构函数，来实现不同输入值所实现的不同功能
                                }

                            }
                            else{
                                Folder X=FileSub.showdir(orderString[1].substring(1));
                                Label exist=new Label("---存在文件夹"+orderString[1].split("\\.")[0]+"---");
                                exist.setPrefSize(400,10);
                                if(X==null)
                                {
                                    setmessage("不存在文件"+orderString[1]);
                                    field.setEditable(false);
                                }
                                else
                                {
                                    field.setEditable(false);
                                    searchfile(X);
                                }
                            }
                            break;
                        case "change":
                            String[] changeorder=orderString[1].split("\\.");
                            if(!FileSub.change(changeorder[0],changeorder[1]))
                                setmessage("指令有误，请检查输入的指令");
                            else
                                setmessage("修改成功");
                            field.setEditable(false);
                            break;
                        case "clear":
                            Writebox.getChildren().clear();
                            Writefield(260,0,commandPane);
                            break;
                        case "cd":
                            if(!FileSub.findFolder(orderString[1]))
                            {
                                setmessage("路径不存在");
                                field.setEditable(false);
                            }
                            else{
                                FileSub.currentpath=orderString[1];
                                setinputmessage(FileSub.currentpath);
                            }
                            break;
                        default:
                            setmessage("错误的指令，请输入正确的指令");
                            field.setEditable(false);
                            break;
                    }
                    changeDiskusing();
                    changeFAT();
                }
            }
        });
    }
    public void searchfile(File X)
    {
        Label exist=new Label("---存在文件"+X.fileName+"---");
        exist.setPrefSize(400,10);
        Label filetype=new Label("文件类型:"+GetFileType(X.type));
        filetype.setPrefSize(400,10);
        Label filesize=new Label("文件大小:"+X.size);
        filesize.setPrefSize(400,10);
        Writebox.getChildren().add(exist);
        AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
        Writebox.getChildren().add(filetype);
        AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
        Writebox.getChildren().add(filesize);
        AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
        setinputmessage(FileSub.currentpath);
    }
    public void searchfile(Folder X)
    {
        Label exist=new Label("---存在文件夹"+X.folderName+"---");
        exist.setPrefSize(400,10);
        Label filetype=new Label("文件类型:"+GetFileType(X.type));
        filetype.setPrefSize(400,10);
        Label filesize=new Label("文件大小:"+X.size);
        filesize.setPrefSize(400,10);
        Writebox.getChildren().add(exist);
        AnchorPane.setTopAnchor(exist,15.0+(Writebox.getChildren().size()-3)*20);
        Writebox.getChildren().add(filetype);
        AnchorPane.setTopAnchor(filetype,15.0+(Writebox.getChildren().size()-3)*20);
        Writebox.getChildren().add(filesize);
        AnchorPane.setTopAnchor(filesize,15.0+((Writebox.getChildren().size()-3))*20);
        setinputmessage(FileSub.currentpath);
    }

    public void flashWindows(String name)
    {
        for(int i=9;i<mainInterface.getChildren().size();i+=2)
        {
            if(mainInterface.getChildren().get(i).getClass().toString().equals("class javafx.scene.control.Label"))
            {
                if(((Label)mainInterface.getChildren().get(i)).getText().equals(name))
                {
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

    public static void setposLabel(String pathname)
    {
        Label label = new Label(pathname+":\\>");
        label.setPrefSize(400,13);//设置文字的大小
        Writebox.getChildren().add(label);
        AnchorPane.setLeftAnchor(label,0.0);
        AnchorPane.setTopAnchor(label,10.0+(Writebox.getChildren().size()-2)*20);
    }
    public void Writefield(double x, double y, Pane commandPane)
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
    public void delete_file_or_folder(String name)
    {
        String[] pos=(FileSub.currentpath+"\\"+name).split("\\\\");//寻找出文件的位置
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
        TreeItem<Pane> Par=item.getParent();
        Par.getChildren().remove(item);
        changeFAT();
        changeDiskusing();
    }
    public void openfile(File X) {
        if(X==null) {
            setmessage("不存在该文件");
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
            if(X.flag==0)
            {
                fileStage.setTitle(X.fileName+"(读写)");//设置窗口的标题
                textArea.setEditable(true);
            }else{
                fileStage.setTitle(X.fileName+"(只读)");
                textArea.setEditable(false);
            }
            fileStage.setOnCloseRequest(new EventHandler<WindowEvent>() {
                @Override
                public void handle(WindowEvent windowEvent) {
                    if(!X.content.equals(textArea.getText()))
                    {
                        Dialog<ButtonType>dialog=new Dialog<>();
                        DialogPane dialogPane=dialog.getDialogPane();
                        dialog.setContentText("是否保存文本内容");
                        dialogPane.setPrefSize(300,100);
                        ObservableList<ButtonType>buttonTypes=dialogPane.getButtonTypes();
                        buttonTypes.addAll(ButtonType.OK,ButtonType.CANCEL);
                        Button apply=(Button) dialogPane.lookupButton(ButtonType.OK);
                        apply.setOnAction(actionEvent -> {
                            try {
                                X.changeFileContent(textArea.getText());
                                FileSub.Disk.blocks[X.num].begin=true;
                                updatePie();
                                changeFAT();
                                changeDiskusing();
                            } catch (UnsupportedEncodingException e) {
                                throw new RuntimeException(e);
                            }
                        });
                        Button cancel=(Button) dialogPane.lookupButton(ButtonType.CANCEL);
                        cancel.setOnAction(actionEvent -> {
                        });
                        dialog.showAndWait();
                    }
                }
            });
            fileStage.show();
        }

    }
    public void openfolder(Folder folder)
    {
        Pane filePane=new Pane();

        for(int i=0;i<folder.children.size();i++)
        {
            if(folder.children.get(i) instanceof File)//如果他属于文件类
            {
                Image fl=new Image("Txt.png");
                ImageView FIV=new ImageView();
                FIV.setImage(fl);
                FIV.setFitHeight(40);
                FIV.setFitWidth(40);
                Label FLname=new Label(((File)folder.children.get(i)).fileName);
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
                            openfile((File)folder.children.get(finalI));
                    }
                });
            }
            else if(folder.children.get(i) instanceof Folder){
                Image fl=new Image("File.png");
                ImageView FIV=new ImageView();
                FIV.setImage(fl);
                FIV.setFitHeight(40);
                FIV.setFitWidth(40);
                Label FLname=new Label(((Folder)folder.children.get(i)).folderName);
                FIV.setLayoutX(15.0+i*30);
                FIV.setLayoutY(10);
                FLname.setLayoutX(27.0+i*30);
                FLname.setLayoutY(50);
                filePane.getChildren().add(FIV);
                filePane.getChildren().add(FLname);
                int finalI1 = i;
                FIV.addEventFilter(MouseEvent.MOUSE_CLICKED, new EventHandler<MouseEvent>() {
                    @Override
                    public void handle(MouseEvent mouseEvent) {
                        openfolder((Folder)folder.children.get(finalI1));
                    }
                });
            }
            //设置文件图片和标签的位置
        }
        Scene fileScene = new Scene(filePane,600,400);//设置窗口的大小
        Stage fileStage = new Stage();
        fileStage.setScene(fileScene);
        fileStage.setTitle(folder.folderName);//设置窗口的标题
        fileStage.show();

    }


    //添加右键菜单
    public class FileMenu extends ContextMenu {
        public  String name;
        public File file;
        public FileMenu(){
            MenuItem fileopenItem=new MenuItem("打开");
            fileopenItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    openfile(file);
                }
            });
            MenuItem filedeleteItem=new MenuItem("删除");
            filedeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.delete_pathfile(file.path);
                    String[] pos=file.path.split("\\\\");
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
                    TreeItem<Pane> Par=item.getParent();
                    Par.getChildren().remove(item);
                    changeFAT();
                    changeDiskusing();
                    flashWindows(name);
                    updatePie();
                }
            });
            MenuItem filetypeItem=new MenuItem("属性");
            filetypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    searchfile(file);
                }
            });
            getItems().addAll(fileopenItem,filedeleteItem,filetypeItem);
        }
    }
    public class FolderMenu extends ContextMenu {
        public String name;
        public Folder folder;
        public FolderMenu() {
            MenuItem folderopenItem = new MenuItem("打开");
            folderopenItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    openfolder(folder);
                }
            });
            MenuItem folderdeleteItem = new MenuItem("删除");
            folderdeleteItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    FileSub.removepathdir(folder.path);
                    String[] pos=folder.path.split("\\\\");
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
                    //点击删除按钮后执行remove和delete函数
                    TreeItem<Pane> Par=item.getParent();
                    Par.getChildren().remove(item);
                    changeFAT();
                    changeDiskusing();
                    flashWindows(name);
                    updatePie();
                }
            });
            MenuItem foldertypeItem = new MenuItem("属性");
            foldertypeItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    //展示文件夹属性
                    searchfile(folder);
                }
            });
            MenuItem folderMenuItem1 = new MenuItem("新建文件");
            folderMenuItem1.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    SetItem(folder.path);
                    createFile(0);
                    updatePie();
                }
            });

            MenuItem folderMenuItem2 = new MenuItem("新建文件夹");
            folderMenuItem2.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    SetItem(folder.path);
                    createFolder(0);
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
                    createFile(0);
                    updatePie();
                }
            });
            
            MenuItem folderMenuItem = new MenuItem("新建文件夹");
            folderMenuItem.setOnAction(new EventHandler<ActionEvent>() {
                @Override
                public void handle(ActionEvent actionEvent) {
                    createFolder(0);
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
                        L.name=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();//将文件名字给到类中
                        L.file =FileSub.findfile(printcurrent());
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
                        item=(TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem();
                        if(event.getButton()!=MouseButton.SECONDARY)
                            return;
                        FolderMenu X=new FolderMenu();
                        X.name=((Label)((TreeItem<Pane>) treeView.getSelectionModel().getSelectedItem()).getValue().getChildren().get(0)).getText();
                        FileSub.findFolder(printcurrent());
                        X.folder=FileSub.F;
                        X.show(node,Side.BOTTOM,0,0);

                    }

                }
            }
        });
    }
    public static Pane make_Pane(String name, boolean is)
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
    public void createFile(int opertype){
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
                if(opertype==1)
                    SetItem(FileSub.currentpath);//获取当前应当选中的item
                item.getChildren().add(newItem);
                int index=FileSub.findFAT(3);//找到对应的空闲的盘块
                File X;
                if(opertype==1)
                {
                    FileSub.findFolder(FileSub.currentpath);//修改当前路径下的所在的文件夹
                    X=new File(filename,FileSub.currentpath+"\\"+filename,index,FileSub.F,4,0);
                }
                else{
                    FileSub.findFolder(printcurrent());//修改当前路径下的所在的文件夹
                    X=new File(filename,printcurrent()+"\\"+filename,index,FileSub.F,4,0);
                }
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
                            FileMenu G=new FileMenu();
                            G.file =X;
                            G.name=X.fileName;
                            G.show(FP,Side.BOTTOM,0,0);
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
    public static void ErrorWindows(String ErrorType, String ErrorString)
    {
        Alert alert=new Alert(Alert.AlertType.ERROR);
        alert.setTitle("错误");
        alert.setHeaderText(ErrorType);
        alert.setContentText(ErrorString);
        alert.showAndWait();
    }

    //新建文件夹窗口
    public void createFolder(int opertype){
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
                Folder Q;
                if(opertype==1)
                {
                    SetItem(FileSub.currentpath);//获取当前应当选中的item
                    Q=FileSub.mkpathdir(filename,FileSub.currentpath);
                }
                else
                    Q=FileSub.mkpathdir(filename,printcurrent());
                item.getChildren().add(newItem);
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
                            X.folder=Q;
                            X.name=Q.folderName;
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
    public static void SetItem(String path)
    {
        String[] pos=path.split("\\\\");
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
    public static String  printcurrent()//获取当前路径
    {
        String road = "";
        for(TreeItem<Pane> it=item;!((Label)it.getValue().getChildren().get(0)).getText().equals("ROOT");it=it.getParent())
        {
            road="\\"+((Label)it.getValue().getChildren().get(0)).getText()+road;
        }
        return  "ROOT"+road;
    }

}