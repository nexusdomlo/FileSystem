package com.example.filesystem;

public class Block {
    int num;//本块号
    int index;//下一块索引 0 表示空闲没有分配 -1表示结束 其他小于128，就是以及分配 表示下一块的块号
    Object object;
    boolean begin;//用于表示此块是否为第一块
    public Block(int num,int index,Object object)
    {
        this.num=num;
        this.index=index;
        this.object=object;
        this.begin=false;
    }


}
