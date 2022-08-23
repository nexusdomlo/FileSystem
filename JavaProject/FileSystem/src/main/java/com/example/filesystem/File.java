package com.example.filesystem;

import java.io.UnsupportedEncodingException;

public class File {
    String fileName;//文件名
    int flag;//只读/读写标志
    int num;//记录文件的起始盘块号
    String content;//文档中保存的字符串
    double size;//占用的字节大小
    Folder parent;//父母文件夹
    String path;//路径S
    boolean open;//记录是否打开
    int type;//记录文件的属性
    public File(String fileName,String path,int num,Folder parent,int type,int flag)//新建文件操作的后端方法
    {
        this.fileName=fileName;//可以从界面中输入的值来获取
        this.path=path;//根据界面输入的时候的路径来获取
        this.num=num;
        this.parent=parent;
        this.content="";
        this.open=false;
        this.size=0;//初始化为占用
        this.type=type;
        this.flag=flag;
        disk D=FileSub.Disk;
    /*    FAT fatTable = FileSub.FatTable;*/
        D.blocks[num].BlockChange(-1,this,true);
        //初始化一个文件，首先这个文件没有写任何的内容，直接index填上-1，object改成this(一般就是文件夹类和文件类），在哪个地方创建就填哪个，是否是begin文件，一般初始化第一个文件就是选择true
    }


    public void changeFileContent(String content) throws UnsupportedEncodingException//从前段里面提取相应的字符串
    {
        this.content=content;//更改对应的字符串
        size=content.getBytes("gbk").length;
    }

}
