package com.example.livestream_update.Ringme.Dialog;


import android.app.Activity;
import android.app.Dialog;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.text.TextUtils;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Window;
import android.view.WindowManager;

import androidx.recyclerview.widget.LinearLayoutManager;

import com.google.android.material.bottomsheet.BottomSheetDialog;
import com.google.android.material.bottomsheet.BottomSheetDialogFragment;
import com.google.gson.Gson;
import com.vtm.R;
import com.vtm.databinding.RmBottomSheetLivestreamFutureVoteBinding;
import com.vtm.ringme.activities.LivestreamFutureActivity;
import com.vtm.ringme.adapter.LivestreamVoteOptionAdapter;
import com.vtm.ringme.api.HomeApi;
import com.vtm.ringme.api.response.VoteResultResponse;
import com.vtm.ringme.common.api.http.HttpCallBack;
import com.vtm.ringme.livestream.activity.LivestreamDetailActivity;
import com.vtm.ringme.livestream.model.LivestreamVoteModel;
import com.vtm.ringme.livestream.model.LivestreamVoteOptionModel;
import com.vtm.ringme.model.livestream.LivestreamModel;
import com.vtm.ringme.utils.ToastUtils;
import com.vtm.ringme.utils.ToastUtilsV2;

import java.util.Objects;


public class LivestreamFutureVoteBottomSheetDialog extends BottomSheetDialogFragment {

    Activity activity;
    RmBottomSheetLivestreamFutureVoteBinding binding;
    private LivestreamModel livestream;
    private LivestreamVoteModel voteModel;
    private LivestreamVoteOptionAdapter optionAdapter;
    private boolean isVoted;
    private long numberVote = 0;

    public static LivestreamFutureVoteBottomSheetDialog newInstance(Activity context, LivestreamModel livestream, LivestreamVoteModel voteModel, boolean voted, long numberVote) {
        LivestreamFutureVoteBottomSheetDialog fragment = new LivestreamFutureVoteBottomSheetDialog();
        fragment.activity = context;
        fragment.livestream = livestream;
        fragment.setVoteModel(voteModel);
        fragment.setVoted(voted);
        fragment.setNumberVote(numberVote);
        return fragment;
    }

    public void setVoteModel(LivestreamVoteModel voteModel) {
        this.voteModel = voteModel;
    }

    public void setVoted(boolean voted) {
        isVoted = voted;
    }

    public void setNumberVote(long numberVote) {
        this.numberVote = numberVote;
    }

    @Override
    public Dialog onCreateDialog(Bundle savedInstanceState) {
        BottomSheetDialog dialog = (BottomSheetDialog) super.onCreateDialog(savedInstanceState);
        binding = RmBottomSheetLivestreamFutureVoteBinding.inflate(LayoutInflater.from(activity));
        dialog.setContentView(binding.getRoot());
        Window window = activity.getWindow();
        window.setBackgroundDrawable(new ColorDrawable(Color.TRANSPARENT));
        WindowManager.LayoutParams layoutParams = window.getAttributes();
        layoutParams.width = WindowManager.LayoutParams.MATCH_PARENT;
        window.setAttributes(layoutParams);

        initView();
        return dialog;
    }

    public void initView() {
        binding.btnClose.setOnClickListener(view -> dismiss());
        binding.tvVoteName.setText(voteModel.getTitle());
        optionAdapter = new LivestreamVoteOptionAdapter(activity);
        optionAdapter.setList(voteModel.getVoteDtos());
        optionAdapter.setListener(option -> {
            if (option.getNumberVote() == 0) {
                binding.edtPredictNumber.setText("");
            } else {
                binding.edtPredictNumber.setText(String.valueOf(option.getNumberVote()));
            }
        });
        binding.rcv.setLayoutManager(new LinearLayoutManager(activity));
        binding.rcv.setAdapter(optionAdapter);
        Log.d("voteLivestream", "initView: 1231");
        binding.btnConfirm.setCardBackgroundColor(activity.getResources().getColor(R.color.main_color_new));
        binding.tvConfirm.setOnClickListener(view -> {
            Log.d("voteLivestream", "initView: 123");
            onConfirmClick();

        });
        if (numberVote != 0) {
            binding.edtPredictNumber.setText(String.valueOf(numberVote));
        }
//        binding.edtPredictNumber.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {
//                String number = Objects.requireNonNull(binding.edtPredictNumber.getText()).toString();
//                boolean isSelected = false;
//                for (LivestreamVoteOptionModel item : voteModel.getVoteDtos()) {
//                    if (item.isSelect()){
//                        isSelected = true;
//                        break;
//                    }
//                }
//                if (TextUtils.isEmpty(number) || !isSelected) {
//                    binding.btnConfirm.setCardBackgroundColor(Color.parseColor("#565656"));
//                } else {
//                    binding.btnConfirm.setCardBackgroundColor(activity.getResources().getColor(R.color.main_color_new));
//                }
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable editable) {
//
//            }
//        });
    }

    private void onConfirmClick() {
        boolean isSelected = false;
        long voteOptionId = 0;
        for (LivestreamVoteOptionModel item : voteModel.getVoteDtos()) {
            if (item.isSelect()) {
                isSelected = true;
                voteOptionId = item.getId();
                break;
            }
        }
        if (!isSelected) {
            ToastUtilsV2.makeText(activity, R.string.select_vote_alert);
        } else if (TextUtils.isEmpty(Objects.requireNonNull(binding.edtPredictNumber.getText()).toString())) {
            ToastUtils.makeText(activity, R.string.enter_predict_number_alert);
        } else {
            HomeApi.getInstance().applyVote(
                    livestream.getId(),
                    voteModel.getId(),
                    voteOptionId,
                    isVoted ? 1 : 0,
                    10,
                    new HttpCallBack() {
                        @Override
                        public void onSuccess(String data) throws Exception {
                            try {
                                Gson gson = new Gson();
                                VoteResultResponse response = gson.fromJson(data, VoteResultResponse.class);
                                if (response.isData()) {
                                    ToastUtils.makeText(activity, R.string.vote_success);
                                    isVoted = true;
                                    numberVote = Long.parseLong(binding.edtPredictNumber.getText().toString().trim());
                                    if (activity instanceof LivestreamFutureActivity) {
                                        ((LivestreamFutureActivity) activity).isVoted = true;
                                        ((LivestreamFutureActivity) activity).numberVote = numberVote;
                                    }
                                    if (activity instanceof LivestreamDetailActivity) {
                                        ((LivestreamDetailActivity) activity).isVoted = true;
                                        ((LivestreamDetailActivity) activity).numberVote = numberVote;
                                    }
                                } else {
                                    ToastUtils.makeText(activity, R.string.error_message_default);
                                }
                            } catch (Exception e) {
                                e.printStackTrace();
                            }
                            dismiss();
                        }

                        @Override
                        public void onFailure(String message) {
                            ToastUtils.makeText(activity, R.string.error_message_default);
                            dismiss();
                        }
                    });
        }
    }
}
