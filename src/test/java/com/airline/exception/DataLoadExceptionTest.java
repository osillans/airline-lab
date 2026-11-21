package com.airline.exception;

import static org.junit.jupiter.api.Assertions.*;

import java.io.IOException;
import org.junit.jupiter.api.Test;

class DataLoadExceptionTest {

    @Test
    void keepsMessage() {
        DataLoadException exception = new DataLoadException("Bad data");

        assertEquals("Bad data", exception.getMessage());
        assertNull(exception.getCause());
    }

    @Test
    void keepsMessageAndCause() {
        IOException cause = new IOException("Disk error");

        DataLoadException exception =
                new DataLoadException("Cannot read", cause);

        assertEquals("Cannot read", exception.getMessage());
        assertSame(cause, exception.getCause());
    }

    @Test
    void isUncheckedException() {
        assertInstanceOf(RuntimeException.class,
                new DataLoadException("Bad data"));
    }
}