package be.planty.managers.assistant.web.rest;

import be.planty.managers.assistant.AssistantManagerApp;

import be.planty.managers.assistant.domain.PairingRequest;
import be.planty.managers.assistant.repository.PairingRequestRepository;
import be.planty.managers.assistant.service.PairingRequestService;
import be.planty.managers.assistant.service.dto.PairingRequestDTO;
import be.planty.managers.assistant.service.mapper.PairingRequestMapper;
import be.planty.managers.assistant.web.rest.errors.ExceptionTranslator;

import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.web.PageableHandlerMethodArgumentResolver;
import org.springframework.http.MediaType;
import org.springframework.http.converter.json.MappingJackson2HttpMessageConverter;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.time.Instant;
import java.time.ZonedDateTime;
import java.time.ZoneOffset;
import java.time.ZoneId;
import java.util.List;


import static be.planty.managers.assistant.web.rest.TestUtil.sameInstant;
import static be.planty.managers.assistant.web.rest.TestUtil.createFormattingConversionService;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

/**
 * Test class for the PairingRequestResource REST controller.
 *
 * @see PairingRequestResource
 */
@RunWith(SpringRunner.class)
@SpringBootTest(classes = AssistantManagerApp.class)
public class PairingRequestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERIFICATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final String DEFAULT_PUBLIC_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_KEY = "BBBBBBBBBB";

    @Autowired
    private PairingRequestRepository pairingRequestRepository;


    @Autowired
    private PairingRequestMapper pairingRequestMapper;
    

    @Autowired
    private PairingRequestService pairingRequestService;

    @Autowired
    private MappingJackson2HttpMessageConverter jacksonMessageConverter;

    @Autowired
    private PageableHandlerMethodArgumentResolver pageableArgumentResolver;

    @Autowired
    private ExceptionTranslator exceptionTranslator;

    private MockMvc restPairingRequestMockMvc;

    private PairingRequest pairingRequest;

    @Before
    public void setup() {
        MockitoAnnotations.initMocks(this);
        final PairingRequestResource pairingRequestResource = new PairingRequestResource(pairingRequestService);
        this.restPairingRequestMockMvc = MockMvcBuilders.standaloneSetup(pairingRequestResource)
            .setCustomArgumentResolvers(pageableArgumentResolver)
            .setControllerAdvice(exceptionTranslator)
            .setConversionService(createFormattingConversionService())
            .setMessageConverters(jacksonMessageConverter).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PairingRequest createEntity() {
        PairingRequest pairingRequest = new PairingRequest()
            .name(DEFAULT_NAME)
            .verificationCode(DEFAULT_VERIFICATION_CODE)
            .requestTime(DEFAULT_REQUEST_TIME)
            .accepted(DEFAULT_ACCEPTED)
            .publicKey(DEFAULT_PUBLIC_KEY);
        return pairingRequest;
    }

    @Before
    public void initTest() {
        pairingRequestRepository.deleteAll();
        pairingRequest = createEntity();
    }

    @Test
    public void createPairingRequest() throws Exception {
        int databaseSizeBeforeCreate = pairingRequestRepository.findAll().size();

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);
        restPairingRequestMockMvc.perform(post("/api/pairing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO)))
            .andExpect(status().isCreated());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(DEFAULT_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(DEFAULT_REQUEST_TIME);
        assertThat(testPairingRequest.isAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
    }

    @Test
    public void createPairingRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pairingRequestRepository.findAll().size();

        // Create the PairingRequest with an existing ID
        pairingRequest.setId("existing_id");
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // An entity with an existing ID cannot be created, so this API call must fail
        restPairingRequestMockMvc.perform(post("/api/pairing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    public void getAllPairingRequests() throws Exception {
        // Initialize the database
        pairingRequestRepository.save(pairingRequest);

        // Get all the pairingRequestList
        restPairingRequestMockMvc.perform(get("/api/pairing-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pairingRequest.getId())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].verificationCode").value(hasItem(DEFAULT_VERIFICATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(sameInstant(DEFAULT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].publicKey").value(hasItem(DEFAULT_PUBLIC_KEY.toString())));
    }
    

    @Test
    public void getPairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.save(pairingRequest);

        // Get the pairingRequest
        restPairingRequestMockMvc.perform(get("/api/pairing-requests/{id}", pairingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pairingRequest.getId()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.verificationCode").value(DEFAULT_VERIFICATION_CODE.toString()))
            .andExpect(jsonPath("$.requestTime").value(sameInstant(DEFAULT_REQUEST_TIME)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.publicKey").value(DEFAULT_PUBLIC_KEY.toString()));
    }
    @Test
    public void getNonExistingPairingRequest() throws Exception {
        // Get the pairingRequest
        restPairingRequestMockMvc.perform(get("/api/pairing-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    public void updatePairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.save(pairingRequest);

        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Update the pairingRequest
        PairingRequest updatedPairingRequest = pairingRequestRepository.findById(pairingRequest.getId()).get();
        updatedPairingRequest
            .name(UPDATED_NAME)
            .verificationCode(UPDATED_VERIFICATION_CODE)
            .requestTime(UPDATED_REQUEST_TIME)
            .accepted(UPDATED_ACCEPTED)
            .publicKey(UPDATED_PUBLIC_KEY);
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(updatedPairingRequest);

        restPairingRequestMockMvc.perform(put("/api/pairing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO)))
            .andExpect(status().isOk());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(UPDATED_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(UPDATED_REQUEST_TIME);
        assertThat(testPairingRequest.isAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
    }

    @Test
    public void updateNonExistingPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If the entity doesn't have an ID, it will be created instead of just being updated
        restPairingRequestMockMvc.perform(put("/api/pairing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    public void deletePairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.save(pairingRequest);

        int databaseSizeBeforeDelete = pairingRequestRepository.findAll().size();

        // Get the pairingRequest
        restPairingRequestMockMvc.perform(delete("/api/pairing-requests/{id}", pairingRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    public void equalsVerifier() throws Exception {
        TestUtil.equalsVerifier(PairingRequest.class);
        PairingRequest pairingRequest1 = new PairingRequest();
        pairingRequest1.setId("id1");
        PairingRequest pairingRequest2 = new PairingRequest();
        pairingRequest2.setId(pairingRequest1.getId());
        assertThat(pairingRequest1).isEqualTo(pairingRequest2);
        pairingRequest2.setId("id2");
        assertThat(pairingRequest1).isNotEqualTo(pairingRequest2);
        pairingRequest1.setId(null);
        assertThat(pairingRequest1).isNotEqualTo(pairingRequest2);
    }

    @Test
    public void dtoEqualsVerifier() throws Exception {
        TestUtil.equalsVerifier(PairingRequestDTO.class);
        PairingRequestDTO pairingRequestDTO1 = new PairingRequestDTO();
        pairingRequestDTO1.setId("id1");
        PairingRequestDTO pairingRequestDTO2 = new PairingRequestDTO();
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
        pairingRequestDTO2.setId(pairingRequestDTO1.getId());
        assertThat(pairingRequestDTO1).isEqualTo(pairingRequestDTO2);
        pairingRequestDTO2.setId("id2");
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
        pairingRequestDTO1.setId(null);
        assertThat(pairingRequestDTO1).isNotEqualTo(pairingRequestDTO2);
    }
}
