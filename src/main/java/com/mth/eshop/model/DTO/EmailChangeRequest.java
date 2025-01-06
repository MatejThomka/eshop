package com.mth.eshop.model.DTO;

public record EmailChangeRequest(
        String oldEmail,
        String newEmail,
        String confirmEmail
) {}
