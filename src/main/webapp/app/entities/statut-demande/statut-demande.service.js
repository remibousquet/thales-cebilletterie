(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('StatutDemande', StatutDemande);

    StatutDemande.$inject = ['$resource', 'DateUtils'];

    function StatutDemande ($resource, DateUtils) {
        var resourceUrl =  'api/statut-demandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.date = DateUtils.convertDateTimeFromServer(data.date);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
