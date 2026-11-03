package org.route;


import lombok.Builder;
import lombok.NonNull;
import org.springframework.stereotype.Component;
@Builder
public record Customer(@NonNull String fullName,@NonNull String phone, String email) {}
