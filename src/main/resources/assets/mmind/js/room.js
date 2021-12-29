'use strict';

let Room = (function () {

    let joinRoom = function (result) {
        $('#room_number_label').html(result.roomNumber);
        $('#room_pwd_label').html(atob(result.roomPassword));
        $('#user_name_label').html(result.userName);
        $('#login_room').hide();
        $('#room_info').show();
        $('#game_room_content').attr('src', 'index.html');
        $('#game_room').show();
        if("JOIN_ADMIN" === result.action) {
            window.localStorage.setItem('is_admin', 'true');
        } else if("JOIN_HOST" === result.action) {
            console.log("Joined as host!");
        } else if("JOIN_GUEST" === result.action) {
            console.log("Joined as guest!");
        }
    };

    let join = function (evt){
        evt.preventDefault();
        console.log('submit');
        let strPwd = btoa($('#pwd').val());
        let roomNumber = $('#room_number').val();
        let secret = $('#secret_code').val();
        let name = $('#player_name').val();

        $.ajax({
            cache: false,
            type: "POST",
            url: encodeURI('/api/mmind/auth'),
            data: JSON.stringify({
                playerName: name,
                password: strPwd,
                secret: secret,
                roomNumber: roomNumber,
                token: window.localStorage.getItem('sessid') ? window.localStorage.getItem('sessid') : null
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result) {
                console.log('success');
                if(result.action !== 'NONE') {
                    window.localStorage.setItem('sessid', result.token);
                    window.localStorage.setItem('room_number', $('#room_number').val());
                    window.localStorage.setItem('player_id', result.playerId);
                    window.localStorage.setItem('ownsecret', secret);
                    if(result.opponentName) {
                        window.localStorage.setItem('opponentName', result.opponentName);
                    }
                    joinRoom(result, $('#secret_code').val());
                }
            },
            error: function (jqxhr, reasonStr, typeStr) {
                console.log(typeStr + ' ' + JSON.stringify(jqxhr.responseJSON) + ': ' + reasonStr);
                if(jqxhr.responseJSON.error.indexOf('wrong password') > 0) {
                    $("#pwd").css("border-color","red");
                } else if(jqxhr.responseJSON.error.indexOf('room is full') > 0) {
                    $("#room_number").css("border-color","red");
                }
            }
        }).done(function (resp) {
            console.log(resp);
        })
    };

    return {
        join: join,
    };
})();

$(document).ready(function () {
    function cleanLocalStorage() {
        let cacheKeys = ['sessid', 'room_number', 'ownsecret', 'is_admin', 'lastConsumedEventId', 'player_id',
            'opponentName'];
        cacheKeys.forEach(function(key) {
            window.localStorage.removeItem(key);
        });
    }
    console.log('ready');
    cleanLocalStorage();
    $('#room_form').submit(Room.join);
})