package com.example.livestream_update.Ringme.Helper;


import android.annotation.SuppressLint;
import android.content.ClipData;
import android.content.ClipDescription;
import android.content.Context;
import android.graphics.Color;
import android.graphics.Typeface;
import android.text.Html;
import android.text.Spannable;
import android.text.SpannableString;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.BackgroundColorSpan;
import android.text.style.ClickableSpan;
import android.text.style.ForegroundColorSpan;
import android.text.style.StyleSpan;
import android.text.style.UnderlineSpan;
import android.util.TypedValue;
import android.view.View;
import android.widget.EditText;
import android.widget.TextView;

import com.vtm.R;
import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.StringUtils;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;
import com.vtm.ringme.values.Version;

import org.json.JSONArray;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.text.DecimalFormat;
import java.text.DecimalFormatSymbols;
import java.text.Normalizer;
import java.text.NumberFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

/**
 * Created by ThaoDV on 6/13/14.
 */
public class TextHelper {
    public static final int MAX_LENGTH_DEFAULT = 35;
    public static final int MAX_NUMBER_NOTIFY = 9;
    private static final String TAG = TextHelper.class.getSimpleName();
    private static final String BAD_WORD_MASK = "***";
    private static final String HOST_START_M = "m.";
    private static final String HOST_START_WWW_M = "www.m.";
    private static final String HOST_START_WWW = "www.";
    private static TextHelper mInstant;
    //private static String tinyEncryptionKey = "!AndroidEncryptionKey@2015#Viettel*";
    private static final String REGEX_SIEUHAI = "[+-[&]|!(){}\\[\\]^\"~*?:\\\\/]+";
    private static Locale localeVN;
    private Pattern patternNormalizer;
    private Pattern patternD;
    private Pattern patternXmlCr;
    private Pattern patternXmlLf;
    private Pattern mPatternYoutube;
    private Pattern patternRingMeVideo;
    private Pattern patternRingMeChannel;
    private Pattern patternMusicSong;
    private Pattern patternMusicAlbum;
    private Pattern patternMusicVideo;
    private Pattern patternMusicPlaylist;
    private Pattern patternMoviesDetail;
    private Pattern patternNetNewsDetail;
    private Pattern patternBadWord;
    private Pattern patternSensitiveWord;
    private Pattern patternTiinDetail;

    private TextHelper() {
        initPattern();
        initPatternXml();
    }

    public static synchronized TextHelper getInstant() {
        if (mInstant == null)
            mInstant = new TextHelper();
        return mInstant;
    }

