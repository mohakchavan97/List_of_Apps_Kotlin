package com.mohakchavan.applists.affirmations.model

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes

data class AffirmationModel(
    @StringRes val stringResId: Int,
    @DrawableRes val imageResId: Int
)
