# Nonce Checker

A nonce is a random string that is meant to be used just once. So, a case where a nonce is 
received more than once in a limited time period is considered an error.

# Build

This project is configured to be built with gradle. Please have Gradle 6.7.1 or later.
on a terminal window, navigate to the folder where you cloned this project.

run <code>gradle jar</code>

That command should generate a jar file - nonce_checker.jar under build/libs folder.

# Running

Once the project is successfully built, you can run it with the following command:

```cmd
java -jar nonce_checker.jar <FILE Path> (<nonceTTL>)
```

