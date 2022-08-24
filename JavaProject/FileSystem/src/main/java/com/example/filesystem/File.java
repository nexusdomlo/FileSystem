package com.example.filesystem;

import java.io.UnsupportedEncodingException;

public class File {
    String fileName;//文件名
    int flag;//只读/读写标志
    int num;//记录文件的起始盘块号
    String content;//文档中保存的字符串
    int size;//占用的字节大小
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
        D.blocks[num].BlockChange(-1,this,true);
        //初始化一个文件，首先这个文件没有写任何的内容，直接index填上-1，object改成this(一般就是文件夹类和文件类），在哪个地方创建就填哪个，是否是begin文件，一般初始化第一个文件就是选择true
    }


    public void changeFileContent(String content) throws UnsupportedEncodingException//从前段里面提取相应的字符串
    {
        this.content=content;//更改对应的字符串
        if(size>content.getBytes("gbk").length)//需要回收磁
        {
            int count=0;//多出来的数量
            int begin=num;
            while(FileSub.FatTable.IndexArray[begin]!=-1)
            {
                if(count>content.getBytes("gbk").length)
                {
                    int index=begin;
                    begin=FileSub.FatTable.IndexArray[begin];//迭代之后的值
                    FileSub.FatTable.IndexArray[index]=0;//更新FAT表
                    FileSub.Disk.blocks[index].BlockChange(0,null,true);
                }
                else
                {
                    begin=FileSub.FatTable.IndexArray[begin];
                    count+=64;
                }

            }
        }
        else if(size<content.getBytes("gbk").length){
            int x=num;//先找到一个空闲的磁盘位置
            int count=0;
            while(FileSub.FatTable.IndexArray[x]!=-1)
            {
                x=FileSub.FatTable.IndexArray[x];
                count+=64;
            }
            while(count<content.getBytes("gbk").length)
            {
                int number=FileSub.findFAT(x);//找到对应的位置
                FileSub.FatTable.IndexArray[x]=number;
                FileSub.Disk.blocks[x].BlockChange(number,this,false);//更新磁盘的内容
                x=number;
                count+=64;
                if(count>=content.getBytes("gbk").length)
                {
                    FileSub.FatTable.IndexArray[x]=-1;
                    FileSub.Disk.blocks[x].BlockChange(-1,this,false);
                    break;
                }
            }
        }
        size=content.getBytes("gbk").length;
    }
}
