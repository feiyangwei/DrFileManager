package com.drxx.drfilemanager.utils;

import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.storage.StorageManager;
import android.support.v4.content.FileProvider;
import android.util.Log;

import com.drxx.drfilemanager.Constants;
import com.drxx.drfilemanager.fragment.RenameFragment;
import com.drxx.drfilemanager.model.FileInfo;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.FileWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.RandomAccessFile;
import java.io.UnsupportedEncodingException;
import java.lang.reflect.Array;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.ArrayList;
import java.util.List;

public class FileUtils {
    private static final String TAG = "FileUtils";

    /**
     * 创建文件
     *
     * @param filePath 文件地址
     * @param fileName 文件名
     * @return
     */
    public static boolean createFile(String filePath, String fileName) {

        String strFilePath = filePath + fileName;

        File file = new File(filePath);
        if (!file.exists()) {
            /**  注意这里是 mkdirs()方法  可以创建多个文件夹 */
            file.mkdirs();
        }

        File subfile = new File(strFilePath);

        if (!subfile.exists()) {
            try {
                boolean b = subfile.createNewFile();
                return b;
            } catch (IOException e) {
                e.printStackTrace();
            }
        } else {
            return true;
        }
        return false;
    }

    /**
     * 遍历文件夹下的文件
     *
     * @param file 地址
     */
    public static List<FileInfo> getFile(FileInfo file) {

        List<FileInfo> list = new ArrayList<>();
        File[] fileArray = new File(file.getFilePath()).listFiles();
        if (fileArray == null) {
            return null;
        } else {
            for (File f : fileArray) {
                String type;
                if (f.isDirectory()) {
                    type = Constants.FILE_TYPE_DIR;
                } else {
                    type = Constants.FILE_TYPE_FILE;
                }
                list.add(0, new FileInfo(f.getName(), f.getPath(), type));
            }
        }
        return list;
    }

    /**
     * 删除文件
     *
     * @param filePath 文件地址
     * @return
     */
    public static boolean deleteFiles(String filePath) {
        List<FileInfo> fileInfos = getFile(new FileInfo(filePath));
        List<File> files = new ArrayList<>();
        for (FileInfo f : fileInfos) {
            files.add(0, new File(f.getFilePath()));
        }
        if (files.size() != 0) {
            for (int i = 0; i < files.size(); i++) {
                File file = files.get(i);

                /**  如果是文件则删除  如果都删除可不必判断  */
                if (file.isFile()) {
                    file.delete();
                }

            }
        }
        return true;
    }


