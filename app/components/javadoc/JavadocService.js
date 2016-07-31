app.factory('Javadoc', function () {
    var javadoc = {};

    javadoc.check = function(v) {
        var http = new XMLHttpRequest();
        var url = 'javadoc/' + v;

        http.open('HEAD', url, false);
        http.send();

        return http.status != 404;
    };

    return javadoc;
});