(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Paiement', Paiement);

    Paiement.$inject = ['$resource', 'DateUtils'];

    function Paiement ($resource, DateUtils) {
        var resourceUrl =  'api/paiements/:id';

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
