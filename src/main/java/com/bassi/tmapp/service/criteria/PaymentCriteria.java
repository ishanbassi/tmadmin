package com.bassi.tmapp.service.criteria;

import com.bassi.tmapp.domain.enumeration.PaymentPurpose;
import java.io.Serializable;
import java.util.Objects;
import java.util.Optional;
import org.springdoc.core.annotations.ParameterObject;
import tech.jhipster.service.Criteria;
import tech.jhipster.service.filter.*;

/**
 * Criteria class for the {@link com.bassi.tmapp.domain.Payment} entity. This class is used
 * in {@link com.bassi.tmapp.web.rest.PaymentResource} to receive all the possible filtering options from
 * the Http GET request parameters.
 * For example the following could be a valid request:
 * {@code /payments?id.greaterThan=5&attr1.contains=something&attr2.specified=false}
 * As Spring is unable to properly convert the types, unless specific {@link Filter} class are used, we need to use
 * fix type specific filters.
 */
@ParameterObject
@SuppressWarnings("common-java:DuplicatedBlocks")
public class PaymentCriteria implements Serializable, Criteria {

    /**
     * Class for filtering PaymentPurpose
     */
    public static class PaymentPurposeFilter extends Filter<PaymentPurpose> {

        public PaymentPurposeFilter() {}

        public PaymentPurposeFilter(PaymentPurposeFilter filter) {
            super(filter);
        }

        @Override
        public PaymentPurposeFilter copy() {
            return new PaymentPurposeFilter(this);
        }
    }

    private static final long serialVersionUID = 1L;

    private LongFilter id;

    private StringFilter gateway;

    private StringFilter gatewayPaymentId;

    private BigDecimalFilter amount;

    private StringFilter currency;

    private StringFilter status;

    private StringFilter paymentMethod;

    private ZonedDateTimeFilter createdDate;

    private BooleanFilter deleted;

    private ZonedDateTimeFilter modifiedDate;

    private StringFilter orderId;

    private StringFilter gatewayOrderId;

    private StringFilter failureReason;

    private PaymentPurposeFilter purpose;

    private LongFilter trademarkId;

    private LongFilter userProfileId;

    private Boolean distinct;

    public PaymentCriteria() {}

    public PaymentCriteria(PaymentCriteria other) {
        this.id = other.optionalId().map(LongFilter::copy).orElse(null);
        this.gateway = other.optionalGateway().map(StringFilter::copy).orElse(null);
        this.gatewayPaymentId = other.optionalGatewayPaymentId().map(StringFilter::copy).orElse(null);
        this.amount = other.optionalAmount().map(BigDecimalFilter::copy).orElse(null);
        this.currency = other.optionalCurrency().map(StringFilter::copy).orElse(null);
        this.status = other.optionalStatus().map(StringFilter::copy).orElse(null);
        this.paymentMethod = other.optionalPaymentMethod().map(StringFilter::copy).orElse(null);
        this.createdDate = other.optionalCreatedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.deleted = other.optionalDeleted().map(BooleanFilter::copy).orElse(null);
        this.modifiedDate = other.optionalModifiedDate().map(ZonedDateTimeFilter::copy).orElse(null);
        this.orderId = other.optionalOrderId().map(StringFilter::copy).orElse(null);
        this.gatewayOrderId = other.optionalGatewayOrderId().map(StringFilter::copy).orElse(null);
        this.failureReason = other.optionalFailureReason().map(StringFilter::copy).orElse(null);
        this.purpose = other.optionalPurpose().map(PaymentPurposeFilter::copy).orElse(null);
        this.trademarkId = other.optionalTrademarkId().map(LongFilter::copy).orElse(null);
        this.userProfileId = other.optionalUserProfileId().map(LongFilter::copy).orElse(null);
        this.distinct = other.distinct;
    }

    @Override
    public PaymentCriteria copy() {
        return new PaymentCriteria(this);
    }

    public LongFilter getId() {
        return id;
    }

    public Optional<LongFilter> optionalId() {
        return Optional.ofNullable(id);
    }

    public LongFilter id() {
        if (id == null) {
            setId(new LongFilter());
        }
        return id;
    }

    public void setId(LongFilter id) {
        this.id = id;
    }

    public StringFilter getGateway() {
        return gateway;
    }

    public Optional<StringFilter> optionalGateway() {
        return Optional.ofNullable(gateway);
    }

    public StringFilter gateway() {
        if (gateway == null) {
            setGateway(new StringFilter());
        }
        return gateway;
    }

    public void setGateway(StringFilter gateway) {
        this.gateway = gateway;
    }

    public StringFilter getGatewayPaymentId() {
        return gatewayPaymentId;
    }

    public Optional<StringFilter> optionalGatewayPaymentId() {
        return Optional.ofNullable(gatewayPaymentId);
    }

