package org.duksung.matdog_server_hanuim.utils;

//응답메세지
public class ResponseMessage {
    //예시
    public static final String READ_USER = "회원 정보 조회 성공";
    public static final String ALREADY_USER = "이미 존재하는 id입니다.";

    public static final String READ_REGISTER = "공고 조회 성공";
    public static final String READ_LIKE_REGISTER = "좋아요 공고 조회 성공";
    public static final String NOT_FOUND_USER = "회원을 찾을 수 없습니다.";
    public static final String NOT_FOUND_REGISTER = "공고를 찾을 수 없습니다.";
    public static final String CREATED_USER = "회원 가입 성공";
    public static final String UPDATE_USER = "회원 정보 수정 성공";
    public static final String UPDATE_REGISTER = "공고 정보 수정 성공";
    public static final String UPDATE_REGISTER_LOST = "실종 공고 수정 성공";
    public static final String UPDATE_REGISTER_SPOT = "목격 공고 수정 성공";
    public static final String UPDATE_REGISTER_LIKE = "목격 공고 수정 성공";
    public static final String DELETE_USER = "회원 탈퇴 성공";
    public static final String DELETE_REGISTER = "분양 공고 삭제 성공";
    public static final String CREATED_REGISTER = "분양 공고 등록 성공";
    public static final String CREATED_REGISTER_LIKE = "좋아요 공고 등록 성공";
    public static final String CREATED_REGISTER_LOST = "실종 공고 등록 성공";
    public static final String CREATED_REGISTER_SPOT = "목격 공고 등록 성공";

    public static final String CREATED_REGISTER_IMG = "공고 이미지 등록 성공";

    public static final String LIKE_CONTENT = "컨텐츠 좋아요/해제 성공";

    public static final String SEARCH_REGISTER = "분양 공고 검색 성공";

    public static final String INTERNAL_SERVER_ERROR = "서버 내부 에러";

    public static final String DB_ERROR = "데이터베이스 에러";


    public static final String LOGIN_SUCCESS = "로그인 성공";
    public static final String LOGIN_FAIL = "로그인 실패";

    public static final String WRONG_PASSWORD = "비밀번호 틀림";

    public static final String UNAUTHORIZED = "인증 실패";

}

