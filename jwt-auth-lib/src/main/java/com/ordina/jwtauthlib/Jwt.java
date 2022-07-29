package com.ordina.jwtauthlib;

import com.ordina.jwtauthlib.common.tokenizer.JwtDecoder;
import com.ordina.jwtauthlib.common.tokenizer.JwtGenerator;

public interface Jwt {

    static JwtDecoder.NeedToken decoder() {
        return new JwtDecoder().builder();
    }

    static JwtGenerator.NeedPrivateKey generator() {
        return new JwtGenerator().builder();
    }

}
