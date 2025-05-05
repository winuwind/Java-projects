package Messenger.Universal;

import Messenger.Message.ServerMessage;
import Messenger.Message.TypeServerMessage;
import Messenger.Message.TypeUserMessage;
import Messenger.Message.UserMessage;
import Messenger.Server.Chat;
import Messenger.Universal.StaticFunctions;
import org.junit.jupiter.api.Test;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

public class StaticFunctionsTest {

    @Test
    void testEscapeAndUnescapeXml() {
        String original = "<tag attr=\"value\">text & 'more'</tag>";
        String escaped = StaticFunctions.escapeXml(original);
        String unescaped = StaticFunctions.unescapeXml(escaped);

        assertNotEquals(original, escaped);
        assertEquals(original, unescaped);
    }

    @Test
    void testUserMessageToXMLAndBack() {
        UserMessage original = new UserMessage(TypeUserMessage.Simple, "Hello <World>", "Alice", 42);
        byte[] xmlBytes = StaticFunctions.toXML(original);
        byte[] withoutFirstFour = Arrays.copyOfRange(xmlBytes, 4, xmlBytes.length);
        UserMessage parsed = StaticFunctions.userMessageFromXML(withoutFirstFour);

        assertEquals(original.type(), parsed.type());
        assertEquals(original.data(), parsed.data());
        assertEquals(original.userName(), parsed.userName());
        assertEquals(original.chatId(), parsed.chatId());
    }

    @Test
    void testServerMessageSimpleToXMLAndBack() {
        Chat.Message message = new Chat.Message("Test <data>", "Bob", false);
        message.setTime(LocalDateTime.now());
        ServerMessage original = new ServerMessage(TypeServerMessage.Simple, null, 10, message, null, null);
        byte[] xmlBytes = StaticFunctions.toXML(original);
        byte[] withoutFirstFour = Arrays.copyOfRange(xmlBytes, 4, xmlBytes.length);
        ServerMessage parsed = StaticFunctions.serverMessageFromXml(withoutFirstFour);

        assertEquals(original.type(), parsed.type());
        assertEquals(original.chatId(), parsed.chatId());
        assertEquals(original.message().getData(), parsed.message().getData());
        assertEquals(original.message().getSender(), parsed.message().getSender());
        assertEquals(original.message().isImage(), parsed.message().isImage());
        assertEquals(original.message().getSendingTime(), parsed.message().getSendingTime());
    }

    @Test
    void testServerMessageHistoryToXMLAndBack() {
        List<Chat.Message> msgs = List.of(
                new Chat.Message("Hi", "User1", false),
                new Chat.Message("ImageData", "User2", true)
        );
        msgs.get(0).setTime(LocalDateTime.now());
        msgs.get(1).setTime(LocalDateTime.now());

        Chat.ChatMessage chatMessage = new Chat.ChatMessage("TestChat", 7, new ArrayList<>(msgs));
        ServerMessage original = new ServerMessage(TypeServerMessage.History, null, 7, null, chatMessage, null);

        byte[] xmlBytes = StaticFunctions.toXML(original);
        byte[] withoutFirstFour = Arrays.copyOfRange(xmlBytes, 4, xmlBytes.length);
        ServerMessage parsed = StaticFunctions.serverMessageFromXml(withoutFirstFour);

        assertEquals(original.type(), parsed.type());
        assertEquals(original.chatMessage().chatId(), parsed.chatMessage().chatId());
        assertEquals(original.chatMessage().nameChat(), parsed.chatMessage().nameChat());
        assertEquals(original.chatMessage().messages().size(), parsed.chatMessage().messages().size());
    }

    @Test
    void testServerMessageChatInfoToXMLAndBack() {
        ArrayList<String> users = new ArrayList<>(List.of("Alice", "Bob", "Charlie"));
        Chat.ChatInfo chatInfo = new Chat.ChatInfo("MyChat", "Alice", 3, users);
        ServerMessage original = new ServerMessage(TypeServerMessage.ChatInfo, null, 3, null, null, chatInfo);

        byte[] xmlBytes = StaticFunctions.toXML(original);
        byte[] withoutFirstFour = Arrays.copyOfRange(xmlBytes, 4, xmlBytes.length);
        ServerMessage parsed = StaticFunctions.serverMessageFromXml(withoutFirstFour);

        assertEquals(original.type(), parsed.type());
        assertEquals(original.chatInfo().getChatId(), parsed.chatInfo().getChatId());
        assertEquals(original.chatInfo().getChatName(), parsed.chatInfo().getChatName());
        assertEquals(original.chatInfo().getNameCreator(), parsed.chatInfo().getNameCreator());
        assertEquals(original.chatInfo().getUsers(), parsed.chatInfo().getUsers());
    }
}
