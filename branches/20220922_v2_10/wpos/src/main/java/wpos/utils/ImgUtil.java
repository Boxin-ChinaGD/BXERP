package wpos.utils;

//import android.graphics.Bitmap;
//import android.net.Uri;
//import android.os.Environment;
//import android.util.Log;

import com.sun.jndi.toolkit.url.Uri;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.net.URI;
import java.net.URISyntaxException;

public class ImgUtil {

    /**
     * 以时间戳命名将bitmap写入文件
     * @param imageUri
     */
//    public static void writeFileByBitmap2(Bitmap bitmap) {
//        String path = Environment.getExternalStorageDirectory().getAbsolutePath();//�ֻ����õĴ洢λ��
//        File file = new File(path);
//        File imageFile = new File(file, System.currentTimeMillis() + ".png");
//
//        if (!file.exists()) {
//            file.mkdirs();
//        }
//        try {
//            imageFile.createNewFile();
//            FileOutputStream outputStream = new FileOutputStream(imageFile);
//            bitmap.compress(Bitmap.CompressFormat.JPEG, 100, outputStream);
//            outputStream.flush();
//            outputStream.close();
//        } catch (FileNotFoundException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        } catch (IOException e) {
//            // TODO Auto-generated catch block
//            e.printStackTrace();
//        }
//    }
    private void deleFile(Uri imageUri){
        try {
            File file = new File(new URI(imageUri.toString()));
            if (file.exists()) {
                file.delete();
            }
        } catch (URISyntaxException e) {
            // TODO Auto-generated catch block
            e.printStackTrace();
        }
    }
    public static void Log(String str) {
//        Log.d("zhu", str);
    }
}

