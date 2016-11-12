package com.blundell.hangovercures;

import org.junit.Test;

import static org.assertj.core.api.Assertions.assertThat;

public class AdvertRequestTest {

    @Test
    public void shouldServeMockAdvertsOnEmulators() throws Exception {
        AdvertRequest advertRequest = new AdvertRequest();

        assertThat(advertRequest.shouldUseEmulatorForTestRequests()).isTrue();
    }
}
