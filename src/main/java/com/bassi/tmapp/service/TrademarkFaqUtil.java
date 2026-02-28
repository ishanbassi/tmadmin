package com.bassi.tmapp.service;

import com.bassi.tmapp.domain.Trademark;
import com.bassi.tmapp.service.dto.FaqItem;
import java.util.ArrayList;
import java.util.List;

public class TrademarkFaqUtil {

    public static List<FaqItem> generate(Trademark tm) {
        List<FaqItem> faqs = new ArrayList<>();

        String label = tm.getName() != null && !tm.getName().isBlank() ? tm.getName() : "application number " + tm.getApplicationNo();

        String appNo = String.valueOf(tm.getApplicationNo());

        // 1. Status FAQ
        if (tm.getTrademarkStatus() != null) {
            faqs.add(
                new FaqItem(
                    "What is the status of trademark " + label + "?",
                    "The trademark application number " +
                    appNo +
                    " is currently marked as \"" +
                    tm.getTrademarkStatus() +
                    "\" in the official trademark registry."
                )
            );
        }

        // 2. Owner FAQ
        if (tm.getProprietorName() != null) {
            faqs.add(
                new FaqItem(
                    "Who is the owner of trademark " + label + "?",
                    "The trademark application number " + appNo + " is owned by " + tm.getProprietorName() + "."
                )
            );
        }

        // 3. Class FAQ
        if (tm.getTmClass() != null) {
            faqs.add(
                new FaqItem(
                    "Which class does trademark " + label + " belong to?",
                    "Trademark application number " +
                    appNo +
                    " is filed under Class " +
                    tm.getTmClass() +
                    ", which relates to the specified goods and services."
                )
            );
        }

        // 4. Agent FAQ
        if (tm.getAgentName() != null) {
            faqs.add(
                new FaqItem(
                    "Who is the trademark agent for application " + appNo + "?",
                    "The trademark agent associated with application number " + appNo + " is " + tm.getAgentName() + "."
                )
            );
        }

        // 5. Journal FAQ
        if (tm.getJournalNo() != null) {
            faqs.add(
                new FaqItem(
                    "Was trademark " + label + " published in the journal?",
                    "Yes, trademark application number " + appNo + " was published in Journal No. " + tm.getJournalNo() + "."
                )
            );
        }

        // 6. Details FAQ (SEO gold)
        if (tm.getDetails() != null) {
            faqs.add(
                new FaqItem(
                    "What is trademark " + label + "?",
                    "Trademark application number " + appNo + " relates to \"" + tm.getDetails() + "\"."
                )
            );
        }

        return faqs;
    }
}
