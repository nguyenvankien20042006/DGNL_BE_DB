package vn.rikkei.cinemabox.customer;

import java.time.LocalDateTime;

public record CustomerResponse(
        Long id,
        String fullName,
        String email,
        String phone,
        String memberLevel,
        LocalDateTime createdAt) {

    public static CustomerResponse from(Customer c) {
        return new CustomerResponse(
                c.getId(),
                c.getFullName(),
                c.getEmail(),
                c.getPhone(),
                c.getMemberLevel(),
                c.getCreatedAt()
        );
    }
}
