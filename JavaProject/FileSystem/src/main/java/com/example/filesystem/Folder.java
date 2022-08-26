package com.example.filesystem;

import java.util.ArrayList;
//目录类
public class Folder {
    String folderName;//文件夹名字
    int num;//起始盘块号
    int size=64;//占用的字节大小，目录都是64的字节大小，占据八个目录项
    Folder parent;//父母文件夹
    String path;//路径
    ArrayList<Object>children;//孩子节点
    int type;//记录目录属性
    ArrayList<String>item=new ArrayList<>();//用字符来直接记录目录项，方便观察
 
    public Folder(String folderName,String path,int num,Folder parent)
    {
        this.folderName=folderName;
        this.path=path;
        this.num=num;
        this.parent=parent;
        this.children=new ArrayList<>();
        this.type=8;
        for(int i=0;i<8;i++)
        {
            item.add("$       ");//初始化的过程，添加8个$，就是八个空目录项
        }
        disk D=FileSub.Disk;
        D.blocks[num].BlockChange(-1,this,true);
    }
    public boolean addChildrenNode(Object object)//用于添加孩子节点的函数
    {
        if(this.children.size()>8)
            return false;
        if(object instanceof File)//如果新创建的子节点属于文件类
        {
            children.add(object);//向孩子节点中添加对应的子节点
            String itemString=((File) object).fileName+".TT"+((File) object).type+((File) object).num+((File) object).size;
            item.set(children.size()-1,itemString);//将这个位置对应的文件或者目录改成相应的目录项（用字符串表示）
        }
        if(object instanceof Folder)
        {
            children.add(object);
            String itemString=((Folder) object).folderName+"  "+((Folder) object).type+((Folder) object).num+0;
            item.set(children.size()-1,itemString);
        }
        return true;
    }


}
