package com.rsoi.kursach.enums;

import java.io.ObjectInputStream;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.Properties;
import org.hibernate.HibernateException;
import org.hibernate.engine.spi.SessionImplementor;
import org.hibernate.type.BasicTypeRegistry;
import org.hibernate.type.SingleColumnType;
import org.hibernate.usertype.EnhancedUserType;
import org.hibernate.usertype.ParameterizedType;

public class GenericEnumUserType implements EnhancedUserType, ParameterizedType, Serializable{
    
    private static final String DEFAULT_IDENTIFIER_METHOD_NAME = "getCode";
    private static final String DEFAULT_VALUE_OF_METHOD_NAME = "findByCode";
    private static final Class[] NULL_CLASS_VARARG = null;
    private static final Object[] NULL_OBJECT_VARARG = null;
    private static final char SINGLE_QUOTE = '\'';

    private Class<? extends Enum> enumClass;
    private Method identifierMethod;
    private int[] sqlTypes;
    private SingleColumnType<Object> type;
    private Method valueOfMethod;

    public Object assemble(Serializable cached, Object owner) throws HibernateException {
        return cached;
    }

    public Object deepCopy(Object value) throws HibernateException {
        return value;
    }

    public Serializable disassemble(Object value) throws HibernateException {
        return (Serializable) value;
    }

    public boolean equals(Object x, Object y) throws HibernateException {
        return x == y;
    }



	public Object fromXMLString(String xmlValue) {
        return Enum.valueOf(enumClass, xmlValue);
    }

    public int hashCode(Object x) throws HibernateException {
        return x.hashCode();
    }

    public boolean isMutable() {
        return false;
    }

    public Object nullSafeGet(ResultSet rs, String[] names, SessionImplementor session, Object owner) throws HibernateException, SQLException {
        Object identifier = this.type.nullSafeGet(rs, names[0], session);
        if(identifier != null && !rs.wasNull()) {
            try {
                return this.valueOfMethod.invoke(this.enumClass, new Object[]{identifier});
            } catch (Exception var8) {
                String msg = "Exception while invoking valueOfMethod [" + this.valueOfMethod.getName() + "] of Enum class [" + this.enumClass.getName() + "] with argument of type [" + identifier.getClass().getName() + "], value=[" + identifier + "]";
                throw new HibernateException(msg, var8);
            }
        } else {
            return null;
        }
    }

    public void nullSafeSet(PreparedStatement st, Object value, int index, SessionImplementor session) throws HibernateException, SQLException {
        if(value == null) {
            st.setNull(index, this.sqlTypes[0]);
        } else {
            try {
                Object exception = this.identifierMethod.invoke(value, NULL_OBJECT_VARARG);
                this.type.set(st, exception, index, session);
            } catch (Exception var7) {
                String msg = "Exception while invoking identifierMethod [" + this.identifierMethod.getName() + "] of Enum class [" + this.enumClass.getName() + "] with argument of type [" + value.getClass().getName() + "], value=[" + value + "]";
                throw new HibernateException(msg, var7);
            }
        }

    }

	public String objectToSQLString(Object value) {
        return SINGLE_QUOTE + ((Enum) value).name() + SINGLE_QUOTE;
    }

    public Object replace(Object original, Object target, Object owner) throws HibernateException {
        return original;
    }

	public Class returnedClass() {
        return enumClass;
    }

    public void setParameterValues(Properties parameters) {
        String enumClassName = parameters.getProperty("enumClass");
        String identifierMethodName = parameters.getProperty("identifierMethod", DEFAULT_IDENTIFIER_METHOD_NAME);
        String valueOfMethodName = parameters.getProperty("valueOfMethod", DEFAULT_VALUE_OF_METHOD_NAME);

        initialize(enumClassName, identifierMethodName, valueOfMethodName);
    }

    public int[] sqlTypes() {
        return sqlTypes;
    }

    public String toXMLString(Object value) {
        return ((Enum<?>) value).name();
    }

    @SuppressWarnings("unchecked")
    private void initialize(String enumClassName, String identifierMethodName, String valueOfMethodName) {
        try {
            enumClass = Class.forName(enumClassName).asSubclass(Enum.class);
        } catch (ClassNotFoundException exception) {
            throw new HibernateException("Enum class not found", exception);
        }

        try {
            identifierMethod = enumClass.getMethod(identifierMethodName, NULL_CLASS_VARARG);
        } catch (Exception exception) {
            throw new HibernateException("Failed to obtain identifier method", exception);
        }
        Class<?> identifierType = identifierMethod.getReturnType();

        try {
            valueOfMethod = enumClass.getMethod(valueOfMethodName, identifierType);
        } catch (Exception exception) {
            throw new HibernateException("Failed to obtain valueOf method", exception);
        }

        //TODO: We really shouldn't be instantiating this, but I don't know how to get the SessionImplementor here
        BasicTypeRegistry registry = new BasicTypeRegistry();

        type = (SingleColumnType<Object>) registry.getRegisteredType(identifierType.getName());
        if (type == null) {
            throw new HibernateException("Unsupported identifier type " + identifierType.getName());
        }
        sqlTypes = new int[]{type.sqlType()};
    }

    /**
     * Prevents attempts to deserialize the {@code GenericEnumUserType} directly, as {@link SerializationProxy}
     * should have been written in its place.
     *
     * @param stream ignored
     */
    private void readObject(ObjectInputStream stream) {
        throw new UnsupportedOperationException(getClass().getName() + " cannot be deserialized directly");
    }

    /**
     * Replaces this {@code GenericEnumUserType} (which is not really serializable due to the {@code Method} fields)
     * with a simple {@link SerializationProxy} containing the names for the enum class, the identifier method and
     * the valueOf method.
     *
     * @return a new {@link SerializationProxy} to be serialized in this type's place
     */
    private Object writeReplace() {
        return new SerializationProxy(enumClass.getName(), identifierMethod.getName(), valueOfMethod.getName());
    }

    /**
     * Simple proxy to serialize in place of the more complicated {@link GenericEnumUserType}. This proxy contains
     * all the data necessary to recreate/reinitialize the full {@link GenericEnumUserType}.
     */
    private static class SerializationProxy implements Serializable {

		private final String enumClassName;
        private final String identifierMethodName;
        private final String valueOfMethodName;

        private SerializationProxy(String enumClassName, String identifierMethodName, String valueOfMethodName) {
            this.enumClassName = enumClassName;
            this.identifierMethodName = identifierMethodName;
            this.valueOfMethodName = valueOfMethodName;
        }

        private Object readResolve() {
            GenericEnumUserType type = new GenericEnumUserType();
            type.initialize(enumClassName, identifierMethodName, valueOfMethodName);

            return type;
        }
    }
    
}
