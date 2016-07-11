# CarTop

**Workspace setup process**
------------------------

**1:** Navigate to your desired directory (here and later - 'work_directory'):
```
$ cd /path/to/your/desired/directory
```

**2:**  Execute next command (clone git repo):
```
$ git clone https://github.com/Raylyan/Cartop.git
```

**3:** Download and install [Android Studio](https://developer.android.com/studio/index.html):

**4:** Open Android Studio, choose "Open an existing project", and open selected directory
```
work_directory/
```

**5:** After that you should be able to build an application

**Building apk with Gradle Wrapper (gradlew, built it in Android Studio):**
------------------------

**1:** Navigate to work_directory
```
$ cd work_directory/
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

**1:** Download and install [Fabric plugin](https://fabric.io/downloads) in Android Studio:

**2:** Edit emails distribution list in txt file:
```
work_dirrectory/beta_distribution_emails.txt
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
