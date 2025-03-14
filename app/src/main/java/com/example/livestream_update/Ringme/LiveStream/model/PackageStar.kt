package com.vtm.ringme.livestream.model

import com.google.gson.annotations.SerializedName
import java.io.Serializable

class PackageStar: Serializable {
    @SerializedName("id")
    var id = 0
    @SerializedName("code")
    var code = ""
    @SerializedName("price")
    var price = 0.0
    @SerializedName("image")
    var image = ""
    @SerializedName("value")
    var value = 0.0
    var isSelected = false
}