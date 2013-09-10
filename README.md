Wallpaper Manifest XML Format
=============================

Only URL and Author, if there is one, are needed. Other tags are not bad, but are optional.
Those tags:
1. thumbUrl
2. date
3. name

Adding Categories
-----------------
* `id="<string>"` unique id string 
* `name="<string>"` visual name 

### Example
	<category id="<string>" name="<string>" />


Adding Wallpapers
-----------------
* `url="<url of wallpaper>"`
* `thumbUrl="<url of thumbnail>"`
* `author="<author>"` may be used for filtering later
* `date="<date added>"` may be used for filtering later
* `name="<wallpaper name>"`

### Example
	<wallpaper 
		url="someurl.jpg"
		thumbUrl="someurl_small.jpg"
		author="exmaple"
		date="May 19, 2012"
		name="Some Wallpaper" />
