package com.travelog.plan.dto;


import com.travelog.plan.exception.WrongTransportationException;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.util.Arrays;

@Getter
@RequiredArgsConstructor
public enum Transportation {
    CAR(1),
    WALK(2),
    PT(3);

    private final int transportation;

    public static Transportation getTransportation(int transportation) {
        try {
            return Arrays.stream(Transportation.values())
                    .filter(x -> x.getTransportation() == transportation)
                    .findFirst()
                    .orElseThrow(() -> new IllegalArgumentException("유효하지 않은 이동수단 : " + transportation));
        } catch (IllegalArgumentException e) {
            throw new WrongTransportationException("유효하지 않은 이동수단 : " + transportation);
        }
    }
}
