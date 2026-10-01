package Messenger.Universal;

import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.io.Serializable;

import static org.junit.jupiter.api.Assertions.*;

class StaticFunctionsTest {

    static class Dummy implements Serializable {
        String name;
        int number;

        Dummy(String name, int number) {
            this.name = name;
            this.number = number;
        }

        @Override
        public boolean equals(Object obj) {
            if (this == obj) return true;
            if (!(obj instanceof Dummy d)) return false;
            return number == d.number && name.equals(d.name);
        }
    }

    @Test
    void testSerializeDeserialize() throws IOException, ClassNotFoundException {
        Dummy original = new Dummy("test", 42);
        String serialized = StaticFunctions.serializeToString(original);
        Object deserialized = StaticFunctions.deserializeFromString(serialized);
        assertEquals(original, deserialized);
    }

    @Test
    void testDeserializeInvalidString() {
        String invalidBase64 = "not a valid base64 string";
        assertThrows(IllegalArgumentException.class, () -> {
            StaticFunctions.deserializeFromString(invalidBase64);
        });
    }
}
