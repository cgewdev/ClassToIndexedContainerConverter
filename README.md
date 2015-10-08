# Class to IndexedContainer converter

### Features

<ul>
  <li>Create a Vaadin IndexedContainer straight from your class</li>
  <li>Supply it with a suiting collection and the IndexedContainer is filled with items</li>
  <li>You can set fields not to be included simply by supplying their names</li>
</ul>

### Usage

You'd probably call it like this: <code>createIndexedContainerFromClass(Some.class, null, "");</code><br>
Instead of null you can supply a suiting collection. Its values will be out into the IndexedContainer.<br>
If you dont want certain fields to be included you can just supply them.
<code>createIndexedContainerFromClass(Some.class, null, "field1", "field2");</code>
