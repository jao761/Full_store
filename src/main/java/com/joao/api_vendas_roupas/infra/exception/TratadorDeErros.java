package com.joao.api_vendas_roupas.infra.exception;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.time.LocalDateTime;
import java.util.Locale;

@ControllerAdvice
public class TratadorDeErros  {

    @ExceptionHandler(RegraDeNegocioException.class)
    public ResponseEntity<DadosDetaLhamentoErro> tratarErroRegraDeNegocio(RegraDeNegocioException ex) {
        return ResponseEntity.badRequest().body(new DadosDetaLhamentoErro(
                ex.getMessage(), LocalDateTime.now(), HttpStatus.BAD_REQUEST
        ));
    }


    private record DadosDetaLhamentoErro(
            String getMessage,
            LocalDateTime localDateTime,
            HttpStatus http
    ){}
    
}
