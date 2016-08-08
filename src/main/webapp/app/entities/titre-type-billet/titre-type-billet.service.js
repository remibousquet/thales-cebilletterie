(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('TitreTypeBillet', TitreTypeBillet);

    TitreTypeBillet.$inject = ['$resource'];

    function TitreTypeBillet ($resource) {
        var resourceUrl =  'api/titre-type-billets/:id';

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
