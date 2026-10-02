package Messenger.Message;

import Messenger.Server.Chat;

public record ServerMessage(TypeServerMessage type, String data, int chatId, Chat.Message message, Chat.ChatMessage chatMessage, Chat.ChatInfo chatInfo) {
}
