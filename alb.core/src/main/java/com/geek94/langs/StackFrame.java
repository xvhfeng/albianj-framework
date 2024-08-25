package com.geek94.langs;


import com.geek94.utils.LangUtil;
import com.geek94.utils.StringsUtil;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.HashSet;
import java.util.Set;

@Data
@AllArgsConstructor
@ToString
@NoArgsConstructor
public class StackFrame {

    private static Set<String> ignoreStackFrameClasses = new HashSet<>();

    public void addIgnoreSTackFrameClass(String fullClassName){
        ignoreStackFrameClasses.add(fullClassName);
    }

    public void addIgnoreSTackFrameClass(Class<?> clazz){
        ignoreStackFrameClasses.add(clazz.getName());
    }


//    static  {
//        ServRouter._FilterStackFrameClasses.add(StackFrame.class.getName());
//    }
    private String filename;

    private String className;

    private String methodName;

    private int line;

    public String pickStackInfoWapper(){
        StackTraceElement s = LangUtil.findCalledStackFilter(1, ignoreStackFrameClasses);
        this.className = s.getClassName();
        this.line = s.getLineNumber();
        this.filename = s.getFileName();
        this.methodName = s.getMethodName();
        return StringsUtil.nonIdxFmt("{}:{} {}@{}",this.filename,this.line,this.className,this.methodName);
    }
}
