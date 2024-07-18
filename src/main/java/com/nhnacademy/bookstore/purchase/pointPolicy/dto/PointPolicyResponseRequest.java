package com.nhnacademy.bookstore.purchase.pointPolicy.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;

@Builder
public record PointPolicyResponseRequest(@NotBlank  String policyKey, @NotNull Integer policyValue) {
    }

