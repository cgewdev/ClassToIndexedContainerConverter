# ClassToIndexedContainerConverter

### Features

<ul>
  <li>Create a Vaadin IndexedContainer straight from your class</li>
  <li>Supply it with a suiting collection and the IndexedContainer is filled with items</li>
  <li>You can set fields not to be included simply by supplying their names</li>
</ul>

### Usage

<code>public IndexedContainer createIndexedContainerFromClass(Class classToConvert, Collection collection, String... fieldsToIgnore)</code>
