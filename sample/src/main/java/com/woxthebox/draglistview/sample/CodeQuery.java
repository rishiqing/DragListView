package com.woxthebox.draglistview.sample;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Typeface;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.Drawable;
import android.text.Html.ImageGetter;
import android.text.Html.TagHandler;
import android.text.InputFilter;
import android.text.TextWatcher;
import android.text.method.MovementMethod;
import android.util.Log;
import android.util.SparseArray;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnFocusChangeListener;
import android.view.View.OnLongClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.view.ViewGroup.LayoutParams;
import android.view.ViewParent;
import android.view.animation.Animation;
import android.widget.EditText;
import android.widget.FrameLayout;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RelativeLayout;
import android.widget.TableRow;
import android.widget.TextView;

import static android.text.Html.fromHtml;


/**
 * 使用流畅接口的方式进行调用和设置
 *
 * @author abu
 */

@SuppressLint("NewApi")
public class CodeQuery {
    private View root = null;
    private View view = null;
    private SparseArray<View> cache;
    private Context context;
//    private ViewAdaptive viewAdaptive;

    public CodeQuery(Context context) {
        this.context = context;
    }


    public CodeQuery setRoot(View root) {
        return setRoot(root, true);
    }


    public CodeQuery setRoot(View root, boolean isAdaptive) {
        this.root = root;
        this.view = root;
        cache = new SparseArray<View>();
//        if (isAdaptive) {
//            viewAdaptive = new ViewAdaptive(ac.getApp());
//            viewAdaptive.parseView(root);
//        }
        return this;
    }

    public View getRoot() {
        return root;
    }

    public View getView() {
        return view;
    }

    public CodeQuery id(int id) {
        this.view = cache.get(id);
        if (CodeCheck.isNotNull(view)) {
            // LogUtils.log(getClass(), "cache:id" + id);
            return this;
        }
        this.view = root.findViewById(id);

        if (CodeCheck.isNotNull(view)) {
            cache.put(id, view);
        }
        return this;
    }

    public CodeQuery findViewByTag(Object tag) {
        this.view = root.findViewWithTag(tag);
        return this;
    }

    public Object getTag() {
        return view.getTag();
    }

    public Object getTag(int tag) {
        return view.getTag(tag);
    }

    public CodeQuery click(OnClickListener click) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setOnClickListener(click);
        return this;
    }

    public CodeQuery longClick(OnLongClickListener l) {
        // TODO Auto-generated method stub
        view.setOnLongClickListener(l);
        return this;
    }

    public CodeQuery Selected(boolean selected) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setSelected(selected);

        return this;
    }

    public CodeQuery pressed(boolean pressed) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setPressed(pressed);

        return this;
    }

    public CodeQuery touch(OnTouchListener touch) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setOnTouchListener(touch);
        return this;
    }

    public ViewParent getParent() {
        // TODO Auto-generated method stub
        if (checkView()) {
            return null;
        }
        return view.getParent();
    }

    public CodeQuery margin(int m) {
        // TODO Auto-generated method stub
        margin(m, m, m, m);
        return this;
    }

    public CodeQuery margin(int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        ViewParent parent = view.getParent();
        if (parent == null) {
            Log.d("margin", "parent is null!");
            return this;
        }

        String clazz = parent.getClass().getSimpleName();
        if (parent instanceof LinearLayout) {
            // if ("LinearLayout".equals(clazz)) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view
                    .getLayoutParams();
            params.setMargins(l, t, r, b);

        } else if (parent instanceof RelativeLayout) {
            // } else if ("RelativeLayout".equals(clazz)) {
            RelativeLayout.LayoutParams params = (RelativeLayout.LayoutParams) view
                    .getLayoutParams();
            params.setMargins(l, t, r, b);
        } else if (parent instanceof TableRow) {
            TableRow.LayoutParams params = (TableRow.LayoutParams) view
                    .getLayoutParams();
            params.setMargins(l, t, r, b);
        } else if (parent instanceof FrameLayout) {
            FrameLayout.LayoutParams params = (FrameLayout.LayoutParams) view
                    .getLayoutParams();
            params.setMargins(l, t, r, b);
        }

        return this;
    }

