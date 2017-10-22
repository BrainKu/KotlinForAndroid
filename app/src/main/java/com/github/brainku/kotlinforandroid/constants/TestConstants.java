package com.github.brainku.kotlinforandroid.constants;

import android.os.Build;

import com.github.brainku.kotlinforandroid.BuildConfig;

import selfdriven.ku.cgiapt.BuildConfigFile;
import selfdriven.ku.cgiapt.CGI;
import selfdriven.ku.cgiapt.SimpleCGI;

/**
 * Created by brainku on 17/10/21.
 */

@BuildConfigFile(BuildConfig.class)
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


    static final String D_HOST = "D_HOST_CGI";
    static final String D_HOST_DEBUG = "D_HOST_DEBUG_CGI";

    static final String STAR_HOST = "http://start.com";
    static final String STAR_HOST_DEBUG = "http://debug.star";

    @SimpleCGI({C_HOST, "/CONTENT"})
    public static String simpleA = C_HOST + "/CONTENT";

    @SimpleCGI({D_HOST, "/HELLO_WORLD"})
    public static String helloD;

    @SimpleCGI({STAR_HOST, "/content"})
    public static String startvalue;

    @CGI({C, C_DEBUG})
    public static String c;

    static {
        initEnv();
    }

    private static void initEnv() {
        CGIInitHelper.initCGI(DEBUG);
    }
}
