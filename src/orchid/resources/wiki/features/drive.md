One of the core features that FTC-Lib has is having drivetrains.

There are built-in drivetrains, such as HolonomicDrive and MecanumDrive, that make robot movement easier.

First, we create a mecanum drive with the four drive motors.
```kotlin
val mecanum: Drive = MecanumDrive(listOf("drive", "motors", "go", "here!"))
```

To set the power of the drivetrain and go in a specific direction, we can use:
```kotlin
mecanum.move(180, 1)
```

We can do the same thing with rotation:
```kotlin
holonomic.rotate(0.5)
```

Now, if we want to stop the drivetrain:
```kotlin
holonomic.stop()
```

But what if we want the drivetrain to only move a specific amount? We can enter the distance:
```kotlin
holonomic.move(180, 0.25, 300)
```

The same thing goes for rotation as well:
```kotlin
holonomic.rotate(0.1, 325)
```

### Reference Code
```kotlin
val holonomic: Drive = MecanumDrive(listOf("drive", "motors", "go", "here!"))

holonomic.move(180, 1)

holonomic.rotate(0.5)

holonomic.stop()

holonomic.move(180, 0.25, 300)

holonomic.rotate(0.1, 325)
```