////    public CodeQuery marginCode(int l, int t, int r, int b) {
////        // TODO Auto-generated method stub
////        if (checkView()) {
////            return this;
////        }
////        return margin(l * ScreenAdaptiveHelper.wp, t
////                * ScreenAdaptiveHelper.wp, r * ScreenAdaptiveHelper.wp, b
////                * ScreenAdaptiveHelper.wp);
////    }
////
////
////    public CodeQuery marginCode(int lr, int tb) {
////        // TODO Auto-generated method stub
////        if (checkView()) {
////            return this;
////        }
////        return margin(lr * ScreenAdaptiveHelper.wp, tb
////                * ScreenAdaptiveHelper.wp, lr * ScreenAdaptiveHelper.wp, tb
////                * ScreenAdaptiveHelper.wp);
////    }
//
//    public CodeQuery marginCode(int m) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        return marginCode(m, m, m, m);
//    }

    public CodeQuery padding(int l, int t, int r, int b) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setPadding(l, t, r, b);
        return this;
    }

    public CodeQuery padding(int lr, int tb) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setPadding(lr, tb, lr, tb);
        return this;
    }

    public CodeQuery startAnim(Animation anim) {
        // TODO Auto-generated method stub
        view.startAnimation(anim);
        return this;
    }

    public CodeQuery clearAnim() {
        // TODO Auto-generated method stub
        view.clearAnimation();
        return this;
    }

    public CodeQuery padding(int padding) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setPadding(padding, padding, padding, padding);
        return this;
    }

//    public CodeQuery paddingCode(int l, int t, int r, int b) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        view.setPadding(ScreenAdaptiveHelper.wp * l, ScreenAdaptiveHelper.wp
//                * t, ScreenAdaptiveHelper.wp * r, ScreenAdaptiveHelper.wp * b);
//        return this;
//    }
//
//    public CodeQuery paddingCode(int lr, int tb) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        view.setPadding(ScreenAdaptiveHelper.wp * lr, ScreenAdaptiveHelper.wp
//                * tb, ScreenAdaptiveHelper.wp * lr, ScreenAdaptiveHelper.wp
//                * tb);
//        return this;
//    }
//
//    public CodeQuery paddingCode(int padding) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        int pad = ScreenAdaptiveHelper.wp * padding;
//        view.setPadding(pad, pad, pad, pad);
//        return this;
//    }

    public CodeQuery params(LayoutParams params) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setLayoutParams(params);
        return this;
    }

    public CodeQuery width(int w) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.getLayoutParams().width = w;
        return this;
    }

//    public CodeQuery widthCode(float w) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        view.getLayoutParams().width = (int) (ScreenAdaptiveHelper.wp * w);
//        return this;
//    }

    public CodeQuery height(int h) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.getLayoutParams().height = h;
        return this;
    }

//    public CodeQuery rect(int r) {
//        // TODO Auto-generated method stub
//        return heightCode(r).widthCode(r);
//    }
//
//    public CodeQuery rectPx(int r) {
//        // TODO Auto-generated method stub
//        return height(r).width(r);
//    }

//    public CodeQuery heightCode(int h) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        view.getLayoutParams().height = ScreenAdaptiveHelper.wp * h;
//        return this;
//    }

    public CodeQuery bgResource(int resid) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

//        Bitmap bit = new CodeBitmap().readBitmapResource(
//                context.getResources(), resid,
//                view.getWidth(), view.getHeight());
//        if (Codeb.isNotNullBitmap(bit)) {
//            view.setBackgroundDrawable(new BitmapDrawable(
//                    context.getResources(), bit));
//        } else {
        view.setBackgroundResource(resid);
//        }

        return this;
    }

    //
