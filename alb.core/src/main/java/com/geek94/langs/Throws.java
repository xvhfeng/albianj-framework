package com.geek94.langs;

import com.geek94.utils.LangUtil;
import com.geek94.utils.StringsUtil;

import java.io.IOException;
import java.io.PrintWriter;
import java.io.StringWriter;
import java.lang.reflect.InvocationTargetException;
import java.security.PrivilegedActionException;
import java.sql.SQLException;
import java.util.Collection;
import java.util.Iterator;

public class Throws {

    public static void ifTrue(boolean test, String fmt, Object... paras) {
        if (test) {
            throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    public static void ifFalse(boolean test, String fmt, Object... paras) {
        if (!test) {
            throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    public static void ifNull(Object test, String fmt, Object... paras) {
        if (null == test) {
            throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    public static void ifNonNull(Object test, String fmt, Object... paras) {
        if (null != test) {
            throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    public static void raise(String fmt, Object... paras) {
        throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras));
    }

    public static void raise(Throwable t) {
        throw new AblThrowable(t);
    }

    public static void raiseIfTrue(boolean cond, Throwable t) {
        if (cond) {
            throw new AblThrowable(t);
        }
    }

    public static void raise(Throwable t, String fmt, Object... paras) {
        throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras), t);
    }

    public static void raiseIfTrue(boolean test, Throwable t, String fmt, Object... paras) {
        if (test) {
            throw new AblThrowable(StringsUtil.nonIdxFmt(fmt, paras), t);
        }
    }

    /**
     * Shorthand for "if null, throw IllegalArgumentException"
     *
     * @throws IllegalArgumentException "null {name}" if o is null
     */
    public static void iaxIfNull(final Object test, final String paraName) {
        if (null == test) {
            String message = "null " + (null == paraName ? "input" : paraName);
            throw new IllegalArgumentException(message);
        }
    }

    public static void iaxIfNotNull(final Object test, final String paraName) {
        if (null != test) {
            String message = "not null " + (null == paraName ? "input" : paraName);
            throw new IllegalArgumentException(message);
        }
    }

    /**
     * Shorthand for "if not null or not assignable, throw IllegalArgumentException"
     *
     * @param c the Class to check - use null to ignore type check
     * @throws IllegalArgumentException "null {name}" if o is null
     */
    public static final void iaxIfNotAssignable(final Object[] ra, final Class<?> c, final String paraName) {
        iaxIfNull(ra, paraName);
        String label = (null == paraName ? "input" : paraName);
        for (int i = 0; i < ra.length; i++) {
            if (null == ra[i]) {
                String m = " null " + label + "[" + i + "]";
                throw new IllegalArgumentException(m);
            } else if (null != c) {
                Class<?> actualClass = ra[i].getClass();
                if (!c.isAssignableFrom(actualClass)) {
                    String message = label + " not assignable to " + c.getName();
                    throw new IllegalArgumentException(message);
                }
            }
        }
    }

    /**
     * Shorthand for "if not null or not assignable, throw IllegalArgumentException"
     *
     * @throws IllegalArgumentException "null {name}" if o is null
     */
    public static final void iaxIfNotAssignable(final Object test, final Class<?> c, final String paraName) {
        iaxIfNull(test, paraName);
        if (null != c) {
            Class<?> actualClass = test.getClass();
            if (!c.isAssignableFrom(actualClass)) {
                String message = paraName + " not assignable to " + c.getName();
                throw new IllegalArgumentException(message);
            }
        }
    }


    public static final void iaxIfTrue(boolean test, String fmt, Object... paras) {
        if (test) {
            throw new IllegalArgumentException(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    /**
     * Shorthand for "if false, throw IllegalArgumentException"
     *
     * @throws IllegalArgumentException "{message}" if test is false
     */
    public static final void iaxIfFalse(boolean test, String fmt, Object... paras) {
        if (!test) {
            throw new IllegalArgumentException(StringsUtil.nonIdxFmt(fmt, paras));
        }
    }

    /**
     * @return "({UnqualifiedExceptionClass}) {message}"
     */
    public static String renderExceptionShort(Throwable e) {
        if (null == e) {
            return "(Throwable) null";
        }
        return "(" + LangUtil.unqualifiedClassName(e) + ") " + e.getMessage();
    }

    /**
     * Renders exception <code>t</code> after unwrapping and eliding any test packages.
     *
     * @param t <code>Throwable</code> to print.
     * @see StringsUtil.StringChecker#TEST_PACKAGES
     */
    public static String renderException(Throwable t) {
        return renderException(t, true);
    }

    /**
     * Renders exception <code>t</code>, unwrapping, optionally eliding and limiting total number of lines.
     *
     * @param t     <code>Throwable</code> to print.
     * @param elide true to limit to 100 lines and elide test packages
     * @see StringsUtil.StringChecker#TEST_PACKAGES
     */
    public static String renderException(Throwable t, boolean elide) {
        if (null == t) {
            return "null throwable";
        }
        t = unwrapException(t);
        StringBuffer stack = stackToString(t, false);
        if (elide) {
            StringsUtil.elideEndingLines(StringsUtil.StringChecker.TEST_PACKAGES, stack, 100);
        }
        return stack.toString();
    }

    /**
     * Dump message and stack to StringBuffer.
     */
    public static StringBuffer stackToString(Throwable throwable, boolean skipMessage) {
        if (null == throwable) {
            return new StringBuffer();
        }
        StringWriter buf = new StringWriter();
        PrintWriter writer = new PrintWriter(buf);
        if (!skipMessage) {
            writer.println(throwable.getMessage());
        }
        throwable.printStackTrace(writer);
        try {
            buf.close();
        } catch (IOException ioe) {
        } // ignored
        return buf.getBuffer();
    }

    /**
     * @return Throwable input or tail of any wrapped exception chain
     */
    public static Throwable unwrapException(Throwable t) {
        Throwable current = t;
        Throwable next = null;
        while (current != null) {
            // Java 1.2 exceptions that carry exceptions
            if (current instanceof InvocationTargetException) {
                next = ((InvocationTargetException) current).getTargetException();
            } else if (current instanceof ClassNotFoundException) {
                next = ((ClassNotFoundException) current).getException();
            } else if (current instanceof ExceptionInInitializerError) {
                next = ((ExceptionInInitializerError) current).getException();
            } else if (current instanceof PrivilegedActionException) {
                next = ((PrivilegedActionException) current).getException();
            } else if (current instanceof SQLException) {
                next = ((SQLException) current).getNextException();
            }
            // ...getException():
            // javax.naming.event.NamingExceptionEvent
            // javax.naming.ldap.UnsolicitedNotification
            // javax.xml.parsers.FactoryConfigurationError
            // javax.xml.transform.TransformerFactoryConfigurationError
            // javax.xml.transform.TransformerException
            // org.xml.sax.SAXException
            // 1.4: Throwable.getCause
            // java.util.logging.LogRecord.getThrown()
            if (null == next) {
                break;
            } else {
                current = next;
                next = null;
            }
        }
        return current;
    }

    /**
     * Shorthand for
     * "if any not null or not assignable, throw IllegalArgumentException"
     *
     * @throws IllegalArgumentException "{name} is not assignable to {c}"
     */
    public static final void iaxIfNotAllAssignable(final Collection collection,
                                                        final Class c, final String raise) {
        iaxIfNull(collection, raise);
        if (null != c) {
            for (Iterator iter = collection.iterator(); iter.hasNext(); ) {
                iaxIfNotAssignable(iter.next(), c, raise);
            }
        }

    }



}
