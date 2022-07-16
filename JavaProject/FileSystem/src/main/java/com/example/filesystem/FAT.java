package com.example.filesystem;

import java.util.ArrayList;

public class FAT {
    Folder root;
    ArrayList<Object>filesOpened;
    Block[] blocks;
    public  FAT()
    {
        root=new Folder();
        Folder fat=new Folder();
        blocks=new Block[128];
        //blocks[0]=new Block(0,-1,FAT.class);
        //blocks[1]=new Block(1,-1,FAT.class);
        //blocks[2]=new Block(2,-1,Object);
    }

}
