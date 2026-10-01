package Messenger.Server;

import Messenger.Server.Chat.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {

    private Chat chat;

    @BeforeEach
    void setUp() {
        String[] users = {"Bob", "Charlie"};
        chat = new Chat(users, "Alice", 1, null, false);
    }

    @Test
    void testInitialState() {
        assertEquals("Bob", chat.getNameChat());
        assertEquals("Alice", chat.getNameCreator());
        assertEquals(1, chat.getChatId());
        assertTrue(chat.getUsers().contains("Alice"));
        assertFalse(chat.getUsers().contains("Bob"));
        assertTrue(chat.getUsers().contains("Charlie"));
        assertTrue(chat.getMessages().isEmpty());
    }

    @Test
    void testRenameChatByCreator() {
        chat.renameChat("NewChat", "Alice");
        assertEquals("NewChat", chat.getNameChat());
    }

    @Test
    void testRenameChatByNonCreator() {
        chat.renameChat("HackChat", "Bob");
        assertNotEquals("HackChat", chat.getNameChat());
    }

    @Test
    void testAddUserByCreator() {
        chat.addUser(new String[]{"Dave"}, "Alice");
        assertTrue(chat.getUsers().contains("Dave"));
    }

    @Test
    void testAddUserByNonCreator() {
        chat.addUser(new String[]{"Eve"}, "Bob");
        assertFalse(chat.getUsers().contains("Eve"));
    }

    @Test
    void testRemoveUserByCreator() {
        chat.removeUser("Bob", "Alice");
        assertFalse(chat.getUsers().contains("Bob"));
    }

    @Test
    void testRemoveUserBySelf() {
        chat.removeUser("Charlie", "Charlie");
        assertFalse(chat.getUsers().contains("Charlie"));
    }

    @Test
    void testRemoveUserByOtherUserFails() {
        chat.removeUser("Charlie", "Bob"); // Bob не создатель, и не Charlie
        assertTrue(chat.getUsers().contains("Charlie"));
    }

    @Test
    void testDeleteChatByCreator() {
        assertTrue(chat.deleteChat("Alice"));
    }

    @Test
    void testDeleteChatByNonCreator() {
        assertFalse(chat.deleteChat("Charlie"));
        assertFalse(chat.getUsers().contains("Charlie"));
    }

    @Test
    void testNewMessageLimit() {
        for (int i = 0; i < 105; i++) {
            chat.newMessage(new Message("msg" + i, "Alice", false));
        }
        assertEquals(100, chat.getMessages().size());
        assertEquals("msg5", chat.getMessages().get(0).getData());
    }

    @Test
    void testIsParticipant() {
        assertTrue(chat.isParticipant("Alice"));
        assertFalse(chat.isParticipant("Zoe"));
    }
}
