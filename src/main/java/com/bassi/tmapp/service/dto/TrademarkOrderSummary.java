package com.bassi.tmapp.service.dto;

import java.math.BigDecimal;
import java.util.List;

public class TrademarkOrderSummary {

    private TrademarkDTO trademarkDTO;
    private List<OrderSummary> orderSummaries;

    public TrademarkDTO getTrademarkDTO() {
        return trademarkDTO;
    }

    public void setTrademarkDTO(TrademarkDTO trademarkDTO) {
        this.trademarkDTO = trademarkDTO;
    }

    public List<OrderSummary> getOrderSummaries() {
        return orderSummaries;
    }

    public void setOrderSummaries(List<OrderSummary> orderSummaries) {
        this.orderSummaries = orderSummaries;
    }

    public static class OrderSummary {

        private Integer tmClass;
        private BigDecimal fees;

        public Integer getTmClass() {
            return tmClass;
        }

        public void setTmClass(Integer tmClass) {
            this.tmClass = tmClass;
        }

        public BigDecimal getFees() {
            return fees;
        }

        public void setFees(BigDecimal fees) {
            this.fees = fees;
        }
    }

    @Override
    public String toString() {
        return "TrademarkOrderSummary [trademarkDTO=" + trademarkDTO + ", orderSummaries=" + orderSummaries + "]";
    }
}
