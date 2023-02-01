package com.sdefaa.jni;

/**
 * @author Julius Wong
 * <p>
 * 猫叫声解析器
 * <p>
 * @since 1.0.0
 */
public class MeowParser {

    static {
        System.load(MeowParser.class.getClassLoader().getResource("meowParserJNI.dll").getPath());
    }

    /**
     * 解析器初始化方法
     */
    public static native void init();

    /**
     * 解析方法
     *
     * @param parserContent 解析内容实体
     * @return 解析结果实体
     */
    public static native ParserResult parse(ParserContent parserContent) throws ParserException;

    /**
     * 解析器销毁方法
     */
    public static native void destroy();

}
