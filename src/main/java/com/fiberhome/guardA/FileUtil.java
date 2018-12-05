package com.fiberhome.guardA;

/**
 * @author 黃帆
 * @date 2018/10/26 14:58
 */
public class FileUtil {
    /*
     * deal the differences between linux and windows
     */
    public static String dealPath(String path) {
        String rt = "";
        String sysname = FileUtil.getSysName();
        if (sysname.contains("linux")) {
            rt = path.replace("\\", "/");
        }
        if (sysname.contains("window")) {
            rt = path.replace("/", "\\");
        }
        return rt;

    }
    /**
     * return linux or windows version
     */
    public static String getSysName() {

        String rt = "window";

        String sysname = System.getProperty("os.name");

        if (sysname.toLowerCase().contains("linux")) {
            rt = "linux";
        }
        return rt;
    }
}
