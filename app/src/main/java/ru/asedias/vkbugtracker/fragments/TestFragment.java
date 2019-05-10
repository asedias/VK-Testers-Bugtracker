package ru.asedias.vkbugtracker.fragments;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.Locale;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.webmethods.GetReportInfo;
import ru.asedias.vkbugtracker.api.webmethods.models.Report;

/**
 * Created by rorom on 20.10.2018.
 */

public class TestFragment extends LoaderFragment {

    private TextView text;
    private int rid = 52963;

    public static TestFragment newInstance(int rid) {
        TestFragment fr = new TestFragment();
        Bundle args = new Bundle();
        args.putInt("rid", rid);
        fr.setArguments(args);
        return fr;
    }

    @Override
    public WebRequest getRequest() {
        if(getArguments() != null) {
            this.rid = getArguments().getInt("rid", 52963);
        }
        return new GetReportInfo(rid, new Callback<Report>() {
            @Override
            public void onResponse(Call<Report> call, Response<Report> response) {
                try {
                    Report ttt = response.body();
                    String format = "%s (%s)\n%s\n%s\n%s %s\nComments: %d";
                    String out = String.format(Locale.getDefault(), format, ttt.author.author_name, ttt.author.author_photo, ttt.title, ttt.description, ttt.details.get(0).title, ttt.details.get(0).description, ttt.attachments.size());
                    //Log.e("RESULT", out);
                    //Toast.makeText(getActivity(), out, Toast.LENGTH_LONG).show();
                    text.setText(out);
                } catch(Exception e) {
                    Log.e("RESPONSE", "Catch Exception", e.fillInStackTrace());
                }
                showContent();
            }

            @Override
            public void onFailure(Call<Report> call, Throwable t) {
                showError(t.getLocalizedMessage());
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
