package com.example.morpheus.service;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Objects;

public class CloudButtonMapper {

    public static class CloudLink {
        public final String label;
        public final String href;
        public CloudLink(String label, String href) {
            this.label = label;
            this.href = href;
        }
    }

    public static List<CloudLink> mapLinks(String cloudTypeRaw, String awsSecurityRegionRaw, String awsSustainabilityRegionRaw) {
        String cloudType = cloudTypeRaw == null ? null : cloudTypeRaw.trim().toLowerCase();
        String awsSecurityRegion = (awsSecurityRegionRaw == null || awsSecurityRegionRaw.isBlank()) ? "ap-southeast-1" : awsSecurityRegionRaw.trim();
        String awsSustainabilityRegion = (awsSustainabilityRegionRaw == null || awsSustainabilityRegionRaw.isBlank()) ? "us-east-1" : awsSustainabilityRegionRaw.trim();

        if (cloudType == null) {
            return Collections.emptyList();
        }

        List<CloudLink> links = new ArrayList<>(2);
        switch (cloudType) {
            case "google":
                links.add(new CloudLink("GCP – Security", "https://console.cloud.google.com/active-assist/list/security"));
                links.add(new CloudLink("GCP – Sustainability", "https://console.cloud.google.com/carbon"));
                break;
            case "amazon":
                links.add(new CloudLink(
                    "AWS – Sustainability",
                    String.format("https://%s.console.aws.amazon.com/costmanagement/home#/customer-carbon-footprint-tool", awsSustainabilityRegion)
                ));
                links.add(new CloudLink(
                    "AWS – Security (Trusted Advisor)",
                    String.format("https://%s.console.aws.amazon.com/trustedadvisor/home?region=%s#/category/security", awsSecurityRegion, awsSecurityRegion)
                ));
                break;
            case "azure":
                links.add(new CloudLink("Azure – Security (Advisor)", "https://portal.azure.com/#view/Microsoft_Azure_Expert/AdvisorMenuBlade/~/Security"));
                links.add(new CloudLink("Azure – Sustainability", "https://portal.azure.com/#view/Microsoft_Azure_Sustainability/Emissions.MenuView/~/EmissionDetailsId"));
                break;
            default:
                // Not applicable
                break;
        }
        return links;
    }
}