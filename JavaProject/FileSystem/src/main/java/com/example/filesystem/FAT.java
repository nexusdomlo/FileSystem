package com.example.filesystem;

public class FAT {
    public int[] IndexArray;
    public FAT()
    {
        IndexArray=new int[128];
        for(int i=0;i<128;i++)
        {
            if(i<=2)
            {
                IndexArray[i]=-1;
            }
            else
                IndexArray[i]=0;
        }
    }
}
