"use strict";

let token = !localStorage.getItem('sessid') ? null : localStorage.getItem('sessid');
let roomNumberStr = localStorage.getItem('room_number');

let Main = (function () {
    function MMind() {
        return {
            SendNumber: function (guess) {
                let q = $.Deferred();
                $.ajax({
                    type: "POST",
                    url: encodeURI('/api/mmind/submit'),
                    data: JSON.stringify({
                        token: token,
                        roomNumber: roomNumberStr,
                        guess: guess
                    }),
                    contentType: "application/json; charset=utf-8",
                    dataType: "json"
                }).done(function (res) {
                    if (!res.failure) {
                        renderer.updateUserHistory(res.moveResult);
                    }
                    q.resolve(res);
                }).fail(function (res) {
                    console.log(res);
                    q.resolve();
                });
                return q.promise();
            }
        };
    }

    function Renderer() {
        function appendHistory(tableBodySelector, moveResult) {
            let node = $(document.createElement('tr'));
            let guess = moveResult.guess;
            for (let i = 0; i < guess.length; ++i) {
                let column = $(document.createElement('th'));
                column.html(guess.charAt(i));
                node.append(column);
            }
            let resultColumn = $(document.createElement('th'));
            let fixesNode = $(document.createElement('span'));
            fixesNode.html('F:' + moveResult.fixes);
            resultColumn.append(fixesNode);

            let spikesNode = $(document.createElement('span'));
            spikesNode.html('P: ' + moveResult.spikes);
            resultColumn.append(spikesNode);

            node.append(resultColumn);
            $(tableBodySelector).append(node);
        }

        let updateUserHistory = function (move) {
            console.log('updating view');
            appendHistory('#user_table tbody', move);
        };

        let updateOpponentsMove = function (move) {
            console.log('update opponent\'s move');
            appendHistory('#opponent_table tbody', move);
        };

        return {
            updateUserHistory: updateUserHistory,
            updateOpponentsMove: updateOpponentsMove
        };
    }

    let Alerts = function () {
        return {
            showError: function (msg, millis) {
                let div = $("#error-alert");
                div.html(msg);
                div.show();
                div.fadeTo(millis, 500).slideUp(500, function () {
                    div.slideUp(500);
                });
            }
        };
    };

    let mmind = MMind();
    let renderer = Renderer();
    let alerts = Alerts();

    let timeoutVar = null;

    function retrieveStatus() {
        $.get(encodeURI('/api/mmind/status?token=' + token + '&room='+ roomNumberStr))
            .done(function (statusResponse) {
                if(statusResponse.makeMove) {
                    if(statusResponse.lastMove)
                    renderer.updateOpponentsMove(statusResponse.lastMove);
                    $('#btn_guess').prop('disabled', false);
                    stopFunction();
                }
            })
            .fail(function (failedResponse) {
                alerts.showError('failed to retrieve status: ' + failedResponse);
            })
            .always(function () {
                console.log('retrieved status');
            });
    }

    function stopFunction(){
        clearTimeout(timeoutVar);
    }

    let cycleRefresh = function () {
        retrieveStatus();
        timeoutVar = setTimeout(cycleRefresh, 5000);
    };
    let sendNumber = function (evt) {
        evt.preventDefault();
        if (token != null) {
            let guessBtn = $("#btn_guess");
            let guessVal = $('#guess_value');
            guessBtn.prop('disabled', true);
            if (guessVal.val() && guessVal.val().trim().length > 0) {
                let num = parseInt(guessVal.val());
                if (num >= 0 && num <= 9999) {
                    mmind.SendNumber(num).then(function () {
                        guessVal.val('');
                        cycleRefresh();
                    });
                } else {
                    alerts.showError("Invalid: Numbers go from 0000 to 9999", 1500);
                    guessBtn.prop('disabled', false);
                }
            } else {
                alerts.showError("Invalid: Number to send must not be empty", 1500);
                guessBtn.prop('disabled', false);
            }
        }
    };

    return {
        sendNumber: sendNumber,
        cycleRefresh: cycleRefresh
    };
})();

$(document).ready(function () {
    if (!token) {
        window.alert('SORRY: we\'re unable to connect to the server please refresh the page...')
        window.location.replace("room/index.html");
        return;
    }
    $('#mmind_form').submit(Main.sendNumber);

    function addRowOwnNumber() {
        let item = localStorage.getItem('ownsecret');
        let targetRow = $('#opponent_number_guess');
        for (let i = 0; i < item.length; ++i) {
            let child = $('<th>');
            child.html(item.charAt(i));
            targetRow.append(child);
        }
    }
    addRowOwnNumber();
    console.log('JOINEDDDDD!!!!');
    Main.cycleRefresh();
});