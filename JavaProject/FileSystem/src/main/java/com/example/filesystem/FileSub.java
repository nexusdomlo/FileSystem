package com.example.filesystem;

import java.util.ArrayList;
import java.util.Arrays;

public class FileSub {
    public static FAT FatTable=new FAT();
    public static disk Disk=new disk();
    public static Folder F=Disk.root;//记录当前所在文件夹的变量，一般初始化为根目录
    public static String currentpath="ROOT";//记录当前路径的变量 这个路径指的是当前在输入文字栏显示的路径
    public static char[] buffer1=new char[64];
    public static char[] buffer2=new char[64];
    /*双缓冲是第一个缓冲区读入数据完成时第二个缓冲区开始工作，读入用户区结束后判断第一个缓冲区是否停止工作，如果停止工作那继续向第一个缓冲读入数据。*/
    public static int  findFAT(int begin)//查找FAT中的空闲位置，查找到FAT中的空闲位置之后就找到对应的BLock来更改
    {
        //参数begin用来表示遍历开始的下标，begin前面的磁盘都已经用来作为系统数据区
        for(int x=begin;x<128;x++)
        {
            if(FatTable.IndexArray[x]==0)
            {
                return x;
            }
        }
        return -1;
    }
    public static boolean findFolder(String p)//输入路径寻找文件
    {
        for(int i=2;i<128;i++)
        {
            if(Disk.blocks[i].object==null)
                continue;
            if(Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.Folder"))
            {
                if(p.equals(((Folder)Disk.blocks[i].object).path))
                {
                    FileSub.F=(Folder)Disk.blocks[i].object;
                    return true;
                }
            }
        }
        return false;
    }
    public static File findfile(String p)
    {
        for(int i=2;i<128;i++)
        {
            if(Disk.blocks[i].object==null)
                continue;
            if(Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.File"))
            {
                if(p.equals(((File)Disk.blocks[i].object).path))
                {
                   return (File)Disk.blocks[i].object;
                }
            }
        }
        return null;
    }


    public static boolean open_file(String fileName,int operatetype)//一个是文件名，一个是操作类型  返回的是是否已经打开的提示，如果找不到文件就返回false
    {//open_file只是将文件加入fileopened表中
        //operatetype中0代表写操作，1代表读操作
        for(int i=0;i<8;i++)
        {
           if((F.item.get(i).substring(0,3)).equals(fileName))//找到对应的文件
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
/*
    public static void write_file(String filename,char[] buffer,int length,boolean begin)//文件名，缓冲，长度，是否为第一块
    {
        if(length<=0)
            return;
        for(int i=0;i<Disk.filesOpened.size();i++)
        {
            if(Disk.filesOpened.get(i).path.equals(currentpath+"\\"+filename))
            {
                if(Disk.filesOpened.get(i).operatetype==1)
                    return;//如果操作是读，而不是写，那么不能写操作
                if(Arrays.toString(buffer1).length()<64||Arrays.toString(buffer2).length()<64)
                {
                    File file=(File)Disk.blocks[Disk.filesOpened.get(i).begin].object;
                    file.content=file.content+Arrays.toString(buffer);//模拟写文件，按照缓冲区来一个个输入
                    buffer1=new char[64];//将缓冲区的东西清空
                    buffer2=new char[64];
                    Disk.filesOpened.get(i).end= FatTable.IndexArray[Disk.filesOpened.get(i).end];
                    Disk.blocks[Disk.filesOpened.get(i).end].BlockChange(-1,file,false);
                    FatTable.IndexArray[Disk.filesOpened.get(i).end]=-1;//更新FAT表
                    return;
                }
                else if(Arrays.toString(buffer1).length()==64)
                {
                    File file=(File)Disk.blocks[Disk.filesOpened.get(i).begin].object;
                    file.content=file.content+Arrays.toString(buffer);//模拟写文件，按照缓冲区来一个个输入
                    buffer1=new char[64];//将缓冲区的东西清空
                    int index=findFAT(Disk.filesOpened.get(i).end);//找到一个空余的磁盘
                    Disk.blocks[Disk.filesOpened.get(i).end].BlockChange(index,file,begin);
                    FatTable.IndexArray[Disk.filesOpened.get(i).end]=index;//更改对应的FAT表的值
                    Disk.filesOpened.get(i).end=index;//将写指针位置更新
                    if(length-64==0)
                    {
                        FatTable.IndexArray[Disk.filesOpened.get(i).end]=-1;//如果恰好读完，那么直接改为-1
                        Disk.blocks[Disk.filesOpened.get(i).end].BlockChange(-1,file,begin);
                    }
                    write_file(filename,buffer2,length-64,false);
                    return;
                }
                else
                {
                    File file=(File)Disk.blocks[Disk.filesOpened.get(i).begin].object;
                    file.content=file.content+Arrays.toString(buffer);//模拟写文件，按照缓冲区来一个个输入
                    buffer2=new char[64];
                    int index=findFAT(Disk.filesOpened.get(i).end);//找到一个空余的磁盘
                    Disk.blocks[Disk.filesOpened.get(i).end].BlockChange(index,file,begin);
                    FatTable.IndexArray[Disk.filesOpened.get(i).end]=index;//更改对应的FAT表的值
                    Disk.filesOpened.get(i).end=index;//将写指针位置更新
                    if(length-64==0)
                    {
                        FatTable.IndexArray[Disk.filesOpened.get(i).end]=-1;
                        Disk.blocks[Disk.filesOpened.get(i).end].BlockChange(-1,file,begin);
                    }
                    write_file(filename,buffer1,length-64,false);//缓冲区2满了然后用缓冲区1来读取
                    return;
                }
            }
        }
        open_file(filename,0);//如果不存在就以写操作来打开文件
        write_file(filename,buffer,length,begin);
    }
*/
    public static void close_file(String filename)
    {
        for(int i=0;i<Disk.filesOpened.size();i++)
        {
            if(Disk.filesOpened.get(i).path.equals(currentpath+"\\"+filename))
            {
                File file=(File)Disk.blocks[Disk.filesOpened.get(i).begin].object;
                file.open=false;//将文件的open属性更改
                Disk.filesOpened.remove(i);//从已打开文件表中删除
                break;
            }
        }
    }
    public static boolean delete_pathfile(String path)
    {
        File file=findfile(path);
        int pos=0;
        for(int index=file.num;index!=-1;index=pos)
        {
            pos=FatTable.IndexArray[index];
            Disk.blocks[index].BlockChange(0,null,true);//不断的迭代然后更新磁盘块 ，归还磁盘块
            FatTable.IndexArray[index]=0;
        }
        for(int i=0;i<file.parent.item.size();i++)
        {
            if(file.parent.item.get(i).split("\\.")[0].equals(file.fileName)) {
                file.parent.item.remove(i);
                file.parent.children.remove(i);
                return true;
            }
        }
        return false;
    }


    public static boolean delete_file(String filename)
    {
        findFolder(currentpath);//先修改FileSub.F
        for(int i=0;i<F.item.size();i++)
        {
            if(F.item.get(i).split("\\.")[0].equals(filename))
            {
                File file = (File) (F.children.get(i));
                int pos=0;
                for(int index=file.num;index!=-1;index=pos)
                {
                    pos=FatTable.IndexArray[index];
                    Disk.blocks[index].BlockChange(0,null,true);//不断的迭代然后更新磁盘块 ，归还磁盘块
                    FatTable.IndexArray[index]=0;
                }
                F.item.remove(i);//删除目录项
                F.children.remove(i);//删除对应的孩子节点
                return true;//操作成功
            }
        }
        return false;
    }
    public static File typefile(String filename)
    {
        for(int i=0;i<F.item.size();i++)
        {
            String name=F.item.get(i).split("\\.")[0];
            if(name.equals(filename))
            {
                File file=(File)F.children.get(i);
                return file;
                /*然后就是将file中的各项信息展示出来*/
            }
        }
        return null;//目录不存在
    }
    public static boolean change(String filename,String t)
    {
/*        for(int i=0;i<F.item.size();i++)
        {
            System.out.println(F.item.get(i).split("\\.")[0]);
        }*/
        System.out.println(t);
        for(int i=0;i<F.item.size();i++)
        {
            if(F.item.get(i).split("\\.")[0].equals(filename))
            {
                File file=(File)F.children.get(i);
                if(t.equals("OR"))
                {
                    file.flag=1;
                    return true;
                }
                else if(t.equals("RAW")) {
                    file.flag = 0;//更改文件的属性
                    return true;
                }else
                    return false;
                /*然后就是将file中的各项信息展示出来*/
            }
        }
        return false;
    }
    public static Folder mkpathdir(String Foldername,String path)
    {
        findFolder(path);
        for(int i=0;i<F.item.size();i++)
            if(F.item.get(i).substring(0,3).equals(Foldername))//有同名目录
                return null;
        int index=findFAT(2);
        Folder folder=new Folder(Foldername,path+"\\"+Foldername,index,F);
        Disk.blocks[index].BlockChange(-1,folder,true);
        F.children.add(folder);
        F.addChildrenNode(folder);
        FatTable.IndexArray[index]=-1;
        return folder;
    }

    public static Folder showdir(String Foldername)
    {
        for(int i=0;i<F.item.size();i++)
        {

            if(F.item.get(i).split(" ")[0].equals(Foldername))//有同名目录
            {
                return (Folder)F.children.get(i);
            }

        }
        return null;
    }
    public static boolean removedir(String Foldername)
    {
        findFolder(currentpath);//修改FileSub.F
        if(Foldername.equals("ROOT"))
            return false;
        for(int i=0;i<F.item.size();i++)
        {
            if(F.item.get(i).split(" ")[0].equals(Foldername))//有同名目录
            {
                Folder folder=(Folder)F.children.get(i);
                if(folder.size==0)//删除空目录首先要找到该目录，如果目录不存在，指令执行失败；如果存在，但是根目录或非空目录，显示不能删除，操作失败
                    return false;
                else {
                    Disk.blocks[folder.num].BlockChange(-1,null,true);//变更磁盘块
                    FatTable.IndexArray[folder.num]=0;//变更FAT表
                    F.item.remove(i);
                    F.children.remove(i);//更改目录项和子节点
                    return true;
                }
            }
        }
        return false;//找不到同名目录项
    }
    public static boolean removepathdir(String path)
    {
        if(path.equals("ROOT"))
            return false;
        for(int i=0;i<128;i++)
        {
            if(Disk.blocks[i].object==null)
                continue;
            if(Disk.blocks[i].object.getClass().toString().equals("class com.example.filesystem.Folder"))
            {
                Folder X=(Folder)FileSub.Disk.blocks[i].object;
                if(X.path.equals(path))
                {
                    if(X.children.size()>0)
                        return false;
                    Folder F=X.parent;
                    FatTable.IndexArray[X.num]=0;
                    Disk.blocks[X.num].BlockChange(-1,null,true);
                    for(int j=0;j<F.children.size();j++)
                    {
                        if(F.children.get(j).getClass().toString().equals("class com.example.filesystem.Folder"))
                        {
                            if(((Folder)F.children.get(j)).path.equals(path))
                            {
                                F.item.remove(j);
                                F.children.remove(j);
                                return true;
                            }
                        }
                    }
                }
            }
        }
        return false;
    }

}
