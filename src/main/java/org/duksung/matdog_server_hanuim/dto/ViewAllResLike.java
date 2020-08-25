package org.duksung.matdog_server_hanuim.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import org.duksung.matdog_server_hanuim.model.RegisterRes;

import java.util.List;

@Data
@Builder
@AllArgsConstructor
public class ViewAllResLike<T> {
    private int status;

    private String message;

    private String id;

    private T registerRes;

//    public static <T> ViewAllResLike<T> res(final int status, String readRegister, final String message, List<RegisterRes> registerResList){
//        return res(status, message);
//    }

    public static <T> ViewAllResLike<T> res(final int status, final String message){
        return res(status, message);
    }

    public static <T> ViewAllResLike<T> res(final int status, final String message, final String id, final T t){
        return ViewAllResLike.<T>builder()
                .status(status)
                .message(message)
                .id(id)
                .registerRes(t)
                .build();
    }

    //public static final viewAllRes FAIL_DEFAULT_RES = new viewAllRes(StatusCode.INTERNAL_SERVER_ERROR, ResponseMessage.INTERNAL_SERVER_ERROR);

    //public static final viewAllRes FAIL_AUTHORIZATION_RES = new viewAllRes(StatusCode.UNAUTHORIZED, ResponseMessage.UNAUTHORIZED);
}
