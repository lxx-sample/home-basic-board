var MessageHandlerManager = (function () {

    var MESSAGE_HANDLER_MAPPING = {};

    this.registerHandler = function (handler, method, action) {
        if (MESSAGE_HANDLER_MAPPING[handler] && MESSAGE_HANDLER_MAPPING[handler][method]) {
            throw 'handler ' + handler + '#' + method + ' already exists';
        }

        if (!MESSAGE_HANDLER_MAPPING[handler]) {
            MESSAGE_HANDLER_MAPPING[handler] = {};
        }

        MESSAGE_HANDLER_MAPPING[handler][method] = action;
    }

    this.get = function(handler, method) {
        if (MESSAGE_HANDLER_MAPPING[handler] && MESSAGE_HANDLER_MAPPING[handler][method]) {
            return MESSAGE_HANDLER_MAPPING[handler][method];
        }

        throw 'no handler ' + handler + '#' + method + ' exists';
    }

    return this;
})();