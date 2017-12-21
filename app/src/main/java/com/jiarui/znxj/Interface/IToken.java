package com.jiarui.znxj.Interface;

/**
 * 获取Token的回调接口
 * date 2017/3/8.
 */
public interface IToken {
    /**
     * 1: Token获取失败，网络错误
     * 0：Token获取成功
     */
    void success(int errtype);

}
