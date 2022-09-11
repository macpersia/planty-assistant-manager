package be.planty.assistant.domain;

import static org.assertj.core.api.Assertions.assertThat;

import be.planty.assistant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PairingRequestTest {

    @Test
    void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PairingRequest.class);
        PairingRequest pairingRequest1 = new PairingRequest();
        pairingRequest1.setId(1L);
        PairingRequest pairingRequest2 = new PairingRequest();
        pairingRequest2.setId(pairingRequest1.getId());
        assertThat(pairingRequest1).isEqualTo(pairingRequest2);
        pairingRequest2.setId(2L);
        assertThat(pairingRequest1).isNotEqualTo(pairingRequest2);
        pairingRequest1.setId(null);
        assertThat(pairingRequest1).isNotEqualTo(pairingRequest2);
    }
}
