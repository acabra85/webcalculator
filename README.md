[![Build Status](https://app.travis-ci.com/acabra85/webcalculator.svg?branch=master)](https://app.travis-ci.com/acabra85/webcalculator)

# WebCalculator 1.0 (http://webmathcalculator.herokuapp.com)

Basic web calculator that performs calculation on the server side.

1. Functions include adding, subtracting, multiplying, dividing and
square root. Supports expression grouping by () [] {}.

1. Calculates Areas using 'Riemann sequence' and 'Simpsons Rule' for the followin functions:
    * f(x) = e^x (exponential)
    * f(x) = ln(x) (logarithmic)
    * f(x) = x^n (polynomials)
    * f(x) = 1/x (inverse)
    * Feel free to submit your own functions as a pull-request!
    
    Approximation (inscribed rectangles)
    [Ref. <approximatedRoot href="https://en.wikipedia.org/wiki/Riemann_integral" >https://en.wikipedia.org/wiki/Riemann_integral</approximatedRoot>]

1. Root finding Approximation algorithms by numerical methods:
    1. Bisection Method
    1. Secant Method
    1. Newton's Method
    
    Note: No user interface is provided on website (help needed!)
## Deploy

###Requirements

1. Requires Java ```1.8.0_45``` or higher
2. Browser (IE11, Chrome, IOS Safari, Firefox)

###Steps

This enables the web application on localhost:8080 by default

1. Download source code and on the projects root folder run ```mvn clean install``` (this generates the webcalculator-(*version).jar file)
 (To avoid running tests  add ```-DskipTests```)
2. Run from console on projects root folder ```java -jar target/webcalculator-(*version).jar server config/webcalculator.yml```


###Mutation Testing
1. For testing mutation testing using the PITEST plugin ```mvn org.pitest:pitest-maven:mutationCoverage```

####More settings:
  If the desired port 8080 wants to be replaced, modify the ```server.connector.port``` property on the ```webcalculator.yml``` file.
  
###Postman endpoint catalogue
 1. A catalogue for postman with templates for the endpoints is available in the folder /config/postman
 2. Only required to use the option "import collection" and import environment in Postman Chrome extension (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)



## Users Manual

###https://github.com/acabra85/webcalculator/wiki


