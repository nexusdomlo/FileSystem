package com.example.filesystem;

import java.util.ArrayList;


public class disk {
    Folder root;//根目录
    ArrayList<FileTable> filesOpened;//记录文件是否打开的列表
    Block[] blocks;//128个磁盘块数组
    public  disk()
    {
        root=new Folder("root","root",2,null);//用于初始化根目录，名字是root，路径直接就是root，起始盘块号为2 ，父母文件夹为root,type是8表示这个是一个目录，根目录
        blocks=new Block[128];
        blocks[0]=new Block(0,-1,FileSub.FatTable);
        blocks[1]=new Block(1,-1,FileSub.FatTable);
        blocks[2]=new Block(2,-1,this.root);
        for(int i=3;i<128;i++)
        {
            blocks[i]=new Block(i,0,null);//填入null表示这个磁盘块被初始化，然后没有任何的数据
        }

    }
}
