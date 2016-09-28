package com.example.windy.smartremotepc.activity;

import android.app.Activity;
import android.content.Intent;
import android.content.res.Resources;
import android.graphics.drawable.Drawable;
import android.inputmethodservice.Keyboard;
import android.inputmethodservice.KeyboardView;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;

import com.example.windy.smartremotepc.mode.CodeIntent;

import com.example.windy.smartremotepc.R;
import com.example.windy.smartremotepc.item.MyTextView;

/**
 * Created by Windy on 24/06/2016.
 */
public class KeyboardFunctionActivity extends Activity {
    private KeyboardView kbv = null;
    private Keyboard kb = null;
    private Keyboard kbFn = null;
    private Keyboard kbSp = null;
    private Button btOk = null;
    private int keycodeFirst = 0;
    private LinearLayout viewFunction = null;
    View rootView = null;
    long time;
    float x, y;
    int[] functionCode = new int[5];
    int countFunction = 0;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        rootView = getLayoutInflater().inflate(R.layout.keybroad_function, null);
        setContentView(rootView);
        kbv = (KeyboardView) findViewById(R.id.keyboardKF);
        kb = new Keyboard(this, R.xml.qwerty);
        kbFn = new Keyboard(this, R.xml.fn);
        kbv.setKeyboard(kb);
        kbv.invalidateAllKeys();
        btOk = (Button) findViewById(R.id.btOkKF);
        viewFunction = (LinearLayout) findViewById(R.id.viewFunctionKF);
        setTouchControl();
    }

    //Chuyển Keyboard0p-;
    private void switchKeyboard() {
        if (kbv.getKeyboard() == kb)
            kbv.setKeyboard(kbFn);
        else kbv.setKeyboard(kb);
        kbv.invalidateAllKeys();
    }

    private void result(byte[] data) {
        Intent intent = getIntent();
        intent.putExtra("code", data);
        setResult(CodeIntent.RESULT_KEYBOARD, intent);
    }


    public void setTouchControl() {
        kbv.setOnKeyboardActionListener(new KeyboardView.OnKeyboardActionListener() {
            @Override
            public void onPress(int primaryCode) {
                keycodeFirst = primaryCode;

                Log.i("Function", "test ");
            }

            @Override
            public void onRelease(int primaryCode) {
                if (countFunction == 5 || (primaryCode != keycodeFirst)) return;
                else countFunction++;
                Drawable drawable = null;
                String name = null;
                View view = null;
                for (Keyboard.Key key : kbv.getKeyboard().getKeys()) {
                    if (key.codes[0] == primaryCode) {
                        drawable = key.icon;
                        name = key.label + "";
                    }
                }
                int size = (int) (Resources.getSystem().getDisplayMetrics().widthPixels * 0.15f);
                if (drawable != null) {
                    view = new ImageView(KeyboardFunctionActivity.this) {
                        int code;

                        @Override
                        public int getId() {
                            return code;
                        }

                        @Override
                        public void setId(int c) {
                            code = c;
                        }
                    };
                    ((ImageView) view).setImageDrawable(drawable);
                } else {
                    view = new MyTextView(KeyboardFunctionActivity.this) {
                        int code;

                        @Override
                        public int getId() {
                            return code;
                        }

                        @Override
                        public void setId(int c) {
                            code = c;
                        }
                    };
                    ((MyTextView) view).setText(name);
                    ((MyTextView) view).setGravity(Gravity.CENTER);
                    ((MyTextView) view).setTextSize(size);
                    ((MyTextView) view).setTextColor(0xFF606060);
                }
                view.setBackgroundResource(android.R.drawable.checkbox_off_background);
                viewFunction.addView(view, new LinearLayout.LayoutParams(size, size));
                size = (int) (size * 0.15f);
                view.setPadding(size, size, size, size);
                view.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        viewFunction.removeView(v);
                        countFunction--;
                        String code = "";
                        for (int i = 0; i < viewFunction.getChildCount(); i++) {
                            code = code + viewFunction.getChildAt(i).getId() + " - ";
                        }
                    }
                });
                //lưu code của function
                view.setId(primaryCode);
                String code = "";
                for (int i = 0; i < viewFunction.getChildCount(); i++) {
                    code = code + viewFunction.getChildAt(i).getId() + " - ";
                }
                Log.i("Function", code);
            }

            @Override
            public void onKey(int primaryCode, int[] keyCodes) {
            }

            @Override
            public void onText(CharSequence text) {
            }

            @Override
            public void swipeLeft() {
                switchKeyboard();
            }

            @Override
            public void swipeRight() {
                switchKeyboard();
            }

            @Override
            public void swipeDown() {
                finish();
            }

            @Override
            public void swipeUp() {
            }
        });

    }
}
