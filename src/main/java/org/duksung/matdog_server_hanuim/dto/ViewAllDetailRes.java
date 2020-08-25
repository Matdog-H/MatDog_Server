package org.duksung.matdog_server_hanuim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class ViewAllDetailRes<T> {
    private int status;

    private String message;

    private T register;

    private T img;

    public static <T> ViewAllDetailRes<T> res(final int status, final String message){
        return res(status, message);
    }

    public static <T> ViewAllDetailRes<T> res(final int status, final String message, final T t, final T t2){
        return ViewAllDetailRes.<T>builder()
                .status(status)
                .message(message)
                .register(t)
                .img(t2)
                .build();
    }
}
