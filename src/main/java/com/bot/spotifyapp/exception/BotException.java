package com.bot.spotifyapp.exception;

import com.bot.spotifyapp.to.ErrorResponseTO;
import com.bot.spotifyapp.type.BotExceptionType;
import com.bot.spotifyapp.util.Constants;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

@Getter@Setter@ToString
public class BotException extends RuntimeException {

    private BotExceptionType type;

    private String message;

    private ErrorResponseTO error;

    public BotException() {
        super();
    }

    public BotException(String mess) {
        super(mess);
        this.message = mess;
    }

    public BotException(String mess, BotExceptionType type) {
        super(mess);
        this.message = mess;
        this.type = type;
        setErrorIfError();
    }

    public BotException(String mess, BotExceptionType type, ErrorResponseTO error) {
        super(mess);
        this.message = mess;
        this.type = type;
        this.error = error;
    }

    private void setErrorIfError() {
        if (this.type == BotExceptionType.ERROR) {
            ErrorResponseTO customError = new ErrorResponseTO();
            customError.setStatus(Constants.STATUS_CODE_BAD_REQUEST);
            customError.setMessage(this.message);
            this.error = customError;
        }
    }
}
