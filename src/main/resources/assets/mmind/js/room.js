'use strict';

let Room = (function () {

    let joinRoom = function (result, secret) {
        $('#room_number_label').html(result.roomNumber);
        $('#room_pwd_label').html(result.roomPassword);
        $('#user_name_label').html(result.userName);
        $('#login_room').hide();
        $('#room_info').show();
        $('#game_room_content').attr('src', 'index.html');
        $('#game_room').show();
        window.localStorage.setItem('ownsecret', secret);
        if("JOIN_ADMIN" === result.action) {
            console.log('admin joined');
        } else if("JOIN_GUEST" === result.action) {
            console.log('guest joined');
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
            type: "POST",
            url: encodeURI('/api/mmind/auth'),
            data: JSON.stringify({
                playerName: name,
                password: strPwd,
                secret: secret,
                roomNumber: roomNumber
            }),
            contentType: "application/json; charset=utf-8",
            dataType: "json",
            success: function (result) {
                console.log('success');
                if(result.action !== 'NONE') {
                    window.localStorage.setItem('sessid', result.token);
                    window.localStorage.setItem('room_number', $('#room_number').val());
                    joinRoom(result, $('#secret_code').val());
                }
            },
            error: function (a) {
                console.log(a);
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
    console.log('ready');
    $('#room_form').submit(Room.join);
})