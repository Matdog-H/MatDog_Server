package org.duksung.matdog_server_hanuim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@Builder
@AllArgsConstructor
public class DetailLikeRes<T> {
    private int status;

    private String message;

    private T register;

    private T img;

    private int likeStatus;

    public static <T> DetailLikeRes<T> res(final int status, final String message){
        return res(status, message);
    }

    public static <T> DetailLikeRes<T> res(final int status, final String message, final T t, final T t2, final int likeStatus){
        return DetailLikeRes.<T>builder()
                .status(status)
                .message(message)
                .register(t)
                .img(t2)
                .likeStatus(likeStatus)
                .build();
    }
}
