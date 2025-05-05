package Messenger.Server;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;

import static org.junit.jupiter.api.Assertions.*;

class ChatTest {
    private Chat chat;

    @BeforeEach
    void setUp() {
        String[] users = {"alice", "bob"};
        chat = new Chat(users, "alice", 1, new ArrayList<>(), false);
    }

    @Test
    void testChatCreation() {
        assertEquals("alice", chat.getNameChat());
        assertEquals("alice", chat.getNameCreator());
        assertEquals(1, chat.getChatId());
        assertTrue(chat.isParticipant("alice"));
        assertTrue(chat.isParticipant("bob"));
    }

    @Test
    void testRenameChatByCreator() {
        chat.renameChat("newName", "alice");
        assertEquals("newName", chat.getNameChat());
    }

    @Test
    void testRenameChatByNonCreator() {
        chat.renameChat("failName", "bob");
        assertNotEquals("failName", chat.getNameChat());
    }

    @Test
    void testAddUserByCreator() {
        chat.addUser(new String[]{"charlie"}, "alice");
        assertTrue(chat.isParticipant("charlie"));
    }

    @Test
    void testAddUserByNonCreator() {
        chat.addUser(new String[]{"charlie"}, "bob");
        assertFalse(chat.isParticipant("charlie"));
    }

    @Test
    void testRemoveUserByCreator() {
        chat.removeUser("bob", "alice");
        assertFalse(chat.isParticipant("bob"));
    }

    @Test
    void testRemoveSelfByUser() {
        chat.removeUser("bob", "bob");
        assertFalse(chat.isParticipant("bob"));
    }

    @Test
    void testRemoveUserWithoutPermission() {
        chat.removeUser("alice", "bob");
        assertTrue(chat.isParticipant("alice"));
    }

    @Test
    void testDeleteChatByCreator() {
        assertTrue(chat.deleteChat("alice"));
    }

    @Test
    void testDeleteChatByUser() {
        assertFalse(chat.deleteChat("bob"));
    }

    @Test
    void testNewMessageAdded() {
        Chat.Message message = new Chat.Message("Hello", "alice", false);
        chat.newMessage(message);
        assertEquals(1, chat.getMessages().size());
        assertEquals("Hello", chat.getMessages().get(0).getData());
    }
}