//    public CodeQuery bgDrable(int imgId) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//
//        Bitmap bit = new CodeBitmap().readBitmapResource(
//                context.getActivity().getResources(), imgId,
//                view.getWidth(), view.getHeight());
//        BitmapDrawable bd = new BitmapDrawable(
//                context.getActivity().getResources(), bit);
//        view.setBackgroundDrawable(bd);
//        return this;
//    }


    public CodeQuery bgDrable(Bitmap bitmap) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        bgDrable(new BitmapDrawable(bitmap));
        return this;
    }

    public CodeQuery bgDrable(Drawable drawable) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }


        view.setBackgroundDrawable(drawable);
        return this;
    }

    public CodeQuery bgColor(int c) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setBackgroundColor(c);
        return this;
    }

    public CodeQuery bgColorRes(int c) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        view.setBackgroundColor(context.getResources().getColor(c));
        return this;
    }

    public CodeQuery textSize(float size) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).setTextSize(size);

        }

        return this;
    }


    public CodeQuery textTypeface(int typeface) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).setTypeface(Typeface.defaultFromStyle(typeface));
        }

        return this;
    }

    public CodeQuery textTypeface(Typeface typeface) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).setTypeface(typeface);
        }

        return this;
    }


    public CodeQuery addView(View v, int index) {
        if (checkView()) {
            return this;
        }
        if (this.view instanceof ViewGroup) {
            ((ViewGroup) this.view).addView(v, index);
        }
        return this;
    }

    public CodeQuery removeView(View v) {
        if (this.view instanceof ViewGroup) {
            ((ViewGroup) this.view).removeView(v);
        }
        return this;
    }

    public CodeQuery removeAllView() {
        if (this.view instanceof ViewGroup) {
            ((ViewGroup) this.view).removeAllViews();
        }
        return this;
    }

    public CodeQuery addView(View v) {
        if (checkView()) {
            return this;
        }
        if (this.view instanceof ViewGroup) {
            ((ViewGroup) this.view).addView(v);
        }
        return this;
    }

    public CodeQuery text(Object text) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        ((TextView) view).setText(text + "");

        return this;
    }


//    public CodeQuery text(Object text, int maxLines) {
//        if (checkView()) {
//            return this;
//        }
//        // TODO Auto-generated method stub
//        TextView tv = (TextView) this.view;
//        int width = tv.getLayoutParams().width;
//        TextPaint paint = tv.getPaint();
//        text = StringUtils.ellipsizingText(text + "", maxLines, paint, width);
//        return text(text);
//    }
//
//    public CodeQuery textByteLength(Object text, int blength) {
//        if (checkView()) {
//            return this;
//        }
//
//        int l = StringUtils.length(text + "");
//        String str = text + "";
//        if (blength < l) {
//            str = StringUtils.subStringByByte(str, blength);
//        }
//
//        // TODO Auto-generated method stub
//        if (this.view instanceof TextView) {
//            ((TextView) view).setText(str);
//        }
//
//        return this;
//    }

    public CodeQuery append(Object text) {
        if (checkView()) {
            return this;


        }
        // TODO Auto-generated method stub

        if (this.view instanceof TextView) {
            TextView tv = (TextView) view;
            tv.append(text + "");
        }

        return this;
    }

    public CodeQuery textLength(int length) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof TextView) {
            ((TextView) view)
                    .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                            length)});
        }

        return this;
    }

    public CodeQuery editLength(int length) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof EditText) {
            ((EditText) view)
                    .setFilters(new InputFilter[]{new InputFilter.LengthFilter(
                            length)});
        }

        return this;
    }

    public CodeQuery maxLines(int num) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof TextView) {
            ((TextView) view).setMaxLines(num);
        }

        return this;
    }

    public CodeQuery focusChangeListener(
            OnFocusChangeListener focusChangeListener) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof EditText) {
            view.setOnFocusChangeListener(focusChangeListener);
        }

        return this;
    }

    public CodeQuery hint(String hint) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof EditText) {
            ((EditText) view).setHint(hint);
        }

        return this;
    }

    public CodeQuery textColor(int color) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof TextView) {
            ((TextView) view).setTextColor(color);
        }
        return this;
    }


    public CodeQuery textColorRes(int color) {
        if (checkView()) {
            return this;
        }
        // TODO Auto-generated method stub

        if (this.view instanceof TextView) {
            ((TextView) view).setTextColor(context.getResources().getColor(color));
        }
        return this;
    }


    public String getText() {
        // TODO Auto-generated method stub
        if (checkView()) {
            return "";
        }
        String result = null;
        if (this.view instanceof TextView) {
            result = ((TextView) view).getText() + "";
        }
        return result;
    }

    public CodeQuery textRes(int id) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).setText(context.getString(id));
        }

        return this;
    }


    public CodeQuery setLayoutGravity(int gravity) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            LinearLayout.LayoutParams params = (LinearLayout.LayoutParams) view.getLayoutParams();
            if (CodeCheck.isNotNull(params)) {
                params.gravity = gravity;
                view.setLayoutParams(params);
            }
        }

        return this;
    }


    public CodeQuery gravity(int gravity) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).setGravity(gravity);
        }

        return this;
    }


    /**
     * Paint.UNDERLINE_TEXT_FLAG
     *
     * @param paint
     * @return
     */
    public CodeQuery setPaintFlags(int paint) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }

        if (this.view instanceof TextView) {
            ((TextView) view).getPaint().setFlags(paint);
        }

        return this;
    }

