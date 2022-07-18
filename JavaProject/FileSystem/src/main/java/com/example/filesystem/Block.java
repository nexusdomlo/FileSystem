package com.example.filesystem;

public class Block {
    int num;//本块号
    int index;//下一块索引 0 表示空闲没有分配 -1表示结束 其他小于128，就是以及分配 表示下一块的块号
    Object object;//表示当前块所占据的类
    boolean begin;//用于表示此块是否为第一块
    public Block(int num,int index,Object object)
    {
        this.num=num;
        this.index=index;
        setObject(object);
        this.object=object;
        this.begin=false;

    }
    void setObject(Object object)
    {
        this.object=object;
        if(object instanceof File)//属于文件类
        {

        }
    }
    public void BlockChange(int index,Object object,boolean begin)//第一个数据是本磁盘块所对应的下一个磁盘块的索引，第二个是类，第三个就是表示这个是不是第一块
    {
        this.index=index;
        this.object=object;
        this.begin=begin;
    }


}
