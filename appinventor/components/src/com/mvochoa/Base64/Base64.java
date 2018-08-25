package com.mvochoa.Base64;

import android.annotation.TargetApi;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;

import android.os.Build;
import com.google.appinventor.components.runtime.ComponentContainer;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.runtime.util.ErrorMessages;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;

@DesignerComponent(version = 1, description = "Codificador y Decodificar de imágenes en base64", category = ComponentCategory.EXTENSION, nonVisible = true, iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.WRITE_EXTERNAL_STORAGE, android.permission.READ_EXTERNAL_STORAGE")
public class Base64 extends AndroidNonvisibleComponent {
    Context context;

    public Base64(ComponentContainer container) {
        super(container.$form());
        context = (Context) container.$context();
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SimpleFunction(description = "Retorna una cadena en base64 de la imagen.")
    public String ImageToBase64(String path) {
        String base64 = "";

        try {
            Bitmap bm = BitmapFactory.decodeFile(path);
            ByteArrayOutputStream baos = new ByteArrayOutputStream();
            bm.compress(Bitmap.CompressFormat.JPEG, 100, baos);
            byte[] byteArrayImage = baos.toByteArray();

            base64 = android.util.Base64.encodeToString(byteArrayImage, android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            form.dispatchErrorOccurredEvent(this, "ioBase64", ErrorMessages.ERROR_CANVAS_BITMAP_ERROR, e.getMessage());
        }
        return base64;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SimpleFunction(description = "Retorna la ruta de la imagen.")
    public String Base64ToImage(String base64) {
        String name = (System.currentTimeMillis() / 1000L) + "";
        try {
            File img = File.createTempFile(name, null, context.getExternalCacheDir());
            name = img.getAbsolutePath();

            FileOutputStream fos = new FileOutputStream(img);
            fos.write(android.util.Base64.decode(base64, android.util.Base64.DEFAULT));
            fos.flush();
            fos.close();
        } catch (IOException e) {
            e.printStackTrace();
            form.dispatchErrorOccurredEvent(this, "ioBase64",
                    ErrorMessages.ERROR_CANNOT_SAVE_IMAGE, e.getMessage());
        }

        return name;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SimpleFunction(description = "Retorna la cadena codificada en base64.")
    public String TextToBase64(String text) {
        String base64 = "";
        try {
            base64 = android.util.Base64.encodeToString(text.getBytes(), android.util.Base64.DEFAULT);
        } catch (Exception e) {
            e.printStackTrace();
            form.dispatchErrorOccurredEvent(this, "ioBase64", ErrorMessages.ERROR_CANVAS_BITMAP_ERROR, e.getMessage());
        }
        return base64;
    }

    @TargetApi(Build.VERSION_CODES.FROYO)
    @SimpleFunction(description = "Retorna la cadena en Base64 decodificada.")
    public String Base64ToText(String base64) {
        String text = "";
        try {
            text = new String(android.util.Base64.decode(base64, android.util.Base64.DEFAULT));
        } catch (Exception e) {
            e.printStackTrace();
            form.dispatchErrorOccurredEvent(this, "ioBase64", ErrorMessages.ERROR_CANVAS_BITMAP_ERROR, e.getMessage());
        }
        return text;
    }
}