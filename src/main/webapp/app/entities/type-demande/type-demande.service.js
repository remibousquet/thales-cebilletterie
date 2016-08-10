(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('TypeDemande', TypeDemande);

    TypeDemande.$inject = ['$resource'];

    function TypeDemande ($resource) {
        var resourceUrl =  'api/type-demandes/:id';

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
