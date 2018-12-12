/**
 *  websocket 公用js
 * @type {jQuery}
 */

var websocketObj;//websocket实例

var websocketUrl = ""; // websockt url
var websocketUserkey = ""; // websocket name 的key,确保唯一就好
var websocketHheartCheckTime = 30 * 1000;//心跳检测间隔时间,毫秒
var websocketReconnectTime = 2 * 1000;//重连时间,毫秒
var websocketIsprintLog = false; // 是否打印日志
var websocketLockReconnect = false;//避免重复连接,不要修改这个值
var websocketHeartBeatCheck = "heartBeatCheck";// 心跳字段,不要修改这个值

/**
 * 创建websocket连接
 * @param params json格式字符串
 * -----------------------------------------------
 * @param url (必填) : websocket 的url 以 ws:// 开头
 * @param userKey (必填) : websocket name 的key,确保唯一就好
 * #################################################################
 * @param checkTime 心跳检测间隔时间,毫秒 默认 30 * 1000
 * @param reconnectTime 重连时间,毫秒 默认 2 * 1000
 * @param isprintLog 是否打印日志 默认 false
 * @param reconnect 避免重复连接,不要修改这个值 默认 false
 * @param heartBeatCheck 心跳字段,不要修改这个值 默认 heartBeatCheck
 * ----------------------------------------------------
 * @returns websocketObj : websocket的连接对象
 */
function createWebSocket(params) {
    // 校验数据,给默认值,并判断必填字段
    params = params == null ? {} : params;
    websocketUrl = params.url;
    websocketUserkey = params.userKey;
    if (websocketUrl == null || websocketUrl == "" || websocketUserkey == null || websocketUserkey == "") {
        return null;
    }
    websocketHheartCheckTime = params.checkTime != null ? params.checkTime : websocketHheartCheckTime;//心跳检测间隔时间,毫秒
    websocketReconnectTime = params.reconnectTime != null ? params.reconnectTime : websocketReconnectTime;//重连时间,毫秒
    websocketIsprintLog = params.isprintLog != null ? params.isprintLog : websocketIsprintLog; // 是否打印日志
    websocketLockReconnect = params.reconnect != null ? params.reconnect : websocketLockReconnect;//避免重复连接,不要修改这个值
    websocketHeartBeatCheck = params.heartBeatCheck != null ? params.heartBeatCheck : websocketHeartBeatCheck;// 心跳字段,不要修改这个值

    try {
        websocketObj = new WebSocket(websocketUrl);
        initEventHandle();
    } catch (e) {
        reconnect(url);
    }
    return websocketObj;
}

function initEventHandle() {
    websocketObj.onclose = function () {
        reconnect(websocketUrl);
    };
    websocketObj.onerror = function () {
        reconnect(websocketUrl);
    };
    websocketObj.onopen = function () {
        websocketObj.send(websocketUserkey);
        printlog("create ws conn success , user : " + websocketUserkey);
        //心跳检测重置
        heartCheck.reset().start();
    };
    websocketObj.onmessage = function (event) {
        //如果获取到消息，心跳检测重置
        //拿到任何消息都说明当前连接是正常的
        heartCheck.reset().start();

        // 处理返回来的消息
        var data = event.data;
        var json = null;
        if (data != websocketHeartBeatCheck) {
            if (typeof data == 'string') {
                json = JSON.parse(event.data);
            } else {
                json = data;
            }
            eval(json.method);
        } else {
            printlog("心跳检测成功 : " + toDateString(new Date()));
        }

    }
}

function reconnect(url) {
    if (websocketLockReconnect) return;
    websocketLockReconnect = true;
    //没连接上会一直重连，设置延迟避免请求过多
    setTimeout(function () {
        createWebSocket(url);
        websocketLockReconnect = false;
    }, websocketReconnectTime);
}

//心跳检测
var heartCheck = {
    timeout: websocketHheartCheckTime,
    timeoutObj: null,
    serverTimeoutObj: null,
    reset: function () {
        clearTimeout(this.timeoutObj);
        clearTimeout(this.serverTimeoutObj);
        return this;
    },
    start: function () {
        printlog(toDateString(new Date()) + "  心跳检测开始,间隔时间 : " + websocketHheartCheckTime / 1000 + "秒");
        var self = this;
        this.timeoutObj = setTimeout(function () {
            //这里发送一个心跳，后端收到后，返回一个心跳消息，
            //onmessage拿到返回的心跳就说明连接正常
            websocketObj.send(websocketHeartBeatCheck);
            self.serverTimeoutObj = setTimeout(function () {//如果超过一定时间还没重置，说明后端主动断开了
                websocketObj.close();//如果onclose会执行reconnect，我们执行websocketObj.close()就行了.如果直接执行reconnect 会触发onclose导致重连两次
            }, self.timeout)
        }, this.timeout)
    }
}


//----------- date op -------------
/**
 * 格式化时间, 不处理空值
 */
function toDateString(d, format) {
    if (d == null) {
        return "";
    }
    return toDateStringAndNull(d, format);
}

/**
 * 空值就格式化当前时间
 */
function toDateStringAndNull(d, format) {
    var date = new Date(d || new Date())
        , ymd = [
        this.digit(date.getFullYear(), 4)
        , this.digit(date.getMonth() + 1)
        , this.digit(date.getDate())
    ]
        , hms = [
        this.digit(date.getHours())
        , this.digit(date.getMinutes())
        , this.digit(date.getSeconds())
    ];

    format = format || 'yyyy-MM-dd HH:mm:ss';

    return format.replace(/yyyy/g, ymd[0])
        .replace(/MM/g, ymd[1])
        .replace(/dd/g, ymd[2])
        .replace(/HH/g, hms[0])
        .replace(/mm/g, hms[1])
        .replace(/ss/g, hms[2]);
}

/**
 * 数字前置补零
 */
function digit(num, length, end) {
    var str = '';
    num = String(num);
    length = length || 2;
    for (var i = num.length; i < length; i++) {
        str += '0';
    }
    return num < Math.pow(10, length) ? str + (num | 0) : num;
};

function printlog(msg) {
    if (websocketIsprintLog) {
        console.log(msg);
    }
}