/**
 * Created by Agustin on 10/3/2016.
 */

var SQRT_SYMBOL = 'sqrt (';
var POLINOMIAL_FUNCTION_STR = 'x^n';
var MINUS_SYMBOL = '-';
var OPERATORS = ['+', '*', '/', MINUS_SYMBOL, '[', ']', '{', '}', '(', ')', SQRT_SYMBOL];
var sqrtIndex = OPERATORS.indexOf(SQRT_SYMBOL);
var minusIndex = OPERATORS.indexOf(MINUS_SYMBOL);
var VALID_CODES = [40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 91, 93, 123, 125];
var VALID_KEYS = ['(', ')', '*', '+', MINUS_SYMBOL, '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '[', ']', '{', '}'];
var AVAILABLE_FUNCTIONS = ['e^x', POLINOMIAL_FUNCTION_STR, 'ln(x)', '1/x'];
var SIMPSONS_METHOD_STR = 'Simpson\'s Rule (Cavalieri and Gregory)';
var RIEMANN_METHOD_STR = 'Riemann Rectangles';
var AVAILABLE_APPROXIMATION_METHODS = [RIEMANN_METHOD_STR, SIMPSONS_METHOD_STR, 'Gaussian Quadrature'];
var ENTER_KEY = 'Enter';
var ERROR_RESULTS = ['INFINITY', 'NAN', '-INFINITY'];
var ENTER_CODE = 13;
var ESC_KEY = 27;


var token = "";
var lastButtonEqual = true;
var lastButtonOperator = false;
var activePanel = 0;
var panels = [];
var errorContainer = null;
var ERROR_FADE_TIME = 8000;
var allowInteractions = true;
var integralSubmit = null;
var expressionField = null;
var integralSelectedFunction = 0;
var approximationSelectedMethod = 0;
var inputStackSizes = [];
var divPolynomialOptions = null;
var divMethodOptions = null;
var coefficientsFieldSelector = null;

var STACK_ADD = 0;
var STACK_REMOVE = 1;
var STACK_EMPTY = 2;
var functionsElement = null;
var methodsElement = null;
var prevInputSpace = false;
var prevInputMinus = false;
var prevNumber = false;
var regexSpaces = new RegExp(' ', 'g');

function operateStack(operation, num) {
    if (operation === STACK_ADD) {
        inputStackSizes.push(num);
        return 0;
    } else if (operation === STACK_REMOVE) {
        return inputStackSizes.pop();
    } else if (operation === STACK_EMPTY) {
        inputStackSizes.length = 0;
        return -1;
    }
    return -2;
}

function initIntegralSelectedFunction() {
    if (functionsElement === null) {
        functionsElement = $('#selected_function');
    }
    return functionsElement;
}

function initApproximationMethodsElement() {
    if (methodsElement === null) {
        methodsElement = $('#selected_approximation_method');
    }
    return methodsElement;
}

function initCoefficientsField() {
    if (coefficientsFieldSelector === null) {
        coefficientsFieldSelector = $('#coefficients');
    }
    return coefficientsFieldSelector;
}

function initPolynomialOptions() {
    if (divPolynomialOptions === null) {
        divPolynomialOptions = $('#polynomial_options');
    }
}

function initApproximationOptions() {
    if (divMethodOptions === null) {
        divMethodOptions = $('#method_options');
    }
}

function initIntegralSubmit() {
    if (integralSubmit === null) {
        integralSubmit = $('#sendintegral');
    }
}

function initExpressionField() {
    if (expressionField === null) {
        expressionField = $('#expression')[0];
    }
}
function buildAndAppendErrorDiv(errorContainer, errorMessage) {
    var rand = Math.random() + '';
    var newErrorId =  "error_" + rand.substr(rand.length - 5);
    errorContainer.append('<div class="alert alert-danger ' + newErrorId + '" role="alert" id="' +  newErrorId + '"><b>Error: </b>' + errorMessage + '</div>');
    return newErrorId;
}

function displayError(errorMessage) {
    if (errorContainer === null) {
        errorContainer = $('#error_messages');
    }
    var newErrorId = buildAndAppendErrorDiv(errorContainer, errorMessage);
    $('#' + newErrorId).fadeOut(ERROR_FADE_TIME, function () {
        $('div').remove('.' + newErrorId);
    });
}

function retrieveSessionToken() {
    var q = $.Deferred();
    $.post('/api/calculator/token'
    ).done(function (response) {
        result = response.body.token;
        q.resolve(result);
    }).fail(function (failedResponse) {
        displayError("No connection with the server");
        q.reject();
    });
    return q.promise();
}

function blurIfAvailable(comp) {
    try {
        if (comp && !comp.hasOwnProperty('blur')) {
            $('#' + comp).blur();
        }
    } catch(e) {
        console.log(e);
    }
}

function append(comp, s, override) {
    var consecutiveDots = function (value) {
        var split = value.split(' ');
        for (var i = 0; i < split.length; i++) {
            if (split[i].indexOf('..') >= 0) {
                return true;
            }
        }
        return false;
    };
    if (allowInteractions || override) {
        var opIndex = OPERATORS.indexOf(s);
        lastButtonOperator = opIndex >= 0;
        initExpressionField();
        var lastAddedValue = null;
        expressionField.value = ERROR_RESULTS.indexOf(expressionField.value.toUpperCase()) >= 0 ? '0' : expressionField.value;
        if (lastButtonEqual) {
            if (lastButtonOperator) {
                var binaryOperator = opIndex !== sqrtIndex;
                if (binaryOperator) {
                    lastAddedValue = ' ' + s + ' ';
                    expressionField.value = expressionField.value + lastAddedValue;
                } else {
                    lastAddedValue = s + ' ';
                    expressionField.value = lastAddedValue;
                }
            } else {
                lastAddedValue = s;
                expressionField.value = lastAddedValue;
            }
        } else {
            if (lastButtonOperator){
                lastAddedValue = ' ' + s + ' ';
                expressionField.value = expressionField.value + lastAddedValue;
            } else {
                lastAddedValue = s;
                expressionField.value = expressionField.value + lastAddedValue;
            }
        }
        if (consecutiveDots(expressionField.value)) {
            expressionField.value = expressionField.value.substr(0, expressionField.value.length - 1);
        } else {
            operateStack(STACK_ADD, lastAddedValue.length);
        }
        lastButtonEqual = false;
        blurIfAvailable(comp);
    }
}


var historyComponent = null;
function initHistoryComponent() {
    if(historyComponent === null) {
        historyComponent = $('#history-container'); 
    }
    return historyComponent;
}

function updateHistory() {
    var historyComponent = initHistoryComponent();
    $.get(encodeURI('/api/calculator/renderedhistory?token=' + token)
    ).done(function (historyResponse) {
        historyComponent.text('');
        historyComponent.append(historyResponse.body.renderedTable);
    }).fail(function (failedResponse) {
        displayError(failedResponse.responseJSON.message);
    }).always(function () {
        allowInteractions = true;
    });
}

function executeCalculationWithToken(expressionText, expressionField, calculatedToken) {
    $.post(encodeURI('/api/calculator?token=' + calculatedToken + '&expression=' + expressionText)
    ).done(function (calculationResponse) {
        expressionField.value = calculationResponse.body.result;
        lastButtonEqual = true;
        operateStack(STACK_EMPTY);
        updateHistory();
    }).fail(function (failedResponse) {
        displayError(failedResponse.responseJSON.message);
        allowInteractions = true;
    });
}

function calculate() {
    $('#bcalculate').blur();
    if (allowInteractions) {
        allowInteractions = false;
        initExpressionField();
        var expressionText = encodeURIComponent(expressionField.value);
        if (token.length > 0) {
            executeCalculationWithToken(expressionText, expressionField, token);
        } else {
            retrieveSessionToken().then(function (resultToken) {
                token = resultToken;
                executeCalculationWithToken(expressionText, expressionField, token);
            }, function () {
                allowInteractions = true;
            });
        }
    }
}

function clearResult() {
    lastButtonEqual = true;
    operateStack(STACK_EMPTY);
    initExpressionField();
    expressionField.value = 0;
    $('#clear_result').blur();
}

function displayHistory() {
    if (token.length > 0) {
        var historyComponent = $('#history-container');
        if (historyComponent[0].style.display === 'none') {
            historyComponent[0].style.display = 'block'
        } else {
            historyComponent[0].style.display = 'none'
        }
    }
    $('#display_history').blur();
}

function clearExpressionRecord() {
    initExpressionField();
    expressionField.value = '0';
    lastButtonEqual = true;
    operateStack(STACK_EMPTY);

}

function howManyDigitsToRemove(value, len) {
    if (value.charAt(0) !== MINUS_SYMBOL) {
        return 1;
    } else {
        if (len > 2) {
            return 1;
        }
        return 2;
    }
}

function removeLastCharacter() {
    initExpressionField();
    var expLength = expressionField.value.length;
    if (lastButtonEqual) {
        if (ERROR_RESULTS.indexOf(expressionField.value.toUpperCase()) >= 0) {
            clearExpressionRecord();
        } else {
            expressionField.value = expressionField.value.substr(0, expLength - howManyDigitsToRemove(expressionField.value, expLength));
        }
    } else {
        expressionField.value = expressionField.value.substr(0, expLength - operateStack(STACK_REMOVE));
    }
    if (expressionField.value.length === 0) {
        clearExpressionRecord();
    }
    $('#remove_last').blur();
}

function initCalculationPanels() {
    if (panels.length === 0) {
        panels = [$('#main_calculator_panel'), $('#integral_calculator_panel')];
    }
}
function evaluateKeyPressed(eventCaptured) {
    initCalculationPanels();
    var code = ('charCode' in eventCaptured) ? eventCaptured.charCode : eventCaptured.keyCode;
    var index = VALID_CODES.indexOf(code);
    if (code === ESC_KEY) {
        clearResult();
    }
    if (activePanel === 0) {
        if (index >= 0) {
            append(this, VALID_KEYS[index], true);
        } else if (code === ENTER_CODE) {
            calculate();
        }
    }
}

initCoefficientsField().keypress( function(e) {
    var chr = String.fromCharCode(e.which);
    var index = ' -0123456789'.indexOf(chr);
    if(index < 0) {
        return false;
    } else if (index === 0 && prevInputSpace) {
        return false;
    } else if (index === 1 && prevInputMinus) {
        return false;
    } else if (index === 0 && prevInputMinus) {
        return false;
    } else if (index > 1 || index === 1 && (prevNumber || prevInputSpace) || index === 0 && prevNumber) {
        prevInputSpace = index === 0;
        prevInputMinus = index === 1;
        prevNumber = index > 1;
        return true;
    } else {
        return false;
    }
});

initCoefficientsField().keyup( function(e) {
    if ((e.which === 8 || e.which == 46) && e.target.value.length > 0) {
        var chr = e.target.value.charAt(e.target.value.length-1);
        var index = ' -0123456789'.indexOf(chr);
        prevInputSpace = index === 0;
        prevInputMinus = index === 1;
        prevNumber = index > 1;
    }
    if (e.target.value.length === 0) {
        prevInputSpace = false;
        prevInputMinus = false;
        prevNumber = false;
    }
});

function toggleBasicIntegralPanels(num) {
    initCalculationPanels();
    if (activePanel !== num) {
        clearExpressionRecord();
        panels[activePanel].fadeOut();
        panels[activePanel][0].style.display = 'none';
        activePanel = num;
        panels[activePanel].fadeIn();
        panels[activePanel][0].style.display = 'block';
    }
}

function requestExponentialIntegral() {
    if (allowInteractions) {
        allowInteractions = false;
        initIntegralSubmit();
        initExpressionField();
        integralSubmit.blur();
        if (token.length > 0) {
            executeExponentialIntegralWithToken(token, expressionField);
        } else {
            retrieveSessionToken().then(function (resultToken) {
                token = resultToken;
                executeExponentialIntegralWithToken(token, expressionField);
            }, function () {
                allowInteractions = true;
            });
        }
    }
}

function hasOnlyNumbers(list) {
    var i = 0;
    var onlyNumbers = true;
    for(i = 0; i < list.length && onlyNumbers;i++) {
        try {
            parseFloat(list[i]);
        }catch (e) {
            onlyNumbers = false;
        }
    }
    return onlyNumbers;
}
function validIntegralRequestData(req) {
    var amountThreads = req.numberThreads > 0 && req.numberThreads <= 15;
    var repeatedCalculations = req.repeatedCalculations > 0 && req.repeatedCalculations <= 2147483647;
    var validCoefficients = hasOnlyNumbers(req.coefficients);
    var validRepeatedCalculationsForMethod = AVAILABLE_APPROXIMATION_METHODS[req.approximationMethodId] === SIMPSONS_METHOD_STR ? req.repeatedCalculations%2==0 : true;
    if (!amountThreads) {
        displayError('invalid integral request, the amount of threads is invalid must be in range [1, 15]');
    }
    if (amountThreads && !repeatedCalculations) {
        displayError('invalid integral request, the amount of repeatedCalculations is invalid must be in range [1, 2147483647]');
    }
    if (amountThreads && repeatedCalculations && !validCoefficients) {
        displayError('invalid integral request, the coefficients must contain only numbers');
    }
    if (amountThreads && repeatedCalculations && validCoefficients && !validRepeatedCalculationsForMethod) {
        displayError('invalid integral request, repeated calculations for ' + SIMPSONS_METHOD_STR + ' must be an even number');
    }
    return amountThreads && repeatedCalculations && validCoefficients && validRepeatedCalculationsForMethod;
}

function retrieveJsonArray(value) {
    var valueAsArray = value.length > 0 ? (value.indexOf(' ') >= 0 ? value.trim().replace(regexSpaces, ',') : value.trim ) : '';
    var coefficients = JSON.parse('[' + valueAsArray + ']');
    return coefficients.constructor === Array ? coefficients : [];
}

function buildIntegralRequest() {
    initCoefficientsField();
    var integralRequest = {
        lowerLimit: parseFloat($('#lowerbound')[0].value),
        upperLimit: parseFloat($('#upperbound')[0].value),
        repeatedCalculations: parseInt($('#repeatedcalculations')[0].value),
        numberThreads: parseInt($('#numthreads')[0].value),
        functionId: integralSelectedFunction,
        approximationMethodId: approximationSelectedMethod,
        areaInscribed: approximationSelectedMethod == 0 && $('#inscribed_rectangles')[0].checked,
        coefficients: integralSelectedFunction == 1 ? retrieveJsonArray(coefficientsFieldSelector[0].value) : []
    };
    return integralRequest;
}
function executeExponentialIntegralWithToken(receivedToken, expressionField) {
    var integralRequest = buildIntegralRequest();
    if (validIntegralRequestData(integralRequest)) {
        $.ajax({
            url : encodeURI('/api/calculator/integral?token=' + receivedToken),
            type: 'POST',
            dataType : "json",
            data:  JSON.stringify(integralRequest),
            headers: {
                'Content-Type': 'application/json'
            }
        }).done(function (integralResponse) {
            console.log(integralResponse.body.description + ' => ' + integralResponse.body.result + ' #' + integralRequest.repeatedCalculations);
            expressionField.value = integralResponse.body.approximation;
            lastButtonEqual = true;
            updateHistory();
        }).fail(function (response) {
            displayError('calculating integral: ' + response.responseJSON.message);
            allowInteractions = true;
        });
    } else {
        //displayError('invalid integral request, please verify the constraints for the fields');
        allowInteractions = true;
    }

}
function displayFunctionOptions(num) {
    initPolynomialOptions();
    if (AVAILABLE_FUNCTIONS[num] === POLINOMIAL_FUNCTION_STR) {
        divPolynomialOptions.fadeIn();
    } else{
        divPolynomialOptions.fadeOut();
    }

    
}

function useFunction(num) {
    initIntegralSelectedFunction()[0].value = AVAILABLE_FUNCTIONS[num];
    integralSelectedFunction = num;
    displayFunctionOptions(num);
}
function displayApproximationMethodOptions(num) {
    initApproximationOptions();
    if (AVAILABLE_APPROXIMATION_METHODS[num] === RIEMANN_METHOD_STR) {
        divMethodOptions.fadeIn();
    } else {
        divMethodOptions.fadeOut();
    }

}
function useApproximationMethod(num) {
    initApproximationMethodsElement()[0].value = AVAILABLE_APPROXIMATION_METHODS[num];
    approximationSelectedMethod = num;
    displayApproximationMethodOptions(num);
}
