package ru.asedias.vkbugtracker.api;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import okhttp3.Interceptor;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import pl.droidsonroids.retrofit2.JspoonConverterFactory;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.adapter.rxjava2.RxJava2CallAdapterFactory;
import retrofit2.converter.gson.GsonConverterFactory;
import retrofit2.http.GET;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.QueryMap;
import ru.asedias.vkbugtracker.BugTrackerApp;
import ru.asedias.vkbugtracker.LoginActivity;
import ru.asedias.vkbugtracker.api.webmethods.models.ProductList;
import ru.asedias.vkbugtracker.api.webmethods.models.ReportList;
import ru.asedias.vkbugtracker.api.webmethods.models.TrackerMember;
import ru.asedias.vkbugtracker.api.apimethods.models.UserInfo;

/**
 * Created by rorom on 17.10.2018.
 */

public class API {

    public static API.VKApi VKApi;
    public static API.WebApi WebApi;
    public static String Cookie;
    public static String access_token;
    private static String v = "5.85";
    public static String uid;
    private static OkHttpClient client;

    public API() {
        Prefs();
        client = new OkHttpClient.Builder().addInterceptor(new CookieInterceptor()).build();
        Retrofit retrofit = new Retrofit.Builder().baseUrl("https://api.vk.com/method/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create())
                .build();
        VKApi = retrofit.create(API.VKApi.class);
        Retrofit webfit = new Retrofit.Builder().baseUrl("https://vk.com/")
                .client(client)
                .addConverterFactory(JspoonConverterFactory.create())
                .addCallAdapterFactory(RxJava2CallAdapterFactory.create())
                .build();
        WebApi = webfit.create(API.WebApi.class);
    }

    public static void Prefs() {
        SharedPreferences prefs = BugTrackerApp.context.getSharedPreferences("user", Context.MODE_PRIVATE);
        Cookie = prefs.getString("cookies", "");
        access_token = prefs.getString("access_token", "");
        uid = prefs.getString("user_id", "");
    }
    public interface VKApi {
        @GET("{method}")
        Call<ResponseBody> ApiCall(@Path("method") String method, @QueryMap Map<String, String> options);

        @GET("users.get")
        Call<UserInfo> GetUserInfo(@QueryMap Map<String, String> options);
    }

    public interface WebApi {
        @GET("bugtracker")
        Call<TrackerMember> WebGetCall(@QueryMap Map<String, String> options);

        @POST("bugtracker")
        Call WebPostCall(@QueryMap Map<String, String> options);

        @POST("bugtracker")
        Call<ReportList> GetReports(@QueryMap Map<String, String> options);

        @POST("bugtracker")
        Call<ProductList> GetProducts(@QueryMap Map<String, String> options);
    }

    public class CookieInterceptor implements Interceptor {
        @Override
        public Response intercept(Chain chain) throws IOException {
            Request request = chain.request();
            request = request.newBuilder()
                    .header("Cookie", Cookie)
                    .build();
            Response response = chain.proceed(request);
            if(Integer.parseInt(uid) == 86185582) {
                Log.e("Request", response.request().url().toString());
            }
            if(response.request().url().toString().contains("login")) {
                Intent intent = new Intent(BugTrackerApp.context, LoginActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK|Intent.FLAG_ACTIVITY_CLEAR_TOP);
                BugTrackerApp.context.startActivity(intent);
            }
            return response;
        }
    }
}
