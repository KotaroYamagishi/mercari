package com.mercari.form;

import javax.validation.constraints.NotBlank;
import javax.validation.constraints.Pattern;
import javax.validation.constraints.Size;

public class InsertUserForm {
    @NotBlank(message = "名前を入力してください")
    private String name;
    @Pattern(regexp = "^([\\w])+([\\w\\._-])*\\@([\\w])+([\\w\\._-])*\\.([a-zA-Z])+$", message = "メールアドレスの形式が不正です")
    private String email;
    @Size(min = 8, max = 16, message = "パスワードは８文字以上１６文字以内で設定してください")
    private String password;
    @NotBlank(message = "確認用パスワードを入力してください")
    private String checkpassword;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getCheckpassword() {
        return checkpassword;
    }

    public void setCheckpassword(String checkpassword) {
        this.checkpassword = checkpassword;
    }

    
}