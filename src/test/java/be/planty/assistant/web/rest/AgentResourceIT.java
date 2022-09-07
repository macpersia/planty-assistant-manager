package be.planty.assistant.web.rest;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.hasItem;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import be.planty.assistant.IntegrationTest;
import be.planty.assistant.domain.Agent;
import be.planty.assistant.repository.AgentRepository;
import be.planty.assistant.service.dto.AgentDTO;
import be.planty.assistant.service.mapper.AgentMapper;
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
 * Integration tests for the {@link AgentResource} REST controller.
 */
@IntegrationTest
@AutoConfigureMockMvc
@WithMockUser
class AgentResourceIT {

    private static final String DEFAULT_NAME = "AAAAAAAAAA";
    private static final String UPDATED_NAME = "BBBBBBBBBB";

    private static final String DEFAULT_PUBLIC_KEY = "AAAAAAAAAA";
    private static final String UPDATED_PUBLIC_KEY = "BBBBBBBBBB";

    private static final String DEFAULT_SESSION_ID = "AAAAAAAAAA";
    private static final String UPDATED_SESSION_ID = "BBBBBBBBBB";

    private static final String ENTITY_API_URL = "/api/agents";
    private static final String ENTITY_API_URL_ID = ENTITY_API_URL + "/{id}";

    private static Random random = new Random();
    private static AtomicLong count = new AtomicLong(random.nextInt() + (2 * Integer.MAX_VALUE));

    @Autowired
    private AgentRepository agentRepository;

    @Autowired
    private AgentMapper agentMapper;

    @Autowired
    private EntityManager em;

    @Autowired
    private MockMvc restAgentMockMvc;

    private Agent agent;

    /**
     * Create an entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createEntity(EntityManager em) {
        Agent agent = new Agent().name(DEFAULT_NAME).publicKey(DEFAULT_PUBLIC_KEY).sessionId(DEFAULT_SESSION_ID);
        return agent;
    }

    /**
     * Create an updated entity for this test.
     *
     * This is a static method, as tests for other entities might also need it,
     * if they test an entity which requires the current entity.
     */
    public static Agent createUpdatedEntity(EntityManager em) {
        Agent agent = new Agent().name(UPDATED_NAME).publicKey(UPDATED_PUBLIC_KEY).sessionId(UPDATED_SESSION_ID);
        return agent;
    }

    @BeforeEach
    public void initTest() {
        agent = createEntity(em);
    }

