package br.com.caioogdev.shared.exception;

public class NaoEscontradoException extends RuntimeException{

    public NaoEscontradoException(String mensagem){
        super(mensagem);
    }
}
