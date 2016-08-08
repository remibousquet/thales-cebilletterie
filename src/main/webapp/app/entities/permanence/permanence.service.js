(function() {
    'use strict';
    angular
        .module('cebilletterieApp')
        .factory('Permanence', Permanence);

    Permanence.$inject = ['$resource', 'DateUtils'];

    function Permanence ($resource, DateUtils) {
        var resourceUrl =  'api/permanences/:id';

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