    public StringFilter gatewayPaymentId() {
        if (gatewayPaymentId == null) {
            setGatewayPaymentId(new StringFilter());
        }
        return gatewayPaymentId;
    }

    public void setGatewayPaymentId(StringFilter gatewayPaymentId) {
        this.gatewayPaymentId = gatewayPaymentId;
    }

    public BigDecimalFilter getAmount() {
        return amount;
    }

    public Optional<BigDecimalFilter> optionalAmount() {
        return Optional.ofNullable(amount);
    }

    public BigDecimalFilter amount() {
        if (amount == null) {
            setAmount(new BigDecimalFilter());
        }
        return amount;
    }

    public void setAmount(BigDecimalFilter amount) {
        this.amount = amount;
    }

    public StringFilter getCurrency() {
        return currency;
    }

    public Optional<StringFilter> optionalCurrency() {
        return Optional.ofNullable(currency);
    }

    public StringFilter currency() {
        if (currency == null) {
            setCurrency(new StringFilter());
        }
        return currency;
    }

    public void setCurrency(StringFilter currency) {
        this.currency = currency;
    }

    public StringFilter getStatus() {
        return status;
    }

    public Optional<StringFilter> optionalStatus() {
        return Optional.ofNullable(status);
    }

    public StringFilter status() {
        if (status == null) {
            setStatus(new StringFilter());
        }
        return status;
    }

    public void setStatus(StringFilter status) {
        this.status = status;
    }

    public StringFilter getPaymentMethod() {
        return paymentMethod;
    }

    public Optional<StringFilter> optionalPaymentMethod() {
        return Optional.ofNullable(paymentMethod);
    }

    public StringFilter paymentMethod() {
        if (paymentMethod == null) {
            setPaymentMethod(new StringFilter());
        }
        return paymentMethod;
    }

    public void setPaymentMethod(StringFilter paymentMethod) {
        this.paymentMethod = paymentMethod;
    }

    public ZonedDateTimeFilter getCreatedDate() {
        return createdDate;
    }

    public Optional<ZonedDateTimeFilter> optionalCreatedDate() {
        return Optional.ofNullable(createdDate);
    }

    public ZonedDateTimeFilter createdDate() {
        if (createdDate == null) {
            setCreatedDate(new ZonedDateTimeFilter());
        }
        return createdDate;
    }

    public void setCreatedDate(ZonedDateTimeFilter createdDate) {
        this.createdDate = createdDate;
    }

    public BooleanFilter getDeleted() {
        return deleted;
    }

    public Optional<BooleanFilter> optionalDeleted() {
        return Optional.ofNullable(deleted);
    }

    public BooleanFilter deleted() {
        if (deleted == null) {
            setDeleted(new BooleanFilter());
        }
        return deleted;
    }

    public void setDeleted(BooleanFilter deleted) {
        this.deleted = deleted;
    }

    public ZonedDateTimeFilter getModifiedDate() {
        return modifiedDate;
    }

    public Optional<ZonedDateTimeFilter> optionalModifiedDate() {
        return Optional.ofNullable(modifiedDate);
    }

    public ZonedDateTimeFilter modifiedDate() {
        if (modifiedDate == null) {
            setModifiedDate(new ZonedDateTimeFilter());
        }
        return modifiedDate;
    }

