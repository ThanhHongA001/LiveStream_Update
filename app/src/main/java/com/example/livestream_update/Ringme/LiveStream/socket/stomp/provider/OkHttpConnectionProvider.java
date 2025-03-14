package com.example.livestream_update.Ringme.LiveStream.socket.stomp.provider;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;

import com.vtm.ringme.livestream.socket.stomp.dto.LifecycleEvent;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

import okhttp3.Headers;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.WebSocket;
import okhttp3.WebSocketListener;
import okio.ByteString;

public class OkHttpConnectionProvider extends AbstractConnectionProvider {

    public static final String TAG = "OkHttpConnProvider";

    private final String mUri;
    @NonNull
    private final Map<String, String> mConnectHttpHeaders;
    private final OkHttpClient mOkHttpClient;

    @Nullable
    private WebSocket openSocket;

    public OkHttpConnectionProvider(String uri, @Nullable Map<String, String> connectHttpHeaders, OkHttpClient okHttpClient) {
        super();
        mUri = uri;
        mConnectHttpHeaders = connectHttpHeaders != null ? connectHttpHeaders : new HashMap<>();
        mOkHttpClient = okHttpClient;
    }

    @Override
    public void rawDisconnect() {
        if (openSocket != null) {
            openSocket.close(1000, "");
        }
    }

    @Override
    protected void createWebSocketConnection() {
        Request.Builder requestBuilder = new Request.Builder()
                .url(mUri);

        addConnectionHeadersToBuilder(requestBuilder, mConnectHttpHeaders);

        openSocket = mOkHttpClient.newWebSocket(requestBuilder.build(),
                new WebSocketListener() {
                    @Override
                    public void onOpen(@NonNull WebSocket webSocket, @NonNull Response response) {
                        LifecycleEvent openEvent = new LifecycleEvent(LifecycleEvent.Type.OPENED);

                        TreeMap<String, String> headersAsMap = headersAsMap(response);

                        openEvent.setHandshakeResponseHeaders(headersAsMap);
                        emitLifecycleEvent(openEvent);
                    }

                    @Override
                    public void onMessage(@NonNull WebSocket webSocket, @NonNull String text) {
                        emitMessage(text);
                    }

                    @Override
                    public void onMessage(@NonNull WebSocket webSocket, @NonNull ByteString bytes) {
                        emitMessage(bytes.utf8());
                    }

                    @Override
                    public void onClosed(@NonNull WebSocket webSocket, int code, @NonNull String reason) {
                        openSocket = null;
                        emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED));
                    }

                    @Override
                    public void onFailure(@NonNull WebSocket webSocket, @NonNull Throwable t, Response response) {
                        // in OkHttp, a Failure is equivalent to a JWS-Error *and* a JWS-Close
                         emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.ERROR, new Exception(t)));
                        openSocket = null;
                        emitLifecycleEvent(new LifecycleEvent(LifecycleEvent.Type.CLOSED));
                    }

                    @Override
                    public void onClosing(@NonNull final WebSocket webSocket, final int code, @NonNull final String reason) {
                        webSocket.close(code, reason);
                    }
                }
        );
    }

    @Override
    protected void rawSend(String stompMessage) {
        if (openSocket != null) {
            openSocket.send(stompMessage);
        }
    }

    @Nullable
    @Override
    protected Object getSocket() {
        return openSocket;
    }

    @NonNull
    private TreeMap<String, String> headersAsMap(@NonNull Response response) {
        TreeMap<String, String> headersAsMap = new TreeMap<>();
        Headers headers = response.headers();
        for (String key : headers.names()) {
            headersAsMap.put(key, headers.get(key));
        }
        return headersAsMap;
    }

    private void addConnectionHeadersToBuilder(@NonNull Request.Builder requestBuilder, @NonNull Map<String, String> mConnectHttpHeaders) {
        for (Map.Entry<String, String> headerEntry : mConnectHttpHeaders.entrySet()) {
            requestBuilder.addHeader(headerEntry.getKey(), headerEntry.getValue());
        }
    }
}
