package space.dotcat.assistant.api;


import android.os.SystemClock;

import java.io.IOException;

import okhttp3.Interceptor;
import okhttp3.Request;
import okhttp3.Response;

public class MockingInterceptor implements Interceptor {

    private RequestMatcher mRequestMatcher;

    private MockingInterceptor() {
        mRequestMatcher = new RequestMatcher();
    }

    public static MockingInterceptor create() {
        return new MockingInterceptor();
    }

    @Override
    public Response intercept(Chain chain) throws IOException {
        Request request = chain.request();

        String url = request.url().encodedPath();

        if(mRequestMatcher.shouldIntercept(url)) {
            SystemClock.sleep(500); // creating delay for emulation real client-server work
            return mRequestMatcher.proceed(request, url);
        }

        return chain.proceed(request);
    }
}
