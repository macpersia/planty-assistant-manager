package be.planty.assistant.web.rest;

import static be.planty.assistant.web.rest.TestUtil.sameInstant;
import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.planty.assistant.IntegrationTest;
import be.planty.assistant.domain.PairingRequest;
import be.planty.assistant.repository.PairingRequestRepository;
import be.planty.assistant.service.dto.PairingRequestDTO;
import be.planty.assistant.service.mapper.PairingRequestMapper;
import java.time.Instant;
import java.time.ZoneId;
import java.time.ZoneOffset;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Random;
import java.util.concurrent.atomic.AtomicLong;
import javax.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.transaction.annotation.Transactional;

/**
 * Integration tests for the {@link PairingRequestResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class PairingRequestResourceIT {

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

    private static final String ENTITY_API_URL = "/api/pairing-requests";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private PairingRequestRepository pairingRequestRepository;

    @Autowired
    private PairingRequestMapper pairingRequestMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restPairingRequestMockMvc;

    private PairingRequest pairingRequest;

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

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static PairingRequest createUpdatedEntity(EntityManager em) {
        PairingRequest pairingRequest = new PairingRequest()
            .name(UPDATED_NAME)
            .verificationCode(UPDATED_VERIFICATION_CODE)
            .requestTime(UPDATED_REQUEST_TIME)
            .accepted(UPDATED_ACCEPTED)
            .sessionId(UPDATED_SESSION_ID)
            .publicKey(UPDATED_PUBLIC_KEY)
            .login(UPDATED_LOGIN);
        return pairingRequest;
    }

    @BeforeEach
    public void initTest() {
        pairingRequest = createEntity(em);
    }

    @Test
    @Transactional
    void createPairingRequest() throws Exception {
        int databaseSizeBeforeCreate = pairingRequestRepository.findAll().size();
        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);
        restPairingRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isCreated());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeCreate + 1);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(DEFAULT_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(DEFAULT_REQUEST_TIME);
        assertThat(testPairingRequest.getAccepted()).isEqualTo(DEFAULT_ACCEPTED);
        assertThat(testPairingRequest.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(DEFAULT_LOGIN);
    }

    @Test
    @Transactional
    void createPairingRequestWithExistingId() throws Exception {
        // Create the PairingRequest with an existing ID
        pairingRequest.setId(1L);
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        int databaseSizeBeforeCreate = pairingRequestRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restPairingRequestMockMvc
            .perform(
                post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllPairingRequests() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        // Get all the pairingRequestList
        restPairingRequestMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(pairingRequest.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].verificationCode").value(hasItem(DEFAULT_VERIFICATION_CODE)))
            .andExpect(jsonPath("$.[*].requestTime").value(hasItem(sameInstant(DEFAULT_REQUEST_TIME))))
            .andExpect(jsonPath("$.[*].accepted").value(hasItem(DEFAULT_ACCEPTED.booleanValue())))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID)))
            .andExpect(jsonPath("$.[*].publicKey").value(hasItem(DEFAULT_PUBLIC_KEY)))
            .andExpect(jsonPath("$.[*].login").value(hasItem(DEFAULT_LOGIN)));
    }

    @Test
    @Transactional
    void getPairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        // Get the pairingRequest
        restPairingRequestMockMvc
            .perform(get(ENTITY_API_URL_ID, pairingRequest.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(pairingRequest.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.verificationCode").value(DEFAULT_VERIFICATION_CODE))
            .andExpect(jsonPath("$.requestTime").value(sameInstant(DEFAULT_REQUEST_TIME)))
            .andExpect(jsonPath("$.accepted").value(DEFAULT_ACCEPTED.booleanValue()))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID))
            .andExpect(jsonPath("$.publicKey").value(DEFAULT_PUBLIC_KEY))
            .andExpect(jsonPath("$.login").value(DEFAULT_LOGIN));
    }

    @Test
    @Transactional
    void getNonExistingPairingRequest() throws Exception {
        // Get the pairingRequest
        restPairingRequestMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putExistingPairingRequest() throws Exception {
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

        restPairingRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pairingRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isOk());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(UPDATED_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(UPDATED_REQUEST_TIME);
        assertThat(testPairingRequest.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testPairingRequest.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void putNonExistingPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, pairingRequestDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdatePairingRequestWithPatch() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Update the pairingRequest using partial update
        PairingRequest partialUpdatedPairingRequest = new PairingRequest();
        partialUpdatedPairingRequest.setId(pairingRequest.getId());

        partialUpdatedPairingRequest
            .name(UPDATED_NAME)
            .accepted(UPDATED_ACCEPTED)
            .sessionId(UPDATED_SESSION_ID)
            .publicKey(UPDATED_PUBLIC_KEY)
            .login(UPDATED_LOGIN);

        restPairingRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPairingRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPairingRequest))
            )
            .andExpect(status().isOk());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(DEFAULT_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(DEFAULT_REQUEST_TIME);
        assertThat(testPairingRequest.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testPairingRequest.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void fullUpdatePairingRequestWithPatch() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();

        // Update the pairingRequest using partial update
        PairingRequest partialUpdatedPairingRequest = new PairingRequest();
        partialUpdatedPairingRequest.setId(pairingRequest.getId());

        partialUpdatedPairingRequest
            .name(UPDATED_NAME)
            .verificationCode(UPDATED_VERIFICATION_CODE)
            .requestTime(UPDATED_REQUEST_TIME)
            .accepted(UPDATED_ACCEPTED)
            .sessionId(UPDATED_SESSION_ID)
            .publicKey(UPDATED_PUBLIC_KEY)
            .login(UPDATED_LOGIN);

        restPairingRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedPairingRequest.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedPairingRequest))
            )
            .andExpect(status().isOk());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
        PairingRequest testPairingRequest = pairingRequestList.get(pairingRequestList.size() - 1);
        assertThat(testPairingRequest.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testPairingRequest.getVerificationCode()).isEqualTo(UPDATED_VERIFICATION_CODE);
        assertThat(testPairingRequest.getRequestTime()).isEqualTo(UPDATED_REQUEST_TIME);
        assertThat(testPairingRequest.getAccepted()).isEqualTo(UPDATED_ACCEPTED);
        assertThat(testPairingRequest.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
        assertThat(testPairingRequest.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testPairingRequest.getLogin()).isEqualTo(UPDATED_LOGIN);
    }

    @Test
    @Transactional
    void patchNonExistingPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, pairingRequestDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamPairingRequest() throws Exception {
        int databaseSizeBeforeUpdate = pairingRequestRepository.findAll().size();
        pairingRequest.setId(count.incrementAndGet());

        // Create the PairingRequest
        PairingRequestDTO pairingRequestDTO = pairingRequestMapper.toDto(pairingRequest);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restPairingRequestMockMvc
            .perform(
                patch(ENTITY_API_URL)
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(pairingRequestDTO))
            )
            .andExpect(status().isMethodNotAllowed());

        // Validate the PairingRequest in the database
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deletePairingRequest() throws Exception {
        // Initialize the database
        pairingRequestRepository.saveAndFlush(pairingRequest);

        int databaseSizeBeforeDelete = pairingRequestRepository.findAll().size();

        // Delete the pairingRequest
        restPairingRequestMockMvc
            .perform(delete(ENTITY_API_URL_ID, pairingRequest.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<PairingRequest> pairingRequestList = pairingRequestRepository.findAll();
        assertThat(pairingRequestList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
