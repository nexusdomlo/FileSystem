package com.example.filesystem;

import java.util.ArrayList;

public class FAT {
    Folder root;//根目录
    ArrayList<Object>filesOpened;//记录文件是否打开的列表
    Block[] blocks;//128个磁盘块数组
    public  FAT()
    {

    }

}
