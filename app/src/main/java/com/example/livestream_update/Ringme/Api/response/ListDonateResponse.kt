package com.vtm.ringme.api.response

import com.vtm.ringme.livestream.apis.response.BaseResponse
import com.vtm.ringme.livestream.model.Donate
import com.google.gson.annotations.SerializedName
import java.io.Serializable
import java.util.ArrayList

class ListDonateResponse: BaseResponse() {
    @SerializedName("data")
    var data = ArrayList<Donate>()
}