    public void setModifiedDate(ZonedDateTimeFilter modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public StringFilter getOrderId() {
        return orderId;
    }

    public Optional<StringFilter> optionalOrderId() {
        return Optional.ofNullable(orderId);
    }

    public StringFilter orderId() {
        if (orderId == null) {
            setOrderId(new StringFilter());
        }
        return orderId;
    }

    public void setOrderId(StringFilter orderId) {
        this.orderId = orderId;
    }

    public StringFilter getGatewayOrderId() {
        return gatewayOrderId;
    }

    public Optional<StringFilter> optionalGatewayOrderId() {
        return Optional.ofNullable(gatewayOrderId);
    }

    public StringFilter gatewayOrderId() {
        if (gatewayOrderId == null) {
            setGatewayOrderId(new StringFilter());
        }
        return gatewayOrderId;
    }

    public void setGatewayOrderId(StringFilter gatewayOrderId) {
        this.gatewayOrderId = gatewayOrderId;
    }

    public StringFilter getFailureReason() {
        return failureReason;
    }

    public Optional<StringFilter> optionalFailureReason() {
        return Optional.ofNullable(failureReason);
    }

    public StringFilter failureReason() {
        if (failureReason == null) {
            setFailureReason(new StringFilter());
        }
        return failureReason;
    }

    public void setFailureReason(StringFilter failureReason) {
        this.failureReason = failureReason;
    }

    public PaymentPurposeFilter getPurpose() {
        return purpose;
    }

    public Optional<PaymentPurposeFilter> optionalPurpose() {
        return Optional.ofNullable(purpose);
    }

    public PaymentPurposeFilter purpose() {
        if (purpose == null) {
            setPurpose(new PaymentPurposeFilter());
        }
        return purpose;
    }

    public void setPurpose(PaymentPurposeFilter purpose) {
        this.purpose = purpose;
    }

    public LongFilter getTrademarkId() {
        return trademarkId;
    }

    public Optional<LongFilter> optionalTrademarkId() {
        return Optional.ofNullable(trademarkId);
    }

    public LongFilter trademarkId() {
        if (trademarkId == null) {
            setTrademarkId(new LongFilter());
        }
        return trademarkId;
    }

    public void setTrademarkId(LongFilter trademarkId) {
        this.trademarkId = trademarkId;
    }

    public LongFilter getUserProfileId() {
        return userProfileId;
    }

    public Optional<LongFilter> optionalUserProfileId() {
        return Optional.ofNullable(userProfileId);
    }

    public LongFilter userProfileId() {
        if (userProfileId == null) {
            setUserProfileId(new LongFilter());
        }
        return userProfileId;
    }

    public void setUserProfileId(LongFilter userProfileId) {
        this.userProfileId = userProfileId;
    }

    public Boolean getDistinct() {
        return distinct;
    }

    public Optional<Boolean> optionalDistinct() {
        return Optional.ofNullable(distinct);
    }

    public Boolean distinct() {
        if (distinct == null) {
            setDistinct(true);
        }
        return distinct;
    }

    public void setDistinct(Boolean distinct) {
        this.distinct = distinct;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) {
            return true;
        }
        if (o == null || getClass() != o.getClass()) {
            return false;
        }
        final PaymentCriteria that = (PaymentCriteria) o;
        return (
            Objects.equals(id, that.id) &&
            Objects.equals(gateway, that.gateway) &&
            Objects.equals(gatewayPaymentId, that.gatewayPaymentId) &&
            Objects.equals(amount, that.amount) &&
            Objects.equals(currency, that.currency) &&
            Objects.equals(status, that.status) &&
            Objects.equals(paymentMethod, that.paymentMethod) &&
            Objects.equals(createdDate, that.createdDate) &&
            Objects.equals(deleted, that.deleted) &&
            Objects.equals(modifiedDate, that.modifiedDate) &&
            Objects.equals(orderId, that.orderId) &&
            Objects.equals(gatewayOrderId, that.gatewayOrderId) &&
            Objects.equals(failureReason, that.failureReason) &&
            Objects.equals(purpose, that.purpose) &&
            Objects.equals(trademarkId, that.trademarkId) &&
            Objects.equals(userProfileId, that.userProfileId) &&
            Objects.equals(distinct, that.distinct)
        );
    }

    @Override
    public int hashCode() {
        return Objects.hash(
            id,
            gateway,
            gatewayPaymentId,
            amount,
            currency,
            status,
            paymentMethod,
            createdDate,
            deleted,
            modifiedDate,
            orderId,
            gatewayOrderId,
            failureReason,
            purpose,
            trademarkId,
            userProfileId,
            distinct
        );
    }

    // prettier-ignore
    @Override
    public String toString() {
        return "PaymentCriteria{" +
            optionalId().map(f -> "id=" + f + ", ").orElse("") +
            optionalGateway().map(f -> "gateway=" + f + ", ").orElse("") +
            optionalGatewayPaymentId().map(f -> "gatewayPaymentId=" + f + ", ").orElse("") +
            optionalAmount().map(f -> "amount=" + f + ", ").orElse("") +
            optionalCurrency().map(f -> "currency=" + f + ", ").orElse("") +
            optionalStatus().map(f -> "status=" + f + ", ").orElse("") +
            optionalPaymentMethod().map(f -> "paymentMethod=" + f + ", ").orElse("") +
            optionalCreatedDate().map(f -> "createdDate=" + f + ", ").orElse("") +
            optionalDeleted().map(f -> "deleted=" + f + ", ").orElse("") +
            optionalModifiedDate().map(f -> "modifiedDate=" + f + ", ").orElse("") +
            optionalOrderId().map(f -> "orderId=" + f + ", ").orElse("") +
            optionalGatewayOrderId().map(f -> "gatewayOrderId=" + f + ", ").orElse("") +
            optionalFailureReason().map(f -> "failureReason=" + f + ", ").orElse("") +
            optionalPurpose().map(f -> "purpose=" + f + ", ").orElse("") +
            optionalTrademarkId().map(f -> "trademarkId=" + f + ", ").orElse("") +
            optionalUserProfileId().map(f -> "userProfileId=" + f + ", ").orElse("") +
            optionalDistinct().map(f -> "distinct=" + f + ", ").orElse("") +
        "}";
    }
}
