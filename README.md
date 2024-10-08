# Instructions

Deploy the `custom-info-list-renderer` OSGI module.

Go to Instance Settings, under `Other` go to `category.custom-info-list` and add two entries for ***Custom tag names***, `nice-collection` and `three-d-slider`.

Deploy the `nice-collection` client extension. It will register one JS and one CSS client extension.

Create a site and add the JS and CSS client extension to its pages.

Create some collection of journal articles and create an ***Information Template*** associated to their records.

Put a collection display on a page, you can now choose the `nice-collection` and `three-d-slider` templates and associate an Information Template as item renderer.
