package com.maindark.livestream.util;


import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.util.zip.GZIPInputStream;
import java.util.zip.GZIPOutputStream;
public class Gzip {

        private static final int BYTE_LEN = 256;
        public static final String GZIP_ENCODE_UTF_8 = "UTF-8";

        private Gzip() {

        }

        /**
         * 解压
         * @param bytes 待解压byte数组
         * @return
         * @throws IOException
         */
        public static byte[] uncompress(byte[] bytes) throws IOException {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);

            GZIPInputStream ungzip = new GZIPInputStream(in);
            byte[] buffer = new byte[BYTE_LEN];
            int n;
            while ((n = ungzip.read(buffer)) >= 0) {
                out.write(buffer, 0, n);
            }

            return out.toByteArray();
        }

        /**
         * 解压返回字符串
         * @param bytes 待解压byte数组
         * @param encoding 编码
         * @return
         */
        public static String uncompressToString(byte[] bytes, String encoding) {
            if (bytes == null || bytes.length == 0) {
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            ByteArrayInputStream in = new ByteArrayInputStream(bytes);
            try {
                GZIPInputStream ungzip = new GZIPInputStream(in);
                byte[] buffer = new byte[BYTE_LEN];
                int n;
                while ((n = ungzip.read(buffer)) >= 0) {
                    out.write(buffer, 0, n);
                }
                return out.toString(encoding);
            } catch (IOException e) {
                e.printStackTrace();
            }
            return null;
        }

        /**
         * 压缩
         * @param str 待压缩字符串
         * @param encoding 编码
         * @return
         * @throws IOException
         */
        public static byte[] compress(String str, String encoding) throws IOException {
            if (str == null || str.length() == 0) {
                return null;
            }
            ByteArrayOutputStream out = new ByteArrayOutputStream();
            GZIPOutputStream gzip;
            gzip = new GZIPOutputStream(out);
            gzip.write(str.getBytes(encoding));
            gzip.close();
            return out.toByteArray();
        }

    public static void main(String[] args) {
        String input = "我是好人";
        try {
            //压缩
            byte[] newInfoBytes = Gzip.compress(input, Gzip.GZIP_ENCODE_UTF_8);

            //解压
            String newInput = Gzip.uncompressToString(newInfoBytes, Gzip.GZIP_ENCODE_UTF_8);

            System.out.println(newInput);
        } catch (IOException e) {
            e.printStackTrace();
        }


    }


    }


