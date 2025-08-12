package com.example.morpheus.service;

import com.example.morpheus.service.CloudButtonMapper.CloudLink;
import org.junit.jupiter.api.Test;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class CloudButtonMapperTest {

    @Test
    void googleProducesTwoLinks() {
        List<CloudLink> links = CloudButtonMapper.mapLinks("google", null, null);
        assertEquals(2, links.size());
        assertEquals("GCP – Security", links.get(0).label);
        assertTrue(links.get(0).href.contains("console.cloud.google.com"));
        assertEquals("GCP – Sustainability", links.get(1).label);
    }

    @Test
    void amazonProducesTwoLinksWithDefaultsWhenUnset() {
        List<CloudLink> links = CloudButtonMapper.mapLinks("amazon", "", null);
        assertEquals(2, links.size());
        assertEquals("AWS – Sustainability", links.get(0).label);
        assertTrue(links.get(0).href.contains("https://us-east-1.console.aws.amazon.com"));
        assertEquals("AWS – Security (Trusted Advisor)", links.get(1).label);
        assertTrue(links.get(1).href.contains("region=ap-southeast-1"));
    }

    @Test
    void amazonUsesProvidedRegions() {
        List<CloudLink> links = CloudButtonMapper.mapLinks("amazon", "eu-west-1", "ap-south-1");
        assertTrue(links.get(0).href.contains("https://ap-south-1.console.aws.amazon.com"));
        assertTrue(links.get(1).href.contains("https://eu-west-1.console.aws.amazon.com"));
        assertTrue(links.get(1).href.contains("region=eu-west-1"));
    }

    @Test
    void azureProducesTwoLinks() {
        List<CloudLink> links = CloudButtonMapper.mapLinks("azure", null, null);
        assertEquals(2, links.size());
        assertEquals("Azure – Security (Advisor)", links.get(0).label);
        assertEquals("Azure – Sustainability", links.get(1).label);
    }

    @Test
    void unsupportedReturnsEmpty() {
        assertTrue(CloudButtonMapper.mapLinks("vmware", null, null).isEmpty());
        assertTrue(CloudButtonMapper.mapLinks(null, null, null).isEmpty());
    }
}