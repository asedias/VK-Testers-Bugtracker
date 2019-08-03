package ru.asedias.vkbugtracker.fragments;

import java.util.List;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import ru.asedias.vkbugtracker.BTApp;
import ru.asedias.vkbugtracker.R;
import ru.asedias.vkbugtracker.api.SimpleCallback;
import ru.asedias.vkbugtracker.api.WebRequest;
import ru.asedias.vkbugtracker.api.apimethods.GetUserInfo;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;
import ru.asedias.vkbugtracker.api.webmethods.GetMembers;
import ru.asedias.vkbugtracker.api.webmethods.models.MembersList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.ui.adapters.MembersAdapter;
import ru.asedias.vkbugtracker.ui.adapters.ReportsAdapter;

/**
 * Created by Roma on 10.05.2019.
 */

public class MembersFragment extends RecyclerFragment<MembersAdapter> {

    public MembersFragment() {
        this.mAdapter = new MembersAdapter();
        this.canLoadMore = true;
        this.title = BTApp.String(R.string.prefs_members);
        this.setTitleNeeded = false;
        this.topOffset = BTApp.dp(16);
    }

    @Override
    public WebRequest getRequest() {
        return new GetMembers(this, data -> getUsers(data));
    }

    private MembersList getUsers(MembersList data) {
        StringBuilder ids = new StringBuilder();
        for(int i = 0; i < data.members.size(); i++) {
            ids.append(data.members.get(i).uid).append(", ");
        }
        new GetUserInfo(ids.toString(), new Callback<UserInfo>() {
            @Override
            public void onResponse(Call<UserInfo> call, Response<UserInfo> response) {
                List<UserInfo.User> users = response.body().getResponse();
                for(int i = 0; i < getAdapter().data.members.size(); i++) {
                    for (int i2 = 0; i2 < users.size(); i2++) {
                        UserInfo.User user = users.get(i2);
                        if(getAdapter().data.members.get(i).uid == user.getId()) {
                            String url = user.getPhoto50();
                            if(user.getPhoto200() != null) {
                                url = user.getPhoto200();
                            } else if(user.getPhoto100() != null) {
                                url = user.getPhoto100();
                            }
                            getAdapter().data.members.get(i).photo_url = url;
                            break;
                        }
                    }
                }
                getAdapter().notifyDataSetChanged();
            }
            @Override public void onFailure(Call<UserInfo> call, Throwable t) {
                showError(t.getLocalizedMessage());
            }
        }).execute();
        return data;
    }
}
