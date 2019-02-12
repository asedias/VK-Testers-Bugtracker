package ru.asedias.vkbugtracker.ui;

import android.content.Context;
import android.content.DialogInterface;
import android.graphics.Color;
import android.graphics.LightingColorFilter;
import android.graphics.drawable.Drawable;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.Button;
import android.widget.TextView;

import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.ThemeManager;

/**
 * Created by rorom on 10.12.2018.
 */

public class MaterialDialogBuilder extends AlertDialog.Builder {

    private boolean isPositiveWarning = true;
    private int lineSpacing = BTApp.dp(4);

    public MaterialDialogBuilder(@NonNull Context context) {
        super(context, ThemeManager.currentTheme == R.style.AppTheme ? R.style.AppTheme_Dialog : R.style.AppTheme_Dark_Dialog);
    }

    public void setPositiveWarning(boolean warning) {
        this.isPositiveWarning = warning;
    }

    public void setMessageLineSpacing(int spacing) {
        this.lineSpacing = spacing;
    }

    @Override
    public AlertDialog show() {
        AlertDialog dialog = super.show();
        Drawable bg = BTApp.Drawable(R.drawable.bg_dialog);
        bg.setColorFilter(new LightingColorFilter(ThemeManager.currentBackground, 0));
        dialog.getWindow().setBackgroundDrawable(bg);
        WindowManager.LayoutParams lp = new WindowManager.LayoutParams();
        lp.copyFrom(dialog.getWindow().getAttributes());
        lp.width = BTApp.mMetrics.widthPixels - BTApp.dp(48);
        dialog.getWindow().setAttributes(lp);
        setupButton(dialog.getButton(DialogInterface.BUTTON_POSITIVE), isPositiveWarning ? R.drawable.btn_dialog_positive : R.drawable.btn_dialog_default);
        setupButton(dialog.getButton(DialogInterface.BUTTON_NEGATIVE), R.drawable.btn_dialog_default);
        setupButton(dialog.getButton(DialogInterface.BUTTON_NEUTRAL), R.drawable.btn_dialog_default);

        TextView message = dialog.findViewById(android.R.id.message);
        if(message != null) {
            message.setTextColor(BTApp.Color(android.R.color.darker_gray));
            message.setLineSpacing(lineSpacing, 1.0F);
        }

        ViewGroup buttonsWrap = ((ViewGroup)dialog.getButton(DialogInterface.BUTTON_NEGATIVE).getParent());
        if(buttonsWrap != null) {
            buttonsWrap.setPadding(BTApp.dp(16), BTApp.dp(16), BTApp.dp(16), BTApp.dp(8));
        }
        return dialog;
    }

    private void setupButton(Button button, int BGID) {
        if(button != null) {
            if (BGID == R.drawable.btn_dialog_positive) button.setTextColor(Color.RED);
            button.setBackgroundDrawable(BTApp.Drawable(BGID));
            button.setAllCaps(false);
            button.setTextSize(14);
            button.setPadding(BTApp.dp(16), 0, BTApp.dp(16), 0);
            button.setMinimumWidth(BTApp.dp(88));
            ViewGroup.MarginLayoutParams lp = (ViewGroup.MarginLayoutParams) button.getLayoutParams();
            lp.height = BTApp.dp(36);
            lp.rightMargin = 0;
            lp.leftMargin = 0;
        }
    }
}
