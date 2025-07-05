package com.djupraity.statussaver.model;

import android.net.Uri;

public class WhatsAppStatusModel {

    private String url;
    private String type; // image/video
    private Uri fileUri;
    private String absolutePath;
    private String name;

    public WhatsAppStatusModel(String type, Uri fileUri, String absolutePath, String name) {
        this.type = type;
        this.fileUri = fileUri;
        this.absolutePath = absolutePath;
        this.name = name;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public Uri getFileUri() {
        return fileUri;
    }

    public void setFileUri(Uri fileUri) {
        this.fileUri = fileUri;
    }

    public String getAbsolutePath() {
        return absolutePath;
    }

    public void setAbsolutePath(String absolutePath) {
        this.absolutePath = absolutePath;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }
}


//package com.djupraity.statussaver.model;
//
//import android.net.Uri;
//
//public class WhatsAppStatusModel {
//
//    private String url;
//    private String type; // image/video
//
//    public WhatsAppStatusModel(String url, String type) {
//        this.url = url;
//        this.type = type;
//    }
//
//    public WhatsAppStatusModel( String image, Uri file, String absolutePath, String name) {
//    }
//
//    public String getUrl() {
//        return url;
//    }
//
//    public void setUrl(String url) {
//        this.url = url;
//    }
//
//    public String getType() {
//        return type;
//    }
//
//    public void setType(String type) {
//        this.type = type;
//    }
//}


//package com.djupraity.statussaver.model;
//
//import android.net.Uri;
//
//public class WhatsappStatusModel {
//    private String name;
//    private Uri uri;
//    private String path;
//    private String filename;
//
//    public WhatsappStatusModel(String name, Uri uri, String path, String filename) {
//        this.name = name;
//        this.uri = uri;
//        this.path = path;
//        this.filename = filename;
//    }
//
//    public Uri getUri() {
//        return uri;
//    }
//
//    public void setUri(Uri uri) {
//        this.uri = uri;
//    }
//
//    public String getPath() {
//        return path;
//    }
//
//    public void setPath(String path) {
//        this.path = path;
//    }
//
//    public String getFilename() {
//        return filename;
//    }
//
//    public void setFilename(String filename) {
//        this.filename = filename;
//    }
//
//    public String getName() {
//        return name;
//    }
//
//    public void setName(String name) {
//        this.name = name;
//    }
//}
