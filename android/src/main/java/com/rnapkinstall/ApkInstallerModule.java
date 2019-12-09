package com.rnapkinstall;

import android.content.Intent;
import android.net.Uri;
import android.os.Build;
import android.support.v4.content.FileProvider;

import com.facebook.react.bridge.Promise;
import com.facebook.react.bridge.ReactApplicationContext;
import com.facebook.react.bridge.ReactContextBaseJavaModule;
import com.facebook.react.bridge.ReactMethod;

import java.io.File;

public class ApkInstallerModule extends ReactContextBaseJavaModule {

  private final ReactApplicationContext reactContext;

  public ApkInstallerModule(ReactApplicationContext reactContext) {
    super(reactContext);
    this.reactContext = reactContext;
  }

  @Override
  public String getName() {
    return "ApkInstaller";
  }


  @ReactMethod
  public void install(String filePath, Promise promise) {
    Intent intent = new Intent();
    intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
    intent.setAction(Intent.ACTION_VIEW);
    File apkFile = new File(filePath);
    Uri apkUri;
    try {
      if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
        intent.addFlags(Intent.FLAG_GRANT_READ_URI_PERMISSION);
        String authority = reactContext.getPackageName() + ".fileprovider";
        apkUri = FileProvider.getUriForFile(reactContext, authority, apkFile);
      } else {
        apkUri = Uri.fromFile(apkFile);
      }
      intent.setDataAndType(apkUri, "application/vnd.android.package-archive");
      reactContext.startActivity(intent);
    } catch (Exception e) {
      promise.reject(e);
    }
  }
}