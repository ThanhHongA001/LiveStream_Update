package com.example.livestream_update.Ringme.LiveStream.adapter;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.recyclerview.widget.RecyclerView;

import com.bumptech.glide.Glide;
import com.vtm.R;
import com.vtm.ringme.livestream.holder.LivestreamFunctionHolder;
import com.vtm.ringme.livestream.socket.ListenerV2;
import com.vtm.ringme.values.Constants;

import java.util.List;

import io.reactivex.annotations.NonNull;

public class LivestreamFunctionAdapter extends RecyclerView.Adapter<LivestreamFunctionHolder> {

    private final List<Constants.ChatFunction> functions;
    private ListenerV2.ChatFunctionClickListener listener;

    public void setListener(ListenerV2.ChatFunctionClickListener listener) {
        this.listener = listener;
    }

    public LivestreamFunctionAdapter(List<Constants.ChatFunction> functions) {
        this.functions = functions;
    }

    @NonNull
    @androidx.annotation.NonNull
    @Override
    public LivestreamFunctionHolder onCreateViewHolder(@NonNull @androidx.annotation.NonNull ViewGroup viewGroup, int i) {
        return new LivestreamFunctionHolder(LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.rm_holder_livestream_function, viewGroup, false));
    }

    @Override
    public int getItemViewType(int position) {
        return functions.get(position).getFunctionType();
    }

    @Override
    public void onBindViewHolder(@NonNull @androidx.annotation.NonNull LivestreamFunctionHolder livestreamFunctionHolder, int position) {
        switch (getItemViewType(position)) {
            case Constants.ChatFunction.TYPE_LIKE:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_like).into(livestreamFunctionHolder.getImgFunction());
                break;
            case Constants.ChatFunction.TYPE_LIKED:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_like).into(livestreamFunctionHolder.getImgFunction());
                livestreamFunctionHolder.getImgLiked().setVisibility(View.VISIBLE);
                break;
            case Constants.ChatFunction.TYPE_DONATE:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_gift_livestream).into(livestreamFunctionHolder.getImgFunction());
                 livestreamFunctionHolder.getDivider().setVisibility(View.VISIBLE);
                break;
            case Constants.ChatFunction.TYPE_HEART:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_heart).into(livestreamFunctionHolder.getImgFunction());
                break;
            case Constants.ChatFunction.TYPE_WOW:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_wow).into(livestreamFunctionHolder.getImgFunction());
                break;
            case Constants.ChatFunction.TYPE_HAHA:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_haha).into(livestreamFunctionHolder.getImgFunction());
                break;
            case Constants.ChatFunction.TYPE_SAD:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_sad).into(livestreamFunctionHolder.getImgFunction());
                break;
            case Constants.ChatFunction.TYPE_ANGRY:
                Glide.with(livestreamFunctionHolder.itemView.getContext()).load(R.drawable.rm_ic_react_angry).into(livestreamFunctionHolder.getImgFunction());
                break;
        }

        if (functions.get(position).isChosen()) {
            livestreamFunctionHolder.getImgLiked().setVisibility(View.VISIBLE);
        } else {
            livestreamFunctionHolder.getImgLiked().setVisibility(View.GONE);
        }
        livestreamFunctionHolder.itemView.setOnClickListener(v -> listener.onFunctionClick(getItemViewType(position)));
    }

    @Override
    public int getItemCount() {
        return functions == null ? 0 : functions.size();
    }
}
