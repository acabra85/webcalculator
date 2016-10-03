/**
 * Created by Agustin on 10/3/2016.
 */

var SQRT_SYMBOL = 'sqrt (';
var MINUS_SYMBOL = '-';
var OPERATORS = ['+', '*', '/', MINUS_SYMBOL, '[', ']', '{', '}', '(', ')', SQRT_SYMBOL];
var sqrtIndex = OPERATORS.indexOf(SQRT_SYMBOL);
var minusIndex = OPERATORS.indexOf(MINUS_SYMBOL);
var VALID_CODES = [40, 41, 42, 43, 45, 46, 47, 48, 49, 50, 51, 52, 53, 54, 55, 56, 57, 91, 93, 123, 125];
var VALID_KEYS = ['(', ')', '*', '+', MINUS_SYMBOL, '.', '/', '0', '1', '2', '3', '4', '5', '6', '7', '8', '9', '[', ']', '{', '}'];
var AVAILABLE_FUNCTIONS = ['e^x'];
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
var inputStackSizes = [];

var STACK_ADD = 0;
var STACK_REMOVE = 1;
var STACK_EMPTY = 2;

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
    $.post('/api/token'
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

function updateHistory() {
    var historyComponent = $('#history-container');
    $.get(encodeURI('/api/history?token=' + token)
    ).done(function (historyResponse) {
        historyComponent.text('');
        historyComponent.append(historyResponse.body.tableHTML);
    }).fail(function (failedResponse) {
        displayError(failedResponse.responseJSON.message);
    }).always(function () {
        allowInteractions = true;
    });
}

function executeCalculationWithToken(expressionText, expressionField, calculatedToken) {
    $.post(encodeURI('/api?token=' + calculatedToken + '&expression=' + expressionText)
    ).done(function (calculationResponse) {
        expressionField.value = calculationResponse.body.result;
        lastButtonEqual = true;
        operateStack(STACK_EMPTY);
    }).fail(function (failedResponse) {
        displayError(failedResponse.responseJSON.message);
    }).always(function () {
        updateHistory();
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

function validIntegralRequestData(req) {
    return req.lowerBound <= req.upperBound
        && req.numberThreads > 0 && req.numberThreads <= 15
        && req.repeatedCalculations > 0 && req.repeatedCalculations < 1000000;
}

function executeExponentialIntegralWithToken(receivedToken, expressionField) {
    var integralRequest = {
        lowerBound: parseFloat($('#lowerbound')[0].value),
        upperBound: parseFloat($('#upperbound')[0].value),
        repeatedCalculations: parseInt($('#repeatedcalculations')[0].value),
        numberThreads: parseInt($('#numthreads')[0].value),
        functionId: integralSelectedFunction
    };
    if (validIntegralRequestData(integralRequest)){
        $.ajax({
            url : encodeURI('api/integral?token=' + receivedToken),
            type: 'POST',
            dataType : "json",
            data:  JSON.stringify(integralRequest),
            headers: {
                'Content-Type': 'application/json'
            }
        }).fail(function (response) {
            displayError('unable to calculate integral ' + response.messsage);
        }).done(function (integralResponse) {
            console.log(integralResponse.body.description + ' => ' + integralResponse.body.result + ' #' + integralRequest.repeatedCalculations);
            expressionField.value = integralResponse.body.result;
            lastButtonEqual = true;
        }).always(function () {
            updateHistory();
        });
    } else {
        displayError('invalid integral request, please verify the constraints for the fields');
        allowInteractions = true;
    }

}

function useFunction(num) {
    $('#selected_function')[0].value = AVAILABLE_FUNCTIONS[num];
    integralSelectedFunction = num;
}
