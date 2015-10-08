import com.vaadin.data.Item;
import com.vaadin.data.util.IndexedContainer;

import javax.validation.constraints.NotNull;
import java.lang.reflect.Field;
import java.lang.reflect.Modifier;
import java.util.*;
import java.util.stream.Stream;

/**
 * Created by gewehr.c on 07.10.2015.
 */
public class ClassToIndexedContainerConverter {

    public IndexedContainer createIndexedContainerFromClass(@NotNull Class classToConvert, Collection collection, String... fieldsToIgnore) {
        final IndexedContainer indexedContainer = new IndexedContainer();
        final List<Field> listOfFilteredFields = new ArrayList<>();

        // Filter out unwanted fields or use all fields in case fields to ignore is empty
        if (fieldsToIgnore != null && fieldsToIgnore.length > 0) {
            filterFields(filterOutStaticFields(classToConvert.getDeclaredFields()), fieldsToIgnore).forEach(
                    listOfFilteredFields::add);
        } else {
            Collections.copy(listOfFilteredFields, filterOutStaticFields(classToConvert.getDeclaredFields()));
        }

        // Adds container properties to the indexed container based on the list of supplied fields
        if (listOfFilteredFields.size() > 0) {
            addContainerPropertiesToIndexedContainer(indexedContainer, listOfFilteredFields);

            // Add collection items to container
            if (collection != null) {
                if (collection.size() > 0) {
                    Class classFromCollection = collection.stream().findFirst().get().getClass();
                    if (classToConvert.equals(classFromCollection)) {
                        initValues(collection, indexedContainer, fieldsToIgnore);
                    }
                }
            }
        }

        return indexedContainer;
    }

    private List<Field> filterOutStaticFields(Field[] unfilteredFields) {
        final List<Field> nonStaticFields = new ArrayList<>();

        Stream.of(unfilteredFields).forEach(field -> {
            if (!Modifier.isStatic(field.getModifiers())) {
                nonStaticFields.add(field);
            }
        });

        return nonStaticFields;
    }

    private List<Field> filterFields(List<Field> listOfClassFields, String... fieldsToIgnore) {
        final List<Field> listOfFilteredFields = new ArrayList<>();
        final List<String> listOfFieldsToIgnore = new ArrayList<>();
        Collections.addAll(listOfFieldsToIgnore, fieldsToIgnore);

        listOfClassFields.forEach(field -> {
            // Filter out unwanted fields
            if (!listOfFieldsToIgnore.contains(field.getName())) {
                listOfFilteredFields.add(field);
            }
        });

        return listOfFilteredFields;
    }

    private void addContainerPropertiesToIndexedContainer(IndexedContainer indexedContainer, List<Field> listOfFields) {
        listOfFields.forEach(field -> {
            if (field.getType().equals(String.class)) {
                indexedContainer.addContainerProperty(field.getName(), String.class, "");
            } else if (field.getType().equals(Integer.class)) {
                indexedContainer.addContainerProperty(field.getName(), Integer.class, 0);
            } else if (field.getType().equals(Double.class)) {
                indexedContainer.addContainerProperty(field.getName(), Double.class, 0.0);
            } else if (field.getType().equals(Float.class)) {
                indexedContainer.addContainerProperty(field.getName(), Float.class, 0.0f);
            } else if (field.getType().equals(Long.class)) {
                indexedContainer.addContainerProperty(field.getName(), Long.class, 0);
            } else if (field.getType().equals(Boolean.class)) {
                indexedContainer.addContainerProperty(field.getName(), Boolean.class, false);
            }
        });
    }

    private void initValues(Collection collection, IndexedContainer indexedContainer, String... fieldsToIgnore) {
        collection.forEach(object -> {
            Item item = indexedContainer.getItem(indexedContainer.addItem());
            Stream.of(object.getClass().getDeclaredFields()).forEach(field -> {
                try {
                    if (!Arrays.asList(fieldsToIgnore).contains(field.getName())) {
                        field.setAccessible(true);
                        item.getItemProperty(field.getName()).setValue(field.get(object));
                    }
                } catch (IllegalAccessException e) {
                    e.printStackTrace();
                }
            });
        });
    }
}
