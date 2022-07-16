package com.example.filesystem;

import java.util.ArrayList;

public class Folder {
    String folderName;
    int num;
    double size;
    Folder parent;
    String path;
    ArrayList<Object>children;
    public Folder()
    {}
 
    public Folder(String folderName,String path,int num,Folder parent)
    {
        this.folderName=folderName;
        this.path=path;
        this.num=num;
        this.parent=parent;
        this.size=0;
        this.children=new ArrayList<>();
    }

}
