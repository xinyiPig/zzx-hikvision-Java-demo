package com.example.hkws.util;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.zip.ZipEntry;
import java.util.zip.ZipOutputStream;
import org.springframework.stereotype.Component;

/**
 * zip工具类
 */
@Component
public class ZipUtils {

    private static final int BUFFER_SIZE = 2 * 1024;
    /**
      *     压缩成ZIP
      * @param srcDir 压缩文件存放位置（来源，将所有的路径存放至数组中）
      * @param out  压缩文件名称（目的地）
      * @param KeepDirStructure 是否保留原来的目录结构,true:保留目录结构; 
      *                    false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
      * @throws RuntimeException 压缩失败会抛出运行时异常
      */
    public static void toZip(String[] srcDir, String outDir,
                             boolean KeepDirStructure) throws RuntimeException, Exception {

        OutputStream out = new FileOutputStream(new File(outDir));

        long start = System.currentTimeMillis();
        ZipOutputStream zos = null;
        try {
            zos = new ZipOutputStream(out);
            List<File> sourceFileList = new ArrayList<File>();
            for (String dir : srcDir) {
                File sourceFile = new File(dir);
                sourceFileList.add(sourceFile);
            }
            compress(sourceFileList, zos, false);
            long end = System.currentTimeMillis();
            System.out.println("压缩完成，耗时：" + (end - start) + " ms");
        } catch (Exception e) {
            throw new RuntimeException("zip error from ZipUtils", e);
        } finally {
            if (zos != null) {
                try {
                    zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (out != null) {
                try {
                    out.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }

    }

    /**
     *      *     递归压缩方法
     *      * @param sourceFile 源文件
     *      * @param zos    zip输出流
     *      * @param name    压缩后的名称
     *      * @param keepDirStructure    是否保留原来的目录结构,true:保留目录结构; 
     *      *                                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     *      * @throws Exception
     *     
     */
    private static void compress(File sourceFile, ZipOutputStream zos,
                                 String name, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        if (sourceFile.isFile()) {
            zos.putNextEntry(new ZipEntry(name));
            int len;
            FileInputStream in = new FileInputStream(sourceFile);
            while ((len = in.read(buf)) != -1) {
                zos.write(buf, 0, len);
            }
            // Complete the entry
            zos.closeEntry();
            in.close();
        } else {
            File[] listFiles = sourceFile.listFiles();
            if (listFiles == null || listFiles.length == 0) {
                if (KeepDirStructure) {
                    zos.putNextEntry(new ZipEntry(name + "/"));
                    zos.closeEntry();
                }

            } else {
                for (File file : listFiles) {
                    if (KeepDirStructure) {
                        compress(file, zos, name + "/" + file.getName(),
                                KeepDirStructure);
                    } else {
                        compress(file, zos, file.getName(), KeepDirStructure);
                    }

                }
            }
        }
    }

    /**
     *      *     递归压缩方法
     *      * @param sourceFileList 源文件集合
     *      * @param zos    zip输出流
     *      * @param keepDirStructure    是否保留原来的目录结构,true:保留目录结构; 
     *      *                                         false:所有文件跑到压缩包根目录下(注意：不保留目录结构可能会出现同名文件,会压缩失败)
     *      * @throws Exception
     *     
     */
    private static void compress(List<File> sourceFileList,
                                 ZipOutputStream zos, boolean KeepDirStructure) throws Exception {
        byte[] buf = new byte[BUFFER_SIZE];
        for (File sourceFile : sourceFileList) {
            String name = sourceFile.getName();
            if (sourceFile.isFile()) {
                zos.putNextEntry(new ZipEntry(name));
                int len;
                FileInputStream in = new FileInputStream(sourceFile);
                while ((len = in.read(buf)) != -1) {
                    zos.write(buf, 0, len);
                }
                zos.closeEntry();
                in.close();
            } else {
                File[] listFiles = sourceFile.listFiles();
                if (listFiles == null || listFiles.length == 0) {
                    if (KeepDirStructure) {
                        zos.putNextEntry(new ZipEntry(name + "/"));
                        zos.closeEntry();
                    }

                } else {
                    for (File file : listFiles) {
                        if (KeepDirStructure) {
                            compress(file, zos, name + "/" + file.getName(),
                                    KeepDirStructure);
                        } else {
                            compress(file, zos, file.getName(),
                                    KeepDirStructure);
                        }

                    }
                }
            }
        }
    }

    
    /**
     * 将存放在sourceFilePath目录下的源文件,打包成fileName名称的ZIP文件,并存放到zipFilePath。
     * @param sourceFilePath 待压缩的文件路径
     * @param zipFilePath    压缩后存放路径
     * @param fileName       压缩后文件的名称
     * @return flag
     */
    public static boolean fileToZip(String sourceFilePath,String zipFilePath,String fileName) {
        boolean flag = false;
        File sourceFile = new File(sourceFilePath);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        FileOutputStream fos = null;
        ZipOutputStream zos = null;

        if(sourceFile.exists() == false) {
            System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 不存在. <<<<<<");
        } else {
            try {
                File zipFile = new File(zipFilePath + "/" + fileName + ".zip");
                if(zipFile.exists()) {
                    System.out.println(">>>>>> " + zipFilePath + " 目录下存在名字为：" + fileName + ".zip" + " 打包文件. <<<<<<");
                } else {
                    File[] sourceFiles = sourceFile.listFiles();
                    if(null == sourceFiles || sourceFiles.length < 1) {
                        System.out.println(">>>>>> 待压缩的文件目录：" + sourceFilePath + " 里面不存在文件,无需压缩. <<<<<<");
                    } else {
                        fos = new FileOutputStream(zipFile);
                        zos = new ZipOutputStream(new BufferedOutputStream(fos));
                        byte[] bufs = new byte[1024*10];
                        for(int i=0;i<sourceFiles.length;i++) {
                            // 创建ZIP实体,并添加进压缩包
                            ZipEntry zipEntry = new ZipEntry(sourceFiles[i].getName());
                            zos.putNextEntry(zipEntry);
                            // 读取待压缩的文件并写进压缩包里
                            fis = new FileInputStream(sourceFiles[i]);
                            bis = new BufferedInputStream(fis,1024*10);
                            int read = 0;
                            while((read=bis.read(bufs, 0, 1024*10)) != -1) {
                                zos.write(bufs, 0, read);
                            }
                        }
                        flag = true;
                    }
                }
            } catch (FileNotFoundException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } catch (IOException e) {
                e.printStackTrace();
                throw new RuntimeException(e);
            } finally {
                // 关闭流
                try {
                    if(null != bis) bis.close();
                    if(null != zos) zos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                    throw new RuntimeException(e);
                }
            }
        }

        return flag;
    }
}