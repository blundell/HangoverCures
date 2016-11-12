package com.blundell.hangovercures.details;

import com.blundell.hangovercures.AdvertRequest;
import com.blundell.hangovercures.Cure;

class DetailsUseCase {

    private final DetailsRepository repository;

    public DetailsUseCase(DetailsRepository repository) {
        this.repository = repository;
    }

    public void saveCureRating(Cure cure, int newRating) {
        Cure.Id id = cure.getId();
        repository.saveCureRating(id, newRating);
    }

    public AdvertRequest createAdvertRequest() {
        return new AdvertRequest();
    }

    public CureRating retrieveCureRating(Cure cure) {
        Cure.Id  id = cure.getId();
        return repository.retrieveCureRating(id);
    }
}
