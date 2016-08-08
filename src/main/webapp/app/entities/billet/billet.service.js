(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Billet', Billet);

    Billet.$inject = ['$resource', 'DateUtils'];

    function Billet ($resource, DateUtils) {
        var resourceUrl =  'api/billets/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true},
            'get': {
                method: 'GET',
                transformResponse: function (data) {
                    if (data) {
                        data = angular.fromJson(data);
                        data.dateDebut = DateUtils.convertDateTimeFromServer(data.dateDebut);
                        data.dateFin = DateUtils.convertDateTimeFromServer(data.dateFin);
                    }
                    return data;
                }
            },
            'update': { method:'PUT' }
        });
    }
})();
