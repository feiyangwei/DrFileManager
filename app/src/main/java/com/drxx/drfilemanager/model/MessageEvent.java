package com.drxx.drfilemanager.model;

/**
 * 类描述：消息
 * 创建人：wfy
 * 创建时间：2018/11/22
 * 邮箱：cugb_feiyang@163.com
 */
public class MessageEvent {
    private String flag;
    private String result;
    private String content;

    public MessageEvent(String flag, String result) {
        this.flag = flag;
        this.result = result;
    }

    public MessageEvent(String flag, String result, String content) {
        this.flag = flag;
        this.result = result;
        this.content = content;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public String getFlag() {
        return flag;
    }

    public void setFlag(String flag) {
        this.flag = flag;
    }

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
