/**
 *  websocket 公用js
 * @type {jQuery}
 */
var ws_url;
var ws_user_name;

var ws;//websocket实例
var lockReconnect = false;//避免重复连接
var heartBeatCheck = "heartBeatCheck";
var heartCheck_time = 30 * 1000;//心跳检测间隔时间,毫秒
var reconnect_time = 2 * 1000;//重连时间,毫秒
var isprintLog = false; // 是否打印日志

// createWebSocket(wsUrl, userName);

function createWebSocket(wsUrl, userName, isprintLogFlag) {
    if (isprintLogFlag != null) {
        isprintLog = isprintLogFlag;
    }
    try {
        if (wsUrl != null && wsUrl.length > 0
            && userName != null && userName.length > 0) {

            ws_url = wsUrl;
            ws_user_name = userName;

            ws = new WebSocket(ws_url);
            initEventHandle();
        }
    } catch (e) {
        reconnect(url);
    }
}

function initEventHandle() {
    ws.onclose = function () {
        reconnect(ws_url);
    };
    ws.onerror = function () {
        reconnect(ws_url);
    };
    ws.onopen = function () {
        ws.send(ws_user_name);
        printlog("create ws conn success , user : " + ws_user_name);
        //心跳检测重置
        heartCheck.reset().start();
    };
    ws.onmessage = function (event) {
        //如果获取到消息，心跳检测重置
        //拿到任何消息都说明当前连接是正常的
        heartCheck.reset().start();

        // 处理返回来的消息
        var data = event.data;
        var json = null;
        if (data != heartBeatCheck) {
            if (typeof data == 'string') {
                json = JSON.parse(event.data);
            } else {
                json = data;
            }
            eval(json.method);
        } else {
            printlog("心跳检测成功 : " + formatDate(new Date()));
        }

    }
}

function reconnect(url) {
    if (lockReconnect) return;
    lockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
        createWebSocket(url);
        lockReconnect = false;
    }, reconnect_time);
}

//心跳检测
var heartCheck = {
    timeout: heartCheck_time,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function () {
        printlog(formatDate(new Date()) + "  心跳检测开始,间隔时间 : " + heartCheck_time / 1000 + "秒");
        var self = this;
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            ws.send(heartBeatCheck);
            self.serverTimeoutObj = setTimeout(function () {//如果超过一定时间还没重置，说明后端主动断开了
                ws.close();//如果onclose会执行reconnect，我们执行ws.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
}


//----------- date op -------------
function formatDate(date, fmt) {
    if (fmt == null) {
        fmt = 'yyyy-MM-dd HH:mm:ss';
    }
    if (typeof date == 'number') {
        date = new Date(date);
    }
    if (/(y+)/.test(fmt)) {
        fmt = fmt.replace(RegExp.$1, (date.getFullYear() + '').substr(4 - RegExp.$1.length));
    }
    let o = {
        'M+': date.getMonth() + 1,
        'd+': date.getDate(),
        'H+': date.getHours(),
        'm+': date.getMinutes(),
        's+': date.getSeconds()
    };
    for (let k in o) {
        if (new RegExp(`(${k})`).test(fmt)) {
            let str = o[k] + '';
            fmt = fmt.replace(RegExp.$1, (RegExp.$1.length === 1) ? str : padLeftZero(str));
        }
    }
    return fmt;
};

function padLeftZero(str) {
    return ('00' + str).substr(str.length);
};

function printlog(msg) {
    if(isprintLog){
        console.log(msg);
    }
}