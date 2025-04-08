package com.management.pro.tools;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class Permissions {

    public static final String PERMISSION_ADD_PERMISSION = "PERMISSION_ADD";
    public static final String PERMISSION_PUT_PERMISSION = "PERMISSION_PUT";
    public static final String PERMISSION_DELETE_PERMISSION = "PERMISSION_DELETE";
    public static final String PERMISSION_GET_PERMISSION = "PERMISSION_GET";


    public static final String ROLE_ADD_PERMISSION = "ROLE_ADD";
    public static final String ROLE_PUT_PERMISSION = "ROLE_PUT";
    public static final String ROLE_DELETE_PERMISSION = "ROLE_DELETE";
    public static final String ROLE_GET_PERMISSION = "ROLE_GET";
}
