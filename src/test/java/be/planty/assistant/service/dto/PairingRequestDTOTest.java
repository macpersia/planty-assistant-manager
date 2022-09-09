package be.planty.assistant.service.dto;

import static org.assertj.core.api.Assertions.assertThat;

import be.planty.assistant.web.rest.TestUtil;
import org.junit.jupiter.api.Test;

class PairingRequestDTOTest {

    @Test
    void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PairingRequestDTO.class);
        PairingRequestDTO pairingRequestDTO1 = new PairingRequestDTO();
        pairingRequestDTO1.setId(1L);
        PairingRequestDTO pairingRequestDTO2 = new PairingRequestDTO();
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
        pairingRequestDTO2.setId(pairingRequestDTO1.getId());
        assertThat(pairingRequestDTO1).isEqualTo(pairingRequestDTO2);
        pairingRequestDTO2.setId(2L);
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
        pairingRequestDTO1.setId(null);
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
    }
}
