package com.sesame.framework.serial.define;


import lombok.Data;

@Data
public class SerialNumberRule {

    private String name;			  //中文名称
    private String code;			  //编码
    private boolean needParams;		  //是否需要输入参数
    private boolean needTime;		  //是否需要日期prefix
    private String timeFormat;		  //日期prefix
    private boolean needDelimiter;	  //是否需要分隔符
    private String delimiter;		  //分隔符
    private boolean needLetterPrefix; //是否需要字母前缀
    private String letterPrefix;	  //字母prefix
    private boolean needNumber;		  //是否需要数字累加
    private boolean fixedNumLen;	  //是否固定位数FIXED
    private int numLen;				  //数字位数
    private boolean needLetterSuffix; //是否需要后缀suffix
    private String letterSuffix;	  //后缀值

    public SerialNumberRule(String name, String code, boolean needParams,
                                 boolean needTime, String timeFormat, boolean needDelimiter, String delimiter,
                                 boolean needLetterPrefix, String letterPrefix, boolean needNumber,
                                 boolean fixedNumLen, int numLen, boolean needLetterSuffix, String letterSuffix) {
        this.name = name;
        this.code = code;
        this.needParams = needParams;
        this.needTime = needTime;
        this.timeFormat = timeFormat;
        this.needDelimiter = needDelimiter;
        this.delimiter = delimiter;
        this.needLetterPrefix = needLetterPrefix;
        this.letterPrefix = letterPrefix;
        this.needNumber = needNumber;
        this.fixedNumLen = fixedNumLen;
        this.numLen = numLen;
        this.needLetterSuffix = needLetterSuffix;
        this.letterSuffix = letterSuffix;
    }

}