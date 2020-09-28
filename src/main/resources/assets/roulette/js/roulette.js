"use strict";

let token = null;
let colorsMap = null;

let Main = (function () {
    let Renderer = function () {
        const GREEN = 0;
        const RED = 1;
        const BLACK = 2;
        const idToColorMap = new Map([[GREEN, "green"],[RED, "red"],[BLACK, "black"]]);
        const EMPTY_CELL = '<td class="board-cell"></td>';

        function updateResultCard(resultCard, currNumber) {
            resultCard.removeClass();
            resultCard.addClass("card");
            resultCard.addClass(idToColorMap.get(colorsMap.get(currNumber))+'-cell');
            $('#lastnumber').html(currNumber);
        }

        function updateHistory(lastNumber, resultCard) {
            let row = $('<tr></tr>');
            let cell = $('<td class="board-cell"></td>');
            cell.html(lastNumber);
            if (colorsMap.get(lastNumber) === BLACK) {
                cell.addClass("black-cell");
                resultCard.addClass("black-cell");
                row.append(EMPTY_CELL);
                row.append(cell);
                row.append(EMPTY_CELL);
            } else if (colorsMap.get(lastNumber) === RED) {
                cell.addClass("red-cell");
                resultCard.addClass("red-cell");
                row.append(cell);
                row.append(EMPTY_CELL);
                row.append(EMPTY_CELL);
            } else {
                cell.addClass("green-cell");
                resultCard.addClass("green-cell");
                row.append(EMPTY_CELL);
                row.append(EMPTY_CELL);
                row.append(cell);
            }
            row.prependTo("#history > tbody");
        }

        function updateTags(number) {
            let tags = $('#tags');
            tags.children().hide();
            if (number > 0) {
                tags.find(number % 2 === 0 ? '#tag-even' : '#tag-odd').show();
                tags.find(colorsMap.get(number) === RED ? '#tag-red' : '#tag-black').show();
                tags.find(number > 18 ? '#tag-high' : '#tag-low').show();
            }
        }

        let updateView = function (body) {
            if (body.history.length > 0) {
                let lastNumber = body.history[body.history.length - 1];
                let resultCard = $('#result-card');
                updateResultCard(resultCard, lastNumber, body.history.length > 1 ? body.history[body.history.length - 2] : -1);
                updateTags(lastNumber);
                updateHistory(lastNumber, resultCard);
                $("#history").show();
            }
        };

        return {
            updateView: updateView
        };
    };

    let Roulette = function () {
        return {
            SpinRoulette: function() {
                let q = $.Deferred();
                $.ajax({
                    type: "POST",
                    url: '/api/roulette/spin',
                    data: JSON.stringify({token: token}),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                })
                    .done(function(res) {
                        if(!res.failure) {
                            renderer.updateView(res.body);
                        }
                    })
                    .fail(function(res) {
                        console.log(res);
                    });
                return q.promise();
            },
            SendNumber: function (num) {
                let q = $.Deferred();
                $.ajax({
                    type: "POST",
                    url: encodeURI('/api/roulette/submit?number=' + num),
                    data: JSON.stringify({token: token}),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                })
                    .done(function(res) {
                        if(!res.failure) {
                            renderer.updateView(res.body);
                        }
                    })
                    .fail(function(res) {
                        console.log(res);
                    });
                return q.promise();
            }
        };
    };

    let Alerts = function() {
        return {
            showError: function (msg, millis) {
                let div = $("#error-alert");
                div.html(msg);
                div.show();
                div.fadeTo(millis, 500).slideUp(500, function(){
                    div.slideUp(500);
                });
            }
        };
    };

    let roulette = Roulette();
    let alerts = Alerts();
    let renderer = Renderer();

    let addButton = $("#add-button");
    let spinButton = $("#spin-button");

    return {
        spinRoulette: function () {
            if(token != null) {
                spinButton.attr('disabled', 'disabled');
                roulette.SpinRoulette();
                spinButton.removeAttr('disabled');
            }
        },
        sendNumber: function (evt) {
            evt.preventDefault();
            if(token != null) {
                addButton.attr('disabled', 'disabled');
                if($("#rolled").val() && $("#rolled").val().trim().length > 0) {
                    let num = parseInt($("#rolled").val());
                    if (num >= 0 && num <= 36) {
                        roulette.SendNumber(num);
                    } else {
                        alerts.showError("Invalid: Numbers go from 0 to 36", 1500);
                    }
                } else {
                    alerts.showError("Invalid: Number to send must not be empty", 1500);
                }
                addButton.removeAttr('disabled');
            }
        }
    };
})();

$(document).ready(function(){
    function retrieveToken() {
        let q = $.Deferred();
        $.when($.get('/api/roulette/config')).then(function (data, textStatus, jqXHR) {
            if (jqXHR.status === 200) {
                token = data.body.token;
                colorsMap = new Map(JSON.parse(data.body.configMap));
            }
            q.resolve({success: token !== null});
        }, function (a, b, c) {
            q.resolve({success: false});
        });
        return q.promise();
    }
    retrieveToken().then(function(data) {
        if (data.success) {
            $("#add-button").removeAttr('disabled');
            $("#spin-button").removeAttr('disabled');
        }
    });
    $('#roulette-form').submit(Main.sendNumber);
});
