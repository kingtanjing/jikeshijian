package jvm;

import java.io.*;
import java.lang.String;
import java.nio.file.Files;

/**
 * @author tjw
 * @version 1.0.0
 * @ClassName MyClassLoader
 * @Description TODO
 * @createTime 2021-08-05 22:10
 */
public class MyClassLoader extends ClassLoader {

    public static void main(String[] args) throws Exception {
        MyClassLoader myClassLoader = new MyClassLoader();
        myClassLoader.findClass("Hello").newInstance();
    }

    @Override
    public Class<?> findClass(String name) {

        byte[] bytesCode = null;

        try {
            bytesCode = readFileByBytes("Hello.xlass");
        } catch (IOException e) {
            System.out.println("读取字节码文件失败");
            e.printStackTrace();
        }

        return defineClass(name, bytesCode, 0, bytesCode.length);
    }

    /**
     * 读取字节码文件
     * @param fileurl 文件路径
     * @return java字节码
     * @throws IOException
     */
    public static byte[] readFileByBytes(String fileurl) throws IOException {
        File file = new File(fileurl);

        byte[] originCode = Files.readAllBytes(file.toPath());
        for (int i = 0; i < originCode.length; i++) {
            originCode[i] = (byte) (255 - originCode[i]);
        }
        return originCode;

    }


}
