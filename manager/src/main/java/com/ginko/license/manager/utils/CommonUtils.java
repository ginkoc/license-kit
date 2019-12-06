package com.ginko.license.manager.utils;

import com.ginko.license.manager.contants.Constants;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.OutputStream;
import java.security.SecureRandom;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;

/**
 * @author ginko
 * @date 8/8/19
 */
public class CommonUtils {

    private static final SecureRandom RANDOM = new SecureRandom();

    /**
     * 将{@link Date}对象转换为{@link LocalDate}
     * @param date Date对象
     * @return 转换后的LocalDate
     */
    public static LocalDate date2LocalDate(Date date) {
        return LocalDateTime.ofInstant(date.toInstant(), ZoneId.systemDefault()).toLocalDate();
    }

    /**
     * 计算两个{@link Date}对象之间的天数差
     * @return 天数差
     */
    public static long daysBetween(Date left, Date right) {
        assert left.before(right);
        return daysBetween(date2LocalDate(left), date2LocalDate(right));
    }

    /**
     * 计算两个{@link LocalDate}对象之间的天数差
     * @return 天数差
     */
    public static long daysBetween(LocalDate left, LocalDate right) {
        assert left.isBefore(right);
        return ChronoUnit.DAYS.between(left, right);
    }

    public static String getUniqueLicenseName(String fileName) {
        return getUniqueNameInRootDir(fileName, Constants.LICENSE_SUFFIX);
    }

    public static String getUniqueCertName(String fileName) {
        return getUniqueNameInRootDir(fileName, Constants.CERT_SUFFIX);
    }

    public static String getUniquePubKeystoreName(String fileName) {
        return getUniqueNameInRootDir(fileName, Constants.STORE_SUFFIX);
    }

    public static String getUniqueZipName(String fileName) {
        return getUniqueNameInRootDir(fileName, Constants.ZIP_SUFFIX);
    }

    public static String getUniqueNameInRootDir(String fileName, String suffix) {
        long n = RANDOM.nextLong();
        n = n == -9223372036854775808L ? 0L : Math.abs(n);
        return Constants.LICENSE_ROOT + Constants.SEPARATOR + fileName + "_" + Long.toString(n) + suffix;
    }

    public static boolean deleteFileIfExist(String filePath){
        File file = new File(filePath);
        if (file.exists()) {
            return file.delete();
        }

        return false;
    }

    /**
     * 将指定文件打包为zip文件，不能递归打包
     * @param destFileName 生成的zip文件名
     * @param srcFiles 要打包的文件
     * @return 打包完成后目标文件对象
     * @throws IOException IO异常
     */
    public static File zipFiles(String destFileName, String ...srcFiles) throws IOException {
        if (srcFiles == null) {
            throw new IllegalArgumentException("Given null file names");
        }

        File[] files = new File[srcFiles.length];
        for (int i=0, len= srcFiles.length; i<len; i++) {
            File file = new File(srcFiles[i]);

            if (!file.exists()) {
                throw new FileNotFoundException("Can not find file " + file);
            }

            if (file.isDirectory()) {
                throw new IllegalArgumentException("Can zip a directory " + file);
            }

            files[i] = file;
        }

        File zipFile = new File(destFileName);
        try(ZipOutputStream  zos
                    = new ZipOutputStream(new BufferedOutputStream(new FileOutputStream(zipFile)))) {
            for (File f : files) {
                compressFile(zos, f);
            }
        }

        return zipFile;
    }

    public static void writeBytes(OutputStream os, File srcFile) throws IOException {
        writeBytes(os, srcFile, true);
    }

    public static void writeBytes(OutputStream os, File srcFile, boolean closeStream) throws IOException {
        try(BufferedInputStream bis
                    = new BufferedInputStream(new FileInputStream(srcFile))) {
            int length;
            while ((length = bis.read()) != -1) {
                os.write(length);
            }
        } finally {
            if (closeStream) {
                os.close();
            }
        }
    }

    private static void compressFile(ZipOutputStream zos, File srcFile) throws IOException {
        ZipEntry entry = new ZipEntry(srcFile.getName());
        zos.putNextEntry(entry);
        writeBytes(zos, srcFile, false);
    }
}
