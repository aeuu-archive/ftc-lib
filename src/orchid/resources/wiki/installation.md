### Basic Installation
Installation is simple. To install the main package via gradle, use [JitPack](https://jitpack.io) and add the dependency `com.github.arctfoxx:ftc-lib:VERSION` The annotation processor can be used using the `kapt` scope and the ID `com.github.arctfoxx.ftc-lib:ftc-processor:VERSION'

Make sure you have KotlinPoet installed as well or annotation processing will fail.

Alternatively, the prebuilt jars may be used instead of the JitPack dependency.

### Example Configurations

Gradle Configuration:
```groovy
repositories {
    maven { url 'https://jitpack.io' }
}

dependencies {
    implementation('com.github.arctfoxx:ftc-lib:VERSION')
    
    kapt('com.github.arctfoxx.ftc-lib:ftc-processor:VERSION')
}
```