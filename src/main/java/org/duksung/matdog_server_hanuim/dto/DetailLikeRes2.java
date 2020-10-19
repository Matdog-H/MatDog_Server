package org.duksung.matdog_server_hanuim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DetailLikeRes2<T> {
    private int status;

    private String message;

    private T register;

    private int likeStatus;

    private T contactopen;

    public static <T> DetailLikeRes2<T> res(final int status, final String message){
        return res(status, message);
    }

    public static <T> DetailLikeRes2<T> res(final int status, final String message, final T t, final int likeStatus, final T t3){
        return DetailLikeRes2.<T>builder()
                .status(status)
                .message(message)
                .register(t)
                .likeStatus(likeStatus)
                .contactopen(t3)
                .build();
    }
}