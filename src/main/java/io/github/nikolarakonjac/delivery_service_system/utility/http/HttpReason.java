package io.github.nikolarakonjac.delivery_service_system.utility.http;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class HttpReason {

    //200
    public static final String OK = "Ok";
    //201
    public static final String CREATED = "Created";
    //204
    public static final String NO_CONTENT = "No Content";


    //400
    public static final String BAD_REQUEST = "Bad Request";
    //401
    public static final String UNAUTHORIZED = "Unauthorized";
    //403
    public static final String FORBIDDEN = "Forbidden";
    //404
    public static final String NOT_FOUND = "Not Found";
    //409
    public static final String CONFLICT = "Conflict";

}
