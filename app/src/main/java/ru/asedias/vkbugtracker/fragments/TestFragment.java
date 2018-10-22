package ru.asedias.vkbugtracker.fragments;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import android.widget.Toast;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.LoginActivity;
import ru.asedias.vkbugtracker.MainActivity;
import ru.asedias.vkbugtracker.api.webmethods.GetReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;

/**
 * Created by rorom on 20.10.2018.
 */

public class TestFragment extends LoaderFragment {

    private TextView text;

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);
        this.request = new GetReportList("0", "0", false, new Callback<ReportList>() {
            @Override
            public void onResponse(Call<ReportList> call, Response<ReportList> response) {
                try {
                    ReportList body = response.body();
                    ReportList.ReportItem item = body.reports.get(3);
                    String info = String.format("%s (Загружено %d)\n%s\n%s и ещё %d\nСтатус: %s", body.reports_found, body.reports.size(), item.title, item.tags.get(0).label, item.tags.size(), item.status);
                    Toast.makeText(getActivity(), info, Toast.LENGTH_LONG).show();
                    Log.e("RESPONSE", info);
                    text.setText(info);
                    showContent();
                } catch (Exception e) {
                    /*if(response.raw().request().url().toString().contains("login?")) {
                        startActivity(new Intent(MainActivity.this, LoginActivity.class));
                        finish();
                        return;
                    }*/
                    Log.e("RESPONSE", "Catch Exception", e.fillInStackTrace());
                }
            }

            @Override
            public void onFailure(Call<ReportList> call, Throwable t) {
                Log.e("RESPONSE", "ERROR", t);
            }
        });
    }

    @Override
    protected View OnCreateContentView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        this.text = new TextView(getActivity());
        return this.text;
    }
}
