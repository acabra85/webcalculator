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

    function appendShareButton(result) {
        let buttonShareElm = $('<button class="btn btn-xs btn-secondary room-info-buttons">');
        buttonShareElm.html('&#x1F511;' + atob(result.roomPassword));
        buttonShareElm.click(function () {
            navigator.clipboard.writeText(buildUrlToShare(result.roomNumber, result.roomPassword))
                .then(function () {
                    reportMessage('Link copied to clipboard!');
                    $('#game_room_content').contents().find('input[id=guess_value]').focus();
                }, function () {
                    $('#game_room_content').contents().find('input[id=guess_value]').focus();
                    reportError('Unable to get link to share');
                });
        });
        return buttonShareElm;
    }

    let logout = function () {
        location.reload();
    };

    function appendLogoutButton() {
        let logoutButtonElm = $('<button class="btn btn-xs btn-primary room-info-buttons">');
        logoutButtonElm.html('&#x26D4;');
        logoutButtonElm.click(logout);
        return logoutButtonElm;
    }

    function decorateHelpButton() {
        let helpButtonElm = $('#room_info_help_btn');
        helpButtonElm.html('&#x2754;');
        return helpButtonElm;
    }

    let joinRoom = function (result) {
        $('#room_number_label').html('&#x1F3E0;' +result.roomNumber);
        let roomLabelElm = $('#room_pwd_label');
        roomLabelElm.html('');
        roomLabelElm.append(appendShareButton(result));
        let logoutLabelElm = $('#logout_label');
        logoutLabelElm.html('');
        logoutLabelElm.append(appendLogoutButton());
        decorateHelpButton();
        $('#user_name_label').html(result.userName);
        $('#login_room').hide();
        $('#game_rules_section').hide();
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
            url: encodeURI('/api/fsands/auth'),
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

let Rules = (function () {
    let AVAILABLE_LANGUAGES = [
        {
            id: 'en',
            name: 'English',
            rulesLabel: 'Rules',
            rulesHTML: '<tr><th>Game\'s Objective</th><td> Guess your opponent\'s <b>Secret</b></td></tr>' +
                '<tr><th>Secret</th><td>Four digits in your from 0000 to 9999</td></tr>' +
                '<tr><th>Fixes</th><td>Represented by "<b>F</b>", are digits in your "guess" that are present in your opponent\'s secret in the exact position.</td></tr>' +
                '<tr><th>Spikes</th><td>Represented by "<b>S</b>", are remaining digits from your "guess" (Not Fixes) present in your opponent\'s secret in incorrect position.</td></tr>' +
                '<tr><th>Turn</th><td> A player sends a <b>Guess</b>, and receives a result in the form of "F" and "S"</td></tr>' +
                '<tr><th>Round</th><td> Consist of 2 turns (one of each player), there is no limit on the amount of rounds in a game.</td></tr>' +
                '<tr><th>Game Finishes</th><td>when at the end of one <b>round</b> a player (or both) have guessed their opponent\'s secret (<b>4 Fixes</b>)</td></tr>'
        },
        {
            id: 'es',
            name: 'Español',
            rulesLabel: 'Reglas',
            rulesHTML: '<tr><th>Objetivo</th><td> Encontrar el <b>secreto</b> del oponente</td></tr>' +
                '<tr><th>Secreto</th><td>Cuatro digitos desde 0000 hasta 9999</td></tr>' +
                '<tr><th>Fijas</th><td>Representadas por "<b>F</b>", son digitos de su "Pregunta" que estan presentes y en la posicion correcta del secreto de su oponente.</td></tr>' +
                '<tr><th>Picas</th><td>Representadas por "<b>S</b>", son los digitos restantes de su "Pregunta" (No Fijas) que estan presentes en el secreto de su oponente en posicion incorrecta.</td></tr>' +
                '<tr><th>Turno</th><td> El jugador hace una <b>Pregunta</b>, y recibe un resultado en la forma de "F" y "S"</td></tr>' +
                '<tr><th>Ronda</th><td> Consiste en 2 turnos (uno de cada jugador), no hay limite de rondas en un juego.</td></tr>' +
                '<tr><th>Fin del Juego</th><td>Cuando al final de una <b>ronda</b> algun jugador (o los 2) ha adivinado el secreto de su oponente (<b>4 Fijas</b>)</td></tr>'
        },
        {
            id: 'dk',
            name: 'Dansk',
            rulesLabel: 'Regler',
            rulesHTML: '<tr><th>Spillets mål</th><td>Gæt din modstanders <b>Hemmelighed</b></td></tr>' +
                '<tr><th>Hemmelighed</th><td>fire ciffre mellem 0000-9999</td></tr>' +
                '<tr><th>Fixes</th><td>Repræsenteret af "<b>F</b>", er cifre i dit gæt der er i din modstanderens hemmelighed i den korrekte position .</td></tr>' +
                '<tr><th>Spikes</th><td>Repræsenteret af "<b>S</b>", er de tilbageværende cifre for dit gæt (ikke Fixes) der er tilstede i din modstanders hemmelighed der er i en ukorrekt position.</td></tr>' +
                '<tr><th>Tur</th><td> En spiller sender et <b>gæt</b>, og for svar i form af "F" og "S"</td></tr>' +
                '<tr><th>Runde</th><td> Består af 2 ture (en for hver spiller), der er ingen grænser for antal ture i spillet.</td></tr>' +
                '<tr><th>Spillets afslutning</th><td>Ved slutningen af en <b>Runde</b>hvor en eller begge spiller har gætte deres modstanders (<b>4 Fixes</b>)</td></tr>'
        },
        {
            id: 'pl',
            name: 'Polski',
            rulesLabel: 'Zasady',
            rulesHTML: '<tr><th>Cel gry</th><td>Zgadnij <b>sekret</b> przeciwnika</td></tr>' +
                '<tr><th>Sekret</th><td>Cztery cyfry w przedziale od 0000 do 9999</td></tr>' +
                '<tr><th>Fixes</th><td>Reprezentowane przez „<b>F</b>" to cyfry w twoim „zgadywaniu”, które są obecne w sekrecie przeciwnika w dokładnej pozycji</td></tr>' +
                '<tr><th>Spikes</th><td>Reprezentowane przez „<b>S</b>” to pozostałe cyfry z twojego „zgadywania” obecne w sekrecie przeciwnika w nieprawidłowej pozycji</td></tr>' +
                '<tr><th>Ruch</th><td> Gracz wpisuje cyfry i naciska <b>Guess</b> i otrzymuje wynik w postaci „<b>F</b>” i „<b>S</b>”</td></tr>' +
                '<tr><th>Runda</th><td>Składa się z 2 <b>ruchy</b> (po jednej dla każdego gracza), nie ma limitu liczby rund w grze</td></tr>' +
                '<tr><th>Zakończenia gry</th><td>gdy pod koniec jednej <b>rundy</b> gracz (lub obaj) odgadli sekret przeciwnika (4 <b>F</b>)</td></tr>'
        }];
    AVAILABLE_LANGUAGES.sort(function(a, b) {
        return a.id < b.id ? -1 : (a.id === b.id ? 0 : 1);
    });

    let langMap = new Map();
    AVAILABLE_LANGUAGES.forEach(function (langObj){
        langMap.set(langObj.id, langObj);
    });

    function updateRulesForSelectors(selectSelector, tableSelector) {
        let language = $(selectSelector).val();
        let gameRulesTableBody = $(tableSelector + ' table tbody');
        let langObj = langMap.get(language) ? langMap.get(language) : langMap.get('en');
        $(tableSelector + ' table thead').find('tr:first').find('th:first').text(langObj.rulesLabel);
        gameRulesTableBody.html(langObj.rulesHTML);
        gameRulesTableBody.show();
    }

    let updateRules = function () {
        updateRulesForSelectors('#language_picker_id', '#game_rules_section');
    };

    let updateRulesModal = function () {
        updateRulesForSelectors('#language_picker_modal_id', '#game_rules_modal_section');
    };

    function appendAvailableLanguages(jqElm) {
        let first = false;
        AVAILABLE_LANGUAGES.forEach(function (lang) {
            let option = $('<option value="' +  lang.id + '">');
            option.html(lang.name);
            if(first) {
                option.attr('selected', 'selected');
                first = false;
            }
            jqElm.append(option);
        })
    }

    return {
        updateRules: updateRules,
        updateRulesModal: updateRulesModal,
        prepareLanguages: function () {
            let languagePickerElm = $('#language_picker_id');
            let languagePickerModalElm = $('#language_picker_modal_id');
            appendAvailableLanguages(languagePickerElm);
            appendAvailableLanguages(languagePickerModalElm);
            languagePickerElm.change(updateRules);
            languagePickerModalElm.change(updateRulesModal);
            updateRules();
            updateRulesModal();
        }
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
            'opponentName', 'sequence_response_id'];
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
    $(window).on("beforeunload", function() {
        return 'Your progress will be lost';
    });
    Rules.prepareLanguages();
    $('#room_form').submit(Room.join);
})
