package com.fiberhome.guardA;

import org.apache.log4j.LogManager;
import org.apache.log4j.Logger;

import java.io.FileInputStream;
import java.io.IOException;
import java.io.InputStream;
import java.util.Properties;

/**
 * @author 黃帆
 * @date 2018/9/25 11:33
 */
public class ConfigUtil {

    private static Logger logger = LogManager.getLogger(ConfigUtil.class.getName());

    private static Properties props = new Properties();

    private static final String defaultpath = "config.properties";

    public static void initialize(String path) throws IOException {
        InputStream inputStream = null;
        try {
            logger.info("config file path:"+path);
            inputStream=new FileInputStream(path);
            if (inputStream==null){
                logger.info("cannot load properties from the given path,will use default path:"+defaultpath);
                inputStream=new FileInputStream(defaultpath);
            }
            props.load(inputStream);
        } catch (IOException e) {
            logger.error(e);
            throw e;
        } finally {
            if (inputStream != null) {
                try {
                    inputStream.close();
                } catch (IOException e) {
                    logger.error(e);
                    throw e;
                }

            }
        }
    }

    public static String getProperty(String key) {
        return props.getProperty(key);
    }

    public static String getProperty(String key, String defaultValue) {
        return props.getProperty(key, defaultValue);
    }
}
