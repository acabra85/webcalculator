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
                        q.resolve(res);
                    } else {
                        q.resolve();
                    }
                }).fail(function (res) {
                    console.log(res);
                    q.resolve();
                });
                return q.promise();
            }
        };
    }

    function Renderer() {
        function addColorGuess(fixes, spikes) {
            return fixes === 4 ? 'status_guess_winner' : spikes === 4 || fixes === 3 ? 'status_guess_close' : '';
        }

        function appendHistory(tableBodySelector, moveResult) {
            let node = $(document.createElement('tr'));
            let guess = moveResult.guess;
            let index_column = $('<th>');
            index_column.html(moveResult.index);
            node.append(index_column)
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

            node.addClass(addColorGuess(moveResult.fixes, moveResult.spikes));
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
        const CLS_MSG_TYPES = ['error_alert', 'your_move_alert', 'info_alert'];
        const opacity = 1500;
        const animationLength = 1500;
        const ERROR = 0;
        const YOUR_MOVE = 1;
        const INFO = 2;

        let beep = function() {
            const BEEP_SOURCE = "data:audio/wav;base64,//uQRAAAAWMSLwUIYAAsYkXgoQwAEaYLWfkWgAI0wWs/ItAAAGDgYtAgAyN+QWaAAihwMWm4G8QQRDiMcCBcH3Cc+CDv/7xA4Tvh9Rz/y8QADBwMWgQAZG/ILNAARQ4GLTcDeIIIhxGOBAuD7hOfBB3/94gcJ3w+o5/5eIAIAAAVwWgQAVQ2ORaIQwEMAJiDg95G4nQL7mQVWI6GwRcfsZAcsKkJvxgxEjzFUgfHoSQ9Qq7KNwqHwuB13MA4a1q/DmBrHgPcmjiGoh//EwC5nGPEmS4RcfkVKOhJf+WOgoxJclFz3kgn//dBA+ya1GhurNn8zb//9NNutNuhz31f////9vt///z+IdAEAAAK4LQIAKobHItEIYCGAExBwe8jcToF9zIKrEdDYIuP2MgOWFSE34wYiR5iqQPj0JIeoVdlG4VD4XA67mAcNa1fhzA1jwHuTRxDUQ//iYBczjHiTJcIuPyKlHQkv/LHQUYkuSi57yQT//uggfZNajQ3Vmz+Zt//+mm3Wm3Q576v////+32///5/EOgAAADVghQAAAAA//uQZAUAB1WI0PZugAAAAAoQwAAAEk3nRd2qAAAAACiDgAAAAAAABCqEEQRLCgwpBGMlJkIz8jKhGvj4k6jzRnqasNKIeoh5gI7BJaC1A1AoNBjJgbyApVS4IDlZgDU5WUAxEKDNmmALHzZp0Fkz1FMTmGFl1FMEyodIavcCAUHDWrKAIA4aa2oCgILEBupZgHvAhEBcZ6joQBxS76AgccrFlczBvKLC0QI2cBoCFvfTDAo7eoOQInqDPBtvrDEZBNYN5xwNwxQRfw8ZQ5wQVLvO8OYU+mHvFLlDh05Mdg7BT6YrRPpCBznMB2r//xKJjyyOh+cImr2/4doscwD6neZjuZR4AgAABYAAAABy1xcdQtxYBYYZdifkUDgzzXaXn98Z0oi9ILU5mBjFANmRwlVJ3/6jYDAmxaiDG3/6xjQQCCKkRb/6kg/wW+kSJ5//rLobkLSiKmqP/0ikJuDaSaSf/6JiLYLEYnW/+kXg1WRVJL/9EmQ1YZIsv/6Qzwy5qk7/+tEU0nkls3/zIUMPKNX/6yZLf+kFgAfgGyLFAUwY//uQZAUABcd5UiNPVXAAAApAAAAAE0VZQKw9ISAAACgAAAAAVQIygIElVrFkBS+Jhi+EAuu+lKAkYUEIsmEAEoMeDmCETMvfSHTGkF5RWH7kz/ESHWPAq/kcCRhqBtMdokPdM7vil7RG98A2sc7zO6ZvTdM7pmOUAZTnJW+NXxqmd41dqJ6mLTXxrPpnV8avaIf5SvL7pndPvPpndJR9Kuu8fePvuiuhorgWjp7Mf/PRjxcFCPDkW31srioCExivv9lcwKEaHsf/7ow2Fl1T/9RkXgEhYElAoCLFtMArxwivDJJ+bR1HTKJdlEoTELCIqgEwVGSQ+hIm0NbK8WXcTEI0UPoa2NbG4y2K00JEWbZavJXkYaqo9CRHS55FcZTjKEk3NKoCYUnSQ0rWxrZbFKbKIhOKPZe1cJKzZSaQrIyULHDZmV5K4xySsDRKWOruanGtjLJXFEmwaIbDLX0hIPBUQPVFVkQkDoUNfSoDgQGKPekoxeGzA4DUvnn4bxzcZrtJyipKfPNy5w+9lnXwgqsiyHNeSVpemw4bWb9psYeq//uQZBoABQt4yMVxYAIAAAkQoAAAHvYpL5m6AAgAACXDAAAAD59jblTirQe9upFsmZbpMudy7Lz1X1DYsxOOSWpfPqNX2WqktK0DMvuGwlbNj44TleLPQ+Gsfb+GOWOKJoIrWb3cIMeeON6lz2umTqMXV8Mj30yWPpjoSa9ujK8SyeJP5y5mOW1D6hvLepeveEAEDo0mgCRClOEgANv3B9a6fikgUSu/DmAMATrGx7nng5p5iimPNZsfQLYB2sDLIkzRKZOHGAaUyDcpFBSLG9MCQALgAIgQs2YunOszLSAyQYPVC2YdGGeHD2dTdJk1pAHGAWDjnkcLKFymS3RQZTInzySoBwMG0QueC3gMsCEYxUqlrcxK6k1LQQcsmyYeQPdC2YfuGPASCBkcVMQQqpVJshui1tkXQJQV0OXGAZMXSOEEBRirXbVRQW7ugq7IM7rPWSZyDlM3IuNEkxzCOJ0ny2ThNkyRai1b6ev//3dzNGzNb//4uAvHT5sURcZCFcuKLhOFs8mLAAEAt4UWAAIABAAAAAB4qbHo0tIjVkUU//uQZAwABfSFz3ZqQAAAAAngwAAAE1HjMp2qAAAAACZDgAAAD5UkTE1UgZEUExqYynN1qZvqIOREEFmBcJQkwdxiFtw0qEOkGYfRDifBui9MQg4QAHAqWtAWHoCxu1Yf4VfWLPIM2mHDFsbQEVGwyqQoQcwnfHeIkNt9YnkiaS1oizycqJrx4KOQjahZxWbcZgztj2c49nKmkId44S71j0c8eV9yDK6uPRzx5X18eDvjvQ6yKo9ZSS6l//8elePK/Lf//IInrOF/FvDoADYAGBMGb7FtErm5MXMlmPAJQVgWta7Zx2go+8xJ0UiCb8LHHdftWyLJE0QIAIsI+UbXu67dZMjmgDGCGl1H+vpF4NSDckSIkk7Vd+sxEhBQMRU8j/12UIRhzSaUdQ+rQU5kGeFxm+hb1oh6pWWmv3uvmReDl0UnvtapVaIzo1jZbf/pD6ElLqSX+rUmOQNpJFa/r+sa4e/pBlAABoAAAAA3CUgShLdGIxsY7AUABPRrgCABdDuQ5GC7DqPQCgbbJUAoRSUj+NIEig0YfyWUho1VBBBA//uQZB4ABZx5zfMakeAAAAmwAAAAF5F3P0w9GtAAACfAAAAAwLhMDmAYWMgVEG1U0FIGCBgXBXAtfMH10000EEEEEECUBYln03TTTdNBDZopopYvrTTdNa325mImNg3TTPV9q3pmY0xoO6bv3r00y+IDGid/9aaaZTGMuj9mpu9Mpio1dXrr5HERTZSmqU36A3CumzN/9Robv/Xx4v9ijkSRSNLQhAWumap82WRSBUqXStV/YcS+XVLnSS+WLDroqArFkMEsAS+eWmrUzrO0oEmE40RlMZ5+ODIkAyKAGUwZ3mVKmcamcJnMW26MRPgUw6j+LkhyHGVGYjSUUKNpuJUQoOIAyDvEyG8S5yfK6dhZc0Tx1KI/gviKL6qvvFs1+bWtaz58uUNnryq6kt5RzOCkPWlVqVX2a/EEBUdU1KrXLf40GoiiFXK///qpoiDXrOgqDR38JB0bw7SoL+ZB9o1RCkQjQ2CBYZKd/+VJxZRRZlqSkKiws0WFxUyCwsKiMy7hUVFhIaCrNQsKkTIsLivwKKigsj8XYlwt/WKi2N4d//uQRCSAAjURNIHpMZBGYiaQPSYyAAABLAAAAAAAACWAAAAApUF/Mg+0aohSIRobBAsMlO//Kk4soosy1JSFRYWaLC4qZBYWFRGZdwqKiwkNBVmoWFSJkWFxX4FFRQWR+LsS4W/rFRb/////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////////VEFHAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAU291bmRib3kuZGUAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAAMjAwNGh0dHA6Ly93d3cuc291bmRib3kuZGUAAAAAAAAAACU=";
            let snd = new Audio(BEEP_SOURCE);
            snd.play();
        };

        let showInfo = function (msg, durMillis, type) {
            let div = $("#general_information");
            div.html(msg);
            div.addClass(CLS_MSG_TYPES[type]);
            div.show();
            div.fadeTo(durMillis, opacity).slideUp(animationLength, function () {
                div.slideUp(animationLength);
                div.removeClass();
            });
        };
        return {
            showError: function (msg, durationMillis) {
                showInfo(msg, durationMillis, ERROR);
            },
            yourMove: function () {
                beep();
                showInfo('Your Turn!!!', 3000, YOUR_MOVE);
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
                    if(statusResponse.lastMove) {
                        if(statusResponse.lastMove.playerName) {
                            $('#opponent_username').html(statusResponse.lastMove.playerName + '\'s Moves');
                        }
                        renderer.updateOpponentsMove(statusResponse.lastMove);
                    }
                    $('#btn_guess').prop('disabled', false);
                    alerts.yourMove();
                    stopFunction();
                }
            })
            .fail(function (failedResponse) {
                alerts.showError('failed to retrieve status: ' + failedResponse);
                window.location.replace('room/index.html');
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
        timeoutVar = setTimeout(cycleRefresh, 1000);
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
                    mmind.SendNumber(guessVal.val()).then(function (refresh) {
                        guessVal.val('');
                        if(refresh) {
                            cycleRefresh();
                        }
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
        let index_col = $('<th>');
        targetRow.append(index_col);
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