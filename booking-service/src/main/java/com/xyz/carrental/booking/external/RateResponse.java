package com.xyz.carrental.booking.external;

import lombok.*;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class RateResponse {
    private String category;
    private Double ratePerDay;
}
