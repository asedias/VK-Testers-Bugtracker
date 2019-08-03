package ru.asedias.vkbugtracker.api;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.support.annotation.NonNull;
import android.support.v7.app.AlertDialog;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.data.UserData;
import ru.asedias.vkbugtracker.ui.MaterialDialogBuilder;

/**
 * Created by Roma on 11.05.2019.
 */

public class DialogRequest extends MaterialDialogBuilder {

    private WebRequest request;
    private SimpleCallback callback;
    private Activity activity;

    public DialogRequest(@NonNull Activity context) {
        super(context);
        this.activity = context;
    }

    public DialogRequest setRequest(WebRequest request) {
        this.request = request;
        return this;
    }

    public DialogRequest setCallback(SimpleCallback callback) {
        this.callback = callback;
        return this;
    }

    @Override
    public AlertDialog show() {
        setOnCancelListener(dialog -> {
            if(request != null) request.cancel();
        });
        View dialogView = this.activity.getLayoutInflater().inflate(R.layout.dialog_cookie, null);
        dialogView.findViewById(R.id.dialog_webview).setVisibility(View.INVISIBLE);
        dialogView.findViewById(R.id.dialog_title).getLayoutParams().height = 0;
        ((TextView)dialogView.findViewById(R.id.dialog_description)).setText(R.string.loading);
        setView(dialogView);
        AlertDialog dialog =  super.show();
        this.request.callback = new Callback() {
            @Override
            public void onResponse(Call call, Response response) {
                dialog.dismiss();
                callback.onSuccess(response.body());
            }

            @Override
            public void onFailure(Call call, Throwable t) {
                Toast.makeText(getContext(), R.string.network_error_description, Toast.LENGTH_SHORT).show();
            }
        };
        this.request.execute();
        return dialog;
    }
}
