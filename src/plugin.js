var exec = require('cordova/exec');

// var _ready = false;
//
// var _callbacks = [];

var SafeInsets = {

    // ready: function(callback) {
    //     if (callback) {
    //         _ready ? callback() : _callbacks.push(callback);
    //     }
    //     return _ready;
    // },

    top: 0,

    right: 0,

    bottom: 0,

    left: 0

};

window.setTimeout(function () {
    exec(function (insets) {
        SafeInsets.top = insets.top;
        SafeInsets.right = insets.right;
        SafeInsets.bottom = insets.bottom;
        SafeInsets.left = insets.left;
    }, function (error) {
        console.log('[SafeInsets] Error: ' + error);
    }, "SafeInsets", "check");
}, 0);

module.exports = SafeInsets;