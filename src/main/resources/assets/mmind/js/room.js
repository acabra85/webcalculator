'use strict';

const SECRET_LENGTH = 4;

let Room = (function () {

    let buildUrlToShare = function (roomNumber, roomPassword) {
        let idx = window.location.href.indexOf('room');
        return encodeURI(window.location.href.substr(0, idx)
            + 'room?'
            + 'room_number=' + roomNumber
            + '&room_password='+roomPassword);
    }

    let joinRoom = function (result) {
        $('#room_number_label').html('&#x1F3E0;' +result.roomNumber);
        let roomLabelElm = $('#room_pwd_label');
        roomLabelElm.html('');
        let buttonShareElm = $('<button class="btn btn-xs btn-secondary" ' +
            'style="padding: 0rem .75rem; font-size: 0.75em">');
        buttonShareElm.html('&#x1F511;' + atob(result.roomPassword));
        buttonShareElm.click(function(){
            navigator.clipboard.writeText(buildUrlToShare(result.roomNumber, result.roomPassword))
                .then(function(){
                    reportMessage('Link copied to clipboard!');
                    $('#guess_value').focus();
                }, function () {
                    $('#guess_value').focus();
                    reportError('Unable to get link to share');
                });
        });
        roomLabelElm.append(buttonShareElm);
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

    function cleanBorders(elms) {
        elms.forEach(function(elm) {
            elm.css('border-color', '#e6e9ec');
        })
    }

    function markFieldError(pwdElm) {
        pwdElm.css('border-color', 'red');
    }

    function reportError(msg) {
        notifyMessage('error', msg);
    }

    function reportMessage(msg) {
        notifyMessage('info', msg);
    }

    let notifyMessage = function(type, msg) {
        let cls = type === 'error'? 'error_alert' : 'your_move_alert';
        let div = $("#login_notification_section");
        div.removeClass();
        div.addClass('round_edges');
        div.addClass(cls);
        let msgElm = $('<b>').html(msg);
        $('#login_notification_section_msg').html(msgElm);
        div.show();
        div.fadeTo(2000, 1500).slideUp(1500, function () {
            div.slideUp(1500);
        });
    }

    let join = function (evt){
        evt.preventDefault();
        console.log('submit');
        let strPwd = btoa($('#pwd').val());
        let roomNumber = $('#room_number').val();
        let secret = $('#secret_code').val();
        let name = $('#player_name').val();
        if(secret.length !== SECRET_LENGTH) {
            markFieldError($("#secret_code"));
            reportError('Secret must contain exactly: ' + SECRET_LENGTH + ' letters.');
            return;
        }
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
                let roomElm = $("#room_number");
                let pwdElm = $("#pwd");
                let secretCodeElm = $("#secret_code");
                let playerNameElm = $("#player_name");
                cleanBorders([roomElm, pwdElm, secretCodeElm, playerNameElm]);
                console.log(typeStr + ' ' + JSON.stringify(jqxhr.responseJSON) + ': ' + reasonStr);
                if(jqxhr.responseJSON.error.indexOf('wrong password') > 0) {
                    markFieldError(pwdElm);
                    reportError('Wrong Room Password');
                } else if(jqxhr.responseJSON.error.indexOf('room is full') > 0) {
                    markFieldError(roomElm);
                    reportError('Room Is Full, Join another room');
                } else if(jqxhr.responseJSON.error.indexOf('Invalid length for given secret') > 0) {
                    markFieldError(secretCodeElm);
                    reportError('Secret must contain exactly: ' + SECRET_LENGTH + ' letters.');
                } else if(jqxhr.responseJSON.error.indexOf('Empty player name') > 0) {
                    markFieldError(playerNameElm);
                    reportError('Your name must not be empty');
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
    function buildQueryParamsMap() {
        let map = new Map();
        let hasParams = window.location.href.indexOf('?');
        if(!hasParams) return map;
        let hashes = window.location.href.slice(hasParams + 1).split('&');
        for(let i = 0; i < hashes.length; ++i) {
            let item = hashes[i].split('=');
            map.set(item[0], item[1]);
        }
        return map;
    }

    function cleanLocalStorage() {
        let cacheKeys = ['sessid', 'room_number', 'ownsecret', 'is_admin', 'lastConsumedEventId', 'player_id',
            'opponentName'];
        cacheKeys.forEach(function(key) {
            window.localStorage.removeItem(key);
        });
    }
    console.log('ready');
    cleanLocalStorage();

    function fastDefaults(room, password, name, secret) {
        $('#room_number').val(room);
        $('#pwd').val(password);
        $('#player_name').val(name);
        $('#secret_code').val(secret);
    }

    let queryParams = buildQueryParamsMap();
    //fastDefaults('12', 'dasdas', 'name', '1111');
    if(queryParams.has('room_number')) {
        $('#room_number').val(queryParams.get('room_number'));
    }
    if(queryParams.has('room_password')) {
        $('#pwd').val(atob(queryParams.get('room_password')));
    }
    $('#room_form').submit(Room.join);
})