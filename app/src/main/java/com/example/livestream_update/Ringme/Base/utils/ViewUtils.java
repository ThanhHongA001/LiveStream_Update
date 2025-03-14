package com.example.livestream_update.Ringme.Base.utils;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.content.res.Resources;
import android.net.Uri;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.ViewTreeObserver;
import android.view.WindowManager;
import android.widget.TextView;

public class ViewUtils {

    public static boolean appInstalledOrNot(Context context, String uri) {
        boolean app_installed = false;
        if (context != null) {
            PackageManager pm = context.getPackageManager();
            try {
                pm.getPackageInfo(uri, PackageManager.GET_ACTIVITIES);
                app_installed = true;
            } catch (PackageManager.NameNotFoundException e) {
                app_installed = false;
            }
        }
        return app_installed;
    }

    public static int getScreenWidth(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.widthPixels;
    }

    public static int getScreenHeight(Context context) {
        WindowManager windowManager = (WindowManager) context
                .getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        assert windowManager != null;
        windowManager.getDefaultDisplay().getMetrics(dm);
        return dm.heightPixels;
    }

    public static int getStatusBarHeight(Context context) {
        int result = 0;
        int resourceId = context.getResources()
                .getIdentifier("status_bar_height", "dimen", "android");
        if (resourceId > 0) {
            result = context.getResources().getDimensionPixelSize(resourceId);
        }
        return result;
    }

    public static int dpToPx(int dp) {
        return (int) (dp * Resources.getSystem().getDisplayMetrics().density);
    }

    public static int pxToDp(int px) {
        return (int) (px / Resources.getSystem().getDisplayMetrics().density);
    }

    public static int getTextLenghtPerLine(TextView tv, String text) {
        ViewTreeObserver vto = tv.getViewTreeObserver();
        vto.addOnGlobalLayoutListener(new ViewTreeObserver.OnGlobalLayoutListener() {
            @Override
            public void onGlobalLayout() {

            }
        });
        tv.getLineCount();
        tv.setText(text);
        return tv.getLayout().getLineEnd(0);
    }

    public static int getTextPositionNice(TextView tv, String text, float line) {
        String str = "";
        Character strNext;
        int lenght = 40;//getTextLenghtPerLine(tv, text);
        int index = (int) (lenght * line);
        int count = 0;
        try {
            while (true) {
                if (text.length() <= index) {
                    return text.length();
                }
                str = text.substring(0, index);
                if (text.length() <= str.length()) {
                    return text.length();
                }
                strNext = text.charAt(str.length());
                if (strNext == ' ') {
                    return str.length() + 1;
                }
                index++;
                count++;
                if (count == 6) {
                    return index;
                }
            }
        } catch (Exception e) {
            return index;
        }
    }

