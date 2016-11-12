package com.blundell.hangovercures.details;

import com.blundell.hangovercures.Cure;
import com.blundell.hangovercures.Rating;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.verify;

public class DetailsUseCaseTest {

    private static final Cure.Id CURE_ID = new Cure.Id(String.valueOf(123));
    private static final Cure ANY_CURE = new Cure(CURE_ID, "title", "desc", Rating.from(5));
    private static final int NEW_RATING = 1;

    @Mock
    private DetailsRepository mockRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    public void saveRating_usesRepo() throws Exception {
        DetailsUseCase useCase = new DetailsUseCase(mockRepository);

        useCase.saveCureRating(ANY_CURE, NEW_RATING);

        verify(mockRepository).saveCureRating(CURE_ID, NEW_RATING);
    }

    @Test
    public void getRating_usesRepo() throws Exception {
        DetailsUseCase useCase = new DetailsUseCase(mockRepository);

        useCase.retrieveCureRating(ANY_CURE);

        verify(mockRepository).retrieveCureRating(CURE_ID);
    }
}
