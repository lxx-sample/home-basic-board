var MessageHandlerManager = (function () {

    var MESSAGE_HANDLER_MAPPING = {};

    this.registerHandler = function (handler, action) {
        if (MESSAGE_HANDLER_MAPPING[handler]) {
            throw 'handler ' + handler + ' already exists';
        }

        MESSAGE_HANDLER_MAPPING[handler] = action;
    }

    this.get = function(handler, method) {
        if (MESSAGE_HANDLER_MAPPING[handler]) {
            return MESSAGE_HANDLER_MAPPING[handler];
        }

        throw 'no handler ' + handler + ' exists';
    }

    return this;
})();