package com.example.livestream_update.Ringme.LiveStream.fragment;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.app.Dialog;
import android.content.Intent;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;
import android.widget.TextView;

import androidx.appcompat.widget.AppCompatImageView;
import androidx.fragment.app.Fragment;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.vtm.ringme.ApplicationController;
import com.vtm.R;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.common.api.http.HttpCallBack;

import com.vtm.ringme.dialog.LivestreamGetCoinDialog;
import com.vtm.ringme.helper.encrypt.EncryptUtil;
import com.vtm.ringme.helper.encrypt.RSAEncrypt;
import com.vtm.ringme.helper.encrypt.RSAEncryptLivestream;
import com.vtm.ringme.dialog.LoadingDialog;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.adapter.LivestreamChatAdapter;
import com.vtm.ringme.livestream.adapter.LivestreamFunctionAdapter;
import com.vtm.ringme.livestream.apis.LivestreamApiInstance;
import com.vtm.ringme.livestream.apis.LivestreamAuthentication;
import com.vtm.ringme.livestream.apis.LivestreamServices;
import com.vtm.ringme.livestream.apis.response.LiveStreamBuyCoinResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamCoinResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamDonateResponse;
import com.vtm.ringme.livestream.apis.response.LiveStreamGiftResponse;
import com.vtm.ringme.livestream.dialog.DonateDialog;
import com.vtm.ringme.livestream.eventbus.EventBusEvents;
import com.vtm.ringme.livestream.listener.LivestreamCommentActionListener;
import com.vtm.ringme.livestream.model.Coin;
import com.vtm.ringme.livestream.model.FollowSuccess;
import com.vtm.ringme.livestream.model.Gift;
import com.vtm.ringme.livestream.model.LiveStreamBlockNotification;
import com.vtm.ringme.livestream.model.LiveStreamChatMessage;
import com.vtm.ringme.livestream.model.LiveStreamGiftMessage;
import com.vtm.ringme.livestream.model.LivestreamChatModel;
import com.vtm.ringme.livestream.model.ReactCommentNotification;
import com.vtm.ringme.livestream.model.ReactionCommentModel;
import com.vtm.ringme.livestream.socket.SocketManagerV2;

import com.vtm.ringme.model.ReengAccount;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.Log;
import com.vtm.ringme.utils.Utilities;
import com.vtm.ringme.values.Constants;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Objects;

