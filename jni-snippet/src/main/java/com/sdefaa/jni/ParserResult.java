package com.sdefaa.jni;

/**
 * @author Julius Wong
 * <p>
 * 解析结果实体
 * <p>
 * @since 1.0.0
 */
public class ParserResult {
    private String meaning;

    public String getMeaning() {
        return meaning;
    }

    public void setMeaning(String meaning) {
        this.meaning = meaning;
    }

    @Override
    public String toString() {
        return "ParserResult{" +
                "meaning='" + meaning + '\'' +
                '}';
    }
}
