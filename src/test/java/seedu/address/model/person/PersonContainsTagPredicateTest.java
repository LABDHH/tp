package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_ENGLISH;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_FRIEND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;

import java.util.Collections;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;

import seedu.address.model.tag.Tag;

/**
 * Contains integration test (integration with Tag class) for NameContainsTagPredicate.
 */
public class PersonContainsTagPredicateTest {

    @Test
    public void equals() {
        Tag firstTag = new Tag(VALID_TAG_FRIEND);
        Tag secondTag = new Tag(VALID_TAG_HUSBAND);

        PersonContainsTagPredicate firstPredicate = new PersonContainsTagPredicate(
                Collections.singleton(firstTag));
        PersonContainsTagPredicate secondPredicate = new PersonContainsTagPredicate(
                Collections.singleton(secondTag));

        // same object -> returns true
        assertTrue(firstPredicate.equals(firstPredicate));

        // same values -> returns true
        PersonContainsTagPredicate firstPredicateCopy = new PersonContainsTagPredicate(
                Collections.singleton(firstTag));
        assertTrue(firstPredicate.equals(firstPredicateCopy));

        // different types -> returns false
        assertFalse(firstPredicate.equals(1));
        NameContainsKeywordsPredicate diffPredictType =
                new NameContainsKeywordsPredicate(List.of(VALID_TAG_FRIEND));
        assertFalse(firstPredicate.equals(diffPredictType));

        // null -> returns false
        assertFalse(firstPredicate.equals(null));

        // different predicate (of same type) -> returns false
        assertFalse(firstPredicate.equals(secondPredicate));
    }

    @Test
    public void test_nameContainsTag_returnsTrue() {
        Tag tag = new Tag(VALID_TAG_FRIEND);
        PersonContainsTagPredicate predicate = new PersonContainsTagPredicate(Collections.singleton(tag));

        // Person has only one tag
        Person personWithOneTag = new Person.PersonBuilder()
                .name("new")
                .tags(tag.tagName)
                .build();
        assertTrue(predicate.test(personWithOneTag));

        // Person has multiple tags
        Person personWithTwoTags = new Person.PersonBuilder()
                .name("new")
                .tags(tag.tagName, VALID_TAG_HUSBAND)
                .build();
        assertTrue(predicate.test(personWithTwoTags));
        Person otherPersonWithTwoTags = new Person.PersonBuilder()
                .name("new")
                .tags(VALID_TAG_HUSBAND, tag.tagName)
                .build();
        assertTrue(predicate.test(otherPersonWithTwoTags));
        Person personWithThreeTags = new Person.PersonBuilder()
                .name("new")
                .tags(VALID_TAG_HUSBAND, tag.tagName, VALID_TAG_ENGLISH)
                .build();
        assertTrue(predicate.test(personWithThreeTags));

        // Mixed-case tags
        Tag tagUppercase = new Tag(VALID_TAG_FRIEND.toUpperCase());
        PersonContainsTagPredicate predicateWithUpperCaseTag = new PersonContainsTagPredicate(
                Collections.singleton(tagUppercase));
        assertTrue(predicateWithUpperCaseTag.test(personWithOneTag));
        Person personWithLowercaseTag = new Person.PersonBuilder()
                .name("new")
                .tags(VALID_TAG_FRIEND.toLowerCase())
                .build();
        assertTrue(predicateWithUpperCaseTag.test(personWithLowercaseTag));

        Tag tagLowercase = new Tag(VALID_TAG_FRIEND.toLowerCase());
        PersonContainsTagPredicate predicateWithLowerCaseTag = new PersonContainsTagPredicate(
                Collections.singleton(tagLowercase));
        Person personWithUppercaseTag = new Person.PersonBuilder()
                .name("new")
                .tags(VALID_TAG_FRIEND.toLowerCase())
                .build();
        assertTrue(predicateWithLowerCaseTag.test(personWithUppercaseTag));
    }

    @Test
    public void test_nameDoesNotContainTag_returnsFalse() {
        Tag tag = new Tag(VALID_TAG_FRIEND);
        PersonContainsTagPredicate predicate = new PersonContainsTagPredicate(Collections.singleton(tag));

        // Zero tags
        assertFalse(predicate.test(new Person.PersonBuilder()
                .name("name")
                .tags()
                .build())
        );
        // While the default Person built with new PersonBuilder().build() already returns a person
        // without any tags, the additional method call to withTags() (with no parameters) is to make it
        // explicit that the Person to be built has no tags. Additionally, it means that this test method
        // implementation does not need to be updated even if PersonBuilder::new()'s implementation is
        // updated to have a different default set of tags.

        // Non-matching tag
        assertFalse(predicate.test(new Person.PersonBuilder().name("name").tags(VALID_TAG_HUSBAND).build()));

        // Tag matches name, email, and address, but does not match tags
        assertFalse(predicate.test(new Person.PersonBuilder()
                .name(VALID_TAG_FRIEND)
                .email(VALID_TAG_FRIEND + "@email.com")
                .address(VALID_TAG_FRIEND + "Street")
                .tags(VALID_TAG_HUSBAND).build())
        );
    }

    @Test
    public void toStringMethod() {
        Set<Tag> tags = Collections.singleton(new Tag(VALID_TAG_FRIEND));
        PersonContainsTagPredicate predicate = new PersonContainsTagPredicate(tags);

        String expected = PersonContainsTagPredicate.class.getCanonicalName() + "{tags=" + tags + "}";
        assertEquals(expected, predicate.toString());
    }
}
