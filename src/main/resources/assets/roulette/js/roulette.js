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
            resultCard.addClass(idToColorMap.get(colorsMap[currNumber])+'-cell');
            $('#lastnumber').html(currNumber);
        }

        function updateHistory(lastNumber, resultCard) {
            let row = $('<tr></tr>');
            let cell = $('<td class="board-cell"></td>');
            let divContent = $('<div></div>');
            divContent.hide();
            if (colorsMap[lastNumber] === BLACK) {
                divContent.append('<button type="button" class="btn btn-dark btn-sm btn-block">' + lastNumber + '</button>');
                cell.append(divContent);
                resultCard.addClass("black-cell");
                row.append(EMPTY_CELL);
                row.append(cell);
                row.append(EMPTY_CELL);
            } else if (colorsMap[lastNumber] === RED) {
                divContent.append('<button type="button" class="btn btn-danger btn-sm btn-block">' + lastNumber + '</button>');
                cell.append(divContent);
                resultCard.addClass("red-cell");
                row.append(cell);
                row.append(EMPTY_CELL);
                row.append(EMPTY_CELL);
            } else {
                divContent.append('<button type="button" class="btn btn-success btn-sm btn-block">' + lastNumber + '</button>');
                cell.append(divContent);
                resultCard.addClass("green-cell");
                row.append(EMPTY_CELL);
                row.append(EMPTY_CELL);
                row.append(cell);
            }
            row.prependTo("#history > tbody");
            divContent.fadeIn("slow");

        }

        function updateTags(number, stats) {
            let tags = $('#tags');
            tags.children().hide();

            tags.find('#tag-even span').text(stats.even);
            tags.find('#tag-odd span').text(stats.odd);
            tags.find('#tag-zero span').text(stats.zero);
            tags.find('#tag-red span').text(stats.red);
            tags.find('#tag-black span').text(stats.black);
            tags.find('#tag-high span').text(stats.high);
            tags.find('#tag-low span').text(stats.low);

            $('.stats-badge').each(function(idx, e) {
                if(e.id === 'even_fld_badge') $(e).text(stats.even);
                if(e.id === 'odd_fld_badge') $(e).text(stats.odd);
                if(e.id === 'zero_fld_badge') $(e).text(stats.zero);
                if(e.id === 'red_fld_badge') $(e).text(stats.red);
                if(e.id === 'black_fld_badge') $(e).text(stats.black);
                if(e.id === 'high_fld_badge') $(e).text(stats.high);
                if(e.id === 'low_fld_badge') $(e).text(stats.low);
                if(e.id === 'first12_fld_badge') $(e).text(stats.first);
                if(e.id === 'second12_fld_badge') $(e).text(stats.second);
                if(e.id === 'third12_fld_badge') $(e).text(stats.third);
            });

            if (number > 0) {
                tags.find(number % 2 === 0 ? '#tag-even' : '#tag-odd').fadeIn("slow");
                tags.find(colorsMap[number] === RED ? '#tag-red' : '#tag-black').fadeIn("slow");
                tags.find(number > 18 ? '#tag-high' : '#tag-low').fadeIn("slow");
            } else {
                tags.find('#tag-zero').fadeIn("slow");
            }
        }

        function buildButton(strClass, value, icon) {
            let badgeType = value === 0 ? 'success' : (colorsMap[value] === RED ? 'danger' : 'dark');
            let contents = '<span class="badge badge-pill badge-' + badgeType + '">' + value + '</span>';
            return '<button type="button" class="btn'+ (strClass ? ' ' + strClass : '') + '">'+ icon + contents +'</button>'
        }

        function updateHotNumbers(hotNumbers) {
            $('#hot_nums_table tbody tr').remove();
            hotNumbers.forEach((hotNum) => $('#hot_nums_table tbody').append('<tr><td>' + buildButton('btn-danger', hotNum, '🔥') + '</td></tr>'));
        }

        function updateColdNumbers(coldNumbers) {
            $('#cold_nums_table tbody tr').remove();
            coldNumbers.forEach((coldNum) => $('#cold_nums_table tbody').append('<tr><td>' + buildButton('btn-info', coldNum, '❄') + '</td></tr>'));
        }

        function duplicateValue(field) {
            field.val(field.val() * 2);
        }

        function clearIfWin(fld) {
            if(fld.val() > 0) {
                fld.val(0);
            }
        }
        function duplicateIfSet(fld) {
            if(fld.val() > 0) {
                duplicateValue(fld);
            }
        }

        function fiftyChance(isDuplicateAll, criteria, fldCriteria1, oppositeField) {
            if(isDuplicateAll) {
                duplicateAll([fldCriteria1, oppositeField]);
                return;
            }
            if (criteria) {
                clearIfWin(fldCriteria1);
                duplicateIfSet(oppositeField);
            } else {
                clearIfWin(oppositeField);
                duplicateIfSet(fldCriteria1);
            }
        }

        function duplicateAll(fields) {
            fields.forEach(f => duplicateValue(f));
        }

        function thirtyThirdChance(isDuplicateAll, criteria1, criteria2, fldCriteria1, fldCriteria2, oppositeField) {
            if(isDuplicateAll) {
                duplicateAll([fldCriteria1, fldCriteria2, oppositeField]);
                return;
            }
            if (criteria1) {
                clearIfWin(fldCriteria1);
                duplicateIfSet(fldCriteria2);
                duplicateIfSet(oppositeField);

            } else if (criteria2) {
                clearIfWin(fldCriteria2);
                duplicateIfSet(fldCriteria1);
                duplicateIfSet(oppositeField);
            } else {
                clearIfWin(oppositeField);
                duplicateIfSet(fldCriteria1);
                duplicateIfSet(fldCriteria2);
            }
        }

        function executeBets(num) {
            let black_fld = $('#black_fld');
            let red_fld = $('#red_fld');
            let even_fld = $('#even_fld');
            let odd_fld = $('#odd_fld');
            let high_fld = $('#high_fld');
            let low_fld = $('#low_fld');
            let first12_fld = $('#first12_fld');
            let second12_fld = $('#second12_fld');
            let third12_fld = $('#third12_fld');

            let isGreen = colorsMap[num] === GREEN;
            fiftyChance(isGreen, num % 2 === 0, even_fld, odd_fld);
            fiftyChance(isGreen, colorsMap[num] === RED, red_fld, black_fld);
            fiftyChance(isGreen, num < 19, low_fld, high_fld);
            thirtyThirdChance(isGreen, num > 0 && num < 13, num > 12 && num < 25, first12_fld, second12_fld, third12_fld);
        }

        let updateView = function (body) {
            if (body.number >= 0) {
                let resultCard = $('#result-card');
                updateResultCard(resultCard, body.number);
                updateTags(body.number, $('#window_stats').prop('checked') ? body.statsWindow : body.stats);
                updateHistory(body.number, resultCard);
                updateHotNumbers(body.hotNumbers);
                updateColdNumbers(body.coldNumbers);
                executeBets(body.number);
            }
        };

        return {
            updateView: updateView
        };
    };

    let Roulette = function () {
        function rouletteSpeed(speed) {
            let step = speed / 5;
            return speed - ($('#roulette-speed').val() * step) + 100;
        }

        let spinRoulette = function() {
            let q = $.Deferred();


            $.ajax({
                type: "POST",
                url: '/api/roulette/spin',
                data: JSON.stringify({token: token}),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(res) {
                window.setTimeout(function () {
                    if(!res.failure) {
                        let img_result = $('#0' + res.body.number + '_img');
                        $('#roulette-animation').hide();
                        img_result.show();
                        $('#roulette-number').show();
                        renderer.updateView(res.body);
                        window.setTimeout(function () {
                            $('#roulette-number').hide();
                            img_result.hide();
                            $('#result-card').fadeIn();
                            q.resolve();
                        }, rouletteSpeed(1100));
                    } else {
                        q.resolve();
                    }
                }, rouletteSpeed(1900));
            }).fail(function(res) {
                console.log(res);
                q.resolve();
            });
            return q.promise();
        };

        let sendNumber = function (num) {
            let q = $.Deferred();
            $.ajax({
                type: "POST",
                url: encodeURI('/api/roulette/submit?number=' + num),
                data: JSON.stringify({token: token}),
                contentType: "application/json; charset=utf-8",
                dataType: "json"
            }).done(function(res) {
                if(!res.failure) {
                    renderer.updateView(res.body);
                }
                q.resolve();
            })
                .fail(function(res) {
                    console.log(res);
                    q.resolve();
                });
            return q.promise();
        };
        return {
            SpinRoulette: spinRoulette,
            SendNumber: sendNumber
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

    return {
        spinRoulette: function () {
            if(token != null) {
                $("#spin-button").prop('disabled', true);
                $('#result-card').hide();
                $('#roulette-animation').fadeIn();
                roulette.SpinRoulette().then(function () {
                    $("#spin-button").prop('disabled', false);
                });
            }
        },
        sendNumber: function (evt) {
            evt.preventDefault();
            if(token != null) {
                let addButton = $("#add-button");
                let rolled = $('#rolled');
                addButton.prop('disabled', true);
                if(rolled.val() && rolled.val().trim().length > 0) {
                    let num = parseInt(rolled.val());
                    if (num >= 0 && num <= 36) {
                        roulette.SendNumber(num).then(function() {
                            window.setTimeout(function() {
                                rolled.val('');
                                addButton.prop('disabled', false)
                            }, 500);
                        });
                    } else {
                        alerts.showError("Invalid: Numbers go from 0 to 36", 1500);
                        addButton.prop('disabled', false);
                    }
                } else {
                    alerts.showError("Invalid: Number to send must not be empty", 1500);
                    addButton.prop('disabled', false);
                }
            }
        },
        clearBets: function () {
            $('#black_fld').val(0);
            $('#red_fld').val(0);
            $('#even_fld').val(0);
            $('#odd_fld').val(0);
            $('#high_fld').val(0);
            $('#low_fld').val(0);
            $('#first12_fld').val(0);
            $('#second12_fld').val(0);
            $('#third12_fld').val(0);
        }
    };
})();

$(document).ready(function(){
    function retrieveToken() {
        let q = $.Deferred();
        $.when($.get('/api/roulette/config')).then(function (data, textStatus, jqXHR) {
            if (jqXHR.status === 200) {
                token = data.body.token;
                colorsMap = data.body.colorsMap;
            }
            q.resolve({success: token !== null});
        }, function (a, b, c) {
            q.resolve({success: false});
        });
        return q.promise();
    }
    retrieveToken().then(function(tokenResult) {
        if (tokenResult && tokenResult.success) {
            $('#roulette-form').submit(Main.sendNumber);
            $('#spin-button').click(Main.spinRoulette);
            $('#toggle-controls').click(function() {
                $('#other-controls').toggle();
            });
            $("#add-button").prop('disabled', false);
            $("#spin-button").prop('disabled', false);
            $("#toggle-controls").prop('disabled', false);
            $('#clear-flds-button').click(Main.clearBets);
        }
    });
});
