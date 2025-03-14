package com.example.livestream_update.Ringme.LiveStream.socket;

import android.util.Log;

import com.blankj.utilcode.util.GsonUtils;
import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.values.Constants;
import com.google.gson.Gson;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.vtm.ringme.ApplicationController;

import com.vtm.ringme.livestream.model.LiveStreamBlockNotification;
import com.vtm.ringme.livestream.model.LiveStreamChatMessage;
import com.vtm.ringme.livestream.model.LiveStreamGiftMessage;
import com.vtm.ringme.livestream.model.LiveStreamLikeNotification;
import com.vtm.ringme.livestream.model.LiveStreamViewNumber;
import com.vtm.ringme.livestream.model.LivestreamDropStarNotification;
import com.vtm.ringme.livestream.model.LivestreamLiveNotification;
import com.vtm.ringme.livestream.model.LivestreamReloadSurveyNotification;
import com.vtm.ringme.livestream.model.LivestreamWaitNumberNotification;
import com.vtm.ringme.livestream.model.ReactCommentNotification;
import com.vtm.ringme.livestream.socket.stomp.Stomp;
import com.vtm.ringme.livestream.socket.stomp.StompClient;
import com.vtm.ringme.livestream.socket.stomp.dto.StompHeader;
import com.vtm.ringme.livestream.socket.stomp.dto.StompMessage;

import org.apache.commons.lang3.StringEscapeUtils;
import org.greenrobot.eventbus.EventBus;

import java.util.List;
import java.util.UUID;

import io.reactivex.CompletableTransformer;
import io.reactivex.android.schedulers.AndroidSchedulers;
import io.reactivex.disposables.CompositeDisposable;
import io.reactivex.disposables.Disposable;
import io.reactivex.schedulers.Schedulers;

public class SocketManagerV2 {
    //    public static final String domain = "ws://api3camid.metfone.com.kh/live-event/live/event/websocket";
//    public static final String domain = "ws://36.37.252.131/live-event/live/event/websocket";
//    public static final String domain = "ws://kakoakapi.ringme.vn:8080/live-event/live/event/websocket";
    public static final String connectorUser = "user";
    //    public static final String connectorToken = "token";
//    public static final String connectorUsername = "username";
    public static final String connectorContentType = "Content-type";
    public static final String connectorContentTypeValue = "application/json";

    public static final String pathSubscribeCMS = "/user/watching";
    public static final String pathSubscribeChannel = "/watching/";
    public static final String pathSubscribeDonate = "/topic/donate/";
    public static final String pathSendMessage = "/chat/message/";
    private static final String TAG = "SocketV2";
    private static SocketManagerV2 instance;
    StompClient mWebSocketClient;
    //    ReengAccount account;
//    ApplicationController applicationController;
    Disposable disposable, disposableCMS;
    private CompositeDisposable compositeDisposable;
    private String roomId;
    ReengAccount account;
    ApplicationController applicationController;
    private String currentStreamerId;

    private SocketManagerV2() {
    }

    public static SocketManagerV2 getInstance() {
        if (instance == null) {
            instance = new SocketManagerV2();
        }
        return instance;
    }


    public void connect(String url, List<StompHeader> headers, String roomId, String streamerId) {
        resetSubscriptions();
        Log.d(TAG, "url: " + url);
        mWebSocketClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        mWebSocketClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mWebSocketClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
//                            EventBus.getDefault().postSticky(new OnSocketErrorEvent());
                            subToCMS();
                            Log.d(TAG, "Stomp connection opened");
                            sub(roomId, streamerId);
                            break;
                        case ERROR:
                            Log.d(TAG, "Stomp connection error", lifecycleEvent.getException());
                            mWebSocketClient.reconnect();
                            connect(url, headers,roomId, streamerId);
                            break;
                        case CLOSED:
                            Log.d(TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                }, throwable -> Log.d("123", ""));

        compositeDisposable.add(dispLifecycle);

