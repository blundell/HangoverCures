package com.blundell.hangovercures.details;

import com.blundell.hangovercures.Cure;

interface DetailsRepository {

    void saveCureRating(Cure.Id id, int rating);

    CureRating retrieveCureRating(Cure.Id id);
}