    /**
     * 向文件中添加内容
     *
     * @param strcontent 内容
     * @param filePath   地址
     * @param fileName   文件名
     */
    public static void writeToFile(String strcontent, String filePath, String fileName) {
        //生成文件夹之后，再生成文件，不然会出错
        String strFilePath = filePath + fileName;
        // 每次写入时，都换行写

        File subfile = new File(strFilePath);


        RandomAccessFile raf = null;
        try {
            /**   构造函数 第二个是读写方式    */
            raf = new RandomAccessFile(subfile, "rw");
            /**  将记录指针移动到该文件的最后  */
            raf.seek(subfile.length());
            /** 向文件末尾追加内容  */
            raf.write(strcontent.getBytes());

            raf.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    /**
     * 修改文件内容（覆盖或者添加）
     *
     * @param path    文件地址
     * @param content 覆盖内容
     * @param append  指定了写入的方式，是覆盖写还是追加写(true=追加)(false=覆盖)
     */
    public static void modifyFile(String path, String content, boolean append) {
        try {
            FileWriter fileWriter = new FileWriter(path, append);
            BufferedWriter writer = new BufferedWriter(fileWriter);
            writer.append(content);
            writer.flush();
            writer.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 读取文件内容
     *
     * @param filePath 地址
     * @param filename 名称
     * @return 返回内容
     */
    public static String getString(String filePath, String filename) {
        FileInputStream inputStream = null;
        try {
            inputStream = new FileInputStream(new File(filePath + filename));
        } catch (FileNotFoundException e) {
            e.printStackTrace();
        }
        InputStreamReader inputStreamReader = null;
        try {
            inputStreamReader = new InputStreamReader(inputStream, "UTF-8");
        } catch (UnsupportedEncodingException e1) {
            e1.printStackTrace();
        }
        BufferedReader reader = new BufferedReader(inputStreamReader);
        StringBuffer sb = new StringBuffer("");
        String line;
        try {
            while ((line = reader.readLine()) != null) {
                sb.append(line);
                sb.append("\n");
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
        return sb.toString();
    }

    /**
     * 重命名文件
     *
     * @param oldPath 原来的文件地址
     * @param newPath 新的文件地址
     */
    public static void renameFile(String oldPath, String newPath) {
        File oleFile = new File(oldPath);
        File newFile = new File(newPath);
        //执行重命名
        oleFile.renameTo(newFile);
    }


    /**
     * 复制文件
     *
     * @param fromFile 要复制的文件目录
     * @param toFile   要粘贴的文件目录
     * @return 是否复制成功
     */
    public static boolean copy(String fromFile, String toFile) {
        //要复制的文件目录
        File[] currentFiles;
        File root = new File(fromFile);
        //目标目录
        File targetDir = new File(toFile);
        //创建目录
        if (!targetDir.exists()) {
            targetDir.mkdirs();
        }
        if (!root.isDirectory()) {
            boolean result = CopySdcardFile(fromFile, toFile + root.getName());
            return result;
        }
        //如同判断SD卡是否存在或者文件是否存在
        //如果不存在则 return出去
        if (!root.exists()) {
            return false;
        }
        //如果存在则获取当前目录下的全部文件 填充数组
        currentFiles = root.listFiles();
        //遍历要复制该目录下的全部文件
        for (int i = 0; i < currentFiles.length; i++) {
            if (currentFiles[i].isDirectory())//如果当前项为子目录 进行递归
            {
                copy(currentFiles[i].getPath() + "/", toFile + currentFiles[i].getName() + "/");

            } else//如果当前项为文件则进行文件拷贝
            {
                CopySdcardFile(currentFiles[i].getPath(), toFile + currentFiles[i].getName());
            }
        }
        return true;
    }


    //文件拷贝
    //要复制的目录下的所有非子目录(文件夹)文件拷贝
    public static boolean CopySdcardFile(String fromFile, String toFile) {

        try {
            InputStream fosfrom = new FileInputStream(fromFile);
            OutputStream fosto = new FileOutputStream(toFile);
            byte bt[] = new byte[1024];
            int c;
            while ((c = fosfrom.read(bt)) > 0) {
                fosto.write(bt, 0, c);
            }
            fosfrom.close();
            fosto.close();
            return true;

        } catch (Exception ex) {
            return false;
        }
    }

    /**
     * 通过反射调用获取内置存储和外置sd卡根路径(通用)
     *
     * @param mContext    上下文
     * @param is_removale 是否可移除，false返回内部存储，true返回外置sd卡
     * @return
     */
    public static String getStoragePath(Context mContext, boolean is_removale) {

        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getPathFile = storageVolumeClazz.getMethod("getPathFile");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                File file = (File) getPathFile.invoke(storageVolumeElement);

                Log.e("BGA", "getPath = " + file.getPath());
                Log.e("BGA", "getName = " + file.getName());
                Log.e("BGA", "getAbsolutePath = " + file.getAbsolutePath());
                Log.e("BGA", "getParent = " + file.getParent());
                Log.e("BGA", "getTotalSpace = " + file.getTotalSpace());
                Log.e("BGA", "getUsableSpace = " + file.getUsableSpace());
                Log.e("BGA", "getParentFile = " + file.getParentFile());
                Log.e("BGA", "path = " + path);
                //path = /storage/emulated/0
                //path = /storage/c94d6535-caff-44ef-a82d-91c93c50bc31
                boolean removable = (Boolean) isRemovable.invoke(storageVolumeElement);
                if (is_removale == removable) {
                    return path;
                }
            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return null;
    }

    /**
     * 通过反射调用获取内置存储和外置sd卡根路径(通用)
     *
     * @param mContext 上下文
     * @return
     */
    public static List<FileInfo> getStoragePath(Context mContext) {
        List<FileInfo> list = new ArrayList<>();
        StorageManager mStorageManager = (StorageManager) mContext.getSystemService(Context.STORAGE_SERVICE);
        Class<?> storageVolumeClazz = null;
        try {
            storageVolumeClazz = Class.forName("android.os.storage.StorageVolume");
            Method getVolumeList = mStorageManager.getClass().getMethod("getVolumeList");
            Method getPath = storageVolumeClazz.getMethod("getPath");
            Method isRemovable = storageVolumeClazz.getMethod("isRemovable");
            Method getPathFile = storageVolumeClazz.getMethod("getPathFile");
            Object result = getVolumeList.invoke(mStorageManager);
            final int length = Array.getLength(result);
            for (int i = 0; i < length; i++) {
                Object storageVolumeElement = Array.get(result, i);
                String path = (String) getPath.invoke(storageVolumeElement);
                File file = (File) getPathFile.invoke(storageVolumeElement);

                Log.e("BGA", "getPath = " + file.getPath());
                Log.e("BGA", "getName = " + file.getName());
                Log.e("BGA", "getAbsolutePath = " + file.getAbsolutePath());
                Log.e("BGA", "getParent = " + file.getParent());
                Log.e("BGA", "getTotalSpace = " + file.getTotalSpace());
                Log.e("BGA", "getUsableSpace = " + file.getUsableSpace());
                Log.e("BGA", "getParentFile = " + file.getParentFile());
                Log.e("BGA", "path = " + path);
                //path = /storage/emulated/0
                //path = /storage/c94d6535-caff-44ef-a82d-91c93c50bc31
                list.add(new FileInfo(path));

            }
        } catch (ClassNotFoundException e) {
            e.printStackTrace();
        } catch (InvocationTargetException e) {
            e.printStackTrace();
        } catch (NoSuchMethodException e) {
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            e.printStackTrace();
        }
        return list;
    }


    /**
     * 打开文件
     *
     * @param context
     * @param f
     */
    public static void openFile(Context context, File f) {
        Intent intent = new Intent(Intent.ACTION_VIEW);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
        String extension = android.webkit.MimeTypeMap.getFileExtensionFromUrl(Uri.fromFile(f).toString());
        String mimetype = android.webkit.MimeTypeMap.getSingleton().getMimeTypeFromExtension(extension);
        //7.0以上需要
//        if (Build.VERSION.SDK_INT >= 24) {
//            intent.setFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
//            Uri uri = FileProvider.getUriForFile(context, "com.drxx.drfilemanager.fileprovider", f);
//            intent.setDataAndType(uri, mimetype);
//        } else {
//            intent.setDataAndType(Uri.fromFile(f), mimetype);
//        }
        intent.setDataAndType(Uri.fromFile(f), mimetype);
        context.startActivity(intent);
    }

    /**
     * 删除
     */
    private void deleteDocument() {

    }

    /**
     * 移动
     */
    private void moveDocument() {

    }

    /**
     * 重命名
     */
    private void renameDocument() {

    }


}
