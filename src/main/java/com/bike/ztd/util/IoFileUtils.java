package com.bike.ztd.util;


import java.io.*;


public class IoFileUtils {


    /**
     * 删除文件夹,文件
     * @param dir
     * @return
     */
    public static  boolean deleteDir(File dir) {
        //tmp目录不能删除
        if (dir.isDirectory()) {
            String[] children = dir.list();
            //递归删除目录中的子目录下
            if(children != null && children.length>0) {
                for (String child : children) {
                    boolean success = deleteDir(new File(dir, child));
                    if (!success) {
                        return false;
                    }
                }
            }
        }
        // 目录此时为空，可以删除
        return dir.delete();
    }


}
