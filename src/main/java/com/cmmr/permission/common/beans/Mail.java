package com.cmmr.permission.common.beans;

import lombok.*;

import java.util.Set;

@Builder
@Setter
@Getter
@ToString
@NoArgsConstructor
@AllArgsConstructor
public class Mail {

    //邮件主题
    private String subject;

    //邮件内容,可以是html格式
    private String message;

    //接受者
    private Set<String> receivers;
}
