package seedu.address.logic.parser;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static seedu.address.logic.Messages.MESSAGE_INVALID_COMMAND_FORMAT;
import static seedu.address.logic.Messages.MESSAGE_UNKNOWN_COMMAND;
import static seedu.address.logic.commands.CommandTestUtil.VALID_EMPLOYEE_ID_PREFIX_AMY;
import static seedu.address.logic.parser.CliSyntax.PREFIX_NAME;
import static seedu.address.logic.parser.CommandParserTestUtil.assertFieldEqualityFirst;
import static seedu.address.testutil.Assert.assertThrows;

import java.util.Arrays;
import java.util.List;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;

import seedu.address.logic.commands.AddEmployeeCommand;
import seedu.address.logic.commands.ClearCommand;
import seedu.address.logic.commands.DeleteCommand;
import seedu.address.logic.commands.EditCommand;
import seedu.address.logic.commands.ExitCommand;
import seedu.address.logic.commands.FindCommand;
import seedu.address.logic.commands.HelpCommand;
import seedu.address.logic.commands.ListCommand;
import seedu.address.logic.parser.exceptions.ParseException;
import seedu.address.model.person.Employee;
import seedu.address.model.person.EmployeeId;
import seedu.address.testutil.EditEmployeeDescriptorBuilder;
import seedu.address.testutil.EmployeeBuilder;
import seedu.address.testutil.EmployeeUtil;

public class AddressBookParserTest {

    private final AddressBookParser parser = new AddressBookParser();

    @Test
    public void parseCommand_add() throws Exception {
        Employee employee = EmployeeBuilder.defaultEmployee();
        AddEmployeeCommand command = (AddEmployeeCommand) parser.parseCommand(EmployeeUtil.getAddCommand(employee));
        assertFieldEqualityFirst(new AddEmployeeCommand(employee), command);
    }

    @Test
    public void parseCommand_clear() throws Exception {
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD) instanceof ClearCommand);
        assertTrue(parser.parseCommand(ClearCommand.COMMAND_WORD + " 3") instanceof ClearCommand);
    }

    @Test
    public void parseCommand_delete() throws Exception {
        DeleteCommand command = (DeleteCommand) parser.parseCommand(
                DeleteCommand.COMMAND_WORD + " " + EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_AMY));
        assertEquals(new DeleteCommand(EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_AMY)), command);
    }

    @Test
    public void parseCommand_edit() throws Exception {
        Employee employee = new EmployeeBuilder().build();
        EditCommand.EditEmployeeDescriptor descriptor = new EditEmployeeDescriptorBuilder(employee).build();
        EditCommand command = (EditCommand) parser.parseCommand(EditCommand.COMMAND_WORD + " "
                + VALID_EMPLOYEE_ID_PREFIX_AMY + " " + EmployeeUtil.getEditEmployeeDescriptorDetails(descriptor));
        assertFieldEqualityFirst(
                new EditCommand(EmployeeId.fromString(VALID_EMPLOYEE_ID_PREFIX_AMY), descriptor),
                command
        );
    }

    @Test
    public void parseCommand_exit() throws Exception {
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD) instanceof ExitCommand);
        assertTrue(parser.parseCommand(ExitCommand.COMMAND_WORD + " 3") instanceof ExitCommand);
    }

    //Check if it parses to a find command with correct predicates
    @Test
    public void parseCommand_find() throws Exception {
        List<String> keywords = Arrays.asList("foo", "bar", "baz");
        FindCommand command = (FindCommand) parser.parseCommand(
                FindCommand.COMMAND_WORD + " " + PREFIX_NAME
                        + keywords.stream().collect(Collectors.joining(" ")));

        // Create a Employee that should match the keywords
        Employee matchingEmployee = new EmployeeBuilder().withName("foo barson").build();
        Employee nonMatchingEmployee = new EmployeeBuilder().withName("unrelated name").build();

        // Check that the command’s predicate filters correctly
        Predicate<Employee> predicate = command.getPredicate();
        assertTrue(predicate.test(matchingEmployee));
        assertFalse(predicate.test(nonMatchingEmployee));
    }

    @Test
    public void parseCommand_help() throws Exception {
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD) instanceof HelpCommand);
        assertTrue(parser.parseCommand(HelpCommand.COMMAND_WORD + " 3") instanceof HelpCommand);
    }

    @Test
    public void parseCommand_list() throws Exception {
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD) instanceof ListCommand);
        assertTrue(parser.parseCommand(ListCommand.COMMAND_WORD + " 3") instanceof ListCommand);
    }

    @Test
    public void parseCommand_unrecognisedInput_throwsParseException() {
        assertThrows(ParseException.class, String.format(MESSAGE_INVALID_COMMAND_FORMAT, HelpCommand.MESSAGE_USAGE), ()
            -> parser.parseCommand(""));
    }

    @Test
    public void parseCommand_unknownCommand_throwsParseException() {
        assertThrows(ParseException.class, MESSAGE_UNKNOWN_COMMAND, ()
                -> parser.parseCommand("unknownCommand"));
    }
}
