package com.mohakchavan.applists.affirmations.data

import com.mohakchavan.applists.R
import com.mohakchavan.applists.affirmations.model.AffirmationModel

class DataSource {

    fun loadAffirmations(): List<AffirmationModel> {
        return listOf(
            AffirmationModel(R.string.affirmation1, R.drawable.image1),
            AffirmationModel(R.string.affirmation2, R.drawable.image2),
            AffirmationModel(R.string.affirmation3, R.drawable.image3),
            AffirmationModel(R.string.affirmation4, R.drawable.image4),
            AffirmationModel(R.string.affirmation5, R.drawable.image5),
            AffirmationModel(R.string.affirmation6, R.drawable.image6),
            AffirmationModel(R.string.affirmation7, R.drawable.image7),
            AffirmationModel(R.string.affirmation8, R.drawable.image8),
            AffirmationModel(R.string.affirmation9, R.drawable.image9),
            AffirmationModel(R.string.affirmation10, R.drawable.image10),
        )
    }

}