package com.geek94.langs;

import com.geek94.utils.SetUtil;
import com.geek94.utils.StringsUtil;

import java.util.Collection;

/**
 * argument check util
 */
public class ArgChk {

    public static void throwIfNull(Object para,String paraName){
        Throws.iaxIfNull(para,paraName);
    }

    public static void throwIfNotNull(Object para,String paraName){
        Throws.iaxIfNotNull(para,paraName);
    }

    public static void throwIfEmpty(String para,String paraName) {
        Throws.iaxIfTrue(StringsUtil.isEmpty(para),StringsUtil.nonIdxFmt("input:{} is empty.",paraName));
    }

    public static void throwIfNotEmpty(String para,String paraName) {
        Throws.iaxIfTrue(StringsUtil.isNotEmptyTrimmed(para),StringsUtil.nonIdxFmt("input:{} is empty.",paraName));
    }

    public static void throwIfBlank(String para,String paraName) {
        Throws.iaxIfTrue(StringsUtil.isBlank(para),StringsUtil.nonIdxFmt("input:{} is blank.",paraName));
    }

    public static void throwIfNotBlank(String para,String paraName) {
        Throws.iaxIfTrue(StringsUtil.isNotBlank(para),StringsUtil.nonIdxFmt("input:{} is blank.",paraName));
    }

    public static void throwIfEmpty(Collection<?> para,String paraName){
        Throws.iaxIfTrue(SetUtil.isEmpty(para),StringsUtil.nonIdxFmt("input:{} is empty.",paraName));
    }

    public static void throwIfNotEmpty(Collection<?> para,String paraName) {
        Throws.iaxIfTrue(SetUtil.isNotEmpty(para),StringsUtil.nonIdxFmt("input:{} is empty.",paraName));
    }

}