import io.reactivex.annotations.NonNull;
import io.reactivex.annotations.Nullable;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class LivestreamChatFragment extends Fragment implements LivestreamCommentActionListener {

    //view
    RecyclerView rcvMessages;
    DonateDialog donateDialog;
    LivestreamGetCoinDialog getCoinDialog;
    LoadingDialog loadingDialog;
    TextView tvDisableChat;
    AppCompatImageView bottomShadow, topShadow;
    TextView cmtNumber;

    //control
    LivestreamChatAdapter adapterV2;
    LivestreamFunctionAdapter functionAdapter;
    List<LivestreamChatModel> chatModels;
    List<Constants.ChatFunction> functions;
    ApplicationController applicationController;
    Coin currentCoin;
    ReengAccount account;
    Dialog deleteCommentDialog;
    int commentNumber = 0;
    boolean isReachBottom = true;
    LivestreamDetailActivity activity;
    //data
    private LivestreamModel video;
    private ArrayList<ReactionCommentModel> listReactionUser;

    public void setListReactionUser(ArrayList<ReactionCommentModel> listReactionUser) {
        this.listReactionUser = listReactionUser;
    }

    private final String[] strings = {"like", "love", "laugh", "wow", "sad", "angry"};

    public static LivestreamChatFragment newInstance(Bundle args, Activity activity) {
        LivestreamChatFragment fragment = new LivestreamChatFragment();
        fragment.setArguments(args);
        return fragment;
    }

    //Override
    @Nullable
    @androidx.annotation.Nullable
    @Override
    public View onCreateView(@NonNull @androidx.annotation.NonNull LayoutInflater inflater, @Nullable @androidx.annotation.Nullable ViewGroup container, @Nullable @androidx.annotation.Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.rm_fragment_chat_livestream, container, false);
        if (!EventBus.getDefault().isRegistered(this)) {
            EventBus.getDefault().register(this);
        }
        activity = (LivestreamDetailActivity) getActivity();
        initController();
        initView(view);
        return view;
    }

    @Override
    public void onDestroy() {
        EventBus.getDefault().unregister(this);
        super.onDestroy();
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (resultCode == Activity.RESULT_OK) {
            if (requestCode == Constants.requestCode.requestGetCoin) {
                getCurrentCoin();
            }
        }
    }


    //Subscribe
    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveGift(LiveStreamGiftMessage liveStreamGiftMessage) {
        LivestreamChatModel chatModel = new LivestreamChatModel();
        chatModel.setType(Constants.WebSocket.typeGift);
        chatModel.setStreamGift(liveStreamGiftMessage);
        adapterV2.addMessage(chatModel);
        if (isReachBottom)
            rcvMessages.smoothScrollToPosition(adapterV2.getItemCount() - 1);
        EventBus.getDefault().removeStickyEvent(liveStreamGiftMessage);
        commentNumber++;
        cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
    }

    @SuppressLint("SetTextI18n")
    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveChatMessage(LiveStreamChatMessage chatContent) {
        LivestreamChatModel chatModel = new LivestreamChatModel();
        chatModel.setType(Constants.WebSocket.typeMessage);
        chatModel.setChatMessage(chatContent);
        if (listReactionUser != null && listReactionUser.size() > 0 && adapterV2.getChatModels().size() <= 9) {
            for (ReactionCommentModel react : listReactionUser) {
                String chatId = "";
                if (!TextUtils.isEmpty(chatContent.getsIdMessage())) {
                    chatId = chatContent.getsIdMessage();
                } else if (!TextUtils.isEmpty(chatContent.getSmsgId())) {
                    chatId = chatContent.getSmsgId();
                }
                if (chatId.equals(react.getSidMessage())) {
                    chatModel.setReactionType(Integer.parseInt(react.getType()));
                }
            }
        }
        adapterV2.addMessage(chatModel);
        if (isReachBottom)
            rcvMessages.smoothScrollToPosition(adapterV2.getItemCount() - 1);
        EventBus.getDefault().removeStickyEvent(chatContent);
        updateCommentUI(new EventBusEvents.UpdateCommentEvent());
        commentNumber++;
        cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateBlockUi(EventBusEvents.UpdateBlockEvent event) {
        if (event.isBlocked()) {
            bannedChat();
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveBlockNotification(LiveStreamBlockNotification event) {
//            case Constants.WebSocket.banMessage300s:
//            case Constants.WebSocket.banChat:
//                bannedChat();
//                break;
//            case Constants.WebSocket.hideStream:
//                //todo out stream
//                AlertDialog.Builder builder = new AlertDialog.Builder(requireContext());
//                builder.setMessage(requireActivity().getResources().getString(R.string.livestream_kick_out));
//                builder.setPositiveButton(requireActivity().getResources().getString(R.string.ok), (dialog, which) -> requireActivity().finish());
//                builder.setOnDismissListener(dialog -> {
//                    requireActivity().finish();
//                });
//                builder.show();
//                break;
        if (event.getBlockId().equals("3")) {
            adapterV2.deleteComment(event.getChatId());
            commentNumber--;
            cmtNumber.setText(Utilities.shortenLongNumber(chatModels.size()) + (chatModels.size() > 1 ? " Comments" : " Comment"));
            //cmtNumber.setText(Utilities.shortenLongNumber(commentNumber) + (commentNumber > 1 ? " Comments" : " Comment"));
            EventBus.getDefault().removeStickyEvent(event);
        }
        EventBus.getDefault().removeStickyEvent(event);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void updateLikeUi(EventBusEvents.UpdateLikeEvent event) {
        functions.get(0).setFunctionType(event.isLike() ? Constants.ChatFunction.TYPE_LIKED : Constants.ChatFunction.TYPE_LIKE);
        functionAdapter.notifyItemChanged(0);
        EventBus.getDefault().removeStickyEvent(event);
    }

    //apis
    private void donate(Gift gift) {
        showLoadingDialog(requireActivity().getString(R.string.loading_livestream), requireActivity().getString(R.string.loading_livestream));
        String avatar = applicationController.getAvatarUrl(account.getLastChangeAvatar(), account.getJidNumber(), 70);
        String livestreamId = SocketManagerV2.getInstance().getRoomId();
        String streamerId = SocketManagerV2.getInstance().getCurrentStreamerId();
        long timestamp = System.currentTimeMillis();
        String userId = account.getJidNumber();
        String username = account.getName();
        String authenAPIKey = ApplicationController.self().getKakoakApi();
        String dataEncrypt = RSAEncrypt.getInStance(applicationController).encrypt(applicationController, applicationController.getToken());
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        livestreamServices.donate("test", "test", avatar, String.valueOf(gift.getId()), livestreamId, streamerId, timestamp, userId, username)
                .enqueue(new Callback<LiveStreamDonateResponse>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamDonateResponse> call, @androidx.annotation.NonNull Response<LiveStreamDonateResponse> response) {
                        hideLoadingDialog();
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    donateDialog.setCurrentCoin(response.body().getReturnData().getCurrentAmountStar());
                                    currentCoin.setCurrentStar(response.body().getReturnData().getCurrentAmountStar());
                                    getCoinDialog.setBalance(currentCoin.getCurrentStar());
                                    donateDialog.close();
                                    ToastUtils.makeText(requireActivity(), R.string.donate_success);
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamDonateResponse> call, @androidx.annotation.NonNull Throwable throwable) {
                        Log.e("Livestream " + getTag(), throwable.getMessage());
                        hideLoadingDialog();
                        donateDialog.close();
                        ToastUtils.makeText(requireActivity(), R.string.e500_internal_server_error);
                    }
                });
    }

    private void getCurrentCoin() {
        currentCoin = new Coin();
        String userId = applicationController.getCurrentAccount().getJidNumber();
        String token = applicationController.getCurrentAccount().getToken();
        String username = applicationController.getCurrentAccount().getName();
        String timestamp = String.valueOf(System.currentTimeMillis());
        Map<String, String> headers = LivestreamAuthentication.getLivestreamAuth(userId + token + timestamp);
        String authentication = headers.get(Constants.Authentication.authenticationKey);
        String dataEncrypted = headers.get(Constants.Authentication.security);
        String authenAPIKey = ApplicationController.self().getKakoakApi();
        String dataEncrypt = RSAEncryptLivestream.getInStance(applicationController).encrypt(applicationController, EncryptUtil.encryptMD5(userId + timestamp) + "." + token);
        Log.e("Get current coin", "token: " + applicationController.getToken() + "\nmd5: " + EncryptUtil.encryptMD5(userId + timestamp) + "\nRSA: " + dataEncrypt);
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        livestreamServices.getCurrentCoin("test", dataEncrypt, Long.parseLong(timestamp), userId, username)
                .enqueue(new Callback<LiveStreamCoinResponse>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamCoinResponse> call, @androidx.annotation.NonNull Response<LiveStreamCoinResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getCurrentCoin() != null) {
                                    currentCoin = response.body().getCurrentCoin();
                                    donateDialog.setCurrentCoin(currentCoin.getCurrentStar());
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamCoinResponse> call, @androidx.annotation.NonNull Throwable throwable) {
                        Log.e("Livestream " + getTag(), throwable.getMessage());
                    }
                });

    }


    private void getDonateGiftList() {
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        livestreamServices.getGiftFromServer(0, 10, System.currentTimeMillis())
                .enqueue(new Callback<LiveStreamGiftResponse>() {
                    private void onLoadMoreGift(int page) {
                        loadMoreGiftList(page);
                    }

                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamGiftResponse> call, @androidx.annotation.NonNull Response<LiveStreamGiftResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    if (response.body().getGiftResponses().size() > 0) {
                                        donateDialog.setGifts(response.body().getGiftResponses());
                                        donateDialog.setLoadMoreGiftListener(this::onLoadMoreGift);
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamGiftResponse> call, @androidx.annotation.NonNull Throwable throwable) {

                    }
                });
    }

    private void loadMoreGiftList(int page) {
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        livestreamServices.getGiftFromServer(page, 10, System.currentTimeMillis())
                .enqueue(new Callback<LiveStreamGiftResponse>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamGiftResponse> call, @androidx.annotation.NonNull Response<LiveStreamGiftResponse> response) {
                        if (response.isSuccessful()) {
                            if (response.body() != null) {
                                if (response.body().getCode() == 200) {
                                    if (response.body().getGiftResponses().size() > 0) {
                                        donateDialog.addGifts(response.body().getGiftResponses());
                                    }
                                }
                            }
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamGiftResponse> call, @androidx.annotation.NonNull Throwable throwable) {

                    }
                });
    }

    private void doGetCoin(String amount) {
        showLoadingDialog(getString(R.string.loading_livestream), getString(R.string.loading_livestream));
        LivestreamServices livestreamServices = LivestreamApiInstance.getLiveStreamInstance();
        //TODO sau sửa type payment
        livestreamServices.buyCoin(amount, System.currentTimeMillis(), Constants.Server.listAllGift, account.getJidNumber())
                .enqueue(new Callback<LiveStreamBuyCoinResponse>() {
                    @Override
                    public void onResponse(@androidx.annotation.NonNull Call<LiveStreamBuyCoinResponse> call, @androidx.annotation.NonNull Response<LiveStreamBuyCoinResponse> response) {
                        hideLoadingDialog();
                        if (response.isSuccessful()) {
                            if ((response.body() != null)) {
                                if (response.body().getCode() == 200) {
                                    getCurrentCoin();
                                    getCoinDialog.setBalance(currentCoin.getCurrentStar());
                                    showToast(getString(R.string.purchase_success));
                                } else {
                                    showToast(response.message());
                                }
                            } else {
                                showToast(getString(R.string.e500_internal_server_error));
                            }
                        } else {
                            showToast(getString(R.string.e500_internal_server_error));
                        }
                    }

                    @Override
                    public void onFailure(@androidx.annotation.NonNull Call<LiveStreamBuyCoinResponse> call, @androidx.annotation.NonNull Throwable throwable) {
                        hideLoadingDialog();
                        showToast(getString(R.string.e500_internal_server_error));
                    }
                });

    }

    public void showToast(final String msg) {
        ToastUtils.showToast(requireActivity(), msg);
    }

    @Override
    public void onDetach() {
        super.onDetach();
//        SocketManagerV2.getInstance().unSub(SocketManagerV2.getInstance().getRoomId());
    }

    //View
    private void initView(View view) {
        rcvMessages = view.findViewById(R.id.rcv_messages);
        tvDisableChat = view.findViewById(R.id.tv_disable_chat);
        bottomShadow = view.findViewById(R.id.bottom_shadow);
        topShadow = view.findViewById(R.id.top_shadow);
        cmtNumber = view.findViewById(R.id.cmtNumber);

        //data
        chatModels = new ArrayList<>();

        Bundle bundle = getArguments();
        video = (LivestreamModel) Objects.requireNonNull(bundle).getSerializable(Constants.KeyData.video);
        if (video.isBlockComment()) {
            bannedChat();
        }

        adapterV2 = new LivestreamChatAdapter(chatModels, requireActivity(), video.getScreenType(), LivestreamChatAdapter.TYPE_LIVE);
        adapterV2.setCommentActionListener(this);
        adapterV2.setReactionCommentClickListener(this::callApiReactionComment);
        if (video.getEnableChat() == 0) {
            tvDisableChat.setVisibility(View.VISIBLE);
            rcvMessages.setVisibility(View.GONE);
        } else {
            tvDisableChat.setVisibility(View.GONE);
            rcvMessages.setVisibility(View.VISIBLE);
        }

        donateDialog = new DonateDialog(requireActivity());
        loadingDialog = new LoadingDialog(requireActivity(), false);
        donateDialog.setListener(this::donate);
        donateDialog.setGetCoinListener(this::openGetCoinDialog);
        getCoinDialog = new LivestreamGetCoinDialog(requireActivity());
        getCoinDialog.setGetCoinListener(this::doGetCoin);
        functions = new ArrayList<>();
        if (video.isLike())
            functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_LIKED));
        else
            functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_LIKE));
        functions.add(new Constants.ChatFunction(Constants.ChatFunction.TYPE_DONATE));
        functionAdapter = new LivestreamFunctionAdapter(functions);

        LinearLayoutManager layoutManager = new LinearLayoutManager(requireContext());
        layoutManager.setStackFromEnd(true);
        rcvMessages.setLayoutManager(layoutManager);
        rcvMessages.setAdapter(adapterV2);

        getDonateGiftList();
        getCurrentCoin();

        //todo sub channel ở đây vì: cần sub muộn nhất có thể để view và list khởi tạo xong để lấy subscribe của EventBus cho message cũ của stream
        subChannel();

        rcvMessages.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrolled(@androidx.annotation.NonNull RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);

                //recyclerview reach bottom event
                if (!recyclerView.canScrollVertically(RecyclerView.LAYOUT_DIRECTION_LTR)) {
                    bottomShadow.setVisibility(View.GONE);
                    isReachBottom = true;
                } else {
                    if (video.getScreenType() == 0) {
                        bottomShadow.setVisibility(View.VISIBLE);
                    }
                    isReachBottom = false;
                }

                //todo recyclerview reach top event
