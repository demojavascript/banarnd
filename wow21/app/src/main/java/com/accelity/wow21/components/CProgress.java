package com.accelity.wow21.components;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.view.Gravity;
import com.accelity.wow21.R;
/**
 * Created by Rahul on 01-09-2016.
 */
public class CProgress  extends ProgressDialog {

    public static ProgressDialog ctor(Context context) {
        CProgress dialog = new CProgress(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
    public static ProgressDialog ctor(Context context, int theme) {
        CProgress dialog = new CProgress(context);
        dialog.setIndeterminate(true);
        dialog.setCancelable(false);
        dialog.getWindow().setGravity(Gravity.CENTER);
        return dialog;
    }
    public CProgress(Context context) {
        super(context, R.style.CustomProgressDialogTheme);
    }

    public CProgress(Context context, int theme) {
        super(context, theme);
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.custom_progressdialog);
    }
    @Override
    public void show() {
        super.show();
    }

    @Override
    public void dismiss() {
        super.dismiss();
    }
}