//    public CodeQuery html(String html) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        if (this.view instanceof TextView) {
//            ((TextView) view).setText(Html.fromHtml(html));
//        }
//
//        return this;
//    }

//    public CodeQuery htmlWithImage(CoderQueryActivity ac, String source) {
//        // TODO Auto-generated method stub
//        if (this.view instanceof TextView) {
//            int height = ((TextView) view).getLineHeight();
//            html(source, FaceImageGetter.news().setParams(ac, height), null);
//        }
//        return this;
//
//    }


//    public CodeQuery html(String source) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        if (this.view instanceof TextView) {
//            Spanned spanned = Html.fromHtml(source, new HtmlRemoteImageGetter(view),
//                    null);
//            if (spanned instanceof SpannableStringBuilder) {
//                ImageSpan[] imageSpans = spanned.getSpans(0, spanned.length(), ImageSpan.class);
//                for (ImageSpan imageSpan : imageSpans) {
//                    int start = spanned.getSpanStart(imageSpan);
//                    int end = spanned.getSpanEnd(imageSpan);
//                    Drawable d = imageSpan.getDrawable();
//                    StickerSpan newImageSpan = new StickerSpan(view, d, ImageSpan.ALIGN_BASELINE);
//                    ((SpannableStringBuilder) spanned).setSpan(newImageSpan, start, end, Spanned.SPAN_EXCLUSIVE_INCLUSIVE);
//                    ((SpannableStringBuilder) spanned).removeSpan(imageSpan);
//                }
//            }
//            ((TextView) view).setText(spanned);
//        }
//
//        return this;
//    }

    public CodeQuery html(String source, ImageGetter imageGetter,
                          TagHandler tagHandler) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof TextView) {
            ((TextView) view).setText(fromHtml(source, imageGetter,
                    tagHandler));
        }

        return this;
    }

//    public CodeQuery htmlLinkAble(String html, HtmlUrlSpan.OnHtmlUrlListener urlListener) {
//        // TODO Auto-generated method stub
//        htmlLinkAble(html, urlListener, Color.BLUE);
//        return this;
//    }
//
//    public CodeQuery htmlLinkAble(String html, HtmlUrlSpan.OnHtmlUrlListener urlListener,
//                                  int color) {
//        // TODO Auto-generated method stub
//        if (checkView()) {
//            return this;
//        }
//        if (this.view instanceof TextView) {
//            TextView tv = (TextView) view;
//            tv.setText(Html.fromHtml(html));
//            tv.setMovementMethod(LinkMovementMethod.getInstance());
//            CharSequence text = tv.getText();
//            if (text instanceof Spannable) {
//                int end = text.length();
//                Spannable sp = (Spannable) tv.getText();
//                URLSpan[] urls = sp.getSpans(0, end, URLSpan.class);
//                SpannableStringBuilder style = new SpannableStringBuilder(text);
//                style.clearSpans();// should clear old spans
//                // 循环把链接发过去
//                for (URLSpan url : urls) {
//                    HtmlUrlSpan myURLSpan = new HtmlUrlSpan(url.getURL(),
//                            urlListener);
//                    style.setSpan(myURLSpan, sp.getSpanStart(url),
//                            sp.getSpanEnd(url),
//                            Spannable.SPAN_EXCLUSIVE_INCLUSIVE);
//                    style.setSpan(new ForegroundColorSpan(color),
//                            sp.getSpanStart(url), sp.getSpanEnd(url),
//                            Spannable.SPAN_EXCLUSIVE_EXCLUSIVE);
//                }
//                tv.setText(style);
//            }
//        }
//
//        return this;
//    }

    public CodeQuery inputType(int type) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof EditText) {
            ((EditText) view).setInputType(type);
        }

        return this;
    }

    public CodeQuery TextWatcher(TextWatcher watcher) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof EditText) {
            ((EditText) view).addTextChangedListener(watcher);
        }

        return this;
    }

    /**
     * 下面是设置显示最新的内容：
     */
    public CodeQuery Selection(int start, int stop) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof EditText) {
            ((EditText) view).setSelection(start, stop);
        }

        return this;
    }


    /**
     * 光标
     */
    public CodeQuery setSelection(int index) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof EditText) {
            ((EditText) view).setSelection(index);
        }

        return this;
    }


    /**
     * 要在java代码中设置滚动的方法
     */
    public CodeQuery MovementMethod(MovementMethod movement) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof TextView) {
            ((TextView) view).setMovementMethod(movement);
        }

        return this;
    }

    public CodeQuery imgResource(int resId) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
