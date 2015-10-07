import com.vaadin.data.util.IndexedContainer;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by gewehr.c on 07.10.2015.
 */
public class ClassToIndexedContainerConverter {

    public IndexedContainer createIndexedContainerFromClass(@NotNull Class classToConvert, String... fieldsToIgnore) {
        final IndexedContainer indexedContainer = new IndexedContainer();
        final List<Field> listOfFilteredFields = new ArrayList<>();

        // Filter out unwanted fields or use all fields in case fields to ignore is empty
        if (fieldsToIgnore != null && fieldsToIgnore.length > 0) {
            filterFields(classToConvert.getDeclaredFields(), fieldsToIgnore).forEach(listOfFilteredFields::add);
        } else {
            Collections.addAll(listOfFilteredFields, classToConvert.getDeclaredFields());
        }

        // Adds container properties to the indexed container based on the list of supplied fields
        addContainerPropertiesToIndexedContainer(indexedContainer, listOfFilteredFields);

        return indexedContainer;
    }

    private List<Field> filterFields(Field[] listOfClassFields, String... fieldsToIgnore) {
        final List<Field> listOfFilteredFields = new ArrayList<>();
        final List<String> listOfFieldsToIgnore = new ArrayList<>();
        Collections.addAll(listOfFieldsToIgnore, fieldsToIgnore);

        for (Field field : listOfClassFields) {
            // Filter out unwanted and static fields
            if (!listOfFieldsToIgnore.contains(field.getName()) || !Modifier.isStatic(field.getModifiers())) {
                listOfFilteredFields.add(field);
            }
        }

        return listOfFilteredFields;
    }

    private void addContainerPropertiesToIndexedContainer(IndexedContainer indexedContainer, List<Field> listOfFields) {
        listOfFields.forEach(field -> {
            if (!Modifier.isStatic(field.getModifiers())) {
                if (field.getType().equals(String.class)) {
                    indexedContainer.addContainerProperty(field.getName(), String.class, "");
                } else if (field.getType().equals(Integer.class) || field.getType().equals(int.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Integer.class, 0);
                } else if (field.getType().equals(Double.class) || field.getType().equals(double.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Double.class, 0.0);
                } else if (field.getType().equals(Float.class) || field.getType().equals(float.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Float.class, 0.0f);
                } else if (field.getType().equals(Long.class) || field.getType().equals(long.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Long.class, 0);
                } else if (field.getType().equals(Boolean.class) || field.getType().equals(boolean.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Boolean.class, false);
                } else if (field.getType().equals(Object.class)) {
                    indexedContainer.addContainerProperty(field.getName(), Object.class, null);
                }
            }
        });
    }
}
