package com.example.wrlpages.models.register

import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class RegisterDataModel(
    val email: String?,
    val password: String?,
) : Parcelable