//
//        Bitmap bitmap = new CodeBitmap().readBitmapResource(
//                context.getRes(), resId,
//                view.getLayoutParams().width, view.getLayoutParams().height);
//        ((ImageView) view).setImageBitmap(bitmap);
        ((ImageView) view).setImageResource(resId);
        return this;
    }

    public CodeQuery image(Bitmap bitmap) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof ImageView) {
            ((ImageView) view).setImageBitmap(bitmap);
        }

        return this;
    }

    public CodeQuery imageDrawable(Drawable d) {
        // TODO Auto-generated method stub
        if (checkView()) {
            return this;
        }
        if (this.view instanceof ImageView) {
            ((ImageView) view).setImageDrawable(d);
        }

        return this;
    }

//
//    public CodeQuery textAdaptive() {
//        TextAdaptiveHelper.changeViewSize(view);
//        return this;
//    }

    public CodeQuery clickAble(boolean clickAble) {
        if (checkView()) {
            return this;
        }
        view.setClickable(clickAble);
        return this;
    }

    public CodeQuery focusAble(boolean focusable) {
        if (checkView()) {
            return this;
        }
        view.setFocusable(focusable);
        return this;
    }


    public CodeQuery focusableInTouchMode(boolean focusable) {
        if (checkView()) {
            return this;
        }
        view.setFocusableInTouchMode(focusable);
        return this;
    }

    public CodeQuery vision(int v) {
        if (checkView()) {
            return this;
        }

        if (view.getVisibility() == v) {
            return this;
        }

        view.setVisibility(v);
        return this;
    }

    public int getVision() {
        return view.getVisibility();
    }

    public CodeQuery setId(int id) {
        if (checkView()) {
            return this;
        }
        view.setId(id);
        return this;
    }

    public CodeQuery getViewCodeQuery() {
        return new CodeQuery(context).setRoot(view, false);
    }

    public CodeQuery setTag(Object tag) {
        this.view.setTag(tag);
        return this;
    }

    public CodeQuery setTag(int key, Object tag) {
        this.view.setTag(key, tag);
        return this;
    }


    public CodeQuery childQuery(int pos) {
        if (view instanceof ViewGroup) {
            return new CodeQuery(context).setRoot(((ViewGroup) view).getChildAt(pos), false);
        }

        return this;
    }


    private boolean checkView() {
        return !CodeCheck.isNotNull(view);
    }

//    // 关闭硬件加速
//    public CodeQuery cancleHardware() {
//        if (SystemUtils.getSystemVersion() >= SystemUtils.V4_0) {
//            view.setLayerType(View.LAYER_TYPE_SOFTWARE, null);
//        }
//        return this;
//    }

    public int getLineHeight() {
        if (view instanceof TextView) {
            return ((TextView) view).getLineHeight();


        }
        return 0;
    }


//    public CodeQuery loadBitmapFromFile(File file) {
//        Bitmap bitmap = CodeBitmap.readBitmapFile(file, view.getLayoutParams().width, view.getLayoutParams().height);
//
//
//        LogUtils.log(getClass(), bitmap + "");
//
//
//        return image(bitmap);
//    }


