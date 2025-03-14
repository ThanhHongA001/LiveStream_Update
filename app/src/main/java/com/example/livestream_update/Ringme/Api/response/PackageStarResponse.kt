package com.vtm.ringme.api.response

import com.vtm.ringme.livestream.apis.response.BaseResponse
import com.vtm.ringme.livestream.model.PackageStar
import com.google.gson.annotations.SerializedName

class PackageStarResponse : BaseResponse() {
    @SerializedName("data")
    var data = ArrayList<PackageStar>()
}