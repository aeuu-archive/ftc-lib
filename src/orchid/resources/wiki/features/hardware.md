There are two ways to get an instance of a hardware device using the library.
The first method has been shown previously, making use of type inference:

```kotlin
val device: Motor = robot.map("name")
```

You can alternatively use `robot.map.get(String)` which does the same thing:

```kotlin
val device: Motor = robot.map("name")
```

If you're using Java, which doesn't support the `reified` kotlin feature, you'll have to use the following function:

```java
Motor device = robot.map(Motor.class, "name");
```

But you can also use it in Kotlin as well:

```kotlin
val device = robot.map(Motor::class.java, "name")
```