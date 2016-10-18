# WebCalculator 1.0 (http://webmathcalculator.herokuapp.com)

Basic web calculator that performs calculation on the server side.

1. Functions include adding, subtracting, multiplying, dividing and
square root. Supports expression grouping by () [] {}.

2. Calculates Area under the curve f(x) = e^x using the Riemann sequence
Approximation (inscribed rectangles)
[Ref. <a href="https://en.wikipedia.org/wiki/Riemann_integral" >https://en.wikipedia.org/wiki/Riemann_integral</a>]

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
 1. A catalogue for postman with templates for the endpoints is available trhough this https://www.getpostman.com/collections/abdc14b0f2ea89ff1634
 2. Only required to use the option "import" in Postman Chrome extension (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)



## Users Manual

###https://github.com/acabra85/webcalculator/wiki


