var exec = require('cordova/exec');

var SafeInsets = {
    top: 0,
    right: 0,
    bottom: 0,
    left: 0
};

exec(function (insets) {
    SafeInsets.top = insets.top;
    SafeInsets.right = insets.right;
    SafeInsets.bottom = insets.bottom;
    SafeInsets.left = insets.left;

    var style = document.documentElement.style;
    style.setProperty('--safe-inset-top', insets.top + 'px');
    style.setProperty('--safe-inset-right', insets.right + 'px');
    style.setProperty('--safe-inset-bottom', insets.bottom + 'px');
    style.setProperty('--safe-inset-left', insets.left + 'px');

}, function (error) {
    console.log('[SafeInsets] Error: ' + error);
}, "SafeInsets", "check");

module.exports = SafeInsets;