package com.example.filesystem;

public class File {
    String fileName;//文件名
    int flag;//只读/读写标志
    int num;//记录文件的起始盘块号
    String content;//文档中保存的字符串
    double size;//占用的字节大小
    Folder parent;//父母文件夹
    String path;//路径
    boolean open;//文件打开标志
    public File(String fileName,String path,int num,Folder parent)
    {
        this.fileName=fileName;
        this.path=path;
        this.num=num;
        this.parent=parent;
        this.content="";

    }

    public File(String name,int flag,int num,String content,double size,Folder parent,String path,boolean open)
    {
        fileName=name;
        this.flag=flag;
        this.num=num;
        this.content=content;
        this.size=size;
        this.parent=parent;
        this.path=path;
        this.open=open;
    }

}
