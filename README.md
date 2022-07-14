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
<code>(< nonceTTL >)</code> is an optional parameter. If not provided, the cod ewill use 5 minutes.

If you execute the program without any parameters, the program will show how to use it.

```
  You must provide a file path as a single parameter to run this code.
        
        Ex.:
            java gov.cdc.exercise.NonceChecker <FilePath> <nonceTTL>
            
        Where:
            - FIlePath is the path to the file to be processed
            - nonceTTL (Optional:Default 5 min): is the time to live for a nonce. A duplicate within this time period
                  is considered a duplicate. (pass values in minutes)

```


