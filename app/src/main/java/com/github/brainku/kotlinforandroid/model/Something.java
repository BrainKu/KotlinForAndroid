package com.github.brainku.kotlinforandroid.model;

import selfdriven.ku.cgiapt.CCHost;
import selfdriven.ku.cgiapt.CGIAnnotation;

/**
 * Created by zhengxinwei@N3072 on 2017/10/19.
 */

@CGIAnnotation
public class Something {

    public static final String ABC = "abc";
    public static final String EDF = "edf";

    @CCHost({ABC, EDF})
    public static String CONTENT = "/test";

    @CCHost({"abdefgh", "CDEFGHT"})
    public static String HELLO = "/hello";

    public void test() {
    }
}
