(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Subvention', Subvention);

    Subvention.$inject = ['$resource'];

    function Subvention ($resource) {
        var resourceUrl =  'api/subventions/:id';

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
