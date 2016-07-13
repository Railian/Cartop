# CarTop

Workspace setup process
-----------------------

1. Download and install [**Git**](https://git-scm.com/download).

2. Navigate to your desired directory:

    ```
    $ cd path/to/your/desired/directory
    ```

3. Execute next command (clone git repo):

    ```
    $ git clone https://github.com/alexeypro/rapidus-cartop.git
    ```

4. Navigate to the project directory (here and later - 'work_directory')

    ```
    $ cd rapidus-cartop 
    ```

5. Download and install [**Android Studio**](https://developer.android.com/studio/index.html)

6. Open Android Studio, choose "Open an existing project", and open selected directory:

    ```
    /work_directory
    ```

7. After that you should be able to build an application.

Building apk with Gradle Wrapper (gradlew, built in Android Studio)
-------------------------------------------------------------------

1. Download and install _**Android SDK Build-tools, revision 24**_ and _**SDK Platform Android N, API 24**_ (only once, at first time):
  * Navigate to your Android SDK directory 
    ```
    For Mac:
    $ cd Library/Android/sdk
    ```
  * Get list packages available for installation or upgrade:
    ```
    $ ./tools/android list sdk --all
    Refresh Sources:
      (...)
      Fetching URL: https://dl.google.com/android/repository/repository-11.xml
      (...)
    Packages available for installation or update: 166
      1- Android SDK Tools, revision 25.1.7
      2- Android SDK Tools, revision 25.2 rc1
      3- Android SDK Platform-tools, revision 24
      4- Android SDK Build-tools, revision 24
      5- Android SDK Build-tools, revision 23.0.3
      6- Android SDK Build-tools, revision 23.0.2
      7- Android SDK Build-tools, revision 23.0.1
      (...)
      27- Documentation for Android SDK, API 23, revision 1
      28- SDK Platform Android N, API 24, revision 1
      29- SDK Platform Android 6.0, API 23, revision 3
      30- SDK Platform Android 5.1.1, API 22, revision 2
      31- SDK Platform Android 5.0.1, API 21, revision 2
     (...)
    ```
  * To download specific packages you need to specify the number of the item you want to install from the list in the following command:
    ```
    $ ./tools/android update sdk -u -a -t [NO_OF_ITEM_TO_BE_INSTALLED]
    ```
   _Example: To install **Android SDK Build-tools, revision 24,** and **SDK Platform Android N, API 24** type in:_
    ```
    $ ./tools/android update sdk -u -a -t 4
    $ ./tools/android update sdk -u -a -t 28
    ```
2. Navigate to work_directory:
    ```
    $ cd /work_directory
    ```
3. Give access for gradlew (only once, at first time):
    ```
    $ chmod +x gradlew
    ```
4. Make build:
    ```
    $ ./gradlew assembleDebug    
    ```
5. If **BUILD SUCCESSFUL**, apk file will be generated at:
    ```
    /work_directory/app/build/outputs/apk/app-debug.apk
    ```

Distributing apk with Fabric/Crashlytics plugin
-----------------------------------------------

1. Download and install [**Fabric plugin**](https://fabric.io/downloads) in Android Studio:
2. Edit emails distribution list in txt file:
    ```
    /work_directory/beta_distribution_emails.txt
    ```
3. Now build and distribute in one command:
    ```
    $ ./gradlew assembleDebug crashlyticsUploadDistributionDebug --stacktrace
    ```
