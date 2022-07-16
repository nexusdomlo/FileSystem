package com.example.filesystem;

public class File {
    String fileName;//文件名
    int flag;//只读/读写标志
    int num;//记录文件的起始盘块号
    String content;
    double size;
    Folder parent;
    String path;
    boolean open;
    public File(String name,int flag,int num,String content,double size,Folder parent,String path,boolean open)
    {

    }

}
