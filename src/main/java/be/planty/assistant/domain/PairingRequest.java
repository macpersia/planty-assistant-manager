package be.planty.assistant.domain;

import java.io.Serializable;
import java.time.ZonedDateTime;
import javax.persistence.*;
import org.hibernate.annotations.Cache;
import org.hibernate.annotations.CacheConcurrencyStrategy;

/**
 * A PairingRequest.
 */
@Entity
@Table(name = "pairing_request")
@Cache(usage = CacheConcurrencyStrategy.READ_WRITE)
public class PairingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "sequenceGenerator")
    @SequenceGenerator(name = "sequenceGenerator")
    @Column(name = "id")
    private Long id;

    @Column(name = "name")
    private String name;

    @Column(name = "verification_code")
    private String verificationCode;

    @Column(name = "request_time")
    private ZonedDateTime requestTime;

    @Column(name = "accepted")
    private Boolean accepted;

    @Column(name = "session_id")
    private String sessionId;

    @Column(name = "public_key")
    private String publicKey;

    @Column(name = "login")
    private String login;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public PairingRequest id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return this.name;
    }

    public PairingRequest name(String name) {
        this.setName(name);
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationCode() {
        return this.verificationCode;
    }

    public PairingRequest verificationCode(String verificationCode) {
        this.setVerificationCode(verificationCode);
        return this;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public ZonedDateTime getRequestTime() {
        return this.requestTime;
    }

    public PairingRequest requestTime(ZonedDateTime requestTime) {
        this.setRequestTime(requestTime);
        return this;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Boolean getAccepted() {
        return this.accepted;
    }

    public PairingRequest accepted(Boolean accepted) {
        this.setAccepted(accepted);
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getSessionId() {
        return this.sessionId;
    }

    public PairingRequest sessionId(String sessionId) {
        this.setSessionId(sessionId);
        return this;
    }

    public void setSessionId(String sessionId) {
        this.sessionId = sessionId;
    }

    public String getPublicKey() {
        return this.publicKey;
    }

    public PairingRequest publicKey(String publicKey) {
        this.setPublicKey(publicKey);
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }

    public String getLogin() {
        return this.login;
    }

    public PairingRequest login(String login) {
        this.setLogin(login);
        return this;
    }

    public void setLogin(String login) {
        this.login = login;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof PairingRequest)) {
            return false;
        }
        return id != null && id.equals(((PairingRequest) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PairingRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", verificationCode='" + getVerificationCode() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            ", accepted='" + getAccepted() + "'" +
            ", sessionId='" + getSessionId() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            ", login='" + getLogin() + "'" +
            "}";
    }
}
