package com.example.filesystem;

public class FileSub {
    public static FAT FatTable=new FAT();
    public static disk Disk=new disk();
    public static Folder F=Disk.root;//记录当前所在文件夹的变量，一般初始化为根目录
    public static String currentpath;//记录当前路径的变量
    public static int  findFAT(int begin)//查找FAT中的空闲位置，查找到FAT中的空闲位置之后就找到对应的BLock来更改
    {
        //参数begin用来表示遍历开始的下标，begin前面的磁盘都已经用来作为系统数据区
        for(int x=begin;x<128;x++)
        {
            if(FatTable.blocks[x].index==0)
            {
                return x;
            }
        }
        return -1;
    }
    public static boolean open_file(String fileName,int operatetype)//一个是文件名，一个是操作类型  返回的是是否已经打开的提示，如果找不到文件就返回false
    {
        //operatetype中0代表写操作，1代表读操作
        for(int i=0;i<8;i++)
        {
           if((F.item.get(i).substring(0,3)).equals(fileName))
           {
               if(((File)F.children.get(i)).type%2==1)
               {
                   if(operatetype==1)
                   {
                       ((File)F.children.get(i)).open=true;//把对应的标识是否打开文件的参数更改为true
                       FileTable T=new FileTable(((File)F.children.get(i)).path,((File)F.children.get(i)).type,((File)F.children.get(i)).num,
                               ((File)F.children.get(i)).content.length(),operatetype,((File)F.children.get(i)).num,
                               ((File)F.children.get(i)).num+((File)F.children.get(i)).content.length());
                       Disk.filesOpened.add(T);//更新已打开文件表
                       return true;
                   }
                   else
                       return false;//如果这个文件只是一个只读文件，但是用了写操作（0）那么就返回false
               }
               else
               {
                   ((File)F.children.get(i)).open=true;//把对应的标识是否打开文件的参数更改为true
                   FileTable T=new FileTable(((File)F.children.get(i)).path,((File)F.children.get(i)).type,((File)F.children.get(i)).num,
                           ((File)F.children.get(i)).content.length(),operatetype,((File)F.children.get(i)).num,
                           ((File)F.children.get(i)).num);//一般打开文件的时候读和写的位置是一样的
                   Disk.filesOpened.add(T);//更新已打开文件表
                   return true;
               }
           }
        }
        return false;//找不到对应的文件来打开，返回false
    }
    public static String read_file(String fileName,int lengths)
    {
        //如果长度小于等于0返回null，如果读写的方式不对也返回null，找不到也返回null
        if(lengths<=0)
            return null;
        for(int i=0;i<Disk.filesOpened.size();i++)
        {
            if(Disk.filesOpened.get(i).path.equals(currentpath+'\\'+fileName))
            {
                FileTable T=Disk.filesOpened.get(i);
                if(T.operatetype==0)
                    return null;
                else
                {
                    File F=(File)Disk.blocks[T.begin].object;
                    if(lengths>F.content.length())
                    {
                        return F.content;//如果索取长度比原来的字符串的长度还长就直接返回整个字符串
                    }
                    return F.content.substring(0,lengths);//取相应长度的文字
                }
            }
        }
        return null;
    }

}
