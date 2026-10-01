package Messenger.Message;

import java.io.Serial;
import java.io.Serializable;

public record UserMessage(TypeUserMessage type, String data, String userName, int chatId) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
