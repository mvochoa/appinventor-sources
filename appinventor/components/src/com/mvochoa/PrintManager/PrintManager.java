package com.mvochoa.PrintManager;

import android.annotation.TargetApi;
import android.content.Context;
import android.os.Build;
import android.print.PrintAttributes;
import android.print.PrintDocumentAdapter;
import android.print.PrintJob;
import com.google.appinventor.components.annotations.DesignerComponent;
import com.google.appinventor.components.annotations.SimpleFunction;
import com.google.appinventor.components.annotations.SimpleObject;
import com.google.appinventor.components.annotations.UsesPermissions;
import com.google.appinventor.components.common.ComponentCategory;
import com.google.appinventor.components.runtime.AndroidNonvisibleComponent;
import com.google.appinventor.components.runtime.ComponentContainer;

import android.webkit.WebView;
import android.webkit.WebViewClient;

@DesignerComponent(version = 1,
        description = "Ejecuta las clase PrintManager para imprimir ya se html o una página web ©Mvochoa",
        category = ComponentCategory.EXTENSION,
        nonVisible = true,
        iconName = "images/extension.png")
@SimpleObject(external = true)
@UsesPermissions(permissionNames = "android.permission.INTERNET")
public class PrintManager extends AndroidNonvisibleComponent {
    private final ComponentContainer container;
    private WebView webView;

    public PrintManager(ComponentContainer container) {
        super(container.$form());
        this.container = container;
        webView = new WebView(container.$context());
        webView.setWebViewClient(new WebViewClient() {
            @Override
            public void onPageFinished(WebView view, String url) {
                PrintPage(view, url);
            }
        });
    }

    @SimpleFunction(description = "PrintHTMLText imprime una página web a base de cadena de HTML")
    public void PrintHTMLText(String textHTML, String baseURL) {
        webView = new WebView(container.$context());
        if (baseURL.equals("")) {
            baseURL = null;
        }
        webView.loadDataWithBaseURL(baseURL, textHTML, "text/HTML", "UTF-8", null);
    }

    @SimpleFunction(description = "PrintURLWeb imprime una página web en base a una dirección URL")
    public void PrintURLWeb(String Url) {
        webView = new WebView(container.$context());
        webView.loadUrl(Url);
    }

    @TargetApi(Build.VERSION_CODES.LOLLIPOP)
    private void PrintPage(WebView view, String url) {
        android.print.PrintManager printManager = (android.print.PrintManager) container.$context()
                .getSystemService(Context.PRINT_SERVICE);

        String name = this.getClass().getSimpleName() + "Document";
        PrintDocumentAdapter printAdapter = view.createPrintDocumentAdapter(name);

        PrintJob printJob = printManager.print(name, printAdapter, new PrintAttributes.Builder().build());
        if (!printJob.isStarted()) {
            printJob.restart();
        }
    }
}
