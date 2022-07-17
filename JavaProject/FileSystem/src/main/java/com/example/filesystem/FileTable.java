package com.example.filesystem;

public class FileTable {
    String path;//文件路径名
    int type;//文件属性
    int num;//起始盘块号
    int length;//文件长度
    int operatetype;//操作类型
    int begin;//读指针
    int end;//写指针
    public FileTable(String path,int type,int num,int length,int operatetype,int begin,int end)
    {
        this.path=path;
        this.type=type;
        this.num=num;
        this.length=length;
        this.operatetype=operatetype;
        this.begin=begin;
        this.end=end;
    }

}
