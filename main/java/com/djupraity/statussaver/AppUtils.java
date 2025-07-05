package com.djupraity.statussaver;

import android.os.Environment;

import java.io.File;

public class AppUtils {
    public static File RootDirectoryWhatsapp =
            new File(Environment.getExternalStorageDirectory()
                    + "/Download/StatusSaver/Whatsapp");

    public static void createFileFolder() {
        if (!RootDirectoryWhatsapp.exists())
            RootDirectoryWhatsapp.mkdirs();
    }
}


//package com.djupraity.statussaver;
//
//import android.os.Environment;
//
//import java.io.File;
//
//public class AppUtils {
//    public static File RootDirectoryWhatsapp =
//        new File(Environment.getExternalStorageDirectory()
//        +"/Downlaod/StatusSaver/Whatsapp");
//
//    public static void createFileFolder(){
//        if(!RootDirectoryWhatsapp.exists())
//            RootDirectoryWhatsapp.mkdirs();
//    }
//
//}
