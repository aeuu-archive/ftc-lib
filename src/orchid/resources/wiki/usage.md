### Create an Operation Mode

To create an operation mode, we first need to declare a `class` that extends OperationMode. We also need to define a constructor that passes an `OpMode` through to the superclass.

```kotlin
class MyOperationMode(sdk: OpMode) : OperationMode(sdk) {
    // Our code will go here
}
```

However, if you try to run this code, you will find that the operation mode is not registered to the FTC SDK EventLoop.
There are two ways to register your operation mode.

### Registering an Operation Mode
The first and highly suggested method of registering an operation mode is to use the @OperationMode.Bind annotation.
You can choose to specify the operation mode as either Autonomous or Operated.
```kotlin
@OperationMode.Bind(OperationMode.Type.Autonomous)
class MyOperationMode(sdk: OpMode) : OperationMode(sdk) {
    // Our code will go here
}
```

Now, if you try to run the operation mode, it will be registered by the FTC App.

However, if you would not like to use the annotation processor (It requires installing the processor and using `kapt`),
an alternative is to bind the operation mode yourself. This can be done using the OperationModeBindings class.

```kotlin
@Autonomous // Or @TeleOp for Driver Operated
class MyOperationMode : OperationModeBindings {
    val mode = object : OperationMode(this) {
        // Our code will go here
    }
}
```

For the purposes of this wiki, we'll use the @OperationMode.Bind annotation.

### Putting Code in it!

To add our code to it, we'll need to define two methods in our operation mode. Let's look at an example.

```kotlin
@OperationMode.Bind(OperationMode.Type.Autonomous)
class MyOperationMode(sdk: OpMode) : OperationMode(sdk) {
    private val myMotor: Motor? = null

    override fun init() {
        myMotor = robot.map("motorName")
    }

    override fun loop() {
        power = robot.gamepad[0].left.y

        myMotor?.power = power
    }
}
```

This is a pretty basic example, but it showcases the core methods of the library. Let's break it down!

```kotlin
private val myMotor: Motor? = null
```

First, we'll declare a motor and set it to null as we've yet to initialize it.

```kotlin
override fun init() {
    myMotor = robot.map("motorName")
}
```

The code is executed when the INIT button is pressed, before the operation mode is run.
Here, all we do is defined a motor with the name `motorName`.
In the standard SDK, you will have to define the type of the device to import, `DcMotor::class.java` in this case.
But here, as we've defined our `myMotor` variable as having type `Motor`, the type is automatically inferred for us!

If you do need to specify a type, it can be passed as a type parameter in `Robot#map(String)`.

```kotlin
override fun loop() {
    val power = robot.gamepad[0].left.y

    myMotor?.power = power
}
```

Finally, this is the code executed in a loop when the operation mode is running.

First, we we create a variable called `power` that we set to the `y` value of the robot's first gamepad's left joystick.
Then, we set the power of the motor we initialized earlier to `power`.

If you try using this code, it will work!

### Linear Operation Modes
Aside from the normal operation modes, there are linear operation modes.
Unlike the normal operation mode, the linear operation mode act's more like a program.
It will walk through steps one after another, without looping (Obviously, you can add your own loops).

This can be useful when you want to go through a specific set of steps, such as during the automated period.
Linear operation modes also expose some extended functionality such as `#idle()`.

To implement a linear operation mode, all you need to do is to override the `#run()` function.
An example is provided below.

```kotlin
@OperationMode.Bind(OperationMode.Type.Autonomous)
class MyOperationMode(sdk: OpMode) : LinearOperationMode(sdk) {
    override fun run() {
        val motor: Motor = robot.map("motorName")

        motor.move(0.5, 20) // Move the motor by a distance of 20 and at 50% power
    }
}
```