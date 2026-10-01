package Messenger.Message;

import java.io.Serial;
import java.io.Serializable;

public record ServerMessage(TypeServerMessage type, String data, int chatId) implements Serializable {
    @Serial
    private static final long serialVersionUID = 1L;
}