//                if (!recyclerView.canScrollVertically(RecyclerView.LAYOUT_DIRECTION_RTL)) {
//                    topShadow.setVisibility(View.GONE);
//                } else topShadow.setVisibility(View.VISIBLE);
            }
        });
        setUpScreenType();
    }

    private void openGetCoinDialog() {
        getCoinDialog.setBalance(currentCoin.getCurrentStar());
        getCoinDialog.setCancelable(true);
        getCoinDialog.setCanceledOnTouchOutside(true);
        getCoinDialog.show();
    }

    public void showLoadingDialog(final String title, final String message) {
        if (requireActivity().isFinishing()) return;
        requireActivity().runOnUiThread(() -> {
            loadingDialog.setLabel(title);
            loadingDialog.setMessage(message);
            loadingDialog.show();
        });
    }

    public void hideLoadingDialog() {
        if (loadingDialog != null && loadingDialog.isShowing()) {
            loadingDialog.dismiss();
        }
    }

    private void bannedChat() {

    }

    private void openDonateDialog() {
        donateDialog.show();
    }


    private void updateCommentUI(EventBusEvents.UpdateCommentEvent event) {
        EventBus.getDefault().postSticky(event);
    }

    private void doLikeStream(FollowSuccess data) {
        EventBus.getDefault().postSticky(data);
    }

    //other
    private void initController() {
        applicationController = ApplicationController.self();
        account = applicationController.getCurrentAccount();
    }

    private void subChannel() {

    }

    @Override
    public void onClickDeleteComment(String commentId, String avatar, String userName) {
        openDialogConfirmDeleteComment(commentId, avatar, userName);
    }

    @Override
    public void onClickUserProfile(String msisdn, String name) {

    }

    private void openDialogConfirmDeleteComment(String commentId, String avatar, String userName) {
        requireActivity();
        deleteCommentDialog = new Dialog(requireContext());
        if (deleteCommentDialog.getWindow() != null) {
            deleteCommentDialog.getWindow().getDecorView().setBackgroundResource(android.R.color.transparent);
            deleteCommentDialog.getWindow().setLayout(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);

            WindowManager.LayoutParams wlp = deleteCommentDialog.getWindow().getAttributes();
            wlp.gravity = Gravity.CENTER;
            deleteCommentDialog.getWindow().setAttributes(wlp);
        }

        deleteCommentDialog.setContentView(R.layout.rm_dialog_register_data_reqiurement);
        deleteCommentDialog.setCancelable(false);
        deleteCommentDialog.setCanceledOnTouchOutside(false);

        ((TextView) deleteCommentDialog.findViewById(R.id.txt_title_dialog)).setText(R.string.title_delete_comment);
        ((TextView) deleteCommentDialog.findViewById(R.id.txt_dialog)).setText(R.string.message_delete_comment);
        ((TextView) deleteCommentDialog.findViewById(R.id.btn_ok_dialog)).setText(R.string.confirm);
        ((TextView) deleteCommentDialog.findViewById(R.id.btn_cancel_dialog)).setText(R.string.cancel);

        deleteCommentDialog.findViewById(R.id.btn_ok_dialog).setOnClickListener(view -> {
            deleteCommentDialog.findViewById(R.id.ll_buttons).setVisibility(View.GONE);
            deleteCommentDialog.findViewById(R.id.loading).setVisibility(View.VISIBLE);
            apiDeleteComment(commentId, avatar, userName);
        });

        deleteCommentDialog.findViewById(R.id.btn_cancel_dialog).setOnClickListener(v -> {
            deleteCommentDialog.dismiss();
        });
        deleteCommentDialog.show();
    }
    
    private void apiDeleteComment(String commentId, String avatar, String userName) {
        HomeApi.getInstance().deleteCommentLivestream(commentId, video.getId(), video.getChannelId(), avatar, userName, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                if (deleteCommentDialog != null)
                    deleteCommentDialog.dismiss();
                ToastUtils.makeText(requireContext(), R.string.deleted_successfully);
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                if (deleteCommentDialog != null)
                    deleteCommentDialog.dismiss();
                ToastUtils.makeText(requireContext(), R.string.error_message_default);
            }
        });
    }

    private void setUpScreenType() {
        if (video.getScreenType() == 0) {
            topShadow.setVisibility(View.VISIBLE);
        } else topShadow.setVisibility(View.GONE);
    }

    @Subscribe(threadMode = ThreadMode.MAIN, sticky = true)
    public void onReceiveReactionComment(ReactCommentNotification event) {
        adapterV2.updateReaction(event);
        EventBus.getDefault().removeStickyEvent(event);
    }

    private void callApiReactionComment(int type, String smsgId, String action, int i) {
        HomeApi.getInstance().reactionComment(type, smsgId, action, new HttpCallBack() {
            @Override
            public void onSuccess(String data) throws Exception {
                if (action.equals("LIKE")) {
                    adapterV2.setReact(i, type);
                } else {
                    adapterV2.setReact(i, 0);
                }
            }

            @Override
            public void onFailure(String message) {
                super.onFailure(message);
                if(!activity.isFinishing()) {
                    ToastUtils.makeText(activity, R.string.error_message_default);
                }
            }
        });
    }
}
