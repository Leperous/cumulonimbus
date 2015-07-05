package net.ollie.distributed.hazelcast.serialization;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.hazelcast.nio.ObjectDataInput;
import com.hazelcast.nio.ObjectDataOutput;
import com.hazelcast.nio.serialization.StreamSerializer;

/**
 *
 * @author Ollie
 */
public class AutoDataSerializer implements StreamSerializer<AutoDataSerialized> {

    private static final Map<Class<? extends AutoDataSerialized>, List<Field>> sharedFields = new ConcurrentHashMap<>();

    public static AutoDataSerializer isolated(final int type) {
        return new AutoDataSerializer(type, new ConcurrentHashMap<>());
    }

    public static AutoDataSerializer shared(final int type) {
        return new AutoDataSerializer(type, sharedFields);
    }

    private final Map<Class<? extends AutoDataSerialized>, List<Field>> fields;
    private final int typeId;

    protected AutoDataSerializer(final int typeId, final Map<Class<? extends AutoDataSerialized>, List<Field>> fields) {
        this.typeId = typeId;
        this.fields = fields;
    }

    @Override
    public void write(final ObjectDataOutput out, final AutoDataSerialized object) throws IOException {
        if (object == null) {
            out.writeObject(null);
            return;
        }
        try {
            out.writeObject(object.getClass());
            for (final Field field : fields.computeIfAbsent(object.getClass(), this::fields)) {
                final Object value = field.get(out);
                out.writeObject(value);
            }
        } catch (final IllegalAccessException iex) {
            throw new IOException(iex);
        }
    }

    @Override
    public AutoDataSerialized read(final ObjectDataInput in) throws IOException {
        final Class<? extends AutoDataSerialized> clazz = in.readObject();
        if (clazz == null) {
            return null;
        }
        try {
            final AutoDataSerialized object = clazz.newInstance();
            for (final Field field : fields.computeIfAbsent(clazz, this::fields)) {
                final Object value = in.readObject();
                field.set(object, value);
            }
            return object;
        } catch (final IllegalAccessException | InstantiationException ex) {
            throw new IOException(ex);
        }
    }

    private List<Field> fields(final Class<?> clazz) {
        if (clazz == null) {
            return Collections.emptyList();
        }
        final Field[] declaredFields = clazz.getDeclaredFields();
        final List<Field> fields = new ArrayList<>(declaredFields.length);
        for (final Field field : declaredFields) {
            field.setAccessible(true);
            fields.add(field);
        }
        fields.addAll(this.fields(clazz.getSuperclass()));
        return fields;
    }

    @Override
    public int getTypeId() {
        return typeId;
    }

    @Override
    public void destroy() {
    }

}
