# Timdicator [![](https://jitpack.io/v/tsurkis/timdicator.svg)](https://jitpack.io/#tsurkis/timdicator)
 
This library was created for educational purposes first and embodies simplicity in its design. 
You can read more at:

## Download

There are two steps:
1. Adding the Jitpack repository:
   There are two options:
   - Add it in your root build.gradle at the end of repositories:

    ```
      allprojects {
        repositories {
          ...
          maven { url 'https://jitpack.io' }
        }
      }
    ```
    - Add it in the build.gradle of your desired module:

    ```
      repositories {
        ...
        maven { url 'https://jitpack.io' }
      }
    ```

2. Adding the library:

```
  dependencies {
    compile 'com.github.tsurkis:timdicator:1.0.3'
  }
```

## How to use

1. Create Timdicator in a layout or programmatically:
```
  <com.tsurkis.timdicator.Timdicator
      android:id="@+id/timdicator"
      android:layout_width="wrap_content"
      android:layout_height="wrap_content"/>
```

```
  Timdicator timdicator = new Timdicator(context);
```
2. Fetch your view in any desired way:
```
  Timdicator timdicator = findViewById(R.id.timdicator);
```
3. Attach your ViewPager to Timdicator:
    - Regular attachment:
    ```
      timdicator.attach(viewPager);
    ```
    This attachment will bind Timdicator view to the ViewPager as an OnPageChangeListener interface and will allow it to automatically switch states according to page selection. 
     
    - Dynamic attachment:
    ```
      indicator.attachDynamically(viewPager);
    ```
    Initializes Timdicator according to the number of pages present in the ViewPager. Therefore, when using this method there is no need to declare number of circles in your layout. 
Additionaly, this attachment will bind Timdicator view to the ViewPager as an OnPageChangeListener interface and will allow it to automatically switch states according to page selection. 

## Parameters

| Parameters | Xml Attritube | Setter Method | Value |
|---|---|---|---|
| radius | circle_radius | setCircleRadiusInDp(Context, float) | dp |
| distance between each circle | distance_between_circles | setDistanceBetweenCircleInDp(Context, float) | dp |
| number of circles | number_of_circles | setNumberOfCircles(int) | int |
| chosen circle color | chosen_circle_color | setChosenCircleColor(Context, \@ColorRes int) | color |
| default circle color | default_circle_color | setDefaultCircleColor(Context, \@ColorRes int) | color |
