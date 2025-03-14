package com.example.livestream_update.Ringme.LiveStream.socket;

import com.vtm.ringme.livestream.model.Gift;
import com.vtm.ringme.livestream.model.Report;
import com.vtm.ringme.model.livestream.LivestreamModel;

public interface ListenerV2 {
    interface MainLivestreamClick {
        void onItemClick(LivestreamModel video);
    }

    interface ChatFunctionClickListener {
        void onFunctionClick(int functionType);
    }

    interface ReportListener {
        //todo testing, fix later
        void onReportClick(Report reportDetail);
    }

    interface DonateListener {
        void onDonateClick(Gift gift);
    }

    interface OpenGetCoinDialogListener {
        void onOpenGetCoinDialogClick();
    }

    interface GetCoinListener {
        void onGetCoinListener(String amount);
    }

    interface LoadMoreGift {
        void onLoadMoreGift(int page);
    }
}

