# CarTop

**Workspace setup process**
------------------------

**1:** Navigate to your desired directory (here and later - 'Work' directory):
```
$ cd /path/to/your/desired/directory
```

**2:**  Execute next command (clone git repo):
```
$ git clone https://github.com/Raylyan/Cartop.git
```

**3:** Open Android Studio, choose "Open an existing project", and open selected directory
```
For Driver:
your_directory/rapidus-mobile-android/RapidusDriver/
```
```
For Client:
your_directory/rapidus-mobile-android/RapidusClient/
```

**4:** After that you should be able to build an application


**Building apk with Gradle Wrapper (gradlew, built it in Android Studio):**
------------------------

**1:** Navigate to project directory
```
For Driver:
your_directory/rapidus-mobile-android/RapidusDriver/
```
```
For Client:
your_directory/rapidus-mobile-android/RapidusClient/
```

**2:** Give access for gradlew (only once, at first time)
```
$ chmod +x gradlew
```

**3:** Make build
```
For Debug:
$ ./gradlew assembleDebug
```
```
For Release:
$ ./gradlew assembleRelease
```


**Distributing apk with Fabric/Crashlytics plugin:**
------------------------

**1:** Download and install Fabric plugin in Android Studio:
```
https://fabric.io/downloads
```

**2:** Edit emails distribution list in txt file:
```
rapidus-mobile-android/RapidusDriver/beta_distribution_emails.txt
```

**3:** Now build and distribute in one command:
```
Debug
$ ./gradlew assembleDebug crashlyticsUploadDistributionDebug --stacktrace
```
```
Release
$ ./gradlew assembleRelease crashlyticsUploadDistributionRelease --stacktrace
```
