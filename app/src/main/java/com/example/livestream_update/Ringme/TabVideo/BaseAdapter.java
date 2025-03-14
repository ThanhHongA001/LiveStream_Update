package com.example.livestream_update.Ringme.TabVideo;

import android.view.LayoutInflater;
import android.view.View;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.DiffUtil;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;
import java.util.List;

public abstract class BaseAdapter extends RecyclerView.Adapter<BaseAdapter.ViewHolder> {

    protected static final int LOAD_MORE = -1;
    protected static final int END = -2;

    protected final AppCompatActivity activity;
    protected final ArrayList<ItemObject> itemObjects;
    protected final LayoutInflater layoutInflater;

    protected Boolean pause = false;

    protected OnItemListener onItemListener;// lắng nghe các sự kiện

    public BaseAdapter(AppCompatActivity activity) {
        this.activity = activity;
        this.itemObjects = new ArrayList<>();
        this.layoutInflater = LayoutInflater.from(activity);
    }

    public OnItemListener getOnItemListener() {
        return onItemListener;
    }

    public void setOnItemListener(OnItemListener onItemListener) {
        this.onItemListener = onItemListener;
    }

    /**
     * khôi phục lại các tiền trình tải ảnh
     */
    public void resumeRequests() {
        pause = false;
    }

    /**
     * dừng tiến trình load ảnh
     */
    public void pauseRequests() {
        pause = true;
    }

    /**
     * cập nhật dữ liệu mới nhât
     *
     * @param results dữ liệu mới
     */
    public final void updateData(ArrayList<ItemObject> results) {
        if (results == null || results.isEmpty()) return;
        DiffUtil.DiffResult diffResult = DiffUtil.calculateDiff(new DiffCallback(results, itemObjects));
        itemObjects.clear();
        for (ItemObject result : results) {
            itemObjects.add(result.clone());
        }
        diffResult.dispatchUpdatesTo(this);
    }

    @Override
    public int getItemViewType(int position) {
        ItemObject itemObject = itemObjects.get(position);
        if (itemObject.getType() == Type.LOAD_MORE) return LOAD_MORE;
        return END;
    }

    @Override
    public int getItemCount() {
        return itemObjects.size();
    }

    /**
     * kiểm tra xem item mới có phải là item cũ không
     *
     * @param oldItem item cữ
     * @param newItem item mới
     * @return trạng thái của 2 loại item này
     */
    protected boolean areItemsTheSame(ItemObject oldItem, ItemObject newItem) {
        return oldItem.getId().equals(newItem.getId());
    }

    /**
     * kiểm tra dữ liệu mới và dữ liệu cũ có nội dung giống nhau không
     * hàm này được gọi khi @see BaseAdapterV2.areItemsTheSame trả về true
     *
     * @param oldItem dữ liệu cữ
     * @param newItem dữ liệu mới
     * @return trạng thái của dữ liệu cũ so với dữ liệu mới
     */
    protected boolean areContentsTheSame(ItemObject oldItem, ItemObject newItem) {
        return oldItem.toString().equals(newItem.toString());
    }

    /**
     * kiểm tra nhưng thành phần khác nhau của hai item
     * hàm này được gọi khí BaseAdapterV2.areContentsTheSame trả về false
     *
     * @param oldItem item cũ
     * @param newItem item mới
     * @return phần cần cập nhập giao diện
     */
    protected Object getChangePayload(ItemObject oldItem, ItemObject newItem) {
        return null;
    }

    /**
     * loại item
     */
    public enum Type {
        NORMAL,
        BANNER,
        LOAD_MORE,
        EMPTY,
        END
    }

    /**
     * lăng nghe các sự kiện clieck
     */
    public interface OnItemListener {
    }

    /**
     * clone dữ liệu giữa các object
     */
    public interface Clone {
        Clone clone();
    }

    public static class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        public ViewHolder(View itemView) {
            super(itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
        }
    }

    /**
     * object trung gian
     */
    public static class ItemObject implements Clone {

        private String id;
        private Clone info;
        private Type type = Type.NORMAL;

        public String getId() {
            if(id == null) return "";
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public Clone getInfo() {
            return info;
        }

        public void setInfo(Clone info) {
            this.info = info;
        }

        public Type getType() {
            return type;
        }

        public void setType(Type type) {
            this.type = type;
        }

        @Override
        public ItemObject clone() {
            try {
                ItemObject itemObject = new ItemObject();
                if (info == null)
                    info = new DefaultClone();

                itemObject.info = info.clone();
                itemObject.id = id;
                itemObject.type = type;
                return itemObject;
            } catch (Exception e) {
                return this;
            }
        }

        @Override
        public String toString() {
            return "ItemObject{" +
                    "id='" + id + '\'' +
                    ", info=" + info +
                    ", type=" + type +
                    '}';
        }

    }

    /**
     * object mặc định
     */
    public static class DefaultClone implements Clone {

        @Override
        public Clone clone() {
            return null;
        }
    }

    /**
     * cập nhật nội dung item
     */
    private class DiffCallback extends DiffUtil.Callback {

        private final List<ItemObject> oldVideoModels;
        private final List<ItemObject> newVideoModels;

        DiffCallback(List<ItemObject> newVideoModels, List<ItemObject> oldVideoModels) {
            this.newVideoModels = newVideoModels;
            this.oldVideoModels = oldVideoModels;
        }

        @Override
        public int getOldListSize() {
            return oldVideoModels.size();
        }

        @Override
        public int getNewListSize() {
            return newVideoModels.size();
        }

        @Override
        public boolean areItemsTheSame(int oldItemPosition, int newItemPosition) {
            try {
                ItemObject oldItem = oldVideoModels.get(oldItemPosition);
                ItemObject newItem = newVideoModels.get(newItemPosition);
                return BaseAdapter.this.areItemsTheSame(oldItem, newItem);
            } catch (Exception e) {
                return false;
            }
        }

        @Override
        public boolean areContentsTheSame(int oldItemPosition, int newItemPosition) {
            try {
                ItemObject oldItem = oldVideoModels.get(oldItemPosition);
                ItemObject newItem = newVideoModels.get(newItemPosition);
                return BaseAdapter.this.areContentsTheSame(oldItem, newItem);
            } catch (Exception e) {
                return false;
            }
        }

        @Nullable
        @Override
        public Object getChangePayload(int oldItemPosition, int newItemPosition) {
            try {
                ItemObject oldItem = oldVideoModels.get(oldItemPosition);
                ItemObject newItem = newVideoModels.get(newItemPosition);
                return BaseAdapter.this.getChangePayload(oldItem, newItem);
            } catch (Exception e) {
                return null;
            }
        }
    }

}
