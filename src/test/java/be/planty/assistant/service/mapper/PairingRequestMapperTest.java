package be.planty.assistant.service.mapper;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PairingRequestMapperTest {

    private PairingRequestMapper pairingRequestMapper;

    @BeforeEach
    public void setUp() {
        pairingRequestMapper = new PairingRequestMapperImpl();
    }
}
