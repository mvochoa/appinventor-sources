package com.mvochoa.Crypt;

import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;
import java.math.BigInteger;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@DesignerComponent(version = 1,
        description = "Crypt",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
public class Crypt extends AndroidNonvisibleComponent {

    public Crypt(ComponentContainer container) {
        super(container.$form());
    }

    @SimpleFunction(description = "Function DoLogin")
    public String Encrypt(String msg) {
        try {
            MessageDigest md = MessageDigest.getInstance("SHA");
            md.reset();
            return new BigInteger(1, md.digest(msg.getBytes())).toString(32);
        } catch (NoSuchAlgorithmException e) {
            e.printStackTrace();
        }

        return null;
    }
}