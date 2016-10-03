# WebCalculator 1.0

Basic web calculator that performs calculation on the server side.

1. Functions include adding, subtracting, multiplying, dividing and
square root. Supports expression grouping by () [] {}.

2. Calculates Area under the curve f(x) = e^x using the Riemann sequence
Approximation (inscribed rectangles)
[Ref. <a href="https://en.wikipedia.org/wiki/Riemann_integral" >https://en.wikipedia.org/wiki/Riemann_integral</a>]

## Deploy

###Requirements

1. Requires Java ```1.8.0_45``` or higher
2. Internet access to download dependencies (dependencies download on index.htlm when loading):
  a. Jquery https://ajax.googleapis.com/ajax/libs/jquery/3.1.0/jquery.min.js
  b. Boostrap https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/js/bootstrap.min.js
  c. Bootsrap CSS https://maxcdn.bootstrapcdn.com/bootstrap/3.3.7/css/bootstrap.min.css

###Steps

This enables the web application on localhost:8080 by default

1. Download source code and run ```mvn install``` (this generates the webcalculator1.0.jar file)
2. Run from console ```java -jar target\webcalculator-1.0.jar server webcalculator.yml```

####More settings:
  If the desired port 8080 wants to be replaced, modify the ```server.connector.port``` property on the ```webcalculator.yml``` file.
  
###Postman endpoint catalogue
 1. A catalogue for postman with templates for the endpoints is available trhough this https://www.getpostman.com/collections/abdc14b0f2ea89ff1634
 2. Only required to use the option "import" in Postman Chrome extension (https://chrome.google.com/webstore/detail/postman/fhbjgbiflinjbdggehcddcbncdddomop)
  




