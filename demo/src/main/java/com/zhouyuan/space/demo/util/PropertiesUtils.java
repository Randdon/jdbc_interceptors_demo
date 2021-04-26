package com.zhouyuan.space.demo.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.BufferedInputStream;
import java.io.FileInputStream;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.util.Properties;
import java.util.concurrent.locks.ReentrantLock;

/**
 * @author: yuandong
 * @date: 2021/4/25
 * @description: 导入/导出版本号获取与新增工具类
 */
public class PropertiesUtils {

    private static final Logger log = LoggerFactory.getLogger(PropertiesUtils.class);

    private static ReentrantLock lock = new ReentrantLock();

    private static Properties properties;

    /**
     * 版本号键
     */
    private static final String VER_KEY = "version";

    /**
     *
     * @author: yuandong
     * @description: 获取导出/导入版本号
     * @param filePath 导出/导入版本号记录文件路径
     * @date: 2021/4/25
     * @return: java.lang.String
     */
    public static String getVersion(String filePath) {
        lock.lock();
        try {
            properties = new Properties();
            InputStream in = new BufferedInputStream(new FileInputStream(filePath));
            properties.load(in);
            String version = properties.getProperty(VER_KEY);
            log.info("get version successfully!!! filePath:{},version:{}",filePath,version);
            return version;
        } catch (Exception e) {
            log.error("exception while getVersion, filePath:{}",filePath,e);
        } finally {
            lock.unlock();
        }
        return "";
    }

    /**
     *
     * @author: yuandong
     * @description: 版本递增
     * @param filePath 导出/导入版本号记录文件路径
     * @date: 2021/4/25
     * @return: void
     */
    public static void versionPlus(String filePath) {
        lock.lock();
        try {
            int version = Integer.parseInt(getVersion(filePath));
            log.info("ready to plus version, old version:{}, new version:{}",version++, version);
            // 版本加1
            properties.setProperty(VER_KEY, String.valueOf(version));
            FileOutputStream outputFile = new FileOutputStream(filePath);
            properties.store(outputFile, "version plus");
            outputFile.flush();
            outputFile.close();
        } catch (Exception e) {
            log.error("exception while plus version, filePath:{}", filePath, e);
        } finally {
            lock.unlock();
        }
    }
}