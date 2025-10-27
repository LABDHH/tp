package seedu.address.storage;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.nio.file.Path;
import java.nio.file.Paths;

import org.junit.jupiter.api.Test;

import seedu.address.commons.util.JsonUtil;
import seedu.address.model.AddressBook;
import seedu.address.model.person.Person;
import seedu.address.testutil.TypicalPersons;

public class JsonSerializableAddressBookTest {

    private static final Path TEST_DATA_FOLDER = Paths.get("src", "test", "data", "JsonSerializableAddressBookTest");
    private static final Path TYPICAL_PERSONS_FILE = TEST_DATA_FOLDER.resolve("typicalPersonsAddressBook.json");
    private static final Path INVALID_PERSON_FILE = TEST_DATA_FOLDER.resolve("invalidPersonAddressBook.json");
    private static final Path DUPLICATE_PERSON_FILE = TEST_DATA_FOLDER.resolve("duplicatePersonAddressBook.json");

    @Test
    public void toModelType_typicalPersonsFile_success() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(TYPICAL_PERSONS_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBookFromFile = dataFromFile.toModelType();
        AddressBook typicalPersonsAddressBook = TypicalPersons.getTypicalAddressBook();
        assertEquals(addressBookFromFile, typicalPersonsAddressBook);
    }

    @Test
    public void toModelType_invalidPersonFile_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(INVALID_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();
        assertTrue(addressBook.getPersonList().isEmpty());
    }

    @Test
    public void toModelType_duplicatePersons_throwsIllegalValueException() throws Exception {
        JsonSerializableAddressBook dataFromFile = JsonUtil.readJsonFile(DUPLICATE_PERSON_FILE,
                JsonSerializableAddressBook.class).get();
        AddressBook addressBook = dataFromFile.toModelType();
        assertFalse(addressBook.getPersonList().isEmpty());
    }


    @Test
    public void toModelType_pairingsRoundTrip_success() throws Exception {
        Person alice = new Person.PersonBuilder()
                .name("Alice Pauline")
                .phone("94351253")
                .email("alice@example.com")
                .address("123, Jurong West Ave 6, #08-111")
                .tags("friends")
                .build();
        Person benson = new Person.PersonBuilder()
                .name("Benson Meier")
                .phone("98765432")
                .email("johnd@example.com")
                .address("311, Clementi Ave 2, #02-25")
                .tags("owesMoney", "friends").build();
        Person carl = new Person.PersonBuilder()
                .name("Carl Kurz")
                .phone("95352563")
                .email("heinz@example.com")
                .address("wall street")
                .build();

        alice.addPerson(benson);

        AddressBook source = new AddressBook();
        source.addPerson(alice);
        source.addPerson(benson);
        source.addPerson(carl);

        JsonSerializableAddressBook jsonAddressBook = new JsonSerializableAddressBook(source);
        AddressBook rebuilt = jsonAddressBook.toModelType();

        Person rebuiltAlice = rebuilt.getPersonList().stream()
            .filter(p -> p.isSamePerson(alice))
            .findFirst()
            .orElseThrow();
        Person rebuiltBenson = rebuilt.getPersonList().stream()
            .filter(p -> p.isSamePerson(benson))
            .findFirst()
            .orElseThrow();

        assertEquals(1, rebuiltAlice.getPairings().size());
        assertTrue(rebuiltAlice.getPairings().get(0).isSamePerson(benson));
        assertEquals(1, rebuiltBenson.getPairings().size());
        assertTrue(rebuiltBenson.getPairings().get(0).isSamePerson(alice));
    }

}
