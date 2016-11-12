package com.blundell.hangovercures.archive;

import org.junit.Before;
import org.junit.Test;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import static org.mockito.Mockito.*;

public class ArchiveUseCaseTest {

    @Mock
    private ArchiveRepository mockRepository;

    @Before
    public void setUp() throws Exception {
        MockitoAnnotations.initMocks(this);

    }

    @Test(expected = ArchiveUseCase.UseCaseRequestFailedException.class)
    public void failingToSignIn_causesError() throws Exception {
        when(mockRepository.getUser()).thenReturn(new User.SignedOutUser());
        ArchiveUseCase useCase = new ArchiveUseCase(mockRepository);

        useCase.getCures();
    }

    @Test
    public void signedOut_attemptsToSignIn() throws Exception {
        when(mockRepository.getUser()).thenReturn(new User.SignedOutUser());
        ArchiveUseCase useCase = new ArchiveUseCase(mockRepository);

        try {
            useCase.getCures();
        } catch (ArchiveUseCase.UseCaseRequestFailedException ignore) {
            // don't care for this test
        }

        verify(mockRepository, atLeastOnce()).signInUser();
    }

    @Test
    public void signedIn_getsCures() throws Exception {
        when(mockRepository.getUser()).thenReturn(new User.SignedInUser("anyUserId"));
        ArchiveUseCase useCase = new ArchiveUseCase(mockRepository);

        useCase.getCures();

        verify(mockRepository).getCures();
    }
}
