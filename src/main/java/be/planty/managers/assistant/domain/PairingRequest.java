package be.planty.managers.assistant.domain;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.mapping.Document;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Objects;

/**
 * A PairingRequest.
 */
@Document(collection = "pairing_request")
public class PairingRequest implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    private String id;

    @Field("name")
    private String name;

    @Field("verification_code")
    private String verificationCode;

    @Field("request_time")
    private ZonedDateTime requestTime;

    @Field("accepted")
    private Boolean accepted;

    @Field("public_key")
    private String publicKey;

    // jhipster-needle-entity-add-field - JHipster will add fields here, do not remove
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public PairingRequest name(String name) {
        this.name = name;
        return this;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getVerificationCode() {
        return verificationCode;
    }

    public PairingRequest verificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
        return this;
    }

    public void setVerificationCode(String verificationCode) {
        this.verificationCode = verificationCode;
    }

    public ZonedDateTime getRequestTime() {
        return requestTime;
    }

    public PairingRequest requestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
        return this;
    }

    public void setRequestTime(ZonedDateTime requestTime) {
        this.requestTime = requestTime;
    }

    public Boolean isAccepted() {
        return accepted;
    }

    public PairingRequest accepted(Boolean accepted) {
        this.accepted = accepted;
        return this;
    }

    public void setAccepted(Boolean accepted) {
        this.accepted = accepted;
    }

    public String getPublicKey() {
        return publicKey;
    }

    public PairingRequest publicKey(String publicKey) {
        this.publicKey = publicKey;
        return this;
    }

    public void setPublicKey(String publicKey) {
        this.publicKey = publicKey;
    }
    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here, do not remove

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        PairingRequest pairingRequest = (PairingRequest) o;
        if (pairingRequest.getId() == null || getId() == null) {
            return false;
        }
        return Objects.equals(getId(), pairingRequest.getId());
    }

    @Override
    public int hashCode() {
        return Objects.hashCode(getId());
    }

    @Override
    public String toString() {
        return "PairingRequest{" +
            "id=" + getId() +
            ", name='" + getName() + "'" +
            ", verificationCode='" + getVerificationCode() + "'" +
            ", requestTime='" + getRequestTime() + "'" +
            ", accepted='" + isAccepted() + "'" +
            ", publicKey='" + getPublicKey() + "'" +
            "}";
    }
}
