package com.sangle.crud.service.dto;

import com.sangle.crud.domain.UserSubscription;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;

/**
 * A DTO for the {@link UserSubscription} entity.
 */
@SuppressWarnings("common-java:DuplicatedBlocks")
public class UserSubscriptionDTO implements Serializable {

    private Long id;

    private Long userId;

    private Instant startDate;

    private Instant endDate;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public Instant getStartDate() {
        return startDate;
    }

    public void setStartDate(Instant startDate) {
        this.startDate = startDate;
    }

    public Instant getEndDate() {
        return endDate;
    }

    public void setEndDate(Instant endDate) {
        this.endDate = endDate;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (!(o instanceof UserSubscriptionDTO)) {
            return false;
        }

        UserSubscriptionDTO userSubscriptionDTO = (UserSubscriptionDTO) o;
        if (this.id == null) {
            return false;
        }
        return Objects.equals(this.id, userSubscriptionDTO.id);
    }

    @Override
    public int hashCode() {
        return Objects.hash(this.id);
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "JSubscriptionDTO{" +
            "id=" + getId() +
            ", userId=" + getUserId() +
            ", startDate='" + getStartDate() + "'" +
            ", endDate='" + getEndDate() + "'" +
            "}";
    }
}
