package seedu.address.model.person;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.commands.CommandTestUtil.VALID_JOBPOSITION_BOB;
import static seedu.address.logic.commands.CommandTestUtil.VALID_TAG_HUSBAND;
import static seedu.address.testutil.Assert.assertThrows;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.ALICE;
import static seedu.address.testutil.TypicalPersonsWithAnniversaries.BOB;

import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;

import seedu.address.model.person.exceptions.DuplicatePersonException;
import seedu.address.model.person.exceptions.EmployeeNotFoundException;
import seedu.address.testutil.EmployeeBuilder;

public class UniqueEmployeeListTest {

    private final UniqueEmployeeList uniqueEmployeeList = new UniqueEmployeeList();

    @Test
    public void contains_nullEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.contains(null));
    }

    @Test
    public void contains_employeeNotInList_returnsFalse() {
        assertFalse(uniqueEmployeeList.contains(ALICE));
    }

    @Test
    public void contains_employeeInList_returnsTrue() {
        uniqueEmployeeList.add(ALICE);
        assertTrue(uniqueEmployeeList.contains(ALICE));
    }

    @Test
    public void contains_employeeWithSameIdentityFieldsInList_returnsTrue() {
        uniqueEmployeeList.add(ALICE);
        Employee editedAlice = new EmployeeBuilder(ALICE).withJobPosition(VALID_JOBPOSITION_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        assertTrue(uniqueEmployeeList.contains(editedAlice));
    }

    @Test
    public void add_nullEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.add(null));
    }

    @Test
    public void add_duplicateEmployee_throwsDuplicatePersonException() {
        uniqueEmployeeList.add(ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueEmployeeList.add(ALICE));
    }

    @Test
    public void setEmployee_nullTargetEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.setPerson(null, ALICE));
    }

    @Test
    public void setEmployee_nullEditedEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.setPerson(ALICE, null));
    }

    @Test
    public void setEmployee_targetEmployeeNotInList_throwsEmployeeNotFoundException() {
        assertThrows(EmployeeNotFoundException.class, () -> uniqueEmployeeList.setPerson(ALICE, ALICE));
    }

    @Test
    public void setEmployee_editedEmployeeIsSameEmployee_success() {
        uniqueEmployeeList.add(ALICE);
        uniqueEmployeeList.setPerson(ALICE, ALICE);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        expectedUniqueEmployeeList.add(ALICE);
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployee_editedEmployeeHasSameIdentity_success() {
        uniqueEmployeeList.add(ALICE);
        Employee editedAlice = new EmployeeBuilder(ALICE).withJobPosition(VALID_JOBPOSITION_BOB)
                .withTags(VALID_TAG_HUSBAND)
                .build();
        uniqueEmployeeList.setPerson(ALICE, editedAlice);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        expectedUniqueEmployeeList.add(editedAlice);
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployee_editedEmployeeHasDifferentIdentity_success() {
        uniqueEmployeeList.add(ALICE);
        uniqueEmployeeList.setPerson(ALICE, BOB);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        expectedUniqueEmployeeList.add(BOB);
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployee_editedEmployeeHasNonUniqueIdentity_throwsDuplicatePersonException() {
        uniqueEmployeeList.add(ALICE);
        uniqueEmployeeList.add(BOB);
        assertThrows(DuplicatePersonException.class, () -> uniqueEmployeeList.setPerson(ALICE, BOB));
    }

    @Test
    public void remove_nullEmployee_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.remove(null));
    }

    @Test
    public void remove_employeeDoesNotExist_throwsEmployeeNotFoundException() {
        assertThrows(EmployeeNotFoundException.class, () -> uniqueEmployeeList.remove(ALICE));
    }

    @Test
    public void remove_existingEmployee_removesEmployee() {
        uniqueEmployeeList.add(ALICE);
        uniqueEmployeeList.remove(ALICE);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployees_nullUniqueEmployeeList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.setPersons((UniqueEmployeeList) null));
    }

    @Test
    public void setEmployees_uniqueEmployeeList_replacesOwnListWithProvidedUniqueEmployeeList() {
        uniqueEmployeeList.add(ALICE);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        expectedUniqueEmployeeList.add(BOB);
        uniqueEmployeeList.setPersons(expectedUniqueEmployeeList);
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployees_nullList_throwsNullPointerException() {
        assertThrows(NullPointerException.class, () -> uniqueEmployeeList.setPersons((List<Employee>) null));
    }

    @Test
    public void setEmployees_list_replacesOwnListWithProvidedList() {
        uniqueEmployeeList.add(ALICE);
        List<Employee> employeeList = Collections.singletonList(BOB);
        uniqueEmployeeList.setPersons(employeeList);
        UniqueEmployeeList expectedUniqueEmployeeList = new UniqueEmployeeList();
        expectedUniqueEmployeeList.add(BOB);
        assertEquals(expectedUniqueEmployeeList, uniqueEmployeeList);
    }

    @Test
    public void setEmployees_listWithDuplicateEmployees_throwsDuplicatePersonException() {
        List<Employee> listWithDuplicateEmployees = Arrays.asList(ALICE, ALICE);
        assertThrows(DuplicatePersonException.class, () -> uniqueEmployeeList.setPersons(listWithDuplicateEmployees));
    }

    @Test
    public void asUnmodifiableObservableList_modifyList_throwsUnsupportedOperationException() {
        assertThrows(UnsupportedOperationException.class, ()
            -> uniqueEmployeeList.asUnmodifiableObservableList().remove(0));
    }

    @Test
    public void toStringMethod() {
        assertEquals(uniqueEmployeeList.asUnmodifiableObservableList().toString(), uniqueEmployeeList.toString());
    }
}
