# Timdicator [![API](https://img.shields.io/badge/API-14%2B-brightgreen.svg?style=flat)](https://android-arsenal.com/api?level=14) [![](https://jitpack.io/v/tsurkis/timdicator.svg)](https://jitpack.io/#tsurkis/timdicator)
 
This library was created for educational purposes first and embodies simplicity in its design. 
You can read more at: https://android.jlelse.eu/become-an-android-painter-aadf91cec9d4

![Timdicator demo](https://github.com/TSurkis/Timdicator/blob/master/README_assets/indicator_screen_record.gif)

## Implementation

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
    compile 'com.github.tsurkis:timdicator:1.2.0'
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
2.
 - Attach your ViewPager to Timdicator:
    - Version 1.2.0+:
      - Regular attachment:
      ```
        TimdicatorBinder.attachViewPager(timdicator, viewPager);
      ```
      
      - Dynamic attachment:

      ```
        TimdicatorBinder.attachViewPagerDynamically(timdicator, viewPager);
      ```
    - Version 1.0.3 and lower:

      - Regular attachment:
      ```
        timdicator.attach(viewPager);
      ```
     
      - Dynamic attachment:
      ```
        timdicator.attachDynamically(viewPager);
      ```
      
  - Attach your RecyclerView to Timdicator:
    ```
      TimdicatorBinder.attachRecyclerView(timdicator, recyclerView, pageSnapHelper, isHorizontal);
    ```

## Explanation

### ViewPager
**Regular attachment:** This attachment will bind Timdicator view to the ViewPager as an OnPageChangeListener interface and will allow it to automatically switch states according to page selection. 

**Dynamic attachment:** Initializes Timdicator according to the number of pages present in the ViewPager. Therefore, when using this method there is no need to declare number of circles in your layout. 
  Additionaly, this attachment will bind Timdicator view to the ViewPager as an OnPageChangeListener interface and will allow it to automatically switch states according to page selection.

### RecyclerView
RecyclerView Timdicator attachment is currently supported only with snapped pages. The pageSnapHelper and the orientation of the RecyclerView must be passed for the library to determine the correct page.
  

## Life Cycle Handling
All the life cycle handling is done internally. Therefore, there is no need to call any destroy or nullifing method.

## Parameters

| Parameters | Xml Attritube | Setter Method | Value |
|---|---|---|---|
| radius | circle_radius | setCircleRadiusInDp(Context, float) | dp |
| distance between each circle | distance_between_circles | setDistanceBetweenCircleInDp(Context, float) | dp |
| number of circles | number_of_circles | setNumberOfCircles(int) | int |
| chosen circle color | chosen_circle_color | setChosenCircleColor(Context, \@ColorRes int) | color |
| default circle color | default_circle_color | setDefaultCircleColor(Context, \@ColorRes int) | color |

