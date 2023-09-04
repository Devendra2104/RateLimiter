package com.devo_bhai.rateLim.service;

public enum PricingPlan {

    FREE(20),

    BASIC(40),

    PROFESSIONAL(100);

    private int bucketCapacity;

    private PricingPlan(int bucketCapacity) {
        this.bucketCapacity = bucketCapacity;
    }

    public int bucketCapacity() {
        return bucketCapacity;
    }

    static PricingPlan resolvePlanFromApiKey(String apiKey) {
        if (apiKey == null || apiKey.isEmpty()) {
            return FREE;

        } else if (apiKey.startsWith("PX001-")) {
            return PROFESSIONAL;

        } else if (apiKey.startsWith("BX001-")) {
            return BASIC;
        }
        return FREE;
    }
}
