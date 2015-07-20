package com.coverity.security.sql;

import com.coverity.security.sql.test.MockConnection;
import org.testng.annotations.Test;

import java.sql.SQLException;

import static org.testng.Assert.assertEquals;
import static org.testng.Assert.assertTrue;

public class IdentifierEscaperTest {
    @Test
    public void testIdentifierEscaper() throws SQLException {
        final IdentifierEscaper identifierEscaper = new IdentifierEscaper(new MockConnection("`", "#@"));

        assertEquals(identifierEscaper.escapeIdentifier("foo"), "`foo`");
        assertEquals(identifierEscaper.escapeIdentifier(" \"bar\" or"), "` \"bar\" or`");
        assertEquals(identifierEscaper.escapeIdentifier("Twilight's Identifier"), "`Twilight's Identifier`");

        boolean exception = false;
        try {
            identifierEscaper.escapeIdentifier("foo`bar");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("`foobar");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("foobar`");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("`foobar`");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);
    }

    @Test
    public void testUnquotedIdentifierEscaper() throws SQLException {
        final IdentifierEscaper identifierEscaper = new IdentifierEscaper(new MockConnection(" ", "#@"));

        assertEquals(identifierEscaper.escapeIdentifier("foo"), "foo");
        assertEquals(identifierEscaper.escapeIdentifier("bar_#0912@"), "bar_#0912@");

        boolean exception = false;
        try {
            identifierEscaper.escapeIdentifier("foo bar");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("foobar!");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("foo-bar");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);

        exception = false;
        try {
            identifierEscaper.escapeIdentifier("foo'bar");
        } catch (Exception e) { exception = true; }
        assertTrue(exception);
    }
}