package com.example.livestream_update.Ringme.LiveStream.socket;

import android.annotation.SuppressLint;

import com.vtm.ringme.ApplicationController;
import com.vtm.ringme.helper.LogDebugHelper;
import com.vtm.ringme.helper.ReportHelper;
import com.vtm.ringme.livestream.listener.SocketEvent;
import com.vtm.ringme.livestream.model.LiveStreamMessage;
import com.vtm.ringme.livestream.socket.stomp.Stomp;
import com.vtm.ringme.livestream.socket.stomp.StompClient;
import com.vtm.ringme.livestream.socket.stomp.dto.StompMessage;
import com.vtm.ringme.utils.Log;

import org.greenrobot.eventbus.EventBus;
import org.json.JSONObject;

public class SocketManager {
    private static final String TAG = "com.viettel.mocha.module.livestream.socket.SocketManager";
    StompClient mWebSocketClient;

    private SocketManager() {
    }

    public static SocketManager getInstance() {
        return SingletonHelper.INSTANCE;
    }

    @SuppressLint("CheckResult")
    public void connectWebSocket(String url, final String roomID) {
        Log.e(TAG, "connectWebSocket roomID: " + roomID + "\turl: " + url);
        mWebSocketClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        mWebSocketClient.connect();
        mWebSocketClient.lifecycle().subscribe(lifecycleEvent -> {
            switch (lifecycleEvent.getType()) {
                case OPENED: {
                    Log.d(TAG, "Stomp connection opened");
                    mWebSocketClient.topic("/topic/messages/" + roomID).subscribe(message -> {
                        onReceiveMessage(message);
                    });
                    LogDebugHelper.getInstance().logDebugContent("LiveStream: Stomp connection opened");
                    ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_MESSAGE, "LiveStream: Stomp connection opened");
                    SocketEvent event = EventBus.getDefault().getStickyEvent(SocketEvent.class);
                    if (event == null) event = new SocketEvent();
                    event.setType(SocketEvent.TYPE_OPEN);
                    EventBus.getDefault().postSticky(event);
                }
                break;
                case CLOSED: {
                    Log.d(TAG, "Stomp connection closed");
                    LogDebugHelper.getInstance().logDebugContent("LiveStream: Stomp connection closed");
                    ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_MESSAGE, "LiveStream: Stomp connection closed");
                    SocketEvent event = EventBus.getDefault().getStickyEvent(SocketEvent.class);
                    if (event == null) event = new SocketEvent();
                    event.setType(SocketEvent.TYPE_CLOSE);
                    EventBus.getDefault().postSticky(event);
                }
                break;
                case ERROR: {
                    Log.e(TAG, "Stomp connection error", lifecycleEvent.getException());
                    mWebSocketClient.reconnect();
                    LogDebugHelper.getInstance().logDebugContent("LiveStream: Stomp connection error");
                    ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_MESSAGE, "LiveStream: Stomp connection error");
                    SocketEvent event = EventBus.getDefault().getStickyEvent(SocketEvent.class);
                    if (event == null) event = new SocketEvent();
                    event.setType(SocketEvent.TYPE_ERROR);
                    EventBus.getDefault().postSticky(event);
                }
                break;
            }
        });
    }

    private void onReceiveMessage(StompMessage message) {
        try {
            String payload = message.getPayload();
            Log.i(TAG, "onReceiveMessage payload: " + payload);
            JSONObject jso = new JSONObject(payload);
            String id = jso.optString("idcmt");
            String avatar = jso.optString("avatar");
            String from = jso.optString("from");
            long timestamp = jso.optLong("timestamp");
            long timeServer = jso.optLong("timeServer");
            String content = jso.optString("message");
            int type = jso.optInt("type");
            String msisdn = jso.optString("msisdn");
            String roomId = jso.optString("roomId");
            int isLike = jso.optInt("isLike");
            int countLike = jso.optInt("countLike");
            String tags = jso.optString("tags");
            int levelMessage = jso.optInt("levelMessage");
            String rowId = jso.optString("rowId");

            LiveStreamMessage quotedMessage = null;
            if (jso.optJSONObject("quotedMessage") != null) {
                JSONObject jsonQuote = jso.optJSONObject("quotedMessage");
                String quoteId = jsonQuote.optString("idcmt");
                String quoteAvatar = jsonQuote.optString("avatar");
                String quoteFrom = jsonQuote.optString("from");
                long quoteTimestamp = jsonQuote.optLong("timestamp");
                long quoteTimeServer = jsonQuote.optLong("timeServer");
                String quoteContent = jsonQuote.optString("message");
                int quoteType = jsonQuote.optInt("type");
                String quoteMsisdn = jsonQuote.optString("msisdn");
                String quoteRoomId = jsonQuote.optString("roomId");
                String quoteTags = jsonQuote.optString("tags");
                int quoteLevelMessage = jsonQuote.optInt("levelMessage");
                String quoteRowId = jso.optString("rowId");

                quotedMessage = new LiveStreamMessage();
                quotedMessage.setContent(quoteContent);
                quotedMessage.setId(quoteId);
                quotedMessage.setNameSender(quoteFrom);
                quotedMessage.setLastAvatar(quoteAvatar);
                quotedMessage.setMsisdn(quoteMsisdn);
                quotedMessage.setType(quoteType);
                quotedMessage.setTimeStamp(quoteTimestamp);
                quotedMessage.setTimeServer(quoteTimeServer);
                quotedMessage.setQuotedMessage(quotedMessage);
                quotedMessage.setRoomId(quoteRoomId);
                quotedMessage.setLevelMessage(quoteLevelMessage);
                quotedMessage.setTags(quoteTags);
                quotedMessage.setRowId(quoteRowId);
            }

            LiveStreamMessage liveStreamMessage = new LiveStreamMessage();
            liveStreamMessage.setContent(content);
            liveStreamMessage.setId(id);
            liveStreamMessage.setNameSender(from);
            liveStreamMessage.setLastAvatar(avatar);
            liveStreamMessage.setMsisdn(msisdn);
            liveStreamMessage.setType(type);
            liveStreamMessage.setTimeStamp(timestamp);
            liveStreamMessage.setTimeServer(timeServer);
            liveStreamMessage.setQuotedMessage(quotedMessage);
            liveStreamMessage.setRoomId(roomId);
            liveStreamMessage.setLike(isLike);
            liveStreamMessage.setCountLike(countLike);
            liveStreamMessage.setLevelMessage(levelMessage);
            liveStreamMessage.setTags(tags);
            liveStreamMessage.setRowId(rowId);

            EventBus.getDefault().postSticky(liveStreamMessage);
            /*if (liveStreamMessage.getType() != LiveStreamMessage.TYPE_COUNT_LIVE)//Type bang 5 la type cap nhat nguoi ra vao
            {
                LogDebugHelper.getInstance().logDebugContent("LiveStream payload: " + payload);
                ReportHelper.reportError(ApplicationController.self(), ReportHelper.DATA_TYPE_LIVE_STREAM_MESSAGE, "LiveStream payload: " + payload);
            }*/
        } catch (Exception e) {
            Log.e(TAG, e);
        }
    }

    public void sendMessage(String message) {
        if (mWebSocketClient != null)
            mWebSocketClient.send(message);
    }

    public StompClient getWebSocketClient() {
        return mWebSocketClient;
    }

    private void unsubscriber(String roomID) {
        if (mWebSocketClient != null)
            mWebSocketClient.unsubscribePath("/topic/messages/" + roomID);
    }

    public void disconnect() {
        if (mWebSocketClient != null)
            mWebSocketClient.disconnect();
    }

    public boolean isConnected() {
        if (mWebSocketClient != null)
            return mWebSocketClient.isConnected();
        return false;
    }

    private static class SingletonHelper {
        private static final SocketManager INSTANCE = new SocketManager();
    }
}
