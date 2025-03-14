package com.example.livestream_update.Ringme.Helper.emoticon;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.text.Html;
import android.text.SpannableStringBuilder;
import android.text.Spanned;
import android.text.TextUtils;
import android.text.style.ImageSpan;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.EmoBitmapDrawable;
import com.vtm.ringme.helper.TextHelper;
import com.vtm.ringme.model.StickerItem;
import com.vtm.ringme.utils.Log;

import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class EmoticonUtils {
    public static final int NO_OF_EMOTICONS = 85;
    private static Pattern[] emoticonPattern;
    //
    private static EmoticonUtils instance = null;
    private static final String TAG = EmoticonUtils.class.getSimpleName();

    public EmoticonUtils(Context context) {

    }

    //
    private static synchronized void initEmoticonPattern() {
        if (emoticonPattern == null || emoticonPattern.length <= 0) {
            int length = EMOTICON_TEXTS.length;
            // rieng ky tu ":d" se support ca ":d" va ":D"
            emoticonPattern = new Pattern[length];
            emoticonPattern[0] = Pattern.compile(REGEX_KEKE);
            for (int i = 1; i < length; i++) {
                if (i == 64) {
                    emoticonPattern[i] = Pattern.compile(REGEX_HEART);
                } else {
                    emoticonPattern[i] = Pattern.compile(Pattern.quote(EMOTICON_TEXTS[i]));
                }
            }
        }
    }

    public static final String REGEX_KEKE = "\\Q:d\\E|\\Q:D\\E";
    public static final String REGEX_HEART = "\\Q<3\\E|\\Q&lt;3\\E";

    protected static final String[] EMOTICON_TEXTS = {
            ":d", ":-)", ":x", "[dizzy]", ":-(",
            ":p", "[crazy]", "[hum]", "[cute]", "[angry]",
            "[sweat]", ":)", "[sleepy]", ":$", "[razz]",
            "[cool]", "[bad luck]", ":O", "[curse]", "[contempt]",
            "[booger]", "[lust]", "[clap]", ":(", "[think]",
            "[sick]", ":*", "[hug]", "[supercilious]", "[right hum]",
            "[left hum]", "[quiet]", "[grievance]", "[yawn]", "[beat]",
            ":?", ";)", "[shy]", "[gonna cry]", ":h",
            "[silent]", "[strong]", "[weak]", "[awesome]", "[meaningless]",
            "[onlooker]", "[mighty]", "[camera]", "[car]", "[plane]",
            "[love]", "[ultraman]", "[rabbit]", "[panda]", "[no]",
            "[ok]", "[like]", "[tempt]", "[yeah]", "[love u]",
            "[fist]", "[poor]", "[shake hand]", "[rose]", "<3",
            "[broken heart]", "[pig]", "[coffee]", "[mic]", "[moon]",
            "[sun]", "[beer]", "[adorable]", "[gift]", "[follow]",
            "[clock]", "[bike]", "[cake]", "[scarf]", "[glove]",
            "[snow]", "[snowman]", "[hat]", "[leaf]", "[football]"
    };

    public static String[] getEmoticonTexts() {
        return EMOTICON_TEXTS;
    }

    protected static final String[] EMOTICON_KEYS = {
            "001", "002", "003", "004", "005",
            "006", "007", "008", "009", "010",
            "011", "012", "013", "014", "015",
            "016", "017", "018", "019", "020",
            "021", "022", "023", "024", "025",
            "026", "027", "028", "029", "030",
            "031", "032", "033", "034", "035",
            "036", "037", "038", "039", "040",
            "041", "042", "043", "044", "045",
            "046", "047", "048", "049", "050",
            "051", "052", "053", "054", "055",
            "056", "057", "058", "059", "060",
            "061", "062", "063", "064", "065",
            "066", "067", "068", "069", "070",
            "071", "072", "073", "074", "075",
            "076", "077", "078", "079", "080",
            "081", "082", "083", "084", "085"};

    public static String[] getEmoticonKeys() {
        return EMOTICON_KEYS;
    }

    protected static final String[] EMOTICON_TAGS = {
            "<img src=\"001\"/>", "<img src=\"002\"/>", "<img src=\"003\"/>", "<img src=\"004\"/>", "<img " +
            "src=\"005\"/>",
            "<img src=\"006\"/>", "<img src=\"007\"/>", "<img src=\"008\"/>", "<img src=\"009\"/>", "<img " +
            "src=\"010\"/>",
            "<img src=\"011\"/>", "<img src=\"012\"/>", "<img src=\"013\"/>", "<img src=\"014\"/>", "<img " +
            "src=\"015\"/>",
            "<img src=\"016\"/>", "<img src=\"017\"/>", "<img src=\"018\"/>", "<img src=\"019\"/>", "<img " +
            "src=\"020\"/>",
            "<img src=\"021\"/>", "<img src=\"022\"/>", "<img src=\"023\"/>", "<img src=\"024\"/>", "<img " +
            "src=\"025\"/>",
            "<img src=\"026\"/>", "<img src=\"027\"/>", "<img src=\"028\"/>", "<img src=\"029\"/>", "<img " +
            "src=\"030\"/>",
            "<img src=\"031\"/>", "<img src=\"032\"/>", "<img src=\"033\"/>", "<img src=\"034\"/>", "<img " +
            "src=\"035\"/>",
            "<img src=\"036\"/>", "<img src=\"037\"/>", "<img src=\"038\"/>", "<img src=\"039\"/>", "<img " +
            "src=\"040\"/>",
            "<img src=\"041\"/>", "<img src=\"042\"/>", "<img src=\"043\"/>", "<img src=\"044\"/>", "<img " +
            "src=\"045\"/>",
            "<img src=\"046\"/>", "<img src=\"047\"/>", "<img src=\"048\"/>", "<img src=\"049\"/>", "<img " +
            "src=\"050\"/>",
            "<img src=\"051\"/>", "<img src=\"052\"/>", "<img src=\"053\"/>", "<img src=\"054\"/>", "<img " +
            "src=\"055\"/>",
            "<img src=\"056\"/>", "<img src=\"057\"/>", "<img src=\"058\"/>", "<img src=\"059\"/>", "<img " +
            "src=\"060\"/>",
            "<img src=\"061\"/>", "<img src=\"062\"/>", "<img src=\"063\"/>", "<img src=\"064\"/>", "<img " +
            "src=\"065\"/>",
            "<img src=\"066\"/>", "<img src=\"067\"/>", "<img src=\"068\"/>", "<img src=\"069\"/>", "<img " +
            "src=\"070\"/>",
            "<img src=\"071\"/>", "<img src=\"072\"/>", "<img src=\"073\"/>", "<img src=\"074\"/>", "<img " +
            "src=\"075\"/>",
            "<img src=\"076\"/>", "<img src=\"077\"/>", "<img src=\"078\"/>", "<img src=\"079\"/>", "<img " +
            "src=\"080\"/>",
            "<img src=\"081\"/>", "<img src=\"082\"/>", "<img src=\"083\"/>", "<img src=\"084\"/>", "<img " +
            "src=\"085\"/>"};

    /*public static final String STICKER_FULL_FOLDER = "\\sticker\\voice_sticker\\";
    public static final String[] STICKER_IMAGE_NAME = {"1.png", "2.png",
            "3.png", "4.png", "5.png", "6.png", "7.png",
            "8.png", "9.png", "10.png", "11.png", "12.png",
            "13.png", "14.png", "15.png", "16.png", "17.png",
            "18.png", "19.png", "20.png", "21.png", "22.png"};*/

    public static final int BUZZ_STICKER_POSITION = 3; //vi tri buzz sticker trong bo default, bat dau tu 0

    protected static final String[] STICKER_IMAGE_PATHS = {"sticker/voice_sticker/01.png", "sticker/voice_sticker/02.png",
            "sticker/voice_sticker/03.png", "sticker/voice_sticker/04.png", "sticker/voice_sticker/05.png",
            "sticker/voice_sticker/06.png", "sticker/voice_sticker/07.png",
            "sticker/voice_sticker/08.png", "sticker/voice_sticker/09.png", "sticker/voice_sticker/10.png",
            "sticker/voice_sticker/11.png", "sticker/voice_sticker/12.png",
            "sticker/voice_sticker/13.png", "sticker/voice_sticker/14.png", "sticker/voice_sticker/15.png",
            "sticker/voice_sticker/16.png", "sticker/voice_sticker/17.png",
            "sticker/voice_sticker/18.png", "sticker/voice_sticker/19.png", "sticker/voice_sticker/20.png",
            "sticker/voice_sticker/21.png",
            "sticker/voice_sticker/22.png", "sticker/voice_sticker/23.png", "sticker/voice_sticker/24.png"
    };

    public static String[] getStickerImagePaths() {
        return STICKER_IMAGE_PATHS;
    }

    private static final String[] STICKER_VOICE_PATHS = {"sticker/voice_sticker/1voice.mp3",
            "sticker/voice_sticker/2voice.mp3", "sticker/voice_sticker/3voice.mp3", "sticker/voice_sticker/4voice.mp3",
            "sticker/voice_sticker/5voice.mp3", "sticker/voice_sticker/6voice.mp3", "sticker/voice_sticker/7voice.mp3",
            "sticker/voice_sticker/8voice.mp3", "sticker/voice_sticker/9voice.mp3", "sticker/voice_sticker/10voice.mp3",
            "sticker/voice_sticker/11voice.mp3",
            "sticker/voice_sticker/12voice.mp3", "sticker/voice_sticker/13voice.mp3", "sticker/voice_sticker/14voice" +
            ".mp3",
            "sticker/voice_sticker/15voice.mp3", "sticker/voice_sticker/16voice.mp3", "sticker/voice_sticker/17voice" +
            ".mp3",
            "sticker/voice_sticker/18voice.mp3", "sticker/voice_sticker/19voice.mp3", "sticker/voice_sticker/20voice" +
            ".mp3",
            "sticker/voice_sticker/21voice.mp3", "sticker/voice_sticker/22voice.mp3", "sticker/voice_sticker/23voice" +
            ".mp3",
            "sticker/voice_sticker/24voice.mp3"
    };
    public static final int STICKER_DEFAULT_COLLECTION_ID = -1;
    // recent sticker
    /*public static final StickerItem[] RECENT_STICKER_DEFAULT = {
           *//* new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 3, STICKER_IMAGE_PATHS[3], STICKER_VOICE_PATHS[3]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 4, STICKER_IMAGE_PATHS[4], STICKER_VOICE_PATHS[4]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 19, STICKER_IMAGE_PATHS[19], STICKER_VOICE_PATHS[19]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 20, STICKER_IMAGE_PATHS[20], STICKER_VOICE_PATHS[20]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 22, STICKER_IMAGE_PATHS[22], STICKER_VOICE_PATHS[22]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 23, STICKER_IMAGE_PATHS[23], STICKER_VOICE_PATHS[23])*//*
    };*/
    // quick reply
    protected static final int[] QUICK_REPLY_STICKER = {4, 22, 20, 23};

    public static int[] getQuickReplySticker() {
        return QUICK_REPLY_STICKER;
    }

    protected static final StickerItem[] QUICK_REPLY_STICKER_DEFAULT = {
            //    int collectionId, int itemId, String imagePath, String voicePath
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 4, STICKER_IMAGE_PATHS[4], STICKER_VOICE_PATHS[4]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 22, STICKER_IMAGE_PATHS[22], STICKER_VOICE_PATHS[22]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 20, STICKER_IMAGE_PATHS[20], STICKER_VOICE_PATHS[20]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 23, STICKER_IMAGE_PATHS[23], STICKER_VOICE_PATHS[23])};

    public static StickerItem[] getQuickReplyStickerDefault() {
        return QUICK_REPLY_STICKER_DEFAULT;
    }

    // general sticker
    protected static final StickerItem[] GENERAL_STICKER_DEFAULT = {
            //    int collectionId, int itemId, String imagePath, String voicePath
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 0, STICKER_IMAGE_PATHS[0], STICKER_VOICE_PATHS[0]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 1, STICKER_IMAGE_PATHS[1], STICKER_VOICE_PATHS[1]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 2, STICKER_IMAGE_PATHS[2], STICKER_VOICE_PATHS[2]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 3, STICKER_IMAGE_PATHS[3], STICKER_VOICE_PATHS[3]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 4, STICKER_IMAGE_PATHS[4], STICKER_VOICE_PATHS[4]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 5, STICKER_IMAGE_PATHS[5], STICKER_VOICE_PATHS[5]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 6, STICKER_IMAGE_PATHS[6], STICKER_VOICE_PATHS[6]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 23, STICKER_IMAGE_PATHS[23], STICKER_VOICE_PATHS[23]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 7, STICKER_IMAGE_PATHS[7], STICKER_VOICE_PATHS[7]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 8, STICKER_IMAGE_PATHS[8], STICKER_VOICE_PATHS[8]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 9, STICKER_IMAGE_PATHS[9], STICKER_VOICE_PATHS[9]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 10, STICKER_IMAGE_PATHS[10], STICKER_VOICE_PATHS[10]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 11, STICKER_IMAGE_PATHS[11], STICKER_VOICE_PATHS[11]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 12, STICKER_IMAGE_PATHS[12], STICKER_VOICE_PATHS[12]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 22, STICKER_IMAGE_PATHS[22], STICKER_VOICE_PATHS[22]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 13, STICKER_IMAGE_PATHS[13], STICKER_VOICE_PATHS[13]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 14, STICKER_IMAGE_PATHS[14], STICKER_VOICE_PATHS[14]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 15, STICKER_IMAGE_PATHS[15], STICKER_VOICE_PATHS[15]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 16, STICKER_IMAGE_PATHS[16], STICKER_VOICE_PATHS[16]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 17, STICKER_IMAGE_PATHS[17], STICKER_VOICE_PATHS[17]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 18, STICKER_IMAGE_PATHS[18], STICKER_VOICE_PATHS[18]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 19, STICKER_IMAGE_PATHS[19], STICKER_VOICE_PATHS[19]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 20, STICKER_IMAGE_PATHS[20], STICKER_VOICE_PATHS[20]),
            new StickerItem(STICKER_DEFAULT_COLLECTION_ID, 21, STICKER_IMAGE_PATHS[21], STICKER_VOICE_PATHS[21])

    };

    public static StickerItem[] getGeneralStickerDefault() {
        return GENERAL_STICKER_DEFAULT;
    }

    public static String getAssetPathEmoticon(String emoText) {
        initEmoticonPattern();
        Matcher matcher;
        for (int i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            matcher = emoticonPattern[i].matcher(emoText);
            if (matcher.find()) {
                return "file:///android_asset/emoticons/" + EmoticonUtils.EMOTICON_KEYS[i] + ".png";
            }
        }
        return null;
    }

    public static String emoTextToTag(String inputText) {
        initEmoticonPattern();
        if (TextUtils.isEmpty(inputText) ||
                emoticonPattern == null ||
                emoticonPattern.length <= 0) {
            return inputText;
        }
        for (int i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            Pattern pattern = emoticonPattern[i];
            if (pattern != null) {
                inputText = pattern.matcher(inputText).replaceAll(EMOTICON_TAGS[i]);
            }
        }
        return inputText;
    }


    public static String checkMessageLargeEmoticon(String inputText) {
        if (TextUtils.isEmpty(inputText)) return null;
        int length = inputText.length();
        if (length > 20) return null;
        initEmoticonPattern();
        String firstEmo = null;
        Pattern pattern;
        Matcher matcher;
        for (int i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            pattern = emoticonPattern[i];
            matcher = pattern.matcher(inputText);
            if (matcher.find()) {
                firstEmo = matcher.group();
                break;
            }
        }
        if (firstEmo != null && firstEmo.length() == length) {
            return firstEmo;
        }
        return null;
    }

    /**
     * check xem inputText co ton tai khong bo emoticon khong
     * neu co tra te emotag, neu khong tra ve null.
     * ham nay chi dung cho dau vao la text cua 1 emo, nhieu emo khong dung duoc
     *
     * @param input
     * @return
     */
    public static String getExistEmoTagFromText(String input) {
        initEmoticonPattern();
        boolean isExistEmo = false;
        Matcher matcher;
        for (int i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            matcher = emoticonPattern[i].matcher(input);
            if (matcher.find()) {
                isExistEmo = true;
                input = matcher.replaceAll(EMOTICON_TAGS[i]);
            }
        }
        if (isExistEmo) {
            return input;
        } else {
            return null;
        }
    }
    /*public static String getExistEmoTagFromText(String input) {
        for (int i = 0; i < EmoticonUtils.NO_OF_EMOTICONS; i++) {
            if (input.contains(EMOTICON_TEXTS[i])) {
                return emoticonPattern[i].matcher(input).replaceAll(EMOTICON_TAGS[i]);
            }
        }
        return null;
    }*/

    /**
     * important. Sort spans list
     *
     * @param spans
     * @param inputSpan
     * @return
     * @author ThaoDV
     */
    private static Object[] spansSort(Object[] spans, Spanned inputSpan) {
        try {
            int position;
            for (int i = 0; i < spans.length; i++) {
                position = i;
                for (int j = i + 1; j < spans.length; j++) {
                    Object span1 = spans[position];
                    Object span2 = spans[j];
                    if (inputSpan.getSpanStart(span2) < inputSpan
                            .getSpanStart(span1)) {
                        position = j;
                    }
                }
                Object tmp = spans[position];
                spans[position] = spans[i];
                spans[i] = tmp;
            }
            return spans;
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
            return null;
        }
    }

    public static Spanned getSpanned(Context ctx, String emoText, float textSize) {
        Html.ImageGetter imageGetter = EmoticonManager.getInstance(ctx).getImageGetter();
        String emoTag = EmoticonUtils.emoTextToTag(emoText);
        Spanned cs = TextHelper.fromHtml(emoTag, imageGetter, null);
        return cs;
    }

    /**
     * get spanned from tag
     *
     * @param ctx
     * @param tag
     * @param textSize
     * @return spanned
     */
    public static Spanned getSpannedFromTag(Context ctx, String tag, float textSize) {
        Html.ImageGetter imageGetter = EmoticonManager.getInstance(ctx).getImageGetter();
        Spanned cs = TextHelper.fromHtml(tag, imageGetter, null);
        return cs;
    }

    public static Drawable getDrawable(Context context, String emoText) {
        Html.ImageGetter imageGetter = EmoticonManager.getInstance(context).getImageGetter();
        String emoTag = EmoticonUtils.emoTextToTag(emoText);
        return imageGetter.getDrawable(emoTag);
    }

    public static String getRawTextFromSpan(Spanned inputSpan) {
        StringBuilder result = new StringBuilder();
        Object[] spans = inputSpan.getSpans(0, Integer.MAX_VALUE, ImageSpan.class);
        Object[] tmp = spansSort(spans, inputSpan);
        if (tmp != null) {
            spans = tmp;
        }
        // get all spans
        int lastImageSpanPosition = 0; // it's end position of image span
        if (spans.length == 0) {
            SpannableStringBuilder textSpan = new SpannableStringBuilder(
                    inputSpan);
            return textSpan.toString();
        }
        for (int i = 0; i < spans.length; i++) {
            // itarete searching ImageSpan
            Object span = spans[i];
            if (span instanceof ImageSpan) {
                int spanStart = inputSpan.getSpanStart(span);
                if (lastImageSpanPosition == spanStart) {
                    ImageSpan imageSpan = (ImageSpan) span;
                    if (imageSpan.getDrawable() instanceof EmoBitmapDrawable) {
                        EmoBitmapDrawable drawable = (EmoBitmapDrawable) imageSpan
                                .getDrawable();
                        String t = drawable.toString();
                        result.append(t);
                        lastImageSpanPosition = inputSpan.getSpanEnd(span);
                        // if this is the last ImageSpan
                        if (i == spans.length - 1) {
                            SpannableStringBuilder textSpan = new SpannableStringBuilder(
                                    inputSpan.subSequence(lastImageSpanPosition,
                                            inputSpan.length())
                            );
                            result.append(textSpan.toString());
                            return result.toString();
                        } else {
                            continue; // check if image is first span (avoid
                            // creation of empty spans).
                        }
                    } else {
                        result.append(" ");
                    }
                }
                if (spanStart > lastImageSpanPosition) {
                    SpannableStringBuilder textSpan = new SpannableStringBuilder(
                            inputSpan.subSequence(lastImageSpanPosition,
                                    spanStart)
                    );
                    // add all previous spans as a single Spannable object
                    result.append(textSpan.toString());
                }
                Drawable imageDrawable = ((ImageSpan) span).getDrawable();
                if (imageDrawable instanceof EmoBitmapDrawable) {
                    EmoBitmapDrawable drawable = (EmoBitmapDrawable) imageDrawable;
                    String t = drawable.toString();
                    result.append(t);
                } else {
                    result.append(" ");
                }
                lastImageSpanPosition = inputSpan.getSpanEnd(span);
            }

            if (i < spans.length - 1 && !containsImageSpan(spans, i + 1)) {
                // to not lose text in the end
                SpannableStringBuilder textSpan = new SpannableStringBuilder(
                        inputSpan.subSequence(lastImageSpanPosition,
                                inputSpan.getSpanEnd(spans[spans.length - 1])));
                result.append(textSpan.toString());
                break;
            }
        }
        try {
            // add last string to result
            if (spans.length > 0) {
                ImageSpan span = (ImageSpan) spans[spans.length - 1];
                int lastSpanPosition = inputSpan.getSpanEnd(span);
                SpannableStringBuilder text = new SpannableStringBuilder(
                        inputSpan.subSequence(lastSpanPosition,
                                inputSpan.length())
                );
                result.append(text);
            }
        } catch (Exception e) {
            Log.e(TAG, "Exception", e);
        }
        return result.toString();
    }

    private static boolean containsImageSpan(Object[] spans, int index) {
        Object span = spans[index];
        return span instanceof ImageSpan;
    }

    public static synchronized EmoticonUtils getInstance(Context context) {
        if (instance == null) {
            instance = new EmoticonUtils(context);

        }
        return instance;
    }

    public static StickerItem getDefaultStickerByItemId(int itemId) {
        for (StickerItem item : GENERAL_STICKER_DEFAULT) {
            if (item.getItemId() == itemId) {
                return item;
            }
        }
        return null;
    }

    // TODO cheat sau khi đổi cơ chế thread detail sang recycler view thì xem set no ham nay di
    public static void createCacheSpanned(ApplicationController application, String content) {
        EmoticonManager emoticonManager = EmoticonManager.getInstance(application);
        Spanned spanned = emoticonManager.getSpannedFromEmoticonCache(content);
        if (spanned == null) {
            Html.ImageGetter imageGetter = EmoticonManager.getInstance(application).getImageGetter();
            String contentToTag = TextHelper.getInstant().escapeXml(content);
            contentToTag = EmoticonUtils.emoTextToTag(contentToTag);
            spanned = TextHelper.fromHtml(contentToTag, imageGetter, null);
            emoticonManager.addSpannedToEmoticonCache(content, spanned);
        }
    }
}