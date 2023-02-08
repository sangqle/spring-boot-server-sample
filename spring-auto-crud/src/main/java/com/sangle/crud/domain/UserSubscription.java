package com.sangle.crud.domain;

import java.io.Serializable;
import java.time.Instant;
import javax.persistence.*;

/**
 * A UserSubscription.
 */
@Entity
@Table(name = "user_subscription")
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscription implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "id")
    private Long id;

    @Column(name = "user_id")
    private Long userId;

    @Column(name = "start_date")
    private Instant startDate;

    @Column(name = "end_date")
    private Instant endDate;

    @Column(name = "pricing_id")
    private Long pricingId;

    // jhipster-needle-entity-add-field - JHipster will add fields here

    public Long getId() {
        return this.id;
    }

    public UserSubscription id(Long id) {
        this.setId(id);
        return this;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return this.userId;
    }

    public UserSubscription userId(Long userId) {
        this.setUserId(userId);
        return this;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getStartDate() {
        return this.startDate;
    }

    public UserSubscription startDate(Instant startDate) {
        this.setStartDate(startDate);
        return this;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return this.endDate;
    }

    public UserSubscription endDate(Instant endDate) {
        this.setEndDate(endDate);
        return this;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    public Long getPricingId() {
        return this.pricingId;
    }

    public UserSubscription pricingId(Long pricingId) {
        this.setPricingId(pricingId);
        return this;
    }

    public void setPricingId(Long pricingId) {
        this.pricingId = pricingId;
    }

    // jhipster-needle-entity-add-getters-setters - JHipster will add getters and setters here

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscription)) {
            return false;
        }
        return id != null && id.equals(((UserSubscription) o).id);
    }

    @Override
    public int hashCode() {
        // see https://vladmihalcea.com/how-to-implement-equals-and-hashcode-using-the-jpa-entity-identifier/
        return getClass().hashCode();
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "UserSubscription{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            ", pricingId=" + getPricingId() +
            "}";
    }
}
