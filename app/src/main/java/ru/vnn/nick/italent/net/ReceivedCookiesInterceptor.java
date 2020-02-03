package ru.vnn.nick.italent.net;

import java.io.IOException;
import java.util.HashSet;

import okhttp3.Interceptor;
import okhttp3.Response;
import ru.vnn.nick.italent.App;
import ru.vnn.nick.italent.utils.Methods;

public class ReceivedCookiesInterceptor implements Interceptor {
    @Override
    public Response intercept(Chain chain) throws IOException {
        Response originalResponse = chain.proceed(chain.request());
        if (!originalResponse.headers("Set-Cookie").isEmpty()) {
            HashSet<String> cookies = new HashSet<>();
            cookies.addAll(originalResponse.headers("Set-Cookie"));

            Methods.setCookies(App.getAppContext(), cookies);
        }
        return originalResponse;
    }
}
