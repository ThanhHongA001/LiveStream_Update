package com.example.livestream_update.Ringme.LiveStream.eventbus;

public class EventBusEvents {
    public static class UpdateCommentEvent{

    }

    public static class SwitchToChatEvent{

    }

    public static class UpdateBlockEvent{
        private boolean isBlocked;

        public UpdateBlockEvent(boolean isBlocked) {
            this.isBlocked = isBlocked;
        }

        public boolean isBlocked() {
            return isBlocked;
        }

        public void setBlocked(boolean blocked) {
            isBlocked = blocked;
        }
    }

    public static class UpdateLikeEvent{
        private boolean isLike;

        public UpdateLikeEvent(boolean isLike) {
            this.isLike = isLike;
        }

        public boolean isLike() {
            return isLike;
        }

        public void setLike(boolean like) {
            isLike = like;
        }
    }
}
