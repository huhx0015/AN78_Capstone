AN78_Capstone
=============

DEVELOPER: huhx0015

### GO THERE NOW
![an78_preview](https://cloud.githubusercontent.com/assets/1645482/15801503/0455d64e-2a4c-11e6-84e9-c87ca50b1f72.gif)

# Description

Android Nanodegree | Project 7-8: Capstone: Go There Now is an Android application that manages favorite location shortcuts, with the ability to create customized homescreen shortcut that launches Google Maps in navigation mode. The shortcut bypasses many steps of having to manually set and start Google Navigation mode.

Go There Now was developed in part as the final "Capstone" project for the Android Nanodegree program. This project serves to demonstrate all skills learned throughout the program to design, plan, and build an Android application from scratch. Go There Now makes use of the Google Places API and utilizes two Google Play Services, Ads and Location. Go There Now stores and accesses location shortcuts via a ContentProvider and a MySQL Lite database. 

As this application relies on Google Maps and Google Play Services, Google Maps and Google Play Services need to be installed for Go There Now to work correctly.

#  Rubric
Link: https://review.udacity.com/?_ga=1.96847642.442269068.1457085533#!/projects/4307513821/rubric

## Required Components

### Common Project Requirements
- [x] App conforms to common standards found in the Android Nanodegree General Project Guidelines

### Core Platform Development
- [x] App integrates a third-party library.
- [x] App validates all input from servers and users. If data does not exist or is in the wrong format, the app logs this fact and does not crash.
- [x] App includes support for accessibility. That includes content descriptions, navigation using a D-pad, and, if applicable, non-audio versions of audio cues.
- [x] App keeps all strings in a strings.xml file and enables RTL layout switching on all layouts.
- [x] App provides a widget to provide relevant information to the user on the home screen.

### Google Play Services
- [x] App integrates two or more Google services
- [x] Each service imported in the build.gradle is used in the app.
- [x] If Location is used, the app customizes the user’s experience by using the device's location.
- [x] If Admob is used, the app displays test ads. If Admob was not used, student meets specifications.

~~[] If Analytics is used, the app creates only one analytics instance. If Analytics was not used, student meets specifications.~~

~~[] If Maps is used, the map provides relevant information to the user. If Maps was not used, student meets specifications.~~

~~[] If Identity is used, the user’s identity influences some portion of the app. If Identity was not used, student meets specifications.~~

### Material Design
- [x] App theme extends AppCompat.
- [x] App uses an app bar and associated toolbars.
- [x] App uses standard and simple transitions between activities.

### Building
- [x] App builds from a clean repository checkout with no additional configuration.
- [x] App builds and deploys using the installRelease Gradle task.
- [x] App is equipped with a signing configuration, and the keystore and passwords are included in the repository. Keystore is referred to by a relative path.
- [x] All app dependencies are managed by Gradle.

### Data Persistence
- [x] App implements a ContentProvider to access locally stored data.
- [x] App uses a Loader to move its data to its views.
- [x] Must implement at least one of the three
    
    ~~[] If it regularly pulls or sends data to/from a web service or API, app updates data in its cache at regular intervals using a SyncAdapter.~~
    
     OR
    
    ~~[] If it needs to pull or send data to/from a web service or API only once, or on a per request basis (such as a search application), app uses an IntentService to do so.~~
    
     OR
    
    - [x] It it performs short duration, on-demand requests (such as search), app uses an AsyncTask.

## License

    Copyright 2016 Michael Huh

    Licensed under the Apache License, Version 2.0 (the "License");
    you may not use this file except in compliance with the License.
    You may obtain a copy of the License at

        http://www.apache.org/licenses/LICENSE-2.0

    Unless required by applicable law or agreed to in writing, software
    distributed under the License is distributed on an "AS IS" BASIS,
    WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
    See the License for the specific language governing permissions and
    limitations under the License.