        mWebSocketClient.connect(headers);
    }

    public void connectDonate(String url, List<StompHeader> headers, String roomId, String streamerId) {
        resetSubscriptions();
        Log.d(TAG, "url: " + url);
        mWebSocketClient = Stomp.over(Stomp.ConnectionProvider.OKHTTP, url);
        mWebSocketClient.withClientHeartbeat(1000).withServerHeartbeat(1000);
        Disposable dispLifecycle = mWebSocketClient.lifecycle()
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread())
                .subscribe(lifecycleEvent -> {
                    switch (lifecycleEvent.getType()) {
                        case OPENED:
//                            EventBus.getDefault().postSticky(new OnSocketErrorEvent());
                            subToCMS();
                            Log.d(TAG, "Stomp connection opened");
                            subToDonate(roomId);
                            break;
                        case ERROR:
                            Log.d(TAG, "Stomp connection error", lifecycleEvent.getException());
                            mWebSocketClient.reconnect();
                            connectDonate(url, headers,roomId, streamerId);
                            break;
                        case CLOSED:
                            Log.d(TAG, "Stomp connection closed");
                            resetSubscriptions();
                            break;
                        case FAILED_SERVER_HEARTBEAT:
                            Log.d(TAG, "Stomp failed server heartbeat");
                            break;
                    }
                }, throwable -> Log.d("123", ""));

        compositeDisposable.add(dispLifecycle);

        mWebSocketClient.connect(headers);
    }

    public void sub(String roomId, String streamerId) {
        this.roomId = roomId;
        this.currentStreamerId = streamerId;
        Log.d(TAG, "sub: " + roomId);
        disposable = mWebSocketClient.topic(pathSubscribeChannel + roomId)
                .subscribe(this::handleMessage);
        compositeDisposable.add(disposable);
    }

    public void subToDonate(String roomId) {
        this.roomId = roomId;
        Log.d(TAG, "sub: " + roomId);
        disposable = mWebSocketClient.topic(pathSubscribeDonate + roomId)
                .subscribe(this::handleMessage);
        compositeDisposable.add(disposable);
    }

    public void subToCMS() {
        disposableCMS = mWebSocketClient.topic(pathSubscribeCMS)
                .subscribe(this::handleMessage);
        compositeDisposable.add(disposableCMS);
    }

    private void handleMessage(StompMessage message) {
        String chatMessage = StringEscapeUtils.unescapeJava(message.getPayload());
        chatMessage = chatMessage.substring(1, chatMessage.length() - 1);
//        Log.d(TAG, "payload: " + message.getPayload());
        Log.d(TAG, "chatMessage: " + chatMessage);
        JsonObject type = new JsonParser().parse(chatMessage).getAsJsonObject();
        String position = type.get(Constants.WebSocket.type).getAsString();
        Gson gson = new Gson();
        switch (position){
            case Constants.WebSocket.typeViewNumber:
                LiveStreamViewNumber onlineResponse = gson.fromJson(chatMessage, LiveStreamViewNumber.class);
                if (onlineResponse.getNumber() != 0) {
                    onReceiveViewNumber(onlineResponse);
                }
                break;
            case Constants.WebSocket.typeMessage:
                LiveStreamChatMessage liveStreamChatMessage = gson.fromJson(chatMessage, LiveStreamChatMessage.class);
                LiveStreamChatMessage chatContent = LiveStreamChatMessage.convert(liveStreamChatMessage);
                onReceiveChatMessage(chatContent);
                break;
            case Constants.WebSocket.typeGift:
                LiveStreamGiftMessage liveStreamGiftMessage = gson.fromJson(chatMessage, LiveStreamGiftMessage.class);
                onReceiveGift(liveStreamGiftMessage);
                break;
            case Constants.WebSocket.typeBlock:
                LiveStreamBlockNotification liveStreamBlockNotification = gson.fromJson(chatMessage,LiveStreamBlockNotification.class);
                onReceiveBlockNotification(liveStreamBlockNotification);
                break;
            case Constants.WebSocket.typeLike:
                LiveStreamLikeNotification liveStreamLikeNotification = gson.fromJson(chatMessage,LiveStreamLikeNotification.class);
                onReceiveLikeNumber(liveStreamLikeNotification);
                break;
            case Constants.WebSocket.typeNotify:
                LivestreamLiveNotification livestreamLiveNotification = gson.fromJson(chatMessage, LivestreamLiveNotification.class);
                onReceiveLiveNotification(livestreamLiveNotification);
                break;
            case Constants.WebSocket.delete:
                LiveStreamBlockNotification deleteCommentNotification = gson.fromJson(chatMessage, LiveStreamBlockNotification.class);
                onReceiveCommentDelete(deleteCommentNotification);
                break;
            case Constants.WebSocket.typeReactComment:
                ReactCommentNotification reactCommentNotification = gson.fromJson(chatMessage, ReactCommentNotification.class);
                onReceiveReactionComment(reactCommentNotification);
                break;
            case Constants.WebSocket.typeDropStar:
                LivestreamDropStarNotification livestreamDropStarNotification = gson.fromJson(chatMessage, LivestreamDropStarNotification.class);
                onReceiveDropStar(livestreamDropStarNotification);
                break;
            case Constants.WebSocket.typeWait:
                LivestreamWaitNumberNotification livestreamWaitNumberNotification = gson.fromJson(chatMessage, LivestreamWaitNumberNotification.class);
                onReceiveWaitNumber(livestreamWaitNumberNotification);
                break;
            case Constants.WebSocket.reloadSurvey:
                LivestreamReloadSurveyNotification livestreamReloadSurveyNotification = gson.fromJson(chatMessage, LivestreamReloadSurveyNotification.class);
                onReceiveReloadSurvey(livestreamReloadSurveyNotification);
                break;
            default: break;
        }
    }

    private void resetSubscriptions() {
        if (compositeDisposable != null) {
            compositeDisposable.dispose();
        }
        compositeDisposable = new CompositeDisposable();
    }

    private void onReceiveLikeNumber(LiveStreamLikeNotification liveStreamLikeNotification) {
        EventBus.getDefault().postSticky(liveStreamLikeNotification);
    }

    private void onReceiveViewNumber(LiveStreamViewNumber number) {
        EventBus.getDefault().postSticky(number);
    }

    private void onReceiveChatMessage(LiveStreamChatMessage chatContent) {
        EventBus.getDefault().postSticky(chatContent);
    }

    private void onReceiveGift(LiveStreamGiftMessage gift){
        EventBus.getDefault().postSticky(gift);
    }

    private void onReceiveBlockNotification(LiveStreamBlockNotification liveStreamBlockNotification) {
        EventBus.getDefault().postSticky(liveStreamBlockNotification);
    }

    private void onReceiveLiveNotification(LivestreamLiveNotification livestreamLiveNotification) {
        EventBus.getDefault().postSticky(livestreamLiveNotification);
    }

    private void onReceiveCommentDelete(LiveStreamBlockNotification deleteCommentNotification) {
        EventBus.getDefault().postSticky(deleteCommentNotification);
    }

    private void onReceiveReactionComment(ReactCommentNotification reactCommentNotification) {
        EventBus.getDefault().postSticky(reactCommentNotification);
    }

    private void onReceiveDropStar(LivestreamDropStarNotification livestreamDropStarNotification) {
        EventBus.getDefault().postSticky(livestreamDropStarNotification);
    }

    private void onReceiveWaitNumber(LivestreamWaitNumberNotification livestreamWaitNumberNotification) {
        EventBus.getDefault().postSticky(livestreamWaitNumberNotification);
    }

    private void onReceiveReloadSurvey(LivestreamReloadSurveyNotification livestreamReloadSurveyNotification) {
        EventBus.getDefault().postSticky(livestreamReloadSurveyNotification);
    }

    public void sendMessage(String message) {
        String cmsgId = UUID.randomUUID().toString();
        applicationController = ApplicationController.self();
        account = applicationController.getCurrentAccount();
        ChatMessageRequest chatMessage = new ChatMessageRequest();
        chatMessage.setMsgBody(message);
        chatMessage.setType(Constants.WebSocket.typeMessage);
        chatMessage.setUserId(account.getJidNumber());
        chatMessage.setUserName(account.getName());
        chatMessage.setcIdMessage(cmsgId);
        chatMessage.setRoomID(roomId);
        String avatarUrl = applicationController.getAvatarUrl(account.getLastChangeAvatar(),account.getJidNumber(),70);
        chatMessage.setAvatar(avatarUrl);
        chatMessage.setCreatedAt(System.currentTimeMillis());
        String dataMessage = GsonUtils.toJson(chatMessage);
        if (mWebSocketClient != null && isConnected()) {
            mWebSocketClient.send(
                    Constants.WebSocket.pathSendMessage + roomId
                    , dataMessage).subscribe();
        }
    }

    public StompClient getWebSocketClient() {
        return mWebSocketClient;
    }

    public String getCurrentStreamerId() {
        return currentStreamerId;
    }

    public void disconnect() {
        try {
            if (mWebSocketClient != null) {
                Log.d(TAG, "disconnect");
                mWebSocketClient.disconnect();
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public boolean isConnected() {
        if (mWebSocketClient != null)
            return mWebSocketClient.isConnected();
        return false;
    }

    public String getRoomId() {
        return roomId;
    }

    protected CompletableTransformer applySchedulers() {
        return upstream -> upstream
                .unsubscribeOn(Schedulers.newThread())
                .subscribeOn(Schedulers.io())
                .observeOn(AndroidSchedulers.mainThread());
    }

}