package com.maindark.livestream.exception;

import com.maindark.livestream.result.CodeMsg;

public class GlobalException extends RuntimeException{
    private CodeMsg cms;

    public GlobalException(CodeMsg cms) {
        super(cms.toString());
        this.cms = cms;

    }
    public CodeMsg  getCms(){
        return cms;
    }
}
