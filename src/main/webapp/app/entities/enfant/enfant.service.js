(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Enfant', Enfant);

    Enfant.$inject = ['$resource', 'DateUtils'];

    function Enfant ($resource, DateUtils) {
        var resourceUrl =  'api/enfants/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateNaissance = DateUtils.convertDateTimeFromServer(data.dateNaissance);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
