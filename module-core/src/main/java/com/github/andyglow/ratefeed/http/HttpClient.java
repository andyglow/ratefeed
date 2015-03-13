package com.github.andyglow.ratefeed.http;

import com.ning.http.client.AsyncCompletionHandler;
import com.ning.http.client.AsyncHttpClient;
import com.ning.http.client.RequestBuilder;
import com.ning.http.client.Response;

import java.util.concurrent.CompletableFuture;
import java.util.function.Function;

public class HttpClient {

    private static AsyncHttpClient http = new AsyncHttpClient();

    public static CompletableFuture<Response> request(RequestBuilder request) {
        return getAndTransform(request, Function.identity());
    }

    public static <U> CompletableFuture<U> getAndTransform(RequestBuilder request, Function<Response, U> transformer) {
        Handler<U> h = new Handler<U>(transformer);
        http.prepareRequest(request.build()).execute(h);
        return h.future;
    }

    public static void stop() {
        http.close();
    }

    private static class Handler<U> extends AsyncCompletionHandler<Response> {

        private CompletableFuture<U> future = new CompletableFuture<U>();
        private Function<Response, U> transformer;

        private Handler(Function<Response, U> transformer) {
            this.transformer = transformer;
        }

        @Override
        public Response onCompleted(Response response) throws Exception {
            U val = transformer.apply(response);
            future.complete(val);
            return response;
        }

        @Override
        public void onThrowable(Throwable t) {
            future.completeExceptionally(t);
        }

    }

}