    public static void copyToClipboard(Context context, String content) {
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager) context.getSystemService
                (Context.CLIPBOARD_SERVICE);
        ClipData clip = ClipData
                .newPlainText(context.getResources().getString(R.string.copy), content);
        assert clipboard != null;
        clipboard.setPrimaryClip(clip);
    }

    public void initBadWordPatterns(ApplicationController app) {
        if (patternBadWord == null) {
            String config = app.getContentConfigByKey(Constants.PREFERENCE.CONFIG.BAD_WORD_CONFIG);
            Log.d(TAG, "initBadWordPatterns: " + config);
            String pattern = getPatternStringFromConfig(config);
            if (Utilities.notEmpty(pattern)) {
                try {
                    patternBadWord = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            }
        }
    }


    public static String getTextFromClipboard(Context context) {
        if (context == null) return "";
        android.content.ClipboardManager clipboard = (android.content.ClipboardManager)
                context.getSystemService(Context.CLIPBOARD_SERVICE);
        assert clipboard != null;
        if (clipboard.hasPrimaryClip()) {
            ClipData clip = clipboard.getPrimaryClip();
            // if you need text data only, use:
            assert clip != null;
            if (clip.getDescription().hasMimeType(ClipDescription.MIMETYPE_TEXT_PLAIN))
                // WARNING: The item could cantain URI that points to the text data.
                // In this case the getText() returns null and this code fails!
                if (clip.getItemAt(0).getText() == null) {
                    return "";
                } else {
                    return clip.getItemAt(0).getText().toString();
                }
            // or you may coerce the data to the text representation:
            return clip.getItemAt(0).coerceToText(context).toString();
        }
        return "";
    }

    public static int countSpace(String content) {
        if (content == null || content.length() <= 0)
            return -1;
        int size = content.length();
        int spaceCount = 0;
        for (int i = 0; i < size; i++) {
            if (content.charAt(i) == ' ') {
                spaceCount++;
            }
        }
        return spaceCount;
    }

    public void initSensitiveWordPatterns(ApplicationController app) {
        if (patternSensitiveWord == null) {
            String config = app.getContentConfigByKey(Constants.PREFERENCE.CONFIG.SENSITIVE_WORD_CONFIG);
            Log.d(TAG, "initSensitiveWordPatterns: " + config);
            String pattern = getPatternStringFromConfig(config);
            if (Utilities.notEmpty(pattern)) {
                try {
                    patternSensitiveWord = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
                } catch (Exception e) {
                    Log.e(TAG, e);
                }
            } else {
                readDataFromAssert(app);
            }
        }
    }

    public void resetSensitiveWordPatterns(ApplicationController app, String config) {
        patternSensitiveWord = null;
        Log.d(TAG, "resetBadWordPatterns: " + config);
        String pattern = getPatternStringFromConfig(config);
        if (Utilities.notEmpty(pattern)) {
            try {
                patternSensitiveWord = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        } else {
            readDataFromAssert(app);
        }
    }

    public static String removeAccent(String s) {
        String temp = Normalizer.normalize(s, Normalizer.Form.NFD);
        Pattern pattern = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
        return pattern.matcher(temp).replaceAll("").replaceAll("Đ", "D").replaceAll("đ", "d");
    }

    public static String textBoldWithHTML(String text) {
        return "<b>" + text + "</b>";
        //return "<b>" + text + "</b>";
    }

    public static String textBoldWithHTMLWithMaxLength(String text, int maxLength) {
        StringBuilder sb = new StringBuilder();
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "&#8230;";
        }
        sb.append("<b>").append(text).append("</b>");
        return sb.toString();
    }

    public static String getSubLongText(String text, int maxLength) {
        if (TextUtils.isEmpty(text)) return "";
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "...";
        }
        return text;
    }

    public static String textColorBlackWithHTML(String text) {
        return "<font color = \"000000\">" + text + "</font>";
    }

    public static String textColorBlackWithHTMLWithMaxLength(String text, int maxLength) {
        StringBuilder sb = new StringBuilder();
        if (text.length() > maxLength) {
            text = text.substring(0, maxLength) + "&#8230;";
        }
        sb.append("<font color = \"000000\">").append(text).append("</font>");
        return sb.toString();
    }

    public static int parserIntFromString(String input, int defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        int value = defaultValue;
        try {
            value = Integer.valueOf(input);
        } catch (Exception e) {
            //Log.e(TAG, e);
        }
        return value;
    }

    public static long parserLongFromString(String input, long defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        long value = defaultValue;
        try {
            value = Long.valueOf(input);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return value;
    }

    public static double parserDoubleFromString(String input, double defaultValue) {
        if (TextUtils.isEmpty(input)) {
            return defaultValue;
        }
        double value = defaultValue;
        try {
            value = Double.valueOf(input);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return value;
    }

    public static String trimTextOnMedia(String s) {
        s = s.replace("  ", " ");
        s = s.replaceAll("(?m)^[ \t]*\r?\n", "");
        return s;
    }

    public static String replaceUrl(String url) {
        String tmpUrl = url;
        try {
            URL link = new URL(url);
            String host_m = link.getHost();
            if (host_m.startsWith(HOST_START_M)) {
                String host = host_m.substring(2);
                url = url.replaceFirst(host_m, host);
            } else if (host_m.startsWith(HOST_START_WWW_M)) {
                String host = host_m.substring(HOST_START_WWW_M.length());
                url = url.replaceFirst(host_m, host);
            } else if (host_m.startsWith(HOST_START_WWW)) {
                String host = host_m.substring(HOST_START_WWW.length());
                url = url.replaceFirst(host_m, host);
            }
        } catch (Exception e) {
            Log.e(TAG, e);
            return tmpUrl;
        }
        return url;
    }

    public static int countWords(String text) {
        String trim = text.trim();
        if (trim.isEmpty())
            return 0;
        return trim.split("\\s+").length; // separate string around spaces
    }

    public static String getTextNumberNotify(int numberNotify) {
        if (numberNotify > MAX_NUMBER_NOTIFY) {
            return "9+";
        } else {
            return String.valueOf(numberNotify);
        }
    }

    public static String textColorWithHTML(String text, String colorInHtml) {
        if (TextUtils.isEmpty(colorInHtml)) {
            return textColorBlackWithHTML(text);
        }
        return "<font color = \"" + colorInHtml + "\">" + text + "</font>";
    }

    public static Spanned fromHtml(String source) {
        if (Version.hasN()) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY);
        } else {
            return Html.fromHtml(source);
        }
    }

    public static Spanned fromHtml(String source, Html.ImageGetter imageGetter, Html.TagHandler tagHandler) {
        if (Version.hasN()) {
            return Html.fromHtml(source, Html.FROM_HTML_MODE_LEGACY, imageGetter, tagHandler);
        } else {
            return Html.fromHtml(source, imageGetter, tagHandler);
        }
    }

    public static String formatTextDecember(String input) {
        try {
            long inputLong = convertTextDecemberToLong(input, 0);
            DecimalFormat df = new DecimalFormat("###,###,###");
            DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
            sym.setGroupingSeparator('.');
            df.setDecimalFormatSymbols(sym);
            return df.format(inputLong);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return input;
    }

    public static String formatTextDecemberUMoney(String input) {
        try {
            long inputLong = convertTextDecemberToLongUMoney(input, 0);
            DecimalFormat df = new DecimalFormat("###,###,###");
            DecimalFormatSymbols sym = DecimalFormatSymbols.getInstance();
            sym.setGroupingSeparator(',');
            df.setDecimalFormatSymbols(sym);
            return df.format(inputLong);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return input;
    }

    public static long convertTextDecemberToLong(String input, long defValue) {
        try {
            return parserLongFromString(input.replaceAll("\\.", ""), defValue);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return defValue;
    }

    public static long convertTextDecemberToLongUMoney(String input, long defValue) {
        try {
            return parserLongFromString(input.replaceAll(",", ""), defValue);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return defValue;
    }

    public static String formatTextSearchSieHai(String input) {
        return input.replaceAll(REGEX_SIEUHAI, "");
    }

    public static String convert(String org) {
//        return normalizer(org);
        // convert to VNese no sign. @haidh 2008
        char[] arrChar = org.toCharArray();
        char[] result = new char[arrChar.length];
        for (int i = 0; i < arrChar.length; i++) {
            switch (arrChar[i]) {
                case '\u00E1':
                case '\u00E0':
                case '\u1EA3':
                case '\u00E3':
                case '\u1EA1':
                case '\u0103':
                case '\u1EAF':
                case '\u1EB1':
                case '\u1EB3':
                case '\u1EB5':
                case '\u1EB7':
                case '\u00E2':
                case '\u1EA5':
                case '\u1EA7':
                case '\u1EA9':
                case '\u1EAB':
                case '\u1EAD':
                case '\u0203':
                case '\u01CE': {
                    result[i] = 'a';
                    break;
                }
                case '\u00E9':
                case '\u00E8':
                case '\u1EBB':
                case '\u1EBD':
                case '\u1EB9':
                case '\u00EA':
                case '\u1EBF':
                case '\u1EC1':
                case '\u1EC3':
                case '\u1EC5':
                case '\u1EC7':
                case '\u0207': {
                    result[i] = 'e';
                    break;
                }
                case '\u00ED':
                case '\u00EC':
                case '\u1EC9':
                case '\u0129':
                case '\u1ECB': {
                    result[i] = 'i';
                    break;
                }
                case '\u00F3':
                case '\u00F2':
                case '\u1ECF':
                case '\u00F5':
                case '\u1ECD':
                case '\u00F4':
                case '\u1ED1':
                case '\u1ED3':
                case '\u1ED5':
                case '\u1ED7':
                case '\u1ED9':
                case '\u01A1':
                case '\u1EDB':
                case '\u1EDD':
                case '\u1EDF':
                case '\u1EE1':
                case '\u1EE3':
                case '\u020F': {
                    result[i] = 'o';
                    break;
                }
                case '\u00FA':
                case '\u00F9':
                case '\u1EE7':
                case '\u0169':
                case '\u1EE5':
                case '\u01B0':
                case '\u1EE9':
                case '\u1EEB':
                case '\u1EED':
                case '\u1EEF':
                case '\u1EF1': {
                    result[i] = 'u';
                    break;
                }
                case '\u00FD':
                case '\u1EF3':
                case '\u1EF7':
                case '\u1EF9':
                case '\u1EF5': {
                    result[i] = 'y';
                    break;
                }
                case '\u0111': {
                    result[i] = 'd';
                    break;
                }
                case '\u00C1':
                case '\u00C0':
                case '\u1EA2':
                case '\u00C3':
                case '\u1EA0':
                case '\u0102':
                case '\u1EAE':
                case '\u1EB0':
                case '\u1EB2':
                case '\u1EB4':
                case '\u1EB6':
                case '\u00C2':
                case '\u1EA4':
                case '\u1EA6':
                case '\u1EA8':
                case '\u1EAA':
                case '\u1EAC':
                case '\u0202':
                case '\u01CD': {
                    result[i] = 'A';
                    break;
                }
                case '\u00C9':
                case '\u00C8':
                case '\u1EBA':
                case '\u1EBC':
                case '\u1EB8':
                case '\u00CA':
                case '\u1EBE':
                case '\u1EC0':
                case '\u1EC2':
                case '\u1EC4':
                case '\u1EC6':
                case '\u0206': {
                    result[i] = 'E';
                    break;
                }
                case '\u00CD':
                case '\u00CC':
                case '\u1EC8':
                case '\u0128':
                case '\u1ECA': {
                    result[i] = 'I';
                    break;
                }
                case '\u00D3':
                case '\u00D2':
                case '\u1ECE':
                case '\u00D5':
                case '\u1ECC':
                case '\u00D4':
                case '\u1ED0':
                case '\u1ED2':
                case '\u1ED4':
                case '\u1ED6':
                case '\u1ED8':
                case '\u01A0':
                case '\u1EDA':
                case '\u1EDC':
                case '\u1EDE':
                case '\u1EE0':
                case '\u1EE2':
                case '\u020E': {
                    result[i] = 'O';
                    break;
                }
                case '\u00DA':
                case '\u00D9':
                case '\u1EE6':
                case '\u0168':
                case '\u1EE4':
                case '\u01AF':
                case '\u1EE8':
                case '\u1EEA':
                case '\u1EEC':
                case '\u1EEE':
                case '\u1EF0': {
                    result[i] = 'U';
                    break;
                }

                case '\u00DD':
                case '\u1EF2':
                case '\u1EF6':
                case '\u1EF8':
                case '\u1EF4': {
                    result[i] = 'Y';
                    break;
                }
                case '\u0110':
                case '\u00D0':
                case '\u0089': {
                    result[i] = 'D';
                    break;
                }
                default:
                    result[i] = arrChar[i];
            }
        }
        return new String(result);
    }

    //public static SpannableStringBuilder getTextBoldFromKeySearch(String text, String keySearch) {
    public static Spannable getTextBoldFromKeySearch(String text, String keySearch) {
        Log.d(TAG, "getTextBoldFromKeySearch text: " + text + ", keySearch: " + keySearch);
        SpannableStringBuilder builder = new SpannableStringBuilder(text);
        //SpannableString builder = new SpannableString(text);
        if (!TextUtils.isEmpty(keySearch) && !TextUtils.isEmpty(text)) {
            keySearch = keySearch.trim();
            String textUnmark = text.trim();
            //tên giống hệt keySearch
            if (textUnmark.startsWith(keySearch)) {
                int startingIndex = textUnmark.indexOf(keySearch);
                int endingIndex = startingIndex + keySearch.length();
                if (startingIndex >= 0 && endingIndex >= 0) {
                    builder.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0);
                    return builder;
                }
            }
            String keySearchUnmark = TextHelper.getInstant().convertUnicodeToAscci(keySearch).toLowerCase(Locale.US);
            textUnmark = TextHelper.getInstant().convertUnicodeToAscci(text).toLowerCase(Locale.US);
            if (textUnmark.startsWith(keySearchUnmark)) {
                int startingIndex = textUnmark.indexOf(keySearchUnmark);
                int endingIndex = startingIndex + keySearchUnmark.length();
                if (startingIndex >= 0 && endingIndex >= 0) {
                    builder.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0);
                    return builder;
                }
            }
            List<String> listKeySearch = new ArrayList<>(Arrays.asList(keySearchUnmark.split(" ")));
            if (Utilities.notEmpty(listKeySearch)) {
                for (String textItem : listKeySearch) {
                    if (textItem != null) {
                        textItem = textItem.trim();
                        if (textItem.length() > 0) {
                            int startingIndex = textUnmark.indexOf(textItem);
                            int endingIndex = startingIndex + textItem.length();
                            if (startingIndex >= 0 && endingIndex >= 0) {
                                builder.setSpan(new StyleSpan(Typeface.BOLD), startingIndex, endingIndex, 0);
                            }
                        }
                    }
                }
            }
        }
        return builder;
    }

    private static Pattern initPattern(String regex) {
        Pattern pattern = null;
        Log.i(TAG, "initPattern: " + regex);
        if (!TextUtils.isEmpty(regex)) {
            try {
                pattern = Pattern.compile(regex);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return pattern;
    }

    public static String formatCurrencyVN(long number) {
        try {
            if (localeVN == null) localeVN = new Locale("vi", "VN");
            return NumberFormat.getInstance(localeVN).format(number);
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "" + number;
    }

    public static String formatPhoneNumberToString(String phoneNumber) {
        if (phoneNumber == null) return "";
        try {
            return phoneNumber.trim().replaceFirst("(\\d+)(\\d{3})(\\d{3})", "$1.$2.$3");
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return phoneNumber.trim();
    }

    public static String getSeriesCardFromQRCode(String text) {
        Pattern pattern = initPattern("(.*?)(\\d{13,15})(.*?)");
        String result = text;
        if (!TextUtils.isEmpty(text) && pattern != null) {
            Matcher matcher = pattern.matcher(text);
            if (matcher.find()) {
                int index = matcher.groupCount() - 1;
                if (index >= 0) {
                    result = matcher.group(index);
                }
            }
        }
        return result;
    }

    public static String getDomain(String url) {
        try {
            URI uri = new URI(url);
            String domain = uri.getHost();
            return domain.startsWith("www.") ? domain.substring(4) : domain;
        } catch (Exception e) {
            Log.e(TAG, e);
        }
        return "";
    }

    public static String convertUnicodeForSearch(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        return convert(input);
    }

    @SuppressLint("NewApi")
    public static String convertUtf8ToUnicode(String input) {
        if (input != null) {
            try {
                String temp = new String(input.getBytes(StandardCharsets.ISO_8859_1), StandardCharsets.UTF_8);
                if (Version.hasN()) {
                    return Html.fromHtml(temp, Html.FROM_HTML_MODE_LEGACY).toString();
                } else {
                    return Html.fromHtml(temp).toString();
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        return input;
    }

    private void initPattern() {
        patternNormalizer = Pattern.compile("\\p{InCombiningDiacriticalMarks}+");
//        patternA = Pattern.compile("[ă,â,à,ằ,ầ,á,ắ,ấ,ả,ẳ,ẩ,ã,ẵ,ẫ,ạ,ặ,ậ]");
//        patternE = Pattern.compile("[è,é,ẻ,ẽ,ẹ,ê,ề,ế,ể,ễ,ệ]");
//        patternI = Pattern.compile("[ì,í,ỉ,ĩ,ị]");
//        patternO = Pattern.compile("[ô,ơ,ò,ồ,ờ,ó,ố,ớ,ỏ,ổ,ở,õ,ỗ,ỡ,ọ,ộ,ợ]");
//        patternU = Pattern.compile("[ư,ù,ừ,ú,ứ,ủ,ử,ũ,ữ,ụ,ự]");
//        patternY = Pattern.compile("[ỳ,ý,ỷ,ỹ,ỵ]");
        patternD = Pattern.compile("[đ]");
//        patternSpecial = Pattern.compile("[\"{}]");
        mPatternYoutube = Pattern.compile(Config.Pattern.LINK_YOUTUBE, Pattern.CASE_INSENSITIVE);
    }

    private void initPatternXml() {
        patternXmlCr = Pattern.compile("\n");
    }

    public String convertUnicodeToAscci(String input) {
        if (TextUtils.isEmpty(input)) {
            return "";
        }
        input = input.toLowerCase();
        // replace dd
        input = patternD.matcher(input).replaceAll("d");
        String nfdNormalizedString = Normalizer.normalize(input, Normalizer.Form.NFD);
        return patternNormalizer.matcher(nfdNormalizedString).replaceAll("");
    }

    public String escapeXml(String input) {
        input = StringUtils.escapeForXML(input);
        if (patternXmlCr == null) {
            patternXmlCr = Pattern.compile("\n");
        }
        input = patternXmlCr.matcher(input).replaceAll("<br/>");
        return input;
    }

    /**
     * remove underline suggest text
     *
     * @param spannable
     */
    public void removeUnderlines(Spannable spannable) {
        ClickableSpan[] urlSpans = spannable.getSpans(0, spannable.length(), ClickableSpan.class);
        for (ClickableSpan urlSpan : urlSpans) {
            spannable.removeSpan(urlSpan);
        }
        UnderlineSpan[] underlineSpans = spannable.getSpans(0, spannable.length(), UnderlineSpan.class);
        for (UnderlineSpan underlineSpan : underlineSpans) {
            spannable.removeSpan(underlineSpan);
        }
        ForegroundColorSpan[] foregroundColorSpans = spannable.getSpans(0, spannable.length(), ForegroundColorSpan
                .class);
        for (ForegroundColorSpan foregroundColorSpan : foregroundColorSpans) {
            Log.i(TAG, "remove foregroundColorSpan " + foregroundColorSpan);
            spannable.removeSpan(foregroundColorSpan);
        }
        BackgroundColorSpan[] backgroundColorSpans = spannable.getSpans(0, spannable.length(), BackgroundColorSpan
                .class);
        //loi bi mau text o htc la dung class Back...
        for (BackgroundColorSpan backgroundColorSpan : backgroundColorSpans) {
            Log.i(TAG, "remove backgroundColorSpan " + backgroundColorSpan);
            spannable.removeSpan(backgroundColorSpan);
        }
    }

    /**
     * remove style text
     *
     * @param spannable
     */
    public void removeStyles(Spannable spannable) {
        StyleSpan[] styleSpans = spannable.getSpans(0, spannable.length(), StyleSpan.class);
        for (StyleSpan styleSpan : styleSpans) {
            if (styleSpan.getStyle() == Typeface.BOLD) {
                spannable.removeSpan(styleSpan);
            }
            if (styleSpan.getStyle() == Typeface.ITALIC) {
                spannable.removeSpan(styleSpan);
            }
        }
    }

    public String processFriendName(String input) {
        StringBuilder sb = new StringBuilder();
        if (TextUtils.isEmpty(input)) {
            return "";
        } else if (input.length() <= (Constants.MESSAGE.FRIEND_NAME_INBOX_MAX_LENGTH + 1)) {
            sb.append(input);
        } else {// ten dai hon 12 kt
            // truong hop ky tu thu 13 la dau cach thi lay luon 12 kt dau
            String subInput = input.substring(0, Constants.MESSAGE.FRIEND_NAME_INBOX_MAX_LENGTH);
            if (input.charAt(Constants.MESSAGE.FRIEND_NAME_INBOX_MAX_LENGTH) == ' ') {
                sb.append(subInput);
            } else {
                int lastIndexSpace = subInput.lastIndexOf(" ");
                if (lastIndexSpace > 0) { // ten co dau cách, cat tu dau den dau cach gan max leng nhat
                    sb.append(subInput, 0, lastIndexSpace);
                } else {
                    sb.append(subInput);
                }
            }
        }
        sb.append(": ");
        return sb.toString();
    }

    public boolean isYoutubeUrl(String link) {
        Log.d(TAG, "isYoutubeUrl: " + link);
        return !TextUtils.isEmpty(link) && mPatternYoutube.matcher(link).matches();
    }

    public String deleteMultiLine(String input) {
        if (patternXmlCr == null) {
            patternXmlCr = Pattern.compile("\n");
        }
        if (patternXmlLf == null) {
            patternXmlLf = Pattern.compile("\n");
        }
        input = patternXmlCr.matcher(input).replaceAll(" ");
        input = patternXmlLf.matcher(input).replaceAll("");
        return input;
    }

    private void readDataFromAssert(Context context) {
        long beginTime = System.currentTimeMillis();
        Log.d(TAG, "readDataFromAssert");
        XmlPullParserFactory pullParserFactory;
        try {
            pullParserFactory = XmlPullParserFactory.newInstance();
            XmlPullParser parser = pullParserFactory.newPullParser();

            InputStream in_s = context.getAssets().open("DS_tuxau.xml");
            parser.setFeature(XmlPullParser.FEATURE_PROCESS_NAMESPACES, false);
            parser.setInput(in_s, null);
            parseXML(parser);
        } catch (XmlPullParserException | IOException e) {
            Log.e(TAG, e);
        }
        Log.d(TAG, "readDataFromAssert take: " + (System.currentTimeMillis() - beginTime));
        //create pattern
    }

    private void parseXML(XmlPullParser parser) throws XmlPullParserException, IOException {
        int eventType = parser.getEventType();
        StringBuilder sb = new StringBuilder();
        String currentBadWord = null;
        while (eventType != XmlPullParser.END_DOCUMENT) {
            String name;
            switch (eventType) {
                case XmlPullParser.START_DOCUMENT:
                    break;
                case XmlPullParser.START_TAG:
                    name = parser.getName();
                    if ("ss:Table".equals(name)) {
                        currentBadWord = "";
                    } else if (currentBadWord != null) {
                        if ("Data".equals(name)) {
                            currentBadWord = parser.nextText();
                        }
                    }
                    break;
                case XmlPullParser.END_TAG:
                    name = parser.getName();
                    if ("ss:Cell".equalsIgnoreCase(name) && currentBadWord != null) {
//                        String pattern = "(\\\\()?(?ui)(" + currentBadWord + ")(\\\\))?";
//                        sensitiveWordPatterns.add(Pattern.compile(pattern, Pattern.CASE_INSENSITIVE));
//                        sensitiveWordPatterns.add(Pattern.compile("\\b" + currentBadWord + "\\b", Pattern.CASE_INSENSITIVE));
                        //sb.append("|").append(currentBadWord);
                        sb.append("|").append("\\b(?i)").append(currentBadWord).append("\\b");
                    }
            }
            eventType = parser.next();
        }
        String pattern = sb.toString();
        if (Utilities.notEmpty(pattern)) {
            pattern = pattern.substring(1);
            //pattern = "\\b" + pattern + "\\b";
            try {
                patternSensitiveWord = Pattern.compile(pattern, Pattern.CASE_INSENSITIVE);
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
    }



    private boolean checkLinkWithPattern(Pattern pattern, String input) {
        if (!TextUtils.isEmpty(input) && pattern != null) {
            Log.i(TAG, "checkLinkWithPattern pattern: " + pattern.pattern() + "\ninput: " + input);
            Matcher matcher = pattern.matcher(input);
            return matcher.find();
        }
        return false;
    }

    public boolean isLinkMusicSong(String url) {
        boolean result = checkLinkWithPattern(patternMusicSong, url);
        //Log.i(TAG, "isLinkMusicSong url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkMusicAlbum(String url) {
        boolean result = checkLinkWithPattern(patternMusicAlbum, url);
        //Log.i(TAG, "isLinkMusicAlbum url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkMusicVideo(String url) {
        boolean result = checkLinkWithPattern(patternMusicVideo, url);
        //Log.i(TAG, "isLinkMusicVideo url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkMusicPlaylist(String url) {
        boolean result = checkLinkWithPattern(patternMusicPlaylist, url);
        //Log.i(TAG, "isLinkMusicPlaylist url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkMoviesDetail(String url) {
        boolean result = checkLinkWithPattern(patternMoviesDetail, url);
        //Log.i(TAG, "isLinkMovies url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkRingMeVideo(String url) {
        return url.contains("kakoak.tls.tl/video/") || url.contains("kakoak.page.link/video/") ;
    }

    public boolean isLinkRingmeMusic(String url) {
        return url.contains("kakoak.tls.tl/music/");
    }

    public boolean isLinkRingMeMovie(String url) {
        return url.contains("kakoak.tls.tl/movie/")|| url.contains("kakoak.page.link/movie/");
    }

    public boolean isLinkRingMeChannel(String url) {
        boolean result = checkLinkWithPattern(patternRingMeChannel, url);
        return result;
    }

    public boolean isLinkRingMeAddFriend(String url) {
        return url.contains("kakoak.tls.tl/qrchat/");
    }

    public boolean isLinkNetNewsDetail(String url) {
        boolean result = checkLinkWithPattern(patternNetNewsDetail, url);
        //Log.i(TAG, "isLinkNetNewsDetail url: " + url + "\nresult: " + result);
        return result;
    }

    public boolean isLinkTiinDetail(String url) {
        boolean result = checkLinkWithPattern(patternTiinDetail, url);
        return result;
    }

    private String getItemIdFromUrl(Pattern pattern, String url) {
        String result = "";
        if (!TextUtils.isEmpty(url) && pattern != null) {
            Matcher matcher = pattern.matcher(url);
            if (matcher.find()) {
                int index = matcher.groupCount() - 2;
                if (index >= 0) {
                    result = matcher.group(index);
                }
            }
        }
        return result;
    }

    public String getIdentifyOfMusicSong(String url) {
        String result = getItemIdFromUrl(patternMusicSong, url);
        Log.i(TAG, "getIdentifyOfMusicSong url: " + url + "\nidentify: " + result);
        return result;
    }

    public String getIdentifyOfMusicAlbum(String url) {
        String result = getItemIdFromUrl(patternMusicAlbum, url);
        Log.i(TAG, "getIdentifyOfMusicAlbum url: " + url + "\nidentify: " + result);
        return result;
    }

    public String getIdentifyOfMusicVideo(String url) {
        String result = getItemIdFromUrl(patternMusicVideo, url);
        Log.i(TAG, "getIdentifyOfMusicVideo url: " + url + "\nidentify: " + result);
        return result;
    }

    public String getIdentifyOfMusicPlaylist(String url) {
        String result = getItemIdFromUrl(patternMusicPlaylist, url);
        Log.i(TAG, "getIdentifyOfMusicPlaylist url: " + url + "\nidentify: " + result);
        return result;
    }

    public String getMoviesId(String url) {
        String result = getItemIdFromUrl(patternMoviesDetail, url);
        Log.i(TAG, "getMoviesId url: " + url + "\nid: " + result);
        return result;
    }

    public String getRingMeVideoId(String url) {
        String result = getItemIdFromUrl(patternRingMeVideo, url);
        Log.i(TAG, "getRingMeVideoId url: " + url + "\nid: " + result);
        return result;
    }

    public String getRingMeChannelId(String url) {
        String result = getItemIdFromUrl(patternRingMeChannel, url);
        Log.i(TAG, "getRingMeChannelId url: " + url + "\nid: " + result);
        return result;
    }

    public ArrayList<String> getId_Cid_Pid_NetNewsDetail(String url) {
        ArrayList<String> result = null;
        if (!TextUtils.isEmpty(url) && patternNetNewsDetail != null) {
            Matcher matcher = patternNetNewsDetail.matcher(url);
            if (matcher.find()) {
                result = new ArrayList<>();
                int index = matcher.groupCount() - 2;
                if (index >= 4) {
                    String tmp;
                    tmp = matcher.group(index);
                    if (!TextUtils.isEmpty(tmp)) result.add(tmp);
                    tmp = matcher.group(index - 2);
                    if (!TextUtils.isEmpty(tmp)) result.add(tmp);
                    tmp = matcher.group(index - 4);
                    if (!TextUtils.isEmpty(tmp)) result.add(tmp);
                }
            }
        }
        Log.i(TAG, "getId_Cid_Pid_NetNews url: " + url + "\nresult: " + result);
        return result;
    }

    public void initPatternFromConfig(String key, String regex) {
        if (TextUtils.isEmpty(key)) return;
        switch (key) {
            case Constants.PREFERENCE.CONFIG.REGEX_MUSIC_SONG:
                patternMusicSong = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_MUSIC_ALBUM:
                patternMusicAlbum = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_MUSIC_VIDEO:
                patternMusicVideo = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_MUSIC_PLAYLIST:
                patternMusicPlaylist = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_MOVIES_DETAIL:
                patternMoviesDetail = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_KAKOAK_VIDEO:
                patternRingMeVideo = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_KAKOAK_CHANNEL:
                patternRingMeChannel = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_NETNEWS_DETAIL:
                patternNetNewsDetail = initPattern(regex);
                break;
            case Constants.PREFERENCE.CONFIG.REGEX_TIIN_DETAIL:
                patternTiinDetail = initPattern(regex);
                break;
        }
    }

    private String getPatternStringFromConfig(String config) {
        //Config là mảng các từ khóa cần tạo pattern
        String result = "";
        if (Utilities.notEmpty(config) && !"-".equals(config)) {
            try {
                StringBuilder sb = new StringBuilder();
                JSONArray jsonArray = new JSONArray(config);
                for (int i = 0; i < jsonArray.length(); i++) {
                    String tmp = jsonArray.optString(i);
                    if (Utilities.notEmpty(tmp)) {
                        sb.append("|").append("\\b(?i)").append(tmp).append("\\b");
                    }
                }
                result = sb.toString();
                if (Utilities.notEmpty(result)) {
                    result = result.substring(1);
                }
            } catch (Exception e) {
                Log.e(TAG, e);
            }
        }
        //Log.d(TAG, "getPatternStringFromConfig: " + config + "\n" + result);
        return result;
    }





    public boolean checkBadWordToSendComment(EditText editText) {
        boolean result = false;
        if (editText != null) {
            String content = editText.getText().toString();
            if (Utilities.notEmpty(content) && patternBadWord != null) {
                SpannableString spannableString = new SpannableString(content);
                ForegroundColorSpan foregroundSpan = new ForegroundColorSpan(Color.RED);
                try {
                    Matcher matcher = patternBadWord.matcher(content);
                    result = matcher.find();
                    if (result) {
                        int startIndex = matcher.start();
                        int endIndex = matcher.end();
                        spannableString.setSpan(foregroundSpan, startIndex, endIndex, Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
                        editText.setText(spannableString);
                        editText.setSelection(endIndex);
                    }
                } catch (Exception e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public String filterSensitiveWords(String input) {
        String content = input;
        if (Utilities.notEmpty(content) && patternSensitiveWord != null) {
            try {
                Matcher matcher = patternSensitiveWord.matcher(content);
                if (matcher.find()) {
                    content = matcher.replaceAll(BAD_WORD_MASK);
                }
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
        Log.i(TAG, "filterSensitiveWords: " + input + "\n" + content);
        return content;
    }

    public static int getHeight(Context context, String text, int textSize, int deviceWidth) {
        try {
            TextView textView = new TextView(context);
            textView.setText(text);
            textView.setTextSize(TypedValue.COMPLEX_UNIT_DIP, textSize);
            int widthMeasureSpec = View.MeasureSpec.makeMeasureSpec(deviceWidth, View.MeasureSpec.AT_MOST);
            int heightMeasureSpec = View.MeasureSpec.makeMeasureSpec(0, View.MeasureSpec.UNSPECIFIED);
            textView.measure(widthMeasureSpec, heightMeasureSpec);
            return textView.getMeasuredHeight();
        } catch (Exception e) {
            e.printStackTrace();
        }
        return -1;
    }
}