    @Test
    @Transactional
    void createAgent() throws Exception {
        int databaseSizeBeforeCreate = agentRepository.findAll().size();
        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);
        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isCreated());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate + 1);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(DEFAULT_NAME);
        assertThat(testAgent.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
        assertThat(testAgent.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
    }

    @Test
    @Transactional
    void createAgentWithExistingId() throws Exception {
        // Create the Agent with an existing ID
        agent.setId(1L);
        AgentDTO agentDTO = agentMapper.toDto(agent);

        int databaseSizeBeforeCreate = agentRepository.findAll().size();

        // An entity with an existing ID cannot be created, so this API call must fail
        restAgentMockMvc
            .perform(post(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeCreate);
    }

    @Test
    @Transactional
    void getAllAgents() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get all the agentList
        restAgentMockMvc
            .perform(get(ENTITY_API_URL + "?sort=id,desc"))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.[*].id").value(hasItem(agent.getId().intValue())))
            .andExpect(jsonPath("$.[*].name").value(hasItem(DEFAULT_NAME)))
            .andExpect(jsonPath("$.[*].publicKey").value(hasItem(DEFAULT_PUBLIC_KEY)))
            .andExpect(jsonPath("$.[*].sessionId").value(hasItem(DEFAULT_SESSION_ID)));
    }

    @Test
    @Transactional
    void getAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        // Get the agent
        restAgentMockMvc
            .perform(get(ENTITY_API_URL_ID, agent.getId()))
            .andExpect(status().isOk())
            .andExpect(content().contentType(MediaType.APPLICATION_JSON_VALUE))
            .andExpect(jsonPath("$.id").value(agent.getId().intValue()))
            .andExpect(jsonPath("$.name").value(DEFAULT_NAME))
            .andExpect(jsonPath("$.publicKey").value(DEFAULT_PUBLIC_KEY))
            .andExpect(jsonPath("$.sessionId").value(DEFAULT_SESSION_ID));
    }

    @Test
    @Transactional
    void getNonExistingAgent() throws Exception {
        // Get the agent
        restAgentMockMvc.perform(get(ENTITY_API_URL_ID, Long.MAX_VALUE)).andExpect(status().isNotFound());
    }

    @Test
    @Transactional
    void putNewAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent
        Agent updatedAgent = agentRepository.findById(agent.getId()).get();
        // Disconnect from session so that the updates on updatedAgent are not directly saved in db
        em.detach(updatedAgent);
        updatedAgent.name(UPDATED_NAME).publicKey(UPDATED_PUBLIC_KEY).sessionId(UPDATED_SESSION_ID);
        AgentDTO agentDTO = agentMapper.toDto(updatedAgent);

        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentDTO))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgent.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testAgent.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
    }

    @Test
    @Transactional
    void putNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, agentDTO.getId())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                put(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType(MediaType.APPLICATION_JSON)
                    .content(TestUtil.convertObjectToJsonBytes(agentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void putWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(put(ENTITY_API_URL).contentType(MediaType.APPLICATION_JSON).content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void partialUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent.name(UPDATED_NAME);

        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgent.getPublicKey()).isEqualTo(DEFAULT_PUBLIC_KEY);
        assertThat(testAgent.getSessionId()).isEqualTo(DEFAULT_SESSION_ID);
    }

    @Test
    @Transactional
    void fullUpdateAgentWithPatch() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeUpdate = agentRepository.findAll().size();

        // Update the agent using partial update
        Agent partialUpdatedAgent = new Agent();
        partialUpdatedAgent.setId(agent.getId());

        partialUpdatedAgent.name(UPDATED_NAME).publicKey(UPDATED_PUBLIC_KEY).sessionId(UPDATED_SESSION_ID);

        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, partialUpdatedAgent.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(partialUpdatedAgent))
            )
            .andExpect(status().isOk());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
        Agent testAgent = agentList.get(agentList.size() - 1);
        assertThat(testAgent.getName()).isEqualTo(UPDATED_NAME);
        assertThat(testAgent.getPublicKey()).isEqualTo(UPDATED_PUBLIC_KEY);
        assertThat(testAgent.getSessionId()).isEqualTo(UPDATED_SESSION_ID);
    }

    @Test
    @Transactional
    void patchNonExistingAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If the entity doesn't have an ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, agentDTO.getId())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithIdMismatchAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(
                patch(ENTITY_API_URL_ID, count.incrementAndGet())
                    .contentType("application/merge-patch+json")
                    .content(TestUtil.convertObjectToJsonBytes(agentDTO))
            )
            .andExpect(status().isBadRequest());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void patchWithMissingIdPathParamAgent() throws Exception {
        int databaseSizeBeforeUpdate = agentRepository.findAll().size();
        agent.setId(count.incrementAndGet());

        // Create the Agent
        AgentDTO agentDTO = agentMapper.toDto(agent);

        // If url ID doesn't match entity ID, it will throw BadRequestAlertException
        restAgentMockMvc
            .perform(patch(ENTITY_API_URL).contentType("application/merge-patch+json").content(TestUtil.convertObjectToJsonBytes(agentDTO)))
            .andExpect(status().isMethodNotAllowed());

        // Validate the Agent in the database
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeUpdate);
    }

    @Test
    @Transactional
    void deleteAgent() throws Exception {
        // Initialize the database
        agentRepository.saveAndFlush(agent);

        int databaseSizeBeforeDelete = agentRepository.findAll().size();

        // Delete the agent
        restAgentMockMvc
            .perform(delete(ENTITY_API_URL_ID, agent.getId()).accept(MediaType.APPLICATION_JSON))
            .andExpect(status().isNoContent());

        // Validate the database contains one less item
        List<Agent> agentList = agentRepository.findAll();
        assertThat(agentList).hasSize(databaseSizeBeforeDelete - 1);
    }
}
