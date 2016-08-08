(function() {
    'use strict';

    angular
        .module('cebilletterieApp')
        .factory('StatutDemandeSearch', StatutDemandeSearch);

    StatutDemandeSearch.$inject = ['$resource'];

    function StatutDemandeSearch($resource) {
        var resourceUrl =  'api/_search/statut-demandes/:id';

        return $resource(resourceUrl, {}, {
            'query': { method: 'GET', isArray: true}
        });
    }
})();
