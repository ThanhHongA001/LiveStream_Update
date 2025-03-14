package com.vtm.ringme.api.response

import com.vtm.ringme.livestream.apis.response.BaseResponse
import com.google.gson.annotations.SerializedName

class OtpPackageStarResponse: BaseResponse() {
    @SerializedName("requestIdCp")
    var requestIdCp = ""
}