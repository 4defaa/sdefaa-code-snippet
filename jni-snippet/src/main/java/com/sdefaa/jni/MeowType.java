package com.sdefaa.jni;

/**
 * @author Julius Wong
 * <p>
 * Meow的类型
 * <p>
 * @since 1.0.0
 */
public enum MeowType {
    /**
     * 喵喵叫
     */
    MEOW("喵喵"),
    /**
     * 呼噜声
     */
    SNORE("呼噜"),
    /**
     * 振颤声
     */
    CHATTER("振颤"),
    /**
     * 嚎叫声
     */
    HOWL("嚎叫"),
    /**
     * 咆哮声
     */
    ROAR("咆哮"),
    /**
     * 嘶嘶声
     */
    HISS("嘶嘶"),
    /**
     * 无法解析声
     */
    NULL("NULL"),;
    private String content;

    MeowType(String content) {
        this.content = content;
    }

    public String getContent() {
        return content;
    }
}
