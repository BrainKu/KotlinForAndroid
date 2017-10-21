package com.github.brainku.kotlinforandroid.constants;

import selfdriven.ku.cgiapt.CGI;
import selfdriven.ku.cgiapt.SimpleCGI;

/**
 * Created by brainku on 17/10/21.
 */

public class TestConstants {

    public static boolean DEBUG = false;
    private static final String A = "A_CGI";
    private static final String A_DEBUG = "A_DEBUG_CGI";
    private static final String B = "B_CGI";
    private static final String B_DEBUG = "B_DEBUG_CGI";
    private static final String C = "C_CGI";
    private static final String C_DEBUG = "C_DEBUG_CGI";

    static final String C_HOST = "C_HOST_CGI";
    static final String C_HOST_DEBUG = "C_HOST_DEBUG_CGI";

    @SimpleCGI({"C_HOST", "/CONTENT"})
    public static String simpleA = C_HOST + "/CONTENT";

    @CGI({A, A_DEBUG})
    public static String a = A;

    @CGI({B, B_DEBUG})
    public static String b;

    @CGI({C, C_DEBUG})
    public static String c;

    static {
        initEnv();
    }

    private static void initEnv() {
        CGIApi.initCGI(DEBUG);
    }
}