//    public CodeQuery loadBitmap(String url, OnResultHandler<BitmapResult> handler) {
//        imgResource(R.mipmap.default_icon_one);
//        this.view.setTag(R.id.tag_url, url);
//        if (view instanceof ImageView) {
//            CodeBitmap.loadImage(url, (ImageView) view, handler);
//        }
//        return this;
//    }
//
//
//    public CodeQuery loadBitmapOutDefault(String url, OnResultHandler<BitmapResult> handler) {
//        this.view.setTag(R.id.tag_url, url);
//        if (view instanceof ImageView) {
//            CodeBitmap.loadImage(url, (ImageView) view, handler);
//        }
//        return this;
//    }
//
//
//    public CodeQuery loadBitmapNullDefault(String url) {
//        this.view.setTag(R.id.tag_url, url);
//        if (view instanceof ImageView) {
//            CodeBitmap.loadImage(url, (ImageView) view, new BitmapResultHandler());
//        }
//        return this;
//    }
//
//
//    public CodeQuery loadOriginalImage(String url) {
//        imgResource(R.mipmap.default_icon_one);
//        this.view.setTag(R.id.tag_url, url);
//        if (view instanceof ImageView) {
//            CodeBitmap.loadOriginalImage(url, (ImageView) view, new BitmapResultHandler());
//        }
//        return this;
//    }
//
//    public CodeQuery loadBitmap(Object url) {
//        loadBitmap(url + "", new BitmapResultHandler());
//        return this;
//    }


//    public CodeQuery loadImg(String url) {
//        Glide.with(context)
//                .load(url)
//                .centerCrop()
//                .crossFade()
//                .into((ImageView) view);
//        return this;
//    }
//
//
//    public CodeQuery loadImg(String url, BitmapTransformation transformation) {
//        Glide.with(context)
//                .load(url)
//                .transform(transformation)
//                .into((ImageView) view);
//        return this;
//    }

    public CodeQuery setDrable(int left, int top, int right, int bottom) {
        return setDrable(getDrable(left), getDrable(top), getDrable(right), getDrable(bottom));
    }


    public CodeQuery setLeftDrable(int left) {
        return setDrable(getDrable(left), getDrable(0), getDrable(0), getDrable(0));
    }

    public CodeQuery setLeftDrable(Drawable left) {
        return setDrable(left, getDrable(0), getDrable(0), getDrable(0));
    }


    public CodeQuery setDrable(Drawable left, Drawable top, Drawable right, Drawable bottom) {
        if (view instanceof TextView) {
            ((TextView) view).setCompoundDrawables(left, top, right, bottom); //设置左图标
        }

        return this;

    }

    public CodeQuery setDrawablePadding(int padding) {
        if (view instanceof TextView) {
            ((TextView) view).setCompoundDrawablePadding(padding);
        }

        return this;

    }

    private Drawable getDrable(Drawable img_off) {
        if (!CodeCheck.isNotNull(img_off)) {
            return null;
        }
        int rectH = getLineHeight();
        int height = img_off.getIntrinsicHeight();
        int width = img_off.getIntrinsicWidth();
        int rectW = rectH * width / height;
        img_off.setBounds(0, 0, rectW - 2, rectH - 2);
        return img_off;
    }

    private Drawable getDrable(int id) {
        if (id == 0) {
            return null;
        }
        Drawable drable = context.getResources().getDrawable(id);
        return getDrable(drable);
    }

    public static CodeQuery infalter(Context context, int layoutId) {
        CodeQuery result = new CodeQuery(context);
        result.setRoot(LayoutInflater.from(context).inflate(layoutId, null));
        return result;
    }

    public CodeQuery enable(boolean b) {
        view.setEnabled(b);
        return this;
    }

    public CodeQuery scaleType(ImageView.ScaleType scaleType) {
        ((ImageView) view).setScaleType(scaleType);
        return this;
    }

    public void clearText() {
        if (view instanceof EditText) {
            ((EditText) view).getText().clear();
        }
    }

    public void editActionListener(TextView.OnEditorActionListener editorAction) {
        if (view instanceof EditText) {
            ((EditText) view).setOnEditorActionListener(editorAction);
        }
    }

    public CodeQuery reset() {
        this.view = root;
        return this;
    }

    public CodeQuery setFlags(int flag) {
        if (view instanceof TextView) {
            ((TextView) view).getPaint().setFlags(flag);
        }

        return this;
    }

    public CodeQuery setAntiAlias(boolean b) {
        if (view instanceof TextView) {
            ((TextView) view).getPaint().setAntiAlias(b);
        }

        return this;
    }
}
