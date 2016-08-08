(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('TypeBillet', TypeBillet);

    TypeBillet.$inject = ['$resource'];

    function TypeBillet ($resource) {
        var resourceUrl =  'api/type-billets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