    public static void openYoutube(Context context, final String url) {
        /*if (ViewUtils.appInstalledOrNot(context, "com.google.android.youtube")) {
            DialogTrailer dialog = new DialogTrailer(context) {
                @Override
                public String getContentUrl() {
                    String link = "https://www.youtube.com/embed/" + url.split("\\?")[1].replace("v=","") + "?autoplay=1&cc_load_policy=1";
                    return link;
                }
            };
            dialog.show();
        } else {
            context.startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(url)));
        }*/
        if (url.split("\\?").length < 2) return;
        Intent appIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("vnd.youtube:" + url.split("\\?")[1].replace("v=", "")));
        Intent webIntent = new Intent(Intent.ACTION_VIEW, Uri.parse(url));
        try {
            context.startActivity(appIntent);
        } catch (ActivityNotFoundException ex) {
            context.startActivity(webIntent);
        }
    }

    public static String refineHtmlToBlack(Context paramContext, String paramString1, String paramString2, long paramLong, String paramString3) {
        if (paramString1 == null) {
            return "";
        }
        Log.e("Html", paramString1);
        String str = paramString1.replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("<h1>", "<b>").replace("<h2>", "<b>").replace("<h3>", "<b>").replace("</h1>", "</b>").replace("</h2>", "</b>").replace("</h3>", "</b>").replace("<table", "<table width='100%'").replace("<tbody", "<tbody width='100%'").replace("<figure", "<div").replace("</figure>", "</div>").replace("<figcaption", "<div").replace("</figcaption>", "</div>").replace("<script type=\"text/javascript\">window.onload = function () {resizeNewsImage(\"news-image\", 500);}</script>", "").replace("<div class=\"images-common\">", "<div>").replace("<div class=\"images-container\">", "<div>").replace("<div class=\"images-richard\">", "<div>");
        return "<html style='background:#FAFAFA;text-align:left;margin:0 4px 0 4px;'><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><style>@font-face {\n    font-family: MyFont;\n    src: url(\"file:///android_asset/fonts/Roboto-Regular.ttf\")\n}h2 {color: #141414;line-height:26px;text-align:left;}blockquote {margin:0px;}body {color: #141414;line-height:26px;font-family: MyFont;}tbody {color: #141414;line-height:26px}b:not(.mytitle) { line-height:26px}img, object, embed, iframe{width: 110% !important;margin:0 -16px 0 -16px;}strong{ line-height:26px}img + em {background:#eeeeee;color:#884f4f4f;font-style: italic;}i { text-align:left;color: #313131; line-height:26px}</style></head><style></style><body><div style='text-align:left;line-height:26px' >" + str + "<script type=\"text/javascript\">\n   function showImageArticle(url){       JSInterface.showImageArticle(url);\n   }   function showVideoArticle(url){       JSInterface.showVideoArticle(url);\n   }   function showThumb(elementId,src){        document.getElementById(elementId).src = src;    }   function getWebHtml(){        var html = document.getElementsByTagName('html')[0].innerHTML;        JSInterface.getWebHtml(html);   }   function showVideoArticle2(url){        JSInterface.showVideoArticle2(url);   }</script>\n</body></html>";
    }

    public static String refineHtmlToWhite(Context paramContext, String paramString1, String paramString2, long paramLong, String paramString3) {
        if (paramString1 == null) {
            return "";
        }
        String str = paramString1.replace("<html>", "").replace("</html>", "").replace("<head></head>", "").replace("<body>", "").replace("<h1>", "<b>").replace("<h2>", "<b>").replace("<h3>", "<b>").replace("</h1>", "</b>").replace("</h2>", "</b>").replace("</h3>", "</b>").replace("<table", "<table width='100%'").replace("<tbody", "<tbody width='100%'").replace("<figure", "<div").replace("</figure>", "</div>").replace("<figcaption", "<div").replace("</figcaption>", "</div>").replace("<script type=\"text/javascript\">window.onload = function () {resizeNewsImage(\"news-image\", 500);}</script>", "").replace("<div class=\"images-common\">", "<div>").replace("<div class=\"images-container\">", "<div>").replace("<div class=\"images-richard\">", "<div>");
        return "<html style='background:#3a3a3a;text-align:left;margin:0 4px 0 4px;'><head><meta name=\"viewport\" content=\"width=device-width, initial-scale=1.0\"><style>@font-face {\n    font-family: MyFont;\n    src: url(\"file:///android_asset/fonts/Roboto-Regular.ttf\")\n}h2 {color: #F4F4F4;line-height:26px;text-align:left;}blockquote {margin:0px;}body {color: #F4F4F4 !important;line-height:26px;font-family: MyFont;}body *:not(.mytitle) {color:#F4F4F4 !important;}tbody {color: #000000;line-height:26px}b:not(.mytitle) { line-height:26px}img, object, embed, iframe{width: 110% !important;margin:0 -16px 0 -16px;}strong{ line-height:26px}img + em {background:#222222;color:#ffffff;font-style: italic}i { text-align:left;color: #F4F4F4; line-height:26px}</style></head><style></style><body><div style='text-align:left;line-height:26px' >" + str + "<script type=\"text/javascript\">\n   function showImageArticle(url){       JSInterface.showImageArticle(url);\n   }   function showVideoArticle(url){       JSInterface.showVideoArticle(url);\n   }   function showThumb(elementId,src){        document.getElementById(elementId).src = src;    }   function getWebHtml(){        var html = document.getElementsByTagName('html')[0].innerHTML;        JSInterface.getWebHtml(html);   }   function showVideoArticle2(url){        JSInterface.showVideoArticle2(url);   }</script>\n</body></html>";
    }

}

