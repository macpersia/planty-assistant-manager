package be.planty.managers.assistant.web.rest;

import be.planty.managers.assistant.PlantyAssistantManagerApp;

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
import org.springframework.transaction.annotation.Transactional;
import org.springframework.validation.Validator;

import javax.persistence.EntityManager;
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
@SpringBootTest(classes = PlantyAssistantManagerApp.class)
public class PairingRequestResourceIntTest {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_VERIFICATION_CODE = "AAAAAAAAAA";
    private static final String UPDATED_VERIFICATION_CODE = "BBBBBBBBBB";

    private static final ZonedDateTime DEFAULT_REQUEST_TIME = ZonedDateTime.ofInstant(Instant.ofEpochMilli(0L), ZoneOffset.UTC);
    private static final ZonedDateTime UPDATED_REQUEST_TIME = ZonedDateTime.now(ZoneId.systemDefault()).withNano(0);

    private static final Boolean DEFAULT_ACCEPTED = false;
    private static final Boolean UPDATED_ACCEPTED = true;

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLIC_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_LOGIN = "AAAAAAAAAA";
    private static final String UPDATED_LOGIN = "BBBBBBBBBB";

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

    @Autowired
    private EntityManager em;

    @Autowired
    private Validator validator;

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
            .setMessageConverters(jacksonMessageConverter)
            .setValidator(validator).build();
    }

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PairingRequest createEntity(EntityManager em) {
        PairingRequest pairingRequest = new PairingRequest()
            .name(DEFAULT_NAME)
            .verificationCode(DEFAULT_VERIFICATION_CODE)
            .requestTime(DEFAULT_REQUEST_TIME)
            .accepted(DEFAULT_ACCEPTED)
            .sessionId(DEFAULT_SESSION_ID)
            .publicKey(DEFAULT_PUBLIC_KEY)
            .login(DEFAULT_LOGIN);
        return pairingRequest;
    }

    @Before
    public void initTest() {
        pairingRequest = createEntity(em);
    }

    @Test
    @Transactional
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
        assertThat(testPairingRequest.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    public void createPairingRequestWithExistingId() throws Exception {
        int databaseSizeBeforeCreate = pairingRequestRepository.findAll().size();

        // Create the PairingRequest with an existing ID
        pairingRequest.setId(1L);
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
    @Transactional
    public void getAllPairingRequests() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        // Get all the pairingRequestList
        restPairingRequestMockMvc.perform(get("/api/pairing-requests?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pairingRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME.toString())))
            .andExpect(jsonPath("$.[*].verificationCode").value(hasItem(DEFAULT_VERIFICATION_CODE.toString())))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(sameInstant(DEFAULT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID.toString())))
            .andExpect(jsonPath("$.[*].publicKey").value(hasItem(DEFAULT_PUBLIC_KEY.toString())))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN.toString())));
    }
    
    @Test
    @Transactional
    public void getPairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        // Get the pairingRequest
        restPairingRequestMockMvc.perform(get("/api/pairing-requests/{id}", pairingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_UTF8_VALUE))
            .andExpect(jsonPath("$.id").value(pairingRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME.toString()))
            .andExpect(jsonPath("$.verificationCode").value(DEFAULT_VERIFICATION_CODE.toString()))
            .andExpect(jsonPath("$.requestTime").value(sameInstant(DEFAULT_REQUEST_TIME)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID.toString()))
            .andExpect(jsonPath("$.publicKey").value(DEFAULT_PUBLIC_KEY.toString()))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN.toString()));
    }

    @Test
    @Transactional
    public void getNonExistingPairingRequest() throws Exception {
        // Get the pairingRequest
        restPairingRequestMockMvc.perform(get("/api/pairing-requests/{id}", Long.MAX_VALUE))
            .andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    public void updatePairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Update the pairingRequest
        PairingRequest updatedPairingRequest = pairingRequestRepository.findById(pairingRequest.getId()).get();
        // Disconnect from session so that the updates on updatedPairingRequest are not directly saved in db
        em.detach(updatedPairingRequest);
        updatedPairingRequest
            .name(UPDATED_NAME)
            .verificationCode(UPDATED_VERIFICATION_CODE)
            .requestTime(UPDATED_REQUEST_TIME)
            .accepted(UPDATED_ACCEPTED)
            .sessionId(UPDATED_SESSION_ID)
            .publicKey(UPDATED_PUBLIC_KEY)
            .login(UPDATED_LOGIN);
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
        assertThat(testPairingRequest.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    public void updateNonExistingPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc.perform(put("/api/pairing-requests")
            .contentType(TestUtil.APPLICATION_JSON_UTF8)
            .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO)))
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    public void deletePairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        int databaseSizeBeforeDelete = pairingRequestRepository.findAll().size();

        // Delete the pairingRequest
        restPairingRequestMockMvc.perform(delete("/api/pairing-requests/{id}", pairingRequest.getId())
            .accept(TestUtil.APPLICATION_JSON_UTF8))
            .andExpect(status().isOk());

        // Validate the database is empty
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }

    @Test
    @Transactional
    public void equalsVerifier() throws Exception {
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

    @Test
    @Transactional
    public void dtoEqualsVerifier() throws Exception {
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

    @Test
    @Transactional
    public void testEntityFromId() {
        assertThat(pairingRequestMapper.fromId(42L).getId()).isEqualTo(42);
        assertThat(pairingRequestMapper.fromId(null)).isNull();
    }